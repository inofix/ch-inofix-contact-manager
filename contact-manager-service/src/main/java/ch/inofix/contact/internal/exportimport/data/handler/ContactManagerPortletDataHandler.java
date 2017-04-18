package ch.inofix.contact.internal.exportimport.data.handler;

import java.util.List;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.exportimport.kernel.lar.BasePortletDataHandler;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataHandler;
import com.liferay.exportimport.kernel.lar.PortletDataHandlerBoolean;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.xml.Element;

import ch.inofix.contact.constants.PortletKeys;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactLocalService;
import ch.inofix.contact.service.permission.ContactManagerPermission;

/**
 *
 * @author Christian Berndt
 * @created 2017-04-18 15:54
 * @modified 2017-04-18 15:54
 * @version 1.0.0
 *
 */
@Component(immediate = true, property = {
        "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER }, service = PortletDataHandler.class)
public class ContactManagerPortletDataHandler extends BasePortletDataHandler {

    public static final String NAMESPACE = "contact-manager";

    public static final String SCHEMA_VERSION = "1.0.0";

    @Override
    public String getSchemaVersion() {
        return SCHEMA_VERSION;
    }

    @Activate
    protected void activate() {

        setDeletionSystemEventStagedModelTypes(new StagedModelType(Contact.class));

        setExportControls(
                new PortletDataHandlerBoolean(NAMESPACE, "contacts", true, false, null, Contact.class.getName()));

        setImportControls(getExportControls());
    }

    @Override
    protected PortletPreferences doDeleteData(PortletDataContext portletDataContext, String portletId,
            PortletPreferences portletPreferences) throws Exception {

        if (portletDataContext.addPrimaryKey(ContactManagerPortletDataHandler.class, "deleteData")) {

            return portletPreferences;
        }

        _contactLocalService.deleteGroupContacts(portletDataContext.getScopeGroupId());

        return portletPreferences;
    }

    @Override
    protected String doExportData(final PortletDataContext portletDataContext, String portletId,
            PortletPreferences portletPreferences) throws Exception {

        Element rootElement = addExportDataRootElement(portletDataContext);

        if (!portletDataContext.getBooleanParameter(NAMESPACE, "contacts")) {
            return getExportDataRootElementString(rootElement);
        }

        portletDataContext.addPortletPermissions(ContactManagerPermission.RESOURCE_NAME);

        rootElement.addAttribute("group-id", String.valueOf(portletDataContext.getScopeGroupId()));

        ActionableDynamicQuery actionableDynamicQuery = _contactLocalService
                .getExportActionableDynamicQuery(portletDataContext);

        actionableDynamicQuery.performActions();

        return getExportDataRootElementString(rootElement);
    }

    @Override
    protected PortletPreferences doImportData(PortletDataContext portletDataContext, String portletId,
            PortletPreferences portletPreferences, String data) throws Exception {

        if (!portletDataContext.getBooleanParameter(NAMESPACE, "contacts")) {
            return null;
        }

        portletDataContext.importPortletPermissions(ContactManagerPermission.RESOURCE_NAME);

        Element entriesElement = portletDataContext.getImportDataGroupElement(Contact.class);

        List<Element> entryElements = entriesElement.elements();

        for (Element entryElement : entryElements) {
            StagedModelDataHandlerUtil.importStagedModel(portletDataContext, entryElement);
        }

        return null;
    }

    @Override
    protected void doPrepareManifestSummary(PortletDataContext portletDataContext,
            PortletPreferences portletPreferences) throws Exception {

        ActionableDynamicQuery actionableDynamicQuery = _contactLocalService
                .getExportActionableDynamicQuery(portletDataContext);

        actionableDynamicQuery.performCount();
    }

    @Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
    protected void setModuleServiceLifecycle(ModuleServiceLifecycle moduleServiceLifecycle) {
    }

    @Reference(unbind = "-")
    protected void setContactLocalService(ContactLocalService contactLocalService) {

        _contactLocalService = contactLocalService;
    }

    private ContactLocalService _contactLocalService;

    private static Log _log = LogFactoryUtil.getLog(ContactManagerPortletDataHandler.class.getName());
}
