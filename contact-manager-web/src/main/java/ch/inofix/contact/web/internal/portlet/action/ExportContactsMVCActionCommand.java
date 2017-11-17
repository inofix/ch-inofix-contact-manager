package ch.inofix.contact.web.internal.portlet.action;

import java.io.Serializable;
import java.util.Map;
import java.util.TimeZone;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationConstants;
import com.liferay.exportimport.kernel.exception.LARFileNameException;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.kernel.service.ExportImportConfigurationLocalService;
import com.liferay.portal.kernel.exception.NoSuchBackgroundTaskException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.contact.constants.PortletKeys;
import ch.inofix.contact.internal.exportimport.configuration.ExportImportContactsConfigurationSettingsMapFactory;
import ch.inofix.contact.service.ContactService;

/**
 * 
 * @author Christian Berndt
 * @created 2017-11-14 18:10
 * @modified 2017-11-14 18:10
 * @version 1.0.0
 *
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER,
        "mvc.command.name=exportContacts"
    },
    service = MVCActionCommand.class
)
public class ExportContactsMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

        if (cmd.equals(Constants.DELETE)) {
            deleteBackgroundTasks(actionRequest, actionResponse);
        } else if (cmd.equals(Constants.EXPORT)) {
            exportContacts(actionRequest, actionResponse);
        }

        String redirect = ParamUtil.getString(actionRequest, "redirect");

        if (Validator.isNotNull(redirect)) {
            sendRedirect(actionRequest, actionResponse, redirect);
        }

    }

    protected void deleteBackgroundTasks(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
        long groupId = themeDisplay.getScopeGroupId();

        try {
            long[] backgroundTaskIds = ParamUtil.getLongValues(actionRequest, "deleteBackgroundTaskIds");

            for (long backgroundTaskId : backgroundTaskIds) {
                _contactService.deleteBackgroundTask(groupId, backgroundTaskId);
            }
        } catch (Exception e) {
            if (e instanceof NoSuchBackgroundTaskException || e instanceof PrincipalException) {

                SessionErrors.add(actionRequest, e.getClass());

                actionResponse.setRenderParameter("mvcPath", "/error.jsp");

                hideDefaultSuccessMessage(actionRequest);

            } else {
                throw e;
            }
        }

        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");
        String tabs2 = ParamUtil.getString(actionRequest, "tabs2");

        actionResponse.setRenderParameter("tabs1", tabs1);
        actionResponse.setRenderParameter("tabs2", tabs2);

        addSuccessMessage(actionRequest, actionResponse);

    }

    protected void exportContacts(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        hideDefaultSuccessMessage(actionRequest);

        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

        if (Validator.isNull(cmd)) {
            SessionMessages.add(actionRequest,
                    _portal.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_FORCE_SEND_REDIRECT);

            hideDefaultSuccessMessage(actionRequest);

            return;
        }

        try {

            ExportImportConfiguration exportImportConfiguration = getExportImportConfiguration(actionRequest);

            _contactService.exportContactsAsFileInBackground(themeDisplay.getUserId(), exportImportConfiguration);

            sendRedirect(actionRequest, actionResponse);

        } catch (Exception e) {
            SessionErrors.add(actionRequest, e.getClass());

            if (!(e instanceof LARFileNameException)) {
                _log.error(e, e);
            }
        }
    }

    protected ExportImportConfiguration getExportImportConfiguration(ActionRequest actionRequest) throws Exception {

        Map<String, Serializable> exportContactsSettingsMap = null;

        long exportImportConfigurationId = ParamUtil.getLong(actionRequest, "exportImportConfigurationId");

        if (exportImportConfigurationId > 0) {
            ExportImportConfiguration exportImportConfiguration = _exportImportConfigurationLocalService
                    .fetchExportImportConfiguration(exportImportConfigurationId);

            if (exportImportConfiguration != null) {
                exportContactsSettingsMap = exportImportConfiguration.getSettingsMap();
            }
        }

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        if (exportContactsSettingsMap == null) {

            exportContactsSettingsMap = ExportImportContactsConfigurationSettingsMapFactory
                    .buildExportContactsSettingsMap(themeDisplay.getCompanyId(), themeDisplay.getUserId(),
                            themeDisplay.getPlid(), themeDisplay.getScopeGroupId(), PortletKeys.CONTACT_MANAGER,
                            actionRequest.getParameterMap(), themeDisplay.getLocale(), TimeZone.getDefault(), null);
        }

        String taskName = ParamUtil.getString(actionRequest, "name");

        if (Validator.isNull(taskName)) {

            taskName = "contacts";

        }

        return _exportImportConfigurationLocalService.addDraftExportImportConfiguration(themeDisplay.getUserId(),
                taskName, ExportImportConfigurationConstants.TYPE_EXPORT_LAYOUT, exportContactsSettingsMap);
    }

    @Reference(unbind = "-")
    protected void setContactService(ContactService contactService) {
        this._contactService = contactService;
    }

    @Reference(unbind = "-")
    protected void setExportImportConfigurationLocalService(
            ExportImportConfigurationLocalService exportImportConfigurationLocalService) {

        _exportImportConfigurationLocalService = exportImportConfigurationLocalService;
    }

    private static final Log _log = LogFactoryUtil.getLog(ExportContactsMVCActionCommand.class);

    private ExportImportConfigurationLocalService _exportImportConfigurationLocalService;

    private ContactService _contactService;

    @Reference
    private Portal _portal;

}
