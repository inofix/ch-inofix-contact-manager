/**
 * Copyright (c) 2000-present Inofix Gmbh, Luzern. All rights reserved.
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
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetLinkConstants;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.ModelPermissions;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.File;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.base.ContactLocalServiceBaseImpl;

/**
 * The implementation of the contact local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link ch.inofix.contact.service.ContactLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security
 * checks based on the propagated JAAS credentials because this service can only
 * be accessed from within the same VM.
 * </p>
 *
 * @author Stefan Luebbers
 * @see ContactLocalServiceBaseImpl
 * @see ch.inofix.contact.service.ContactLocalServiceUtil
 */
@ProviderType
public class ContactLocalServiceImpl extends ContactLocalServiceBaseImpl {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never reference this class directly. Always use {@link
     * ch.inofix.contact.service.ContactLocalServiceUtil} to access the contact
     * local service.
     */
    @Override
    @Indexable(type = IndexableType.REINDEX)
    public Contact addContact(long userId, String card, String uid, ServiceContext serviceContext)
            throws PortalException {

        // Contact

        User user = userPersistence.findByPrimaryKey(userId);
        long groupId = serviceContext.getScopeGroupId();

        long contactId = counterLocalService.increment();

        Contact contact = contactPersistence.create(contactId);

        Date now = new Date();

        contact.setUuid(serviceContext.getUuid());
        contact.setGroupId(groupId);
        contact.setCompanyId(user.getCompanyId());
        contact.setUserId(user.getUserId());
        contact.setUserName(user.getFullName());
        contact.setExpandoBridgeAttributes(serviceContext);
        contact.setCreateDate(now);
        contact.setModifiedDate(now);

        // TODO: validate the vCard string
        contact.setCard(card);
        contact.setUid(uid);

        contact = contactPersistence.update(contact);

        // Resources

        if (serviceContext.isAddGroupPermissions() || serviceContext.isAddGuestPermissions()) {
            addContactResources(contact, serviceContext.isAddGroupPermissions(),
                    serviceContext.isAddGuestPermissions());
        } else {
            addContactResources(contact, serviceContext.getModelPermissions());
        }

        // Asset

        updateAsset(userId, contact, serviceContext.getAssetCategoryIds(), serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds(), serviceContext.getAssetPriority());

        // Social

        // TODO reactivate socialActivity
        // JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject();
        //
        // extraDataJSONObject.put("title", contact.getFullName(true));
        //
        // socialActivityLocalService.addActivity(userId, groupId,
        // Contact.class.getName(), contact.getContactId(),
        // ContactActivityKeys.ADD_CONTACT,
        // extraDataJSONObject.toString(), 0);

        return contact;
    }

    @Override
    public void addContactResources(Contact contact, boolean addGroupPermissions, boolean addGuestPermissions)
            throws PortalException {

        resourceLocalService.addResources(contact.getCompanyId(), contact.getGroupId(), contact.getUserId(),
                Contact.class.getName(), contact.getContactId(), false, addGroupPermissions, addGuestPermissions);
    }

    @Override
    public void addContactResources(Contact contact, ModelPermissions modelPermissions) throws PortalException {

        resourceLocalService.addModelResources(contact.getCompanyId(), contact.getGroupId(), contact.getUserId(),
                Contact.class.getName(), contact.getContactId(), modelPermissions);
    }

    @Override
    public void addContactResources(long contactId, boolean addGroupPermissions, boolean addGuestPermissions)
            throws PortalException {

        Contact contact = contactPersistence.findByPrimaryKey(contactId);

        addContactResources(contact, addGroupPermissions, addGuestPermissions);
    }

    @Override
    public void addContactResources(long contactId, ModelPermissions modelPermissions) throws PortalException {

        Contact contact = contactPersistence.findByPrimaryKey(contactId);

        addContactResources(contact, modelPermissions);
    }

    @Override
    public Contact deleteContact(long contactId) throws PortalException {

        Contact contact = contactPersistence.findByPrimaryKey(contactId);

        return deleteContact(contact);

    }

    @Override
    public List<Contact> deleteGroupContacts(long groupId) throws PortalException {

        List<Contact> contactList = contactPersistence.findByGroupId(groupId);

        for (Contact contact : contactList) {

            // TODO differ exception types
            try {
                deleteContact(contact);
            } catch (Exception e) {
                _log.error(e);
            }
        }

        return contactList;

    }

    @Indexable(type = IndexableType.DELETE)
    @Override
    @SystemEvent(type = SystemEventConstants.TYPE_DELETE)
    public Contact deleteContact(Contact contact) throws PortalException {

        // Contact

        contactPersistence.remove(contact);

        // Resource

        resourceLocalService.deleteResource(contact.getCompanyId(), Contact.class.getName(),
                ResourceConstants.SCOPE_INDIVIDUAL, contact.getContactId());

        // Asset

        assetEntryLocalService.deleteEntry(Contact.class.getName(), contact.getContactId());

        // TODO remove from socialActivityService?

        return contact;
    }

