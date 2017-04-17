package ch.inofix.contact.service.permission;

import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.BaseResourcePermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import ch.inofix.contact.constants.PortletKeys;

/**
 *
 * @author Stefan Luebbers
 * @author Christian Berndt
 * @created 2017-03-30 14:10
 * @modified 2017-04-14 00:15
 * @version 1.0.1
 *
 */
public class ContactManagerPermission extends BaseResourcePermissionChecker {

    public static final String RESOURCE_NAME = "ch.inofix.contact";

    public static void check(PermissionChecker permissionChecker, long groupId, String actionId)
            throws PrincipalException {

        if (!contains(permissionChecker, groupId, actionId)) {
            throw new PrincipalException();
        }
    }

    public static boolean contains(PermissionChecker permissionChecker, long groupId, String actionId) {

        return contains(permissionChecker, RESOURCE_NAME, PortletKeys.CONTACT_MANAGER, groupId, actionId);
    }

    @Override
    public Boolean checkResource(PermissionChecker permissionChecker, long classPK, String actionId) {

        return contains(permissionChecker, classPK, actionId);
    }
}
