/**
 * Copyright (c) 2000-present Inofix GmbH, Luzern. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package ch.inofix.contact.service.impl;

import java.util.List;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.contact.constants.ContactActionKeys;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.base.ContactServiceBaseImpl;
import ch.inofix.contact.service.permission.ContactManagerPermission;
import ch.inofix.contact.service.permission.ContactPermission;

/**
 * The implementation of the contact remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.contact.service.ContactService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 *
 * @author Christian Berndt
 * @author Stefan Luebbers
 * @created 2015-05-07 23:50
 * @modified 2017-04-10 15:55
 * @version 1.0.5
 * @see ContactServiceBaseImpl
 * @see ch.inofix.contact.service.ContactServiceUtil
 */
@ProviderType
public class ContactServiceImpl extends ContactServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. Always use {@link
     * ch.inofix.contact.service.ContactServiceUtil} to access the contact
     * remote service.
     */
    @Override
    public Contact addContact(String card, String uid, ServiceContext serviceContext) throws PortalException {

        ContactManagerPermission.check(getPermissionChecker(), serviceContext.getScopeGroupId(),
                ContactActionKeys.ADD_CONTACT);

        return contactLocalService.addContact(getUserId(), card, uid, serviceContext);

    }

    @Override
    @Deprecated
    public Contact createContact() throws PortalException {

        // Create an empty contact - no permission check required
        return contactLocalService.createContact(0);

    }

    @Override
    public List<Contact> deleteAllContacts(long groupId) throws PortalException {

        ContactManagerPermission.check(getPermissionChecker(), groupId, ContactActionKeys.DELETE_CONTACTS);

        return contactLocalService.deleteGroupContacts(groupId);
    }

    @Override
    public Contact deleteContact(long contactId) throws PortalException {

        ContactPermission.check(getPermissionChecker(), contactId, ContactActionKeys.DELETE);

        Contact contact = contactLocalService.deleteContact(contactId);

        return contact;

    }

    @Override
    public Contact getContact(long contactId) throws PortalException {

        ContactPermission.check(getPermissionChecker(), contactId, ContactActionKeys.VIEW);

        return contactLocalService.getContact(contactId);

    }

    // TODO reactivate import
    // public long importContactsInBackground(long userId, String taskName,
    // long groupId, boolean privateLayout,
    // Map<String, String[]> parameterMap, File file)
    // throws PortalException {
    //
    // ContactPortletPermission.check(getPermissionChecker(), groupId,
    // ActionKeys.IMPORT_CONTACTS);
    //
    // return ContactLocalServiceUtil.importContactsInBackground(userId,
    // taskName, groupId, privateLayout, parameterMap, file);
    //
    // }

    @Override
    public Contact updateContact(long contactId, String card, String uid, ServiceContext serviceContext)
            throws PortalException {

        ContactPermission.check(getPermissionChecker(), contactId, ContactActionKeys.UPDATE);

        return contactLocalService.updateContact(getUserId(), contactId, card, uid, serviceContext);

    }

    private static Log log = LogFactoryUtil.getLog(ContactServiceImpl.class.getName());
}