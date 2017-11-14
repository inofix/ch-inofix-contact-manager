package ch.inofix.contact.web.internal.portlet.action;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactServiceUtil;

/**
 * 
 * @author Christian Berndt
 * @created 2017-11-14 00:41
 * @modified 2017-11-14 00:41
 * @version 1.0.0
 *
 */
public class ActionUtil {

    public static Contact getContact(HttpServletRequest request) throws Exception {

        long contactId = ParamUtil.getLong(request, "contactId");

        Contact contact = null;

        if (contactId > 0) {
            contact = ContactServiceUtil.getContact(contactId);

            // TODO: Add TrashBin support
            // if (contact.isInTrash()) {
            // throw new NoSuchContactException("{contactId=" +
            // contactId + "}");
            // }
        }

        return contact;
    }

    public static Contact getContact(PortletRequest portletRequest) throws Exception {

        HttpServletRequest request = PortalUtil.getHttpServletRequest(portletRequest);

        return getContact(request);
    }
}
