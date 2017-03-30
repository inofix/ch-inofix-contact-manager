package ch.inofix.contact.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactLocalServiceUtil;

/**
*
* @author Stefan Luebbers
* @created 2017-03-30 14:10
* @modified 2017-03-30 14:10
* @version 1.0.0
*
*/
public class ContactPermission {

    public static void check(PermissionChecker permissionChecker, long contactId, String actionId)
            throws PrincipalException {

        if (!contains(permissionChecker, contactId, actionId)) {
            throw new PrincipalException();
        }
    }

    public static boolean contains(PermissionChecker permissionChecker, Contact contact, String actionId) {

        if (permissionChecker.hasOwnerPermission(contact.getCompanyId(), Contact.class.getName(),
                contact.getContactId(), contact.getUserId(), actionId)) {

            return true;
        }

        return permissionChecker.hasPermission(contact.getGroupId(), Contact.class.getName(), contact.getContactId(),
                actionId);

    }

    public static boolean contains(PermissionChecker permissionChecker, long contactId, String actionId) {

        Contact contact;
        try {
            contact = ContactLocalServiceUtil.getContact(contactId);
            return contains(permissionChecker, contact, actionId);
        } catch (PortalException e) {
            _log.error(e);
        }

        return false;

    }

    private static final Log _log = LogFactoryUtil.getLog(ContactPermission.class.getName());
}