    /**
     * @param groupId
     * @param uid
     * @since 1.0.0
     */
    @Override
    @Deprecated
    public Contact getContact(long groupId, String uid) throws PortalException {

        return contactPersistence.findByG_U(groupId, uid);

    }

    /**
     * Return all contacts which belong to the current group.
     *
     * @param groupId
     * @return all contacts which belong to the current group.
     * @since 1.0.0
     */
    @Override
    public List<Contact> getGroupContacts(long groupId) throws PortalException {

        return contactPersistence.findByGroupId(groupId);

    }

    /**
     * Imports the contacts from the file.
     *
     * @param userId
     *            the primary key of the user
     * @param groupId
     *            the primary key of the group
     * @param privateLayout
     *            whether the layout is private to the group
     * @param parameterMap
     *            the mapping of parameters indicating which information will be
     *            imported.
     * @param file
     *            the file with the data
     * @since 1.0.7
     * @throws PortalException
     *             if a group or user with the primary key could not be found,
     *             or if some other portal exception occurred
     * @see com.liferay.portal.lar.LayoutImporter
     */
    @Override
    public void importContacts(long userId, long groupId, boolean privateLayout, Map<String, String[]> parameterMap,
            File file) throws PortalException {

        // TODO Upgrade import

        // try {
        // ContactImporter contactImporter = new ContactImporter();
        //
        // contactImporter.importContacts(userId, groupId, privateLayout,
        // parameterMap, file);
        // } catch (PortalException pe) {
        // Throwable cause = pe.getCause();
        //
        // if (cause instanceof LocaleException) {
        // throw (PortalException) cause;
        // }
        //
        // _log.error(pe);
        //
        // throw pe;
        // } catch (SystemException se) {
        //
        // _log.error(se);
        //
        // throw se;
        // } catch (Exception e) {
        //
        // _log.error(e);
        //
        // throw new SystemException(e);
        // }
    }

    /**
     * Imports the contacts from the input stream.
     *
     * @param userId
     *            the primary key of the user
     * @param groupId
     *            the primary key of the group
     * @param privateLayout
     *            whether the layout is private to the group
     * @param parameterMap
     *            the mapping of parameters indicating which information will be
     *            imported.
     * @param is
     *            the input stream
     * @since 1.0.7
     * @throws PortalException
     *             if a group or user with the primary key could not be found,
     *             or if some other portal exception occurred
     * @throws SystemException
     *             if a system exception occurred
     */
    @Override
    public void importContacts(long userId, long groupId, boolean privateLayout, Map<String, String[]> parameterMap,
            InputStream is) throws PortalException {

        // TODO Upgrade import

        // File file = null;
        //
        // try {
        // file = FileUtil.createTempFile("vcf");
        //
        // FileUtil.write(file, is);
        //
        // importContacts(userId, groupId, privateLayout, parameterMap, file);
        //
        // } catch (IOException ioe) {
        //
        // _log.error(ioe);
        //
        // throw new SystemException(ioe);
        // } finally {
        // FileUtil.delete(file);
        // }
    }

    /**
     * @param userId
     * @param taskName
     * @param groupId
     * @param privateLayout
     * @param parameterMap
     * @param file
     * @return
     * @since 1.0.7
     * @throws PortalException
     * @throws SystemException
     */
    @Override
    public long importContactsInBackground(long userId, String taskName, long groupId, boolean privateLayout,
            Map<String, String[]> parameterMap, File file) throws PortalException {

        // TODO Upgrade import

        // Map<String, Serializable> taskContextMap = new HashMap<String,
        // Serializable>();
        // taskContextMap.put("userId", userId);
        // taskContextMap.put("groupId", groupId);
        // taskContextMap.put("parameterMap", (Serializable) parameterMap);
        // taskContextMap.put("privateLayout", privateLayout);
        //
        // String[] servletContextNames =
        // parameterMap.get("servletContextNames");
        //
        // BackgroundTask backgroundTask = BackgroundTaskLocalServiceUtil
        // .addBackgroundTask(userId, groupId, taskName,
        // servletContextNames,
        // ContactImportBackgroundTaskExecutor.class,
        // taskContextMap, new ServiceContext());
        //
        // BackgroundTaskLocalServiceUtil.addBackgroundTaskAttachment(userId,
        // backgroundTask.getBackgroundTaskId(), taskName, file);
        //
        // return backgroundTask.getBackgroundTaskId();
        return 0;
    }
    
