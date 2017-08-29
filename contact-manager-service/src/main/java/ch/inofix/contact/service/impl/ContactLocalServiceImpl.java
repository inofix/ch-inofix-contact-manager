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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetLinkConstants;
import com.liferay.exportimport.kernel.controller.ExportImportControllerRegistryUtil;
import com.liferay.exportimport.kernel.controller.ImportController;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManagerUtil;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
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
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.permission.ModelPermissions;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import aQute.bnd.annotation.ProviderType;
import ch.inofix.contact.background.task.ContactImportBackgroundTaskExecutor;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.base.ContactLocalServiceBaseImpl;
import ch.inofix.contact.social.ContactActivityKeys;

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
 * @author Christian Berndt
 * @author Stefan Luebbers
 * @created 2017-06-20 17:19
 * @modified 2017-07-04 17:15
 * @version 1.0.4
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

        // TODO
        // validate();

        long contactId = counterLocalService.increment();

        Contact contact = contactPersistence.create(contactId);

        contact.setUuid(serviceContext.getUuid());
        contact.setGroupId(groupId);
        contact.setCompanyId(user.getCompanyId());
        contact.setUserId(user.getUserId());
        contact.setUserName(user.getFullName());
        contact.setExpandoBridgeAttributes(serviceContext);

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

        JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject();

        // TODO
