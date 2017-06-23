package ch.inofix.contact.search;

import java.util.LinkedHashMap;
import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactLocalService;
import ch.inofix.contact.service.permission.ContactPermission;

/**
 *
 * @author Christian Berndt
 * @author Stefan Luebbers
 * @created 2015-05-20 13:28
 * @modified 2017-06-23 20:34
 * @version 1.1.2
 *
 */
@Component(immediate = true, service = Indexer.class)
public class ContactIndexer extends BaseIndexer<Contact> {

    public static final String CLASS_NAME = Contact.class.getName();

    public ContactIndexer() {
        setDefaultSelectedFieldNames(Field.ASSET_TAG_NAMES, Field.COMPANY_ID, Field.ENTRY_CLASS_NAME,
                Field.ENTRY_CLASS_PK, Field.GROUP_ID, Field.MODIFIED_DATE, Field.SCOPE_GROUP_ID, Field.TITLE, Field.UID,
                Field.URL);
        setFilterSearch(true);
        setPermissionAware(true);
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    @Override
    public boolean hasPermission(PermissionChecker permissionChecker, String entryClassName, long entryClassPK,
            String actionId) throws Exception {

        return ContactPermission.contains(permissionChecker, entryClassPK, ActionKeys.VIEW);
    }

    @Override
    public void postProcessContextBooleanFilter(BooleanFilter contextBooleanFilter, SearchContext searchContext)
            throws Exception {

        addStatus(contextBooleanFilter, searchContext);

    }

    @Override
    public void postProcessSearchQuery(BooleanQuery searchQuery, BooleanFilter fullQueryBooleanFilter,
            SearchContext searchContext) throws Exception {

        addSearchTerm(searchQuery, searchContext, "company", false);
        addSearchTerm(searchQuery, searchContext, "fullName", false);

        LinkedHashMap<String, Object> params = (LinkedHashMap<String, Object>) searchContext.getAttribute("params");

        if (params != null) {
            String expandoAttributes = (String) params.get("expandoAttributes");

            if (Validator.isNotNull(expandoAttributes)) {
                addSearchExpando(searchQuery, searchContext, expandoAttributes);
            }
        }
    }

    @Override
    protected void doDelete(Contact contact) throws Exception {
        deleteDocument(contact.getCompanyId(), contact.getContactId());
    }

    @Override
    protected Document doGetDocument(Contact contact) throws Exception {

        Document document = getBaseModelDocument(CLASS_NAME, contact);

        // Set document field values (in alphabetical order)

        document.addTextSortable("company", contact.getCompany());
        document.addNumberSortable("contactId", contact.getContactId());
        document.addText(Field.CONTENT, contact.getCard());
        document.addDateSortable(Field.CREATE_DATE, contact.getCreateDate());
        document.addText(Field.DESCRIPTION, "TODO: contact description");
        document.addTextSortable("email", contact.getEmail().getAddress());
        // TODO: add default fax
        document.addTextSortable("fullName", contact.getFullName(false));
        // document.addKeyword(Field.GROUP_ID,
        // getSiteGroupId(contact.getGroupId()));
        // TODO add default impp
        document.addDateSortable("modifiedDate", contact.getModifiedDate());
        document.addTextSortable(Field.NAME, contact.getName());
        document.addTextSortable("phone", contact.getPhone().getNumber());
        document.addNumber(Field.STATUS, contact.getStatus());
        // document.addKeyword(Field.SCOPE_GROUP_ID, contact.getGroupId());
        document.addText(Field.TITLE, contact.getName());
        document.addTextSortable("url", contact.getUrl());
        document.addKeyword("vCardUID", contact.getUid());
        document.addTextSortable("x-salutation", contact.getSalutation());

        return document;
    }

    @Override
    protected Summary doGetSummary(Document document, Locale locale, String snippet, PortletRequest portletRequest,
            PortletResponse portletResponse) throws Exception {

        Summary summary = createSummary(document, Field.TITLE, Field.CONTENT);

        return summary;
    }

    @Override
    protected void doReindex(Contact contact) throws Exception {

        Document document = getDocument(contact);

        _indexWriterHelper.updateDocument(getSearchEngineId(), contact.getCompanyId(), document, isCommitImmediately());
    }

    @Override
    protected void doReindex(String[] ids) throws Exception {

        long companyId = GetterUtil.getLong(ids[0]);

        reindexContacts(companyId);
    }

    @Override
    protected void doReindex(String className, long classPK) throws Exception {

        Contact contact = _contactLocalService.getContact(classPK);

        doReindex(contact);
    }

    protected void reindexContacts(long companyId) throws PortalException {

        final IndexableActionableDynamicQuery indexableActionableDynamicQuery = _contactLocalService
                .getIndexableActionableDynamicQuery();

        indexableActionableDynamicQuery.setAddCriteriaMethod(new ActionableDynamicQuery.AddCriteriaMethod() {

            @Override
            public void addCriteria(DynamicQuery dynamicQuery) {

                Property statusProperty = PropertyFactoryUtil.forName("status");

                Integer[] statuses = { WorkflowConstants.STATUS_APPROVED, WorkflowConstants.STATUS_IN_TRASH };

                dynamicQuery.add(statusProperty.in(statuses));
            }

        });
        indexableActionableDynamicQuery.setCompanyId(companyId);
        // TODO: what does setGroupId()?
        // indexableActionableDynamicQuery.setGroupId(groupId);
        indexableActionableDynamicQuery
                .setPerformActionMethod(new ActionableDynamicQuery.PerformActionMethod<Contact>() {

                    @Override
                    public void performAction(Contact contact) {
                        try {
                            Document document = getDocument(contact);

                            indexableActionableDynamicQuery.addDocuments(document);
                        } catch (PortalException pe) {
                            if (_log.isWarnEnabled()) {
                                _log.warn("Unable to index bookmarks contact " + contact.getContactId(), pe);
                            }
                        }
                    }

                });
        indexableActionableDynamicQuery.setSearchEngineId(getSearchEngineId());

        indexableActionableDynamicQuery.performActions();
    }

    @Reference(unbind = "-")
    protected void setContactLocalService(ContactLocalService contactLocalService) {

        _contactLocalService = contactLocalService;
    }

    private static final Log _log = LogFactoryUtil.getLog(ContactIndexer.class);

    private ContactLocalService _contactLocalService;

    @Reference
    private IndexWriterHelper _indexWriterHelper;
}
