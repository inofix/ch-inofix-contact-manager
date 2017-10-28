package ch.inofix.contact.web.internal.portlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import com.liferay.document.library.kernel.exception.FileSizeException;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationSettingsMapFactory;
import com.liferay.exportimport.kernel.exception.LARFileException;
import com.liferay.exportimport.kernel.exception.LARFileSizeException;
import com.liferay.exportimport.kernel.exception.LARTypeException;
import com.liferay.exportimport.kernel.exception.LayoutImportException;
import com.liferay.exportimport.kernel.lar.ExportImportHelper;
import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.MissingReference;
import com.liferay.exportimport.kernel.lar.MissingReferences;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.kernel.service.ExportImportConfigurationLocalService;
import com.liferay.exportimport.kernel.service.ExportImportService;
import com.liferay.exportimport.kernel.staging.StagingUtil;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManagerUtil;
import com.liferay.portal.kernel.exception.LayoutPrototypeException;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.exception.NoSuchBackgroundTaskException;
import com.liferay.portal.kernel.exception.NoSuchResourceException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.JSONPortletResponseUtil;
import com.liferay.portal.kernel.portlet.PortletConfigFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.upload.UploadRequestSizeException;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.contact.constants.PortletKeys;
import ch.inofix.contact.exception.ImageFileFormatException;
import ch.inofix.contact.exception.KeyFileFormatException;
import ch.inofix.contact.exception.NoSuchContactException;
import ch.inofix.contact.exception.SoundFileFormatException;
import ch.inofix.contact.internal.exportimport.configuration.ExportImportContactsConfigurationSettingsMapFactory;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactService;
import ch.inofix.contact.web.configuration.ContactManagerConfiguration;
import ch.inofix.contact.web.configuration.ExportImportConfigurationConstants;
import ch.inofix.contact.web.internal.constants.ContactManagerWebKeys;
import ch.inofix.contact.web.internal.portlet.util.PortletUtil;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.Uid;

/**
 * View Controller of Inofix' contact-manager.
 *
 * @author Stefan Luebbers
 * @author Christian Berndt
 * @created 2017-03-30 19:52
 * @modified 2017-10-28 18:15
 * @version 1.1.3
 */
@Component(
    configurationPid = "ch.inofix.contact.web.configuration.ContactManagerConfiguration", 
    immediate = true, 
    property = { 
        "com.liferay.portlet.css-class-wrapper=ifx-portlet portlet-contact-manager",
        "com.liferay.portlet.display-category=category.inofix",
        "com.liferay.portlet.footer-portlet-javascript=/js/main.js",
        "com.liferay.portlet.header-portlet-css=/css/main.css", 
        "com.liferay.portlet.instanceable=false",
        "javax.portlet.display-name=Contact Manager", 
        "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/view.jsp",
        "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER,        
        "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.security-role-ref=power-user,user" 
    }, 
    service = Portlet.class
)
public class ContactManagerPortlet extends MVCPortlet {

    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        renderRequest.setAttribute(ContactManagerConfiguration.class.getName(), _contactManagerConfiguration);

        super.doView(renderRequest, renderResponse);
    }

    @Override
    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) {

        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

        try {
            if (cmd.equals(Constants.ADD_TEMP)) {

                addTempFileEntry(actionRequest, ExportImportHelper.TEMP_FOLDER_NAME);

                validateFile(actionRequest, actionResponse, ExportImportHelper.TEMP_FOLDER_NAME);
                hideDefaultSuccessMessage(actionRequest);

            } else if (cmd.equals(Constants.DELETE)) {

                deleteContacts(actionRequest, actionResponse);
                addSuccessMessage(actionRequest, actionResponse);

            } else if (cmd.equals("deleteBackgroundTasks")) {

                deleteBackgroundTasks(actionRequest, actionResponse);
                addSuccessMessage(actionRequest, actionResponse);

            } else if (cmd.equals(Constants.DELETE_TEMP)) {

                deleteTempFileEntry(actionRequest, actionResponse, ExportImportHelper.TEMP_FOLDER_NAME);
                hideDefaultSuccessMessage(actionRequest);

            } else if (cmd.equals(Constants.IMPORT)) {

                hideDefaultSuccessMessage(actionRequest);
                importContacts(actionRequest, ExportImportHelper.TEMP_FOLDER_NAME);

                Map<String, String[]> parameters = new HashMap<>();

                String mvcPath = ParamUtil.getString(actionRequest, "mvcPath");
                String tabs1 = ParamUtil.getString(actionRequest, "tabs1");
                String tabs2 = ParamUtil.getString(actionRequest, "tabs2");

                parameters.put("mvcPath", new String[] { mvcPath });
                parameters.put("tabs1", new String[] { tabs1 });
                parameters.put("tabs2", new String[] { tabs2 });

                actionResponse.setRenderParameters(parameters);

            } else if (cmd.equals(Constants.UPDATE)) {

                updateContact(actionRequest, actionResponse);
                addSuccessMessage(actionRequest, actionResponse);

            }
        } catch (Exception e) {

            if (cmd.equals(Constants.ADD_TEMP) || cmd.equals(Constants.DELETE_TEMP)) {

                hideDefaultSuccessMessage(actionRequest);

                // TODO
                // handleUploadException(actionRequest, actionResponse,
                // ExportImportHelper.TEMP_FOLDER_NAME, e);

            } else {
                if ((e instanceof LARFileException) || (e instanceof LARFileSizeException)
                        || (e instanceof LARTypeException)) {

                    SessionErrors.add(actionRequest, e.getClass());
                } else if ((e instanceof LayoutPrototypeException) || (e instanceof LocaleException)) {

                    SessionErrors.add(actionRequest, e.getClass(), e);
                } else {
                    _log.error(e, e);

                    SessionErrors.add(actionRequest, LayoutImportException.class.getName());
                }
            }
        }
    }

    @Override
    public void render(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        try {
            getContact(renderRequest);
        } catch (Exception e) {
            if (e instanceof NoSuchResourceException || e instanceof PrincipalException) {
                SessionErrors.add(renderRequest, e.getClass());
            } else {
                throw new PortletException(e);
            }
        }

        super.render(renderRequest, renderResponse);
    }

    @Override
    public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
            throws PortletException {

        try {
            String resourceID = resourceRequest.getResourceID();

            if (resourceID.equals("download")) {

                 download(resourceRequest, resourceResponse);

            } else if (resourceID.equals("exportContacts")) {

                // TODO
                throw new UnsupportedOperationException();

            } else if (resourceID.equals("importContacts")) {

                importContacts(resourceRequest, resourceResponse);

            } else {
                super.serveResource(resourceRequest, resourceResponse);
            }
        } catch (Exception e) {
            throw new PortletException(e);
        }
    }

    @Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {

        _contactManagerConfiguration = ConfigurableUtil.createConfigurable(
                ContactManagerConfiguration.class, properties);
        
    }

    protected void addTempFileEntry(ActionRequest actionRequest, String folderName) throws Exception {

        UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);

        checkExceededSizeLimit(uploadPortletRequest);

        long groupId = ParamUtil.getLong(actionRequest, "groupId");

        deleteTempFileEntry(groupId, folderName);

        InputStream inputStream = null;

        try {
            String sourceFileName = uploadPortletRequest.getFileName("file");

            inputStream = uploadPortletRequest.getFileAsStream("file");

            String contentType = uploadPortletRequest.getContentType("file");

            _contactService.addTempFileEntry(groupId, folderName, sourceFileName, inputStream, contentType);

        } catch (Exception e) {
            UploadException uploadException = (UploadException) actionRequest.getAttribute(WebKeys.UPLOAD_EXCEPTION);

            if (uploadException != null) {
                Throwable cause = uploadException.getCause();

                // TODO
                // if (cause instanceof FileUploadBase.IOFileUploadException) {
                // if (_log.isInfoEnabled()) {
                // _log.info("Temporary upload was cancelled");
                // }
                // }

                if (uploadException.isExceededFileSizeLimit()) {
                    throw new FileSizeException(cause);
                }

                if (uploadException.isExceededUploadRequestSizeLimit()) {
                    throw new UploadRequestSizeException(cause);
                }
            } else {
                throw e;
            }
        } finally {
            StreamUtil.cleanUp(inputStream);
        }
    }

    protected void checkExceededSizeLimit(HttpServletRequest request) throws PortalException {

        UploadException uploadException = (UploadException) request.getAttribute(WebKeys.UPLOAD_EXCEPTION);

        if (uploadException != null) {
            Throwable cause = uploadException.getCause();

            if (uploadException.isExceededFileSizeLimit() || uploadException.isExceededUploadRequestSizeLimit()) {

                throw new LARFileSizeException(cause);
            }

            throw new PortalException(cause);
        }
    }

    protected void deleteBackgroundTasks(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        _log.info("deleteBackgroundTasks");

        try {
            long[] backgroundTaskIds = ParamUtil.getLongValues(actionRequest, "deleteBackgroundTaskIds");

            for (long backgroundTaskId : backgroundTaskIds) {
                BackgroundTaskManagerUtil.deleteBackgroundTask(backgroundTaskId);
            }
        } catch (Exception e) {
            if (e instanceof NoSuchBackgroundTaskException || e instanceof PrincipalException) {

                SessionErrors.add(actionRequest, e.getClass());

                actionResponse.setRenderParameter("mvcPath", "/error.jsp");
            } else {
                throw e;
            }
        }

        String tabs1 = ParamUtil.getString(actionRequest, "tabs1");
        String tabs2 = ParamUtil.getString(actionRequest, "tabs2");

        actionResponse.setRenderParameter("tabs1", tabs1);
        actionResponse.setRenderParameter("tabs2", tabs2);
    }

    protected void deleteContacts(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        _log.info("deleteContacts");

        long contactId = ParamUtil.getLong(actionRequest, "contactId");

        long[] contactIds = ParamUtil.getLongValues(actionRequest, "deleteContactIds");

        if (contactId > 0) {
            contactIds = new long[] { contactId };
        }

        for (long id : contactIds) {
            _contactService.deleteContact(id);
        }

    }

    protected void deleteTempFileEntry(ActionRequest actionRequest, ActionResponse actionResponse, String folderName)
            throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

        try {
            String fileName = ParamUtil.getString(actionRequest, "fileName");

            _contactService.deleteTempFileEntry(themeDisplay.getScopeGroupId(), folderName, fileName);

            jsonObject.put("deleted", Boolean.TRUE);
        } catch (Exception e) {
            String errorMessage = themeDisplay.translate("an-unexpected-error-occurred-while-deleting-the-file");

            jsonObject.put("deleted", Boolean.FALSE);
            jsonObject.put("errorMessage", errorMessage);
        }

        JSONPortletResponseUtil.writeJSON(actionRequest, actionResponse, jsonObject);

    }

    protected void deleteTempFileEntry(long groupId, String folderName) throws PortalException {

        String[] tempFileNames = _contactService.getTempFileNames(groupId, folderName);

        for (String tempFileEntryName : tempFileNames) {
            _contactService.deleteTempFileEntry(groupId, folderName, tempFileEntryName);
        }
    }

    @Override
    protected void doDispatch(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        if (SessionErrors.contains(renderRequest, PrincipalException.getNestedClasses())
                || SessionErrors.contains(renderRequest, NoSuchContactException.class)) {
            include("/error.jsp", renderRequest, renderResponse);
        } else {
            super.doDispatch(renderRequest, renderResponse);
        }
    }

    protected void getContact(PortletRequest portletRequest) throws Exception {

        long contactId = ParamUtil.getLong(portletRequest, "contactId");

        if (contactId <= 0) {
            return;
        }

        Contact contact = _contactService.getContact(contactId);

        portletRequest.setAttribute(ContactManagerWebKeys.CONTACT, contact);
    }
    
    protected void download(ResourceRequest resourceRequest,
            ResourceResponse resourceResponse) throws PortalException, IOException {

        long contactId = ParamUtil.getLong(resourceRequest, "contactId");

        Contact contact = _contactService.getContact(contactId);

        String card = contact.getCard();
        String name = contact.getFullName(true);

        PortletResponseUtil.sendFile(resourceRequest, resourceResponse, name
                + ".vcf", card.getBytes(), ContentTypes.TEXT_PLAIN_UTF8);

    }

    protected String getEditContactURL(ActionRequest actionRequest, ActionResponse actionResponse, Contact contact)
            throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        String editContactURL = getRedirect(actionRequest, actionResponse);

        if (Validator.isNull(editContactURL)) {
            editContactURL = PortalUtil.getLayoutFullURL(themeDisplay);
        }

        String namespace = actionResponse.getNamespace();
        String windowState = actionResponse.getWindowState().toString();

        editContactURL = HttpUtil.setParameter(editContactURL, "p_p_id", PortletKeys.CONTACT_MANAGER);
        editContactURL = HttpUtil.setParameter(editContactURL, "p_p_state", windowState);
        editContactURL = HttpUtil.setParameter(editContactURL, namespace + "mvcPath",
                templatePath + "edit_contact.jsp");
        editContactURL = HttpUtil.setParameter(editContactURL, namespace + "redirect",
                getRedirect(actionRequest, actionResponse));
        editContactURL = HttpUtil.setParameter(editContactURL, namespace + "backURL",
                ParamUtil.getString(actionRequest, "backURL"));
        editContactURL = HttpUtil.setParameter(editContactURL, namespace + "contactId", contact.getContactId());

        return editContactURL;
    }

    /**
     * from ExportLayoutsMVCAction
     *
     */
    protected ExportImportConfiguration getExportImportConfiguration(ActionRequest actionRequest) throws Exception {

        _log.info("getExportImportConfiguration");

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

            String fileName = ParamUtil.getString(actionRequest, "exportFileName");

            if (Validator.isNull(fileName)) {
                fileName = LanguageUtil.get(actionRequest.getLocale(), "task-records");
            }

            exportContactsSettingsMap = ExportImportContactsConfigurationSettingsMapFactory
                    .buildExportContactsSettingsMap(themeDisplay.getUserId(), themeDisplay.getPlid(),
                            themeDisplay.getScopeGroupId(), PortletKeys.CONTACT_MANAGER,
                            actionRequest.getParameterMap(), themeDisplay.getLocale(), themeDisplay.getTimeZone(),
                            fileName);
        }

        String taskName = ParamUtil.getString(actionRequest, "name");

        if (Validator.isNull(taskName)) {
            taskName = "Contacts";
        }

        return _exportImportConfigurationLocalService.addDraftExportImportConfiguration(themeDisplay.getUserId(),
                taskName, ExportImportConfigurationConstants.TYPE_EXPORT_CONTACTS, exportContactsSettingsMap);
    }

    /**
     * from com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand
     *
     * @param resourceRequest
     * @return
     */
    protected PortletConfig getPortletConfig(ResourceRequest resourceRequest) {

        String portletId = PortalUtil.getPortletId(resourceRequest);

        return PortletConfigFactoryUtil.get(portletId);
    }

    /**
     * from com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand
     *
     * @param resourceRequest
     * @param path
     * @return
     */
    protected PortletRequestDispatcher getPortletRequestDispatcher(ResourceRequest resourceRequest, String path) {

        PortletConfig portletConfig = getPortletConfig(resourceRequest);

        PortletContext portletContext = portletConfig.getPortletContext();

        return portletContext.getRequestDispatcher(path);
    }

    /**
     * Disable the get- / sendRedirect feature of LiferayPortlet.
     */
    @Override
    protected String getRedirect(ActionRequest actionRequest, ActionResponse actionResponse) {
        return null;
    }

    protected void importContacts(ActionRequest actionRequest, String folderName) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long groupId = ParamUtil.getLong(actionRequest, "groupId");

        FileEntry fileEntry = ExportImportHelperUtil.getTempFileEntry(groupId, themeDisplay.getUserId(), folderName);

        InputStream inputStream = null;

        try {
            inputStream = _dlFileEntryLocalService.getFileAsStream(fileEntry.getFileEntryId(), fileEntry.getVersion(),
                    false);

            importContacts(actionRequest, fileEntry.getTitle(), inputStream);

            deleteTempFileEntry(groupId, folderName);

        } finally {
            StreamUtil.cleanUp(inputStream);
        }
    }

    protected void importContacts(ActionRequest actionRequest, String fileName, InputStream inputStream)
            throws Exception {

        long groupId = ParamUtil.getLong(actionRequest, "groupId");

        ExportImportConfiguration exportImportConfiguration = getExportImportConfiguration(actionRequest);

        exportImportConfiguration.setName("Contacts");
        exportImportConfiguration.setGroupId(groupId);

        Map<String, Serializable> settingsMap = new HashMap<>();
        settingsMap.put("targetGroupId", groupId);

        String settings = JSONFactoryUtil.serialize(settingsMap);

        exportImportConfiguration.setSettings(settings);

        _contactService.importContactsInBackground(exportImportConfiguration, inputStream);

    }

    protected void importContacts(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {

        String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);

        PortletRequestDispatcher portletRequestDispatcher = null;

        if (cmd.equals(Constants.IMPORT)) {

            portletRequestDispatcher = getPortletRequestDispatcher(resourceRequest, "/import/processes_list/view.jsp");

        } else {

            portletRequestDispatcher = getPortletRequestDispatcher(resourceRequest,
                    "/import/new_import/import_contacts_resources.jsp");
        }

        portletRequestDispatcher.include(resourceRequest, resourceResponse);
    }

    @Reference
    protected void setContactService(ContactService contactService) {
        this._contactService = contactService;
    }

    @Reference
    protected void setDLFileEntryLocalService(DLFileEntryLocalService dlFileEntryLocalService) {
        this._dlFileEntryLocalService = dlFileEntryLocalService;
    }

    @Reference(unbind = "-")
    protected void setExportImportConfigurationLocalService(
            ExportImportConfigurationLocalService exportImportConfigurationLocalService) {
        _exportImportConfigurationLocalService = exportImportConfigurationLocalService;
    }

    @Reference(unbind = "-")
    protected void setExportImportService(ExportImportService exportImportService) {
        _exportImportService = exportImportService;
    }

    protected void updateContact(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Contact.class.getName(), actionRequest);

        HttpServletRequest request = PortalUtil.getHttpServletRequest(actionRequest);

        UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);

        long contactId = ParamUtil.getLong(actionRequest, "contactId");

        Contact contact = null;

        String card = "";
        String uid = null;

        if (contactId > 0) {

            contact = _contactService.getContact(contactId);
            uid = contact.getUid();
            card = contact.getCard();

        } else {

            VCard vCard = new VCard();
            vCard.setUid(Uid.random());
            uid = vCard.getUid().getValue();
            card = Ezvcard.write(vCard).version(VCardVersion.V4_0).go();

        }

        // Retrieve associated file data

        File[] keyFiles = uploadPortletRequest.getFiles("key.file");
        File[] logoFiles = uploadPortletRequest.getFiles("logo.file");
        File[] photoFiles = uploadPortletRequest.getFiles("photo.file");
        File[] soundFiles = uploadPortletRequest.getFiles("sound.file");

        Map<String, File[]> map = new HashMap<String, File[]>();

        if (keyFiles != null) {
            map.put("key.file", keyFiles);
        }
        if (logoFiles != null) {
            map.put("logo.file", logoFiles);
        }
        if (photoFiles != null) {
            map.put("photo.file", photoFiles);
        }
        if (soundFiles != null) {
            map.put("sound.file", soundFiles);
        }

        // Update the vCard with the request parameters

		try {

            VCard vCard = Ezvcard.parse(card).first();
            vCard = PortletUtil.getVCard(request, vCard, map);
            card = Ezvcard.write(vCard).version(VCardVersion.V4_0).go();

        } catch (ImageFileFormatException iffe) {

            SessionErrors.add(actionRequest, "the-image-file-format-is-not-supported");

            // Store the unmodified contact as a request attribute

            uploadPortletRequest.setAttribute(ContactManagerWebKeys.CONTACT, contact);

            return;

        } catch (KeyFileFormatException kffe) {

            SessionErrors.add(actionRequest, "the-key-file-format-is-not-supported");

            // Store the unmodified contact as a request attribute

            uploadPortletRequest.setAttribute(ContactManagerWebKeys.CONTACT, contact);

            return;

        } catch (SoundFileFormatException sffe) {

            SessionErrors.add(actionRequest, "the-sound-file-format-is-not-supported");

            // Store the unmodified contact as a request attribute

            uploadPortletRequest.setAttribute(ContactManagerWebKeys.CONTACT, contact);

            return;
        }

        if (contactId <= 0) {

            // Add contact

            contact = _contactService.addContact(card, uid, serviceContext);

        } else {

            // Update contact

            contact = _contactService.updateContact(contactId, card, uid, serviceContext);
        }

        String redirect = getEditContactURL(actionRequest, actionResponse, contact);

        actionRequest.setAttribute(WebKeys.REDIRECT, redirect);

        actionRequest.setAttribute(ContactManagerWebKeys.CONTACT, contact);

    }

    protected void validateFile(ActionRequest actionRequest, ActionResponse actionResponse, String folderName)
            throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long groupId = ParamUtil.getLong(actionRequest, "groupId");

        FileEntry fileEntry = ExportImportHelperUtil.getTempFileEntry(groupId, themeDisplay.getUserId(), folderName);

        InputStream inputStream = null;

        try {
            inputStream = _dlFileEntryLocalService.getFileAsStream(fileEntry.getFileEntryId(), fileEntry.getVersion(),
                    false);

            MissingReferences missingReferences = validateFile(actionRequest, inputStream);

            Map<String, MissingReference> weakMissingReferences = missingReferences.getWeakMissingReferences();

            if (weakMissingReferences.isEmpty()) {
                return;
            }

            JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

            if ((weakMissingReferences != null) && !weakMissingReferences.isEmpty()) {

                jsonObject.put("warningMessages",
                        StagingUtil.getWarningMessagesJSONArray(themeDisplay.getLocale(), weakMissingReferences));
            }

            JSONPortletResponseUtil.writeJSON(actionRequest, actionResponse, jsonObject);
        } finally {
            StreamUtil.cleanUp(inputStream);
        }
    }

    protected MissingReferences validateFile(ActionRequest actionRequest, InputStream inputStream) throws Exception {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        long groupId = ParamUtil.getLong(actionRequest, "groupId");
        boolean privateLayout = ParamUtil.getBoolean(actionRequest, "privateLayout");

        Map<String, Serializable> importLayoutSettingsMap = ExportImportConfigurationSettingsMapFactory
                .buildImportLayoutSettingsMap(themeDisplay.getUserId(), groupId, privateLayout, null,
                        actionRequest.getParameterMap(), themeDisplay.getLocale(), themeDisplay.getTimeZone());

        ExportImportConfiguration exportImportConfiguration = _exportImportConfigurationLocalService
                .addDraftExportImportConfiguration(themeDisplay.getUserId(),
                        ExportImportConfigurationConstants.TYPE_IMPORT_CONTACTS, importLayoutSettingsMap);

        return _exportImportService.validateImportLayoutsFile(exportImportConfiguration, inputStream);
    }

    private ContactService _contactService;
    private DLFileEntryLocalService _dlFileEntryLocalService;
    private ExportImportConfigurationLocalService _exportImportConfigurationLocalService;
    private ExportImportService _exportImportService;

    private volatile ContactManagerConfiguration _contactManagerConfiguration;

    private static final Log _log = LogFactoryUtil.getLog(ContactManagerPortlet.class.getName());

}