package ch.inofix.contact.controller;

import static ch.inofix.contact.internal.exportimport.util.ExportImportLifecycleConstants.EVENT_CONTACTS_IMPORT_FAILED;
import static ch.inofix.contact.internal.exportimport.util.ExportImportLifecycleConstants.PROCESS_FLAG_CONTACTS_IMPORT_IN_PROCESS;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.time.StopWatch;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.exportimport.kernel.controller.ExportImportController;
import com.liferay.exportimport.kernel.controller.ImportController;
import com.liferay.exportimport.kernel.lar.MissingReferences;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataContextFactoryUtil;
import com.liferay.exportimport.kernel.lifecycle.ExportImportLifecycleManager;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.Validator;

import ch.inofix.contact.exception.NoSuchContactException;
import ch.inofix.contact.internal.exportimport.util.ExportImportThreadLocal;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactLocalService;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.Categories;
import ezvcard.property.Uid;

/**
 *
 * @author Christian Berndt
 * @created 2017-06-21 18:49
 * @modified 2017-06-21 18:49
 * @version 1.0.0
 *
 */
@Component(immediate = true, property = { "model.class.name=ch.inofix.contact.model.Contact" }, service = {
        ExportImportController.class, ContactImportController.class })
public class ContactImportController implements ImportController {

    @Override
    public void importDataDeletions(ExportImportConfiguration exportImportConfiguration, File file) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void importFile(ExportImportConfiguration exportImportConfiguration, File file) throws Exception {

        PortletDataContext portletDataContext = null;

        try {
            ExportImportThreadLocal.setContactImportInProcess(true);

            // TODO: process import-settings
            // Map<String, Serializable> settingsMap =
            // exportImportConfiguration.getSettingsMap();

            doImportFile(file, exportImportConfiguration.getUserId(), exportImportConfiguration.getGroupId());
            ExportImportThreadLocal.setContactImportInProcess(false);

        } catch (Throwable t) {
            ExportImportThreadLocal.setContactImportInProcess(false);

            _exportImportLifecycleManager.fireExportImportLifecycleEvent(EVENT_CONTACTS_IMPORT_FAILED, getProcessFlag(),
                    PortletDataContextFactoryUtil.clonePortletDataContext(portletDataContext), t);

            throw t;
        }
    }

    @Override
    public MissingReferences validateFile(ExportImportConfiguration exportImportConfiguration, File file)
            throws Exception {

        throw new UnsupportedOperationException();

    }

    protected void doImportFile(File file, long userId, long groupId) throws Exception {

        _log.info("doImportFile");

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setScopeGroupId(groupId);
        serviceContext.setUserId(userId);

        User user = UserLocalServiceUtil.getUser(userId);
        serviceContext.setCompanyId(user.getCompanyId());

        // TODO: read updateExisting from import configuration
        boolean updateExisting = true;

        // boolean updateExisting = GetterUtil.getBoolean(ArrayUtil.getValue(
        // parameterMap.get("updateExisting"), 0));

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        int numAdded = 0;
        int numIgnored = 0;
        int numImported = 0;
        int numProcessed = 0;
        int numUpdated = 0;

        List<VCard> vCards = Ezvcard.parse(file).all();

        for (VCard vCard : vCards) {

            Uid uidObj = vCard.getUid();
            String uid = null;

            if (Validator.isNotNull(uidObj)) {
                uid = uidObj.getValue();
            } else {
                uid = UUID.randomUUID().toString();
                uidObj = new Uid(uid);
                vCard.setUid(uidObj);
            }

            String[] assetTagNames = getAssetTagNames(vCard);

            serviceContext.setAssetTagNames(assetTagNames);

            String card = Ezvcard.write(vCard).version(VCardVersion.V4_0).go();

            // Only add the contact, if the vCard's uid does not yet exist
            // in this scope
            Contact contact = null;

            try {
                contact = _contactLocalService.getContact(groupId, uid);
            } catch (NoSuchContactException ignore) {
                // ignore
            }

            if (contact == null) {
                _contactLocalService.addContact(userId, card, uid, serviceContext);
                numImported++;
            } else {

                if (updateExisting) {

                    _contactLocalService.updateContact(userId, contact.getContactId(), card, uid, serviceContext);
                    numUpdated++;

                } else {
                    numIgnored++;
                }
            }

            if (numProcessed % 100 == 0 && numProcessed > 0) {

                float completed = ((Integer) numProcessed).floatValue() / ((Integer) vCards.size()).floatValue() * 100;

                _log.info("Processed " + numProcessed + " of " + vCards.size() + " cards in " + stopWatch.getTime()
                        + " ms (" + completed + "%).");
            }

            numProcessed++;

        }

        if (_log.isInfoEnabled()) {
            _log.info("Importing contacts takes " + stopWatch.getTime() + " ms.");
            _log.info("Added " + numAdded + " contacts as new, since they did not have a contactId.");
            _log.info("Ignored " + numIgnored + " contacts since they already exist in this instance.");
            _log.info("Imported " + numImported + " contacts since they did not exist in this instance.");
            _log.info("Updated " + numUpdated + " contacts since they already existed in this instance.");
        }

    }

    protected int getProcessFlag() {

        return PROCESS_FLAG_CONTACTS_IMPORT_IN_PROCESS;
    }

    @Reference(unbind = "-")
    protected void setExportImportLifecycleManager(ExportImportLifecycleManager exportImportLifecycleManager) {
        _exportImportLifecycleManager = exportImportLifecycleManager;
    }

    @Reference(unbind = "-")
    protected void setContactLocalService(ContactLocalService contactLocalService) {
        this._contactLocalService = contactLocalService;
    }

    private static String[] getAssetTagNames(VCard vCard) {

        List<Categories> categories = vCard.getCategoriesList();

        List<String> assetTags = new ArrayList<String>();

        for (Categories category : categories) {

            List<String> values = category.getValues();
            assetTags.addAll(values);

        }

        String[] assetTagNames = new String[0];
        return assetTags.toArray(assetTagNames);
    }

    private ContactLocalService _contactLocalService;
    private ExportImportLifecycleManager _exportImportLifecycleManager;

    private static final Log _log = LogFactoryUtil.getLog(ContactImportController.class.getName());

}
