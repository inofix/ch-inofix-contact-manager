package ch.inofix.contact.web.internal.portlet.action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.contact.constants.PortletKeys;
import ch.inofix.contact.exception.ImageFileFormatException;
import ch.inofix.contact.exception.KeyFileFormatException;
import ch.inofix.contact.exception.NoSuchContactException;
import ch.inofix.contact.exception.SoundFileFormatException;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactService;
import ch.inofix.contact.web.internal.portlet.util.PortletUtil;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.Uid;

/**
 * 
 * @author Christian Berndt
 * @created 2017-11-14 00:45
 * @modified 2017-11-14 00:45
 * @version 1.0.0
 *
 */
@Component(
    property = {
        "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER,
        "mvc.command.name=editContact"
    },
    service = MVCActionCommand.class
)
public class EditContactMVCActionCommand extends BaseMVCActionCommand {
    
    protected void deleteGroupContacts(ActionRequest actionRequest) throws Exception {

        _log.info("deleteGroupContacts()");

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Contact.class.getName(), actionRequest);

        _contactService.deleteGroupContacts(serviceContext.getScopeGroupId());

    }

    protected void deleteContacts(ActionRequest actionRequest) throws Exception {

        long contactId = ParamUtil.getLong(actionRequest, "contactId");

        long[] contactIds = ParamUtil.getLongValues(actionRequest, "deleteContactIds");

        if (contactId > 0) {
            contactIds = new long[] { contactId };
        }

        for (long id : contactIds) {
            _contactService.deleteContact(id);
        }

    }

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

        _log.info("doProcessAction");
        _log.info("cmd = " + cmd);

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        Contact contact = null;
        try {

            if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
                contact = updateContact(actionRequest);
            } else if (cmd.equals(Constants.DELETE)) {
                deleteContacts(actionRequest);
            } else if (cmd.equals("deleteGroupContacts")) {
                deleteGroupContacts(actionRequest);
            }

            if (Validator.isNotNull(cmd)) {
                String redirect = ParamUtil.getString(actionRequest, "redirect");
                if (contact != null) {

                    redirect = getSaveAndContinueRedirect(actionRequest, contact, themeDisplay.getLayout(),
                            redirect);

                    sendRedirect(actionRequest, actionResponse, redirect);
                }
            }

        } catch (NoSuchContactException | PrincipalException e) {

            SessionErrors.add(actionRequest, e.getClass());

            actionResponse.setRenderParameter("mvcPath", "/error.jsp");

            // TODO: Define set of exceptions reported back to user. For an
            // example, see EditCategoryMVCActionCommand.java.

        } catch (Exception e) {

            SessionErrors.add(actionRequest, e.getClass());
        }
    }
    
    protected String getSaveAndContinueRedirect(
            ActionRequest actionRequest, Contact contact, Layout layout, String redirect)
        throws Exception {
        
        _log.info("getSaveAndContinueRedirect()");

        PortletConfig portletConfig = (PortletConfig)actionRequest.getAttribute(
            JavaConstants.JAVAX_PORTLET_CONFIG);
        
        LiferayPortletURL portletURL = PortletURLFactoryUtil.create(actionRequest, portletConfig.getPortletName(), layout, PortletRequest.RENDER_PHASE);

        portletURL.setParameter("mvcRenderCommandName", "editContact");

        portletURL.setParameter(Constants.CMD, Constants.UPDATE, false);
        portletURL.setParameter("redirect", redirect, false);
        portletURL.setParameter(
            "groupId", String.valueOf(contact.getGroupId()), false);
        portletURL.setParameter(
            "contactId", String.valueOf(contact.getContactId()), false);
        portletURL.setWindowState(actionRequest.getWindowState());

        return portletURL.toString();
    }

    @Reference(unbind = "-")
    protected void setContactService(ContactService contactService) {
        this._contactService = contactService;
    }

    protected Contact updateContact(ActionRequest actionRequest) throws Exception {
        
        _log.info("updateContact");

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

            return contact;

        } catch (KeyFileFormatException kffe) {

            SessionErrors.add(actionRequest, "the-key-file-format-is-not-supported");

            return contact;

        } catch (SoundFileFormatException sffe) {

            SessionErrors.add(actionRequest, "the-sound-file-format-is-not-supported");

            return contact;
        }
        
        if (contactId <= 0) {

            // Add contact

            contact = _contactService.addContact(card, uid, serviceContext);


        } else {

            // Update contact

            contact = _contactService.updateContact(contactId, card, uid, serviceContext);

        }
                
        return contact;
    }
    
    private ContactService _contactService;

    private static Log _log = LogFactoryUtil.getLog(EditContactMVCActionCommand.class.getName()); 

}
