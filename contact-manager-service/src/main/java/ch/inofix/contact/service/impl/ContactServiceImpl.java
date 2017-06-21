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

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Digester;
import com.liferay.portal.kernel.util.DigesterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.TempFileEntryUtil;

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
 * @modified 2017-06-20 18:07
 * @version 1.0.6
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
    public FileEntry addTempFileEntry(long groupId, String folderName, String fileName, InputStream inputStream,
            String mimeType) throws PortalException {

        _log.info("addTempFileEntry");

        // TODO: re-enable permission check
        // ContactManagerPermission.check(getPermissionChecker(), groupId,
        // ContactActionKeys.IMPORT_CONTACTS);

        return TempFileEntryUtil.addTempFileEntry(groupId, getUserId(),
                DigesterUtil.digestHex(Digester.SHA_256, folderName), fileName, inputStream, mimeType);
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
    public void deleteTempFileEntry(long groupId, String folderName, String fileName) throws PortalException {

        // TODO: re-enable permission check
        // ContactManagerPermission.check(getPermissionChecker(), groupId,
        // ContactActionKeys.IMPORT_CONTACTS);

        TempFileEntryUtil.deleteTempFileEntry(groupId, getUserId(),
                DigesterUtil.digestHex(Digester.SHA_256, folderName), fileName);
    }

    @Override
    public Contact getContact(long contactId) throws PortalException {

        ContactPermission.check(getPermissionChecker(), contactId, ContactActionKeys.VIEW);

        return contactLocalService.getContact(contactId);

    }

    @Override
    public String[] getTempFileNames(long groupId, String folderName) throws PortalException {

        // TODO: re-enable permission checks
        // ContactManagerPermission.check(getPermissionChecker(), groupId,
        // ContactActionKeys.EXPORT_IMPORT_CONTACTS);

        return TempFileEntryUtil.getTempFileNames(groupId, getUserId(),
                DigesterUtil.digestHex(Digester.SHA_256, folderName));
    }

    @Override
    public long importContactsInBackground(ExportImportConfiguration exportImportConfiguration, InputStream inputStream)
            throws PortalException {

        _log.info("importContactsInBackground()");

        Map<String, Serializable> settingsMap = exportImportConfiguration.getSettingsMap();

        long targetGroupId = MapUtil.getLong(settingsMap, "targetGroupId");

        _log.info("targetGroupId = " + targetGroupId);

        // TODO: re-enable permission checks
        // ContactManagerPermission.check(getPermissionChecker(), targetGroupId,
        // ContactActionKeys.IMPORT_CONTACTS);

        return contactLocalService.importContactsInBackground(getUserId(), exportImportConfiguration, inputStream);
    }

    @Override
    public Hits search(long userId, long groupId, String keywords, int start, int end, Sort sort)
            throws PortalException {

        return contactLocalService.search(userId, groupId, keywords, start, end, sort);
    }

    @Override
    public Contact updateContact(long contactId, String card, String uid, ServiceContext serviceContext)
            throws PortalException {

        ContactPermission.check(getPermissionChecker(), contactId, ContactActionKeys.UPDATE);

        return contactLocalService.updateContact(getUserId(), contactId, card, uid, serviceContext);

    }

    private static Log _log = LogFactoryUtil.getLog(ContactServiceImpl.class.getName());
}