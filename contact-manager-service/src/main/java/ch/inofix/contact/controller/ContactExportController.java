package ch.inofix.contact.controller;

import static ch.inofix.contact.internal.exportimport.util.ExportImportLifecycleConstants.EVENT_CONTACTS_EXPORT_FAILED;
import static ch.inofix.contact.internal.exportimport.util.ExportImportLifecycleConstants.EVENT_CONTACTS_EXPORT_STARTED;
import static ch.inofix.contact.internal.exportimport.util.ExportImportLifecycleConstants.EVENT_CONTACTS_EXPORT_SUCCEEDED;
import static ch.inofix.contact.internal.exportimport.util.ExportImportLifecycleConstants.PROCESS_FLAG_CONTACTS_EXPORT_IN_PROCESS;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.time.StopWatch;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.exportimport.kernel.controller.ExportController;
import com.liferay.exportimport.kernel.controller.ExportImportController;
import com.liferay.exportimport.kernel.lar.ExportImportDateUtil;
import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataContextFactoryUtil;
import com.liferay.exportimport.kernel.lifecycle.ExportImportLifecycleManager;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.DateRange;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.zip.ZipWriter;

import ch.inofix.contact.internal.exportimport.util.ExportImportThreadLocal;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactLocalService;

/**
 * @author Christian Berndt
 * @created 2017-04-21 19:23
 * @modified 2017-11-15 19:57
 * @version 1.0.5
 */
@Component(
    immediate = true, 
    property = { "model.class.name=ch.inofix.contact.model.Contact" }, 
    service = {
        ExportImportController.class, 
        ContactExportController.class 
    }
)
public class ContactExportController extends BaseExportImportController implements ExportController {

    public ContactExportController() {
        initXStream();
    }

    @Override
    public File export(ExportImportConfiguration exportImportConfiguration) throws Exception {
        
        PortletDataContext portletDataContext = null;

        try {

            ExportImportThreadLocal.setContactExportInProcess(true);

            portletDataContext = getPortletDataContext(exportImportConfiguration);
            
            exportImportConfiguration.getSettingsMap();

            _exportImportLifecycleManager.fireExportImportLifecycleEvent(EVENT_CONTACTS_EXPORT_STARTED,
                    getProcessFlag(), PortletDataContextFactoryUtil.clonePortletDataContext(portletDataContext));

            File file = doExport(portletDataContext);

            ExportImportThreadLocal.setContactExportInProcess(false);

            _exportImportLifecycleManager.fireExportImportLifecycleEvent(EVENT_CONTACTS_EXPORT_SUCCEEDED,
                    getProcessFlag(), PortletDataContextFactoryUtil.clonePortletDataContext(portletDataContext));

            return file;

        } catch (Throwable t) {

            _log.error(t);

            ExportImportThreadLocal.setContactExportInProcess(false);

            _exportImportLifecycleManager.fireExportImportLifecycleEvent(EVENT_CONTACTS_EXPORT_FAILED,
                    getProcessFlag(), PortletDataContextFactoryUtil.clonePortletDataContext(portletDataContext), t);

            throw t;
        }
    }

    protected File doExport(PortletDataContext portletDataContext) throws Exception {
        
         StopWatch stopWatch = new StopWatch();

         stopWatch.start();

        final StringBuilder sb = new StringBuilder();
        
        sb.append("<Contacts>");
        sb.append(StringPool.NEW_LINE);

        ActionableDynamicQuery actionableDynamicQuery = _contactLocalService.getActionableDynamicQuery();

        actionableDynamicQuery.setGroupId(portletDataContext.getGroupId());

        // TODO: process date-range of portletDataContext

        actionableDynamicQuery.setPerformActionMethod(new ActionableDynamicQuery.PerformActionMethod<Contact>() {

            @Override
            public void performAction(Contact contact) {
                String xml = _xStream.toXML(contact);
                sb.append(xml);
                sb.append(StringPool.NEW_LINE);
            }

        });

        actionableDynamicQuery.performActions();

        sb.append("</Contacts>");

        if (_log.isInfoEnabled()) {
            _log.info("Exporting contacts takes " + stopWatch.getTime() + " ms");
        }

        portletDataContext.addZipEntry("/Contacts.xml", sb.toString());

        ZipWriter zipWriter = portletDataContext.getZipWriter();

        return zipWriter.getFile();

    }

    protected PortletDataContext getPortletDataContext(ExportImportConfiguration exportImportConfiguration)
            throws PortalException {

        Map<String, Serializable> settingsMap = exportImportConfiguration.getSettingsMap();

        long companyId = MapUtil.getLong(settingsMap, "companyId");
        long sourceGroupId = MapUtil.getLong(settingsMap, "sourceGroupId");
        String portletId = MapUtil.getString(settingsMap, "portletId");
        Map<String, String[]> parameterMap = (Map<String, String[]>) settingsMap.get("parameterMap");
        DateRange dateRange = ExportImportDateUtil.getDateRange(exportImportConfiguration);
        
        ZipWriter zipWriter = ExportImportHelperUtil.getPortletZipWriter(portletId);

        PortletDataContext portletDataContext = PortletDataContextFactoryUtil.createExportPortletDataContext(
               companyId, sourceGroupId, parameterMap, dateRange.getStartDate(), dateRange.getEndDate(),
                zipWriter);
        portletDataContext.setPortletId(portletId);

        return portletDataContext;
    }

    protected int getProcessFlag() {
        return PROCESS_FLAG_CONTACTS_EXPORT_IN_PROCESS;
    }

    @Reference(unbind = "-")
    protected void setExportImportLifecycleManager(ExportImportLifecycleManager exportImportLifecycleManager) {
        _exportImportLifecycleManager = exportImportLifecycleManager;
    }

    @Reference(unbind = "-")
    protected void setContactLocalService(ContactLocalService contactLocalService) {

        _contactLocalService = contactLocalService;
    }

    private static final Log _log = LogFactoryUtil.getLog(ContactExportController.class);

    private ExportImportLifecycleManager _exportImportLifecycleManager;
    private ContactLocalService _contactLocalService;

}