    @Override
    public Hits search(long userId, long groupId, String keywords, int start, int end, Sort sort)
            throws PortalException {

        if (sort == null) {
            sort = new Sort(Field.MODIFIED_DATE, true);
        }

        Indexer<Contact> indexer = IndexerRegistryUtil.getIndexer(Contact.class.getName());

        SearchContext searchContext = new SearchContext();

        searchContext.setAttribute(Field.STATUS, WorkflowConstants.STATUS_ANY);

        searchContext.setAttribute("paginationType", "more");

        Group group = GroupLocalServiceUtil.getGroup(groupId);

        searchContext.setCompanyId(group.getCompanyId());

        searchContext.setEnd(end);
        if (groupId > 0) {
            searchContext.setGroupIds(new long[] { groupId });
        }
        searchContext.setSorts(sort);
        searchContext.setStart(start);
        searchContext.setUserId(userId);

        searchContext.setKeywords(keywords);

        return indexer.search(searchContext);

    }

    @Indexable(type = IndexableType.REINDEX)
    @Override
    public void updateAsset(long userId, Contact contact, long[] assetCategoryIds, String[] assetTagNames,
            long[] assetLinkEntryIds, Double priority) throws PortalException {

        boolean visible = true;
        boolean listable = true;

        long classTypeId = 0;
        Date startDate = null;
        Date endDate = null;
        Date expirationDate = null;
        String mimeType = "text/x-vcard";
        String title = contact.getFullName(true);
        String description = contact.getFormattedName();
        String summary = HtmlUtil.extractText(StringUtil.shorten(contact.getFormattedName(), 500));
        // TODO: What does url mean in this context?
        String url = null;
        // TODO: What does layoutUuid mean in this context?
        String layoutUuid = null;
        int height = 0;
        int width = 0;

        AssetEntry assetEntry = assetEntryLocalService.updateEntry(userId, contact.getGroupId(),
                contact.getCreateDate(), contact.getModifiedDate(), Contact.class.getName(), contact.getContactId(),
                contact.getUuid(), classTypeId, assetCategoryIds, assetTagNames, listable, visible, startDate, endDate,
                expirationDate, mimeType, title, description, summary, url, layoutUuid, height, width, priority);

        assetLinkLocalService.updateLinks(userId, assetEntry.getEntryId(), assetLinkEntryIds,
                AssetLinkConstants.TYPE_RELATED);
    }

    /**
     * @since 1.0.0
     */
    @Override
    @Indexable(type = IndexableType.REINDEX)
    public Contact updateContact(long userId, long contactId, String card, String uid, ServiceContext serviceContext)
            throws PortalException {

        // Contact

        User user = userPersistence.findByPrimaryKey(userId);

        long groupId = serviceContext.getScopeGroupId();

        Contact contact = contactPersistence.findByPrimaryKey(contactId);

        Date now = new Date();

        contact.setUuid(serviceContext.getUuid());
        contact.setGroupId(groupId);
        contact.setCompanyId(user.getCompanyId());
        contact.setUserId(user.getUserId());
        contact.setUserName(user.getFullName());
        contact.setExpandoBridgeAttributes(serviceContext);
        contact.setModifiedDate(now);

        // TODO: validate the vCard string
        contact.setCard(card);
        contact.setUid(uid);

        contactPersistence.update(contact);

        // Resources
        // TODO resourceLocalService.updateModel instead?
        resourceLocalService.addModelResources(contact, serviceContext);
        // TODO add resourceLocalService.updateResources?

        // Asset

        resourceLocalService.updateResources(serviceContext.getCompanyId(), serviceContext.getScopeGroupId(),
                contact.getFullName(), contactId, serviceContext.getGroupPermissions(),
                serviceContext.getGuestPermissions());

        updateAsset(userId, contact, serviceContext.getAssetCategoryIds(), serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds(), serviceContext.getAssetPriority()); // TODO

        // Social

        // TODO activate social
        // JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject();
        //
        // extraDataJSONObject.put("title", contact.getFullName(true));
        //
        // socialActivityLocalService.addActivity(userId, groupId,
        // Contact.class.getName(), contact.getContactId(),
        // ContactActivityKeys.UPDATE_CONTACT,
        // extraDataJSONObject.toString(), 0);

        return contact;
    }

    @Override
    public void updateContactResources(Contact contact, ModelPermissions modelPermissions) throws PortalException {

        resourceLocalService.updateResources(contact.getCompanyId(), contact.getGroupId(), Contact.class.getName(),
                contact.getContactId(), modelPermissions);
    }

    @Override
    public void updateContactResources(Contact contact, String[] groupPermissions, String[] guestPermissions)
            throws PortalException {

        resourceLocalService.updateResources(contact.getCompanyId(), contact.getGroupId(), Contact.class.getName(),
                contact.getContactId(), groupPermissions, guestPermissions);
    }

    private static Log _log = LogFactoryUtil.getLog(ContactLocalServiceImpl.class.getName());
}