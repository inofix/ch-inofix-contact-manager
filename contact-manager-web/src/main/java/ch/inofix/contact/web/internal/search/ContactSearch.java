package ch.inofix.contact.web.internal.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortalPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import ch.inofix.contact.model.Contact;

/**
 *
 * @author Christian Berndt
 * @author Stefan Luebbers
 * @created 2015-05-24 22:01
 * @modified 2017-06-24 15:38
 * @version 1.1.1
 *
 */
public class ContactSearch extends SearchContainer<Contact> {

    public static final String EMPTY_RESULTS_MESSAGE = "no-contacts-were-found";

    static List<String> headerNames = new ArrayList<String>();
    static Map<String, String> orderableHeaders = new HashMap<String, String>();

    static {
        headerNames.add("company");
        headerNames.add("contact-id");
        headerNames.add("create-date");
        headerNames.add("email");
        // TODO: enable default fax
        // headerNames.add("fax");
        headerNames.add("full-name");
        // TODO: enable default impp
        // headerNames.add("impp");
        headerNames.add("modified-date");
        headerNames.add("name");
        headerNames.add("phone");
        // TODO: enable the portrait column
        // headerNames.add("portrait");
        headerNames.add("url");
        headerNames.add("user-name");

        orderableHeaders.put("company", "company_sortable");
        orderableHeaders.put("contact-id", "contact-id");
        orderableHeaders.put("create-date", "createDate_Number_sortable");
        orderableHeaders.put("email", "email_sortable");
        // TODO: enable default fax
        // orderableHeaders.put("fax", "fax");
        orderableHeaders.put("full-name", "fullName_sortable");
        // TODO: enable default impp
        // orderableHeaders.put("impp", "impp");
        orderableHeaders.put("modified-date", "modifiedDate_Number_sortable");
        orderableHeaders.put("name", "name");
        orderableHeaders.put("phone", "phone");
        orderableHeaders.put("url", "url_sortable");
        orderableHeaders.put("user-name", "userName_sortable");
    }

    public ContactSearch(PortletRequest portletRequest, PortletURL iteratorURL) {
        this(portletRequest, DEFAULT_CUR_PARAM, iteratorURL);
    }

    public ContactSearch(PortletRequest portletRequest, String curParam, PortletURL iteratorURL) {

        super(portletRequest, new ContactDisplayTerms(portletRequest), new ContactSearchTerms(portletRequest), curParam,
                DEFAULT_DELTA, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

        PortletConfig portletConfig = (PortletConfig) portletRequest.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);

        ContactDisplayTerms displayTerms = (ContactDisplayTerms) getDisplayTerms();
        ContactSearchTerms searchTerms = (ContactSearchTerms) getSearchTerms();

        String portletId = PortletProviderUtil.getPortletId(User.class.getName(), PortletProvider.Action.VIEW);
        String portletName = portletConfig.getPortletName();

        if (!portletId.equals(portletName)) {
            displayTerms.setStatus(WorkflowConstants.STATUS_APPROVED);
            searchTerms.setStatus(WorkflowConstants.STATUS_APPROVED);
        }

        iteratorURL.setParameter(ContactDisplayTerms.COMPANY, String.valueOf(displayTerms.getCompany()));
        iteratorURL.setParameter(ContactDisplayTerms.CONTACT_ID, String.valueOf(displayTerms.getContactId()));
        iteratorURL.setParameter(ContactDisplayTerms.CREATE_DATE, String.valueOf(displayTerms.getCreateDate()));
        iteratorURL.setParameter(ContactDisplayTerms.EMAIL, String.valueOf(displayTerms.getEmail()));
        iteratorURL.setParameter(ContactDisplayTerms.FAX, String.valueOf(displayTerms.getFax()));
        iteratorURL.setParameter(ContactDisplayTerms.FULL_NAME, String.valueOf(displayTerms.getFullName()));
        // TODO: add default impp
        iteratorURL.setParameter(ContactDisplayTerms.MODIFIED_DATE, String.valueOf(displayTerms.getModifiedDate()));
        iteratorURL.setParameter(ContactDisplayTerms.NAME, String.valueOf(displayTerms.getName()));
        iteratorURL.setParameter(ContactDisplayTerms.PHONE, String.valueOf(displayTerms.getPhone()));
        iteratorURL.setParameter(ContactDisplayTerms.USER_NAME, String.valueOf(displayTerms.getUserName()));

        try {
            PortalPreferences preferences = PortletPreferencesFactoryUtil.getPortalPreferences(portletRequest);

            String orderByCol = ParamUtil.getString(portletRequest, "orderByCol");
            String orderByType = ParamUtil.getString(portletRequest, "orderByType");

            if (Validator.isNotNull(orderByCol) && Validator.isNotNull(orderByType)) {

                preferences.setValue(portletId, "contacts-order-by-col", orderByCol);
                preferences.setValue(portletId, "contacts-order-by-type", orderByType);
            } else {
                orderByCol = preferences.getValue(portletId, "contacts-order-by-col", "modified-date");
                orderByType = preferences.getValue(portletId, "contacts-order-by-type", "asc");
            }

            setOrderableHeaders(orderableHeaders);

            if (Validator.isNotNull(orderableHeaders.get(orderByCol))) {
                setOrderByCol(orderableHeaders.get(orderByCol));
            } else {
                _log.error(orderByCol + " is not an orderable header.");
                setOrderByCol(orderByCol);
            }

            setOrderByType(orderByType);

        } catch (Exception e) {
            _log.error(e);
        }
    }

    private static Log _log = LogFactoryUtil.getLog(ContactSearch.class);

}
