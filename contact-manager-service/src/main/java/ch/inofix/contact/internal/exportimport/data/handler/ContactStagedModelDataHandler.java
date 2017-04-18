package ch.inofix.contact.internal.exportimport.data.handler;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.exportimport.kernel.lar.ExportImportPathUtil;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandler;
import com.liferay.exportimport.kernel.lar.StagedModelModifiedDateComparator;
import com.liferay.exportimport.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.xml.Element;

import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactLocalService;

/**
 *
 * @author Christian Berndt
 * @created 2016-04-18 15:48
 * @modified 2016-04-18 15:48
 * @version 1.0.0
 *
 */
@Component(immediate = true, service = StagedModelDataHandler.class)
public class ContactStagedModelDataHandler extends BaseStagedModelDataHandler<Contact> {

    public static final String[] CLASS_NAMES = { Contact.class.getName() };

    @Override
    public void deleteStagedModel(Contact contact) throws PortalException {
        _contactLocalService.deleteContact(contact);
    }

    @Override
    public void deleteStagedModel(String uuid, long groupId, String className, String extraData)
            throws PortalException {

        Contact contact = fetchStagedModelByUuidAndGroupId(uuid, groupId);

        if (contact != null) {
            deleteStagedModel(contact);
        }
    }

    @Override
    public Contact fetchStagedModelByUuidAndGroupId(String uuid, long groupId) {

        return _contactLocalService.fetchContactByUuidAndGroupId(uuid, groupId);
    }

    @Override
    public List<Contact> fetchStagedModelsByUuidAndCompanyId(String uuid, long companyId) {

        return _contactLocalService.getContactsByUuidAndCompanyId(uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
                new StagedModelModifiedDateComparator<Contact>());
    }

    @Override
    public String[] getClassNames() {
        return CLASS_NAMES;
    }

    @Override
    public String getDisplayName(Contact contact) {
        return String.valueOf(contact.getContactId());
    }

    @Override
    protected void doExportStagedModel(PortletDataContext portletDataContext, Contact contact) throws Exception {

        Element contactElement = portletDataContext.getExportDataElement(contact);

        portletDataContext.addClassedModel(contactElement, ExportImportPathUtil.getModelPath(contact), contact);
    }

    @Override
    protected void doImportMissingReference(PortletDataContext portletDataContext, String uuid, long groupId,
            long contactId) throws Exception {

        Contact existingContact = fetchMissingReference(uuid, groupId);

        if (existingContact == null) {
            return;
        }

        Map<Long, Long> contactIds = (Map<Long, Long>) portletDataContext.getNewPrimaryKeysMap(Contact.class);

        contactIds.put(contactId, existingContact.getContactId());
    }

    @Override
    protected void doImportStagedModel(PortletDataContext portletDataContext, Contact contact) throws Exception {

        long userId = portletDataContext.getUserId(contact.getUserUuid());

        ServiceContext serviceContext = portletDataContext.createServiceContext(contact);

        Contact importedContact = null;

        if (portletDataContext.isDataStrategyMirror()) {

            Contact existingContact = fetchStagedModelByUuidAndGroupId(contact.getUuid(),
                    portletDataContext.getScopeGroupId());

            if (existingContact == null) {
                serviceContext.setUuid(contact.getUuid());

                importedContact = _contactLocalService.addContact(userId, contact.getCard(), contact.getUid(),
                        serviceContext);

            } else {

                importedContact = _contactLocalService.updateContact(userId, contact.getContactId(), contact.getCard(),
                        contact.getUid(), serviceContext);
            }
        } else {

            importedContact = _contactLocalService.addContact(userId, contact.getCard(), contact.getUid(),
                    serviceContext);
        }

        portletDataContext.importClassedModel(contact, importedContact);
    }

    @Reference(unbind = "-")
    protected void setContactLocalService(ContactLocalService contactLocalService) {

        _contactLocalService = contactLocalService;
    }

    private ContactLocalService _contactLocalService;

    private static final Log _log = LogFactoryUtil.getLog(ContactStagedModelDataHandler.class);


}