<%--
    edit_contact.jsp: edit a single contact. 
    
    Created:    2015-05-07 23:40 by Christian Berndt
    Modified:   2017-04-12 15:10 by Christian Berndt
    Version:    1.1.8
--%>

<%@ include file="/init.jsp"%>

<%@page import="ch.inofix.contact.web.servlet.taglib.ui.FormNavigatorConstants"%>


<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    if (contact_ == null) {
        contact_ = ContactServiceUtil.createContact();
    }

    // TODO: check permissions
    boolean hasUpdatePermission = true; 

    String redirect = ParamUtil.getString(request, "redirect");

    String backURL = ParamUtil.getString(request, "backURL", redirect);
%>

<portlet:actionURL name="updateContact" var="updateContactURL"
    windowState="<%=LiferayWindowState.POP_UP.toString() %>">
    <portlet:param name="mvcPath" value="/edit_contact.jsp" />
</portlet:actionURL>

<liferay-ui:header title="contact-manager" backURL="<%=backURL%>"
    showBackURL="<%=true%>" />

<aui:form method="post" action="<%=updateContactURL%>" name="fm">

    <aui:input name="contactId" type="hidden"
        value="<%=String.valueOf(contact_.getContactId())%>" />
        
    <liferay-ui:form-navigator
        showButtons="<%= hasUpdatePermission %>"
        id="<%=FormNavigatorConstants.FORM_NAVIGATOR_ID_CONTACT%>" />

</aui:form>

