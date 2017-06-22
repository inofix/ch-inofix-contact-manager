package ch.inofix.contact.web.internal.search;

import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 *
 * @author Christian Berndt
 * @author Stefan Luebbers
 * @created 2015-05-24 22:12
 * @modified 2017-06-22 16:51
 * @version 1.0.6
 *
 */
public class ContactDisplayTerms extends DisplayTerms {

    public static final String COMPANY = "company";
    public static final String CONTACT_ID = "contactId";
    public static final String CREATE_DATE = "createDate";
    public static final String EMAIL = "email";
    public static final String FAX = "fax";
    public static final String FULL_NAME = "fullName";
    // TODO: add default IMPP
    public static final String MODIFIED_DATE = "modifiedDate";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String STATUS = "status";
    public static final String USER_NAME = "userName";

    public ContactDisplayTerms(PortletRequest portletRequest) {
        super(portletRequest);

        company = ParamUtil.getString(portletRequest, COMPANY);
        contactId = ParamUtil.getString(portletRequest, CONTACT_ID);
        createDate = ParamUtil.getString(portletRequest, CREATE_DATE);
        email = ParamUtil.getString(portletRequest, EMAIL);
        fax = ParamUtil.getString(portletRequest, FAX);
        fullName = ParamUtil.getString(portletRequest, FULL_NAME);
        // TODO: add default IMPP
        name = ParamUtil.getString(portletRequest, NAME);
        modifiedDate = ParamUtil.getString(portletRequest, MODIFIED_DATE);
        String statusString = ParamUtil.getString(portletRequest, STATUS);

        if (Validator.isNotNull(statusString)) {
            status = GetterUtil.getInteger(statusString);
        }

        phone = ParamUtil.getString(portletRequest, PHONE);
        userName = ParamUtil.getString(portletRequest, USER_NAME);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    protected String company;
    protected String contactId;
    protected String createDate;
    protected String email;
    protected String fax;
    protected String fullName;
    protected String modifiedDate;
    protected String name;
    protected String phone;
    protected int status;
    protected String userName;

    private static Log _log = LogFactoryUtil.getLog(ContactDisplayTerms.class);

}