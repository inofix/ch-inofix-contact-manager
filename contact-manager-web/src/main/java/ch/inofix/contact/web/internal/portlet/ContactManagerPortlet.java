package ch.inofix.contact.web.internal.portlet;

import java.io.IOException;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.exception.NoSuchResourceException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;

import aQute.bnd.annotation.metatype.Configurable;
import ch.inofix.contact.exception.NoSuchContactException;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactService;
import ch.inofix.contact.web.configuration.ContactManagerConfiguration;
import ch.inofix.contact.web.internal.constants.ContactManagerWebKeys;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.Uid;

/**
 * View Controller of Inofix' timetracker.
 *
 * @author Stefan Luebbers
 * @author Christian Berndt
 * @created 2017-03-30 19:52
 * @modified 2017-04-10 15:42
 * @version 1.0.1
 */

@Component(immediate = true, property = { "com.liferay.portlet.css-class-wrapper=portlet-contact-manager",
        "com.liferay.portlet.display-category=category.inofix", "com.liferay.portlet.instanceable=false",
        "com.liferay.portlet.header-portlet-css=/css/main.css", "javax.portlet.display-name=ContactManager",
        "javax.portlet.init-param.template-path=/", "javax.portlet.init-param.view-template=/view.jsp",
        "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)
public class ContactManagerPortlet extends MVCPortlet {

    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

        renderRequest.setAttribute(ContactManagerConfiguration.class.getName(), _contactManagerConfiguration);

        super.doView(renderRequest, renderResponse);
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

    public void updateContact(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        _log.info("updateContact");

        long contactId = ParamUtil.getLong(actionRequest, "contactId");

        ServiceContext serviceContext = ServiceContextFactory.getInstance(Contact.class.getName(), actionRequest);

        Contact contact = null;

        VCard vCard = null;
        String uid = null;

        if (contactId > 0) {

            contact = _contactService.getContact(contactId);
            uid = contact.getUid();
            vCard = contact.getVCard();

        } else {

            vCard = new VCard();
            vCard.setUid(Uid.random());
            uid = vCard.getUid().getValue();

        }

        // Store contact information in vCard format

        String card = Ezvcard.write(vCard).version(VCardVersion.V4_0).go();

        _log.info(card);

        if (contactId <= 0) {

            // Add contact

            contact = _contactService.addContact(card, uid, serviceContext);

        } else {

            // Update contact

            contact = _contactService.updateContact(contactId, card, uid, serviceContext);
        }

        // TODO
        // String redirect = getEditContactURL(actionRequest, actionResponse,
        // contact);

        // actionRequest.setAttribute(WebKeys.REDIRECT, redirect);

    }

    @Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {
        _contactManagerConfiguration = Configurable.createConfigurable(ContactManagerConfiguration.class, properties);
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

    @Reference
    protected void setContactService(ContactService contactService) {
        this._contactService = contactService;
    }

    private ContactService _contactService;

    private volatile ContactManagerConfiguration _contactManagerConfiguration;

    private static final String REQUEST_PROCESSED = "request_processed";

    private static final Log _log = LogFactoryUtil.getLog(ContactManagerPortlet.class.getName());

}