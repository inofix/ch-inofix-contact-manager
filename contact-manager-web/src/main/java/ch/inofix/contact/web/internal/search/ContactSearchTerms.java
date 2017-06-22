package ch.inofix.contact.web.internal.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DAOParamUtil;

/**
 *
 * @author Christian Berndt
 * @author Stefan Luebbers
 * @created 2015-05-24 22:14
 * @modified 2017-06-22 16:36
 * @version 1.0.5
 *
 */
public class ContactSearchTerms extends ContactDisplayTerms {

    public ContactSearchTerms(PortletRequest portletRequest) {

        super(portletRequest);

        company = DAOParamUtil.getString(portletRequest, COMPANY);
        contactId = DAOParamUtil.getString(portletRequest, CONTACT_ID);
        createDate = DAOParamUtil.getString(portletRequest, CREATE_DATE);
        email = DAOParamUtil.getString(portletRequest, EMAIL);
        fax = DAOParamUtil.getString(portletRequest, FAX);
        fullName = DAOParamUtil.getString(portletRequest, FULL_NAME);
        // TODO: set default impp
        modifiedDate = DAOParamUtil.getString(portletRequest, MODIFIED_DATE);
        name = DAOParamUtil.getString(portletRequest, NAME);
        phone = DAOParamUtil.getString(portletRequest, PHONE);
        status = DAOParamUtil.getInteger(portletRequest, STATUS);
        userName = DAOParamUtil.getString(portletRequest, USER_NAME);

    }
}