//        extraDataJSONObject.put("title", contact.getName());

        socialActivityLocalService.addActivity(userId, groupId, Contact.class.getName(), contactId,
                ContactActivityKeys.ADD_CONTACT, extraDataJSONObject.toString(), 0);

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

    @Override
    public void importContacts(ExportImportConfiguration exportImportConfiguration, File file) throws PortalException {

        try {
            ImportController contactImportController = ExportImportControllerRegistryUtil
                    .getImportController(Contact.class.getName());

            contactImportController.importFile(exportImportConfiguration, file);

        } catch (PortalException pe) {
            Throwable cause = pe.getCause();

            if (cause instanceof LocaleException) {
                throw (PortalException) cause;
            }

            throw pe;
        } catch (SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    @Override
    public void importContacts(ExportImportConfiguration exportImportConfiguration, InputStream inputStream)
            throws PortalException {

        File file = null;

        try {
            file = FileUtil.createTempFile("lar");

            FileUtil.write(file, inputStream);

            importContacts(exportImportConfiguration, file);

        } catch (IOException ioe) {
            throw new SystemException(ioe);
        } finally {
            FileUtil.delete(file);
        }
    }

    @Override
    public long importContactsInBackground(long userId, ExportImportConfiguration exportImportConfiguration, File file)
            throws PortalException {

        Map<String, Serializable> taskContextMap = new HashMap<>();

        taskContextMap.put("exportImportConfigurationId", exportImportConfiguration.getExportImportConfigurationId());

        BackgroundTask backgroundTask = BackgroundTaskManagerUtil.addBackgroundTask(userId,
                exportImportConfiguration.getGroupId(), exportImportConfiguration.getName(),
                ContactImportBackgroundTaskExecutor.class.getName(), taskContextMap, new ServiceContext());

        backgroundTask.addAttachment(userId, file.getName(), file);

        return backgroundTask.getBackgroundTaskId();
    }

    @Override
    public long importContactsInBackground(long userId, ExportImportConfiguration exportImportConfiguration,
            InputStream inputStream) throws PortalException {

        File file = null;

        try {

            // TODO: use format of uploaded file or .vcf
            file = FileUtil.createTempFile("lar");

            FileUtil.write(file, inputStream);

            return importContactsInBackground(userId, exportImportConfiguration, file);

        } catch (IOException ioe) {
            throw new SystemException(ioe);
        } finally {
            FileUtil.delete(file);
        }
    }

    @Override
    public Hits search(long userId, long groupId, String keywords, int start, int end, Sort sort)
            throws PortalException {

        if (sort == null) {
            sort = new Sort(Field.MODIFIED_DATE, true);
        }

        String company = null;
        String fullName = null;
        boolean andOperator = false;

        if (Validator.isNotNull(keywords)) {

            company = keywords;
            fullName = keywords;

        } else {
            andOperator = true;
        }

        return search(userId, groupId, 0, company, fullName, WorkflowConstants.STATUS_ANY, null, andOperator, start,
                end, sort);

    }

    @Override
    public Hits search(long userId, long groupId, long ownerUserId, String company, String fullName, int status,
            LinkedHashMap<String, Object> params, boolean andSearch, int start, int end, Sort sort)
            throws PortalException {

        if (sort == null) {
            sort = new Sort(Field.MODIFIED_DATE, true);
        }

        Indexer<Contact> indexer = IndexerRegistryUtil.getIndexer(Contact.class.getName());

        SearchContext searchContext = buildSearchContext(userId, groupId, ownerUserId, company, fullName, status,
                params, andSearch, start, end, sort);

        return indexer.search(searchContext);

    }

    @Override
    public void updateAsset(long userId, Contact contact, long[] assetCategoryIds, String[] assetTagNames,
            long[] assetLinkEntryIds, Double priority) throws PortalException {

        // TODO
        boolean visible = true;
        // boolean visible = false;
        // if (contact.isApproved()) {
        // visible = true;
        // publishDate = contact.getCreateDate();
        // }

        Date publishDate = contact.getCreateDate();

        // TODO
        String description = "TODO: contact description";
        String summary = HtmlUtil.extractText(StringUtil.shorten(contact.getCard(), 500));

        String className = Contact.class.getName();
        long classPK = contact.getContactId();

        AssetEntry assetEntry = assetEntryLocalService.updateEntry(userId, contact.getGroupId(),
                contact.getCreateDate(), contact.getModifiedDate(), className, classPK, contact.getUuid(), 0,
                assetCategoryIds, assetTagNames, true, visible, null, null, publishDate, null, ContentTypes.TEXT_HTML,
                // contact.getName(), 
                "TODO: contact.getName()",
                description, summary, null, null, 0, 0, priority);

        assetLinkLocalService.updateLinks(userId, assetEntry.getEntryId(), assetLinkEntryIds,
                AssetLinkConstants.TYPE_RELATED);

        // assetEntryLocalService.updateVisible(Contact.class.getName(),
        // classPK, visible);

    }

    @Override
    @Indexable(type = IndexableType.REINDEX)
    public Contact updateContact(long userId, long contactId, String card, String uid, ServiceContext serviceContext)
            throws PortalException {

        // Contact

        long groupId = serviceContext.getScopeGroupId();

        Contact contact = contactPersistence.findByPrimaryKey(contactId);

        // TODO: validate the vCard string

        contact.setGroupId(groupId);
        contact.setExpandoBridgeAttributes(serviceContext);
        contact.setCard(card);

        contactPersistence.update(contact);

        // Asset

        updateAsset(userId, contact, serviceContext.getAssetCategoryIds(), serviceContext.getAssetTagNames(),
                serviceContext.getAssetLinkEntryIds(), serviceContext.getAssetPriority());

        // Social

        JSONObject extraDataJSONObject = JSONFactoryUtil.createJSONObject();

//        extraDataJSONObject.put("title", contact.getName());

        socialActivityLocalService.addActivity(userId, groupId, Contact.class.getName(), contact.getContactId(),
                ContactActivityKeys.UPDATE_CONTACT, extraDataJSONObject.toString(), 0);

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

    protected SearchContext buildSearchContext(long userId, long groupId, long ownerUserId, String company,
            String fullName, int status, LinkedHashMap<String, Object> params, boolean andSearch, int start, int end,
            Sort sort) throws PortalException {

        SearchContext searchContext = new SearchContext();

        searchContext.setAttribute(Field.STATUS, status);

        if (Validator.isNotNull(company)) {
            searchContext.setAttribute("company", company);
        }

        if (Validator.isNotNull(fullName)) {
            searchContext.setAttribute("fullName", fullName);
        }

        searchContext.setAttribute("paginationType", "more");

        Group group = GroupLocalServiceUtil.getGroup(groupId);

        searchContext.setCompanyId(group.getCompanyId());

        if (ownerUserId > 0) {
            searchContext.setOwnerUserId(ownerUserId);
        }

        searchContext.setEnd(end);
        if (groupId > 0) {
            searchContext.setGroupIds(new long[] { groupId });
        }
        searchContext.setSorts(sort);
        searchContext.setStart(start);
        searchContext.setUserId(userId);

        searchContext.setAndSearch(andSearch);

        if (params != null) {

            String keywords = (String) params.remove("keywords");

            if (Validator.isNotNull(keywords)) {
                searchContext.setKeywords(keywords);
            }
        }

        QueryConfig queryConfig = new QueryConfig();

        queryConfig.setHighlightEnabled(false);
        queryConfig.setScoreEnabled(false);

        searchContext.setQueryConfig(queryConfig);

        if (sort != null) {
            searchContext.setSorts(sort);
        }

        searchContext.setStart(start);

        return searchContext;
    }

    private static Log _log = LogFactoryUtil.getLog(ContactLocalServiceImpl.class.getName());
}