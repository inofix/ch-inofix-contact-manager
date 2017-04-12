package ch.inofix.contact.web.internal.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

import ch.inofix.contact.model.Contact;

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

/**
 * 
 * @author Christian Berndt
 * @author Stefan Luebbers
 * @created 2015-05-24 22:01
 * @modified 2017-04-11 16:45
 * @version 1.0.6
 *
 */
public class ContactSearch extends SearchContainer<Contact> {

    public static final String EMPTY_RESULTS_MESSAGE = "no-contacts-were-found";
    

    static List<String> headerNames = new ArrayList<String>();
    static Map<String, String> orderableHeaders = new HashMap<String, String>();

    // The list of header names corresponds bean properties of
    // ch.inofix.portlet.contact.model.ContactImpl
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
        headerNames.add("portrait");
        headerNames.add("status"); 
        headerNames.add("user-name");

        orderableHeaders.put("company", "company");
        orderableHeaders.put("contact-id", "contact-id");
        orderableHeaders.put("create-date", "create-date");
        orderableHeaders.put("email", "email");
        // TODO: enable default fax
        // orderableHeaders.put("fax", "fax");
        orderableHeaders.put("full-name", "full-name");
        // TODO: enable default impp
        // orderableHeaders.put("impp", "impp");
        orderableHeaders.put("modified-date", "modified-date");
        orderableHeaders.put("name", "name");
        orderableHeaders.put("phone", "phone");
        orderableHeaders.put("status", "status");
        orderableHeaders.put("user-name", "user-name");
    }

    

    public ContactSearch(PortletRequest portletRequest, PortletURL iteratorURL) {
        this(portletRequest, DEFAULT_CUR_PARAM, iteratorURL);
    }

    public ContactSearch(PortletRequest portletRequest, String curParam,
            PortletURL iteratorURL) {

        super(portletRequest, new ContactDisplayTerms(portletRequest),
                new ContactSearchTerms(portletRequest), curParam,
                DEFAULT_DELTA, iteratorURL, headerNames, EMPTY_RESULTS_MESSAGE);

        PortletConfig portletConfig = (PortletConfig) portletRequest.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);

        ContactDisplayTerms displayTerms = (ContactDisplayTerms) getDisplayTerms();
        ContactSearchTerms searchTerms = (ContactSearchTerms) getSearchTerms();

        String portletId = PortletProviderUtil.getPortletId(User.class.getName(), PortletProvider.Action.VIEW);
        String portletName = portletConfig.getPortletName();

        iteratorURL.setParameter(ContactDisplayTerms.COMPANY,
                String.valueOf(displayTerms.getCompany()));
        iteratorURL.setParameter(ContactDisplayTerms.CONTACT_ID,
                String.valueOf(displayTerms.getContactId()));
        iteratorURL.setParameter(ContactDisplayTerms.CREATE_DATE,
                String.valueOf(displayTerms.getCreateDate()));
        iteratorURL.setParameter(ContactDisplayTerms.EMAIL,
                String.valueOf(displayTerms.getEmail()));
        iteratorURL.setParameter(ContactDisplayTerms.FAX,
                String.valueOf(displayTerms.getFax()));
        iteratorURL.setParameter(ContactDisplayTerms.FULL_NAME,
                String.valueOf(displayTerms.getFullName()));
        // TODO: add default impp
        iteratorURL.setParameter(ContactDisplayTerms.MODIFIED_DATE,
                String.valueOf(displayTerms.getModifiedDate()));
        iteratorURL.setParameter(ContactDisplayTerms.NAME,
                String.valueOf(displayTerms.getName()));
        iteratorURL.setParameter(ContactDisplayTerms.PHONE,
                String.valueOf(displayTerms.getPhone()));
        iteratorURL.setParameter(ContactDisplayTerms.USER_NAME,
                String.valueOf(displayTerms.getUserName()));

        try {
            PortalPreferences preferences = PortletPreferencesFactoryUtil
                    .getPortalPreferences(portletRequest);

            String orderByCol = ParamUtil.getString(portletRequest,
                    "orderByCol");
            String orderByType = ParamUtil.getString(portletRequest,
                    "orderByType");

            if (Validator.isNotNull(orderByCol)
                    && Validator.isNotNull(orderByType)) {

                preferences.setValue(portletId,
                        "contacts-order-by-col", orderByCol);
                preferences.setValue(portletId,
                        "contacts-order-by-type", orderByType);
            } else {
                orderByCol = preferences.getValue(
                        portletId,
                        "contacts-order-by-col", "last-name");
                orderByType = preferences.getValue(
                        portletId,
                        "contacts-order-by-type", "asc");
            }

            setOrderableHeaders(orderableHeaders);
            setOrderByCol(orderByCol);
            setOrderByType(orderByType);

        } catch (Exception e) {
            log.error(e);
        }
    }
    // Enable logging for this class
    private static Log log = LogFactoryUtil.getLog(ContactSearch.class);

}
