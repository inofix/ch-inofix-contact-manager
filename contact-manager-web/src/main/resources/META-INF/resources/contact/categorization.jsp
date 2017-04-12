<%--
    categorization.jsp: Edit the contact's categorization 
    
    Created:    2015-05-08 18:02 by Christian Berndt
    Modified:   2017-04-12 17:05 by Christian Berndt
    Version:    1.1.3
--%>

<%@ include file="/init.jsp"%>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    if (contact_ == null) {
        contact_ = ContactServiceUtil.createContact();
    }

    // TODO: check permissions
    boolean hasUpdatePermission = true; 
%>

<aui:model-context model="<%= Contact.class %>"/>

<%-- <liferay-ui:panel title="categorization"> --%>
    <aui:fieldset label="categorization">
    
        <aui:input classPK="<%=contact_.getContactId()%>" name="categories"
            type="assetCategories" inlineField="true"
            disabled="<%= !hasUpdatePermission %>"/>
        
        <%-- Always disabled, since the tags are managed via --%>
        <%-- the vCard's categories.                         --%>
        <%--         
        <aui:input classPK="<%=contact_.getContactId()%>" name="tags"
            type="assetTags" helpMessage="asset-tags-help" 
            disabled="<%= !hasUpdatePermission %>"/> 
        --%>
    </aui:fieldset>
<%-- </liferay-ui:panel> --%>