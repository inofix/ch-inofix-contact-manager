<%--
    edit_contact.jsp: edit a single contact. 
    
    Created:    2015-05-07 23:40 by Christian Berndt
    Modified:   2017-07-02 21:11 by Christian Berndt
    Version:    1.2.5
--%>

<%@ include file="init.jsp"%>

<%@page import="ch.inofix.contact.web.servlet.taglib.ui.FormNavigatorConstants"%>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    String title = LanguageUtil.get(request, "new-contact"); 

    if (contact_ == null) {
        contact_ = ContactServiceUtil.createContact();
    } else {
        title = contact_.getName();
    }

    String redirect = ParamUtil.getString(request, "redirect");

    String backURL = ParamUtil.getString(request, "backURL", redirect);

    portletDisplay.setShowBackIcon(true);
    portletDisplay.setURLBack(redirect);
    
    renderResponse.setTitle(title);
    
    request.setAttribute("showTitle", "true");  // used by the inofix-theme

    boolean hasUpdatePermission = ContactPermission.contains(permissionChecker, contact_,
            ContactActionKeys.UPDATE);
    boolean hasViewPermission = ContactPermission.contains(permissionChecker, contact_, ContactActionKeys.VIEW);
    boolean hasDeletePermission = ContactPermission.contains(permissionChecker, contact_,
            ContactActionKeys.DELETE);
    boolean hasPermissionsPermission = ContactPermission.contains(permissionChecker, contact_,
            ContactActionKeys.PERMISSIONS);
%>

<div class="container-fluid-1280">

    <portlet:actionURL var="updateContactURL">
        <portlet:param name="mvcPath" value="/edit_contact.jsp" />
    </portlet:actionURL>

    <aui:form method="post" action="<%=updateContactURL%>" name="fm">

        <aui:input name="backURL" type="hidden" value="<%= backURL %>" />
        <aui:input name="cmd" type="hidden"
            value="<%= Constants.UPDATE %>" />
        <aui:input name="contactId" type="hidden"
            value="<%=String.valueOf(contact_.getContactId())%>" />
        <aui:input name="redirect" type="hidden" value="<%= redirect %>" />

        <div class="lfr-form-content">

            <liferay-ui:form-navigator
                id="<%=FormNavigatorConstants.FORM_NAVIGATOR_ID_CONTACT%>"
                markupView="<%= markupView %>"
                showButtons="<%=hasUpdatePermission%>" />

        </div>

    </aui:form>

</div>
