<%--
    contact_action.jsp: The action menu of the contact manager's default view.
    
    Created:    2017-04-11 16:00 by Stefan Luebbers
    Modified:   2017-06-22 19:19 by Christian Berndt
    Version:    1.0.3
--%>

<%@ include file="/init.jsp"%>

<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>

<%
    ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

    Contact contact_ = (Contact) row.getObject();
    
    String editURL = (String) request.getAttribute("editURL");
    String viewURL = (String) request.getAttribute("viewURL");
    
    editURL = HttpUtil.setParameter(editURL, renderResponse.getNamespace() + "contactId", contact_.getContactId()); 
    viewURL = HttpUtil.setParameter(viewURL, renderResponse.getNamespace() + "contactId", contact_.getContactId()); 

    boolean hasUpdatePermission = ContactPermission.contains(permissionChecker, contact_,
            ContactActionKeys.UPDATE);
    boolean hasViewPermission = ContactPermission.contains(permissionChecker, contact_,
            ContactActionKeys.VIEW);
    boolean hasDeletePermission = ContactPermission.contains(permissionChecker, contact_,
            ContactActionKeys.DELETE);
    boolean hasPermissionsPermission = ContactPermission.contains(permissionChecker, contact_, 
            ContactActionKeys.PERMISSIONS);
%>

<liferay-ui:icon-menu showWhenSingleIcon="true">

    <c:if test="<%=hasViewPermission%>">

        <liferay-ui:icon iconCssClass="icon-eye-open" message="view" 
            url="<%=viewURL%>" />

    </c:if>

    <c:if test="<%=hasViewPermission%>">

        <portlet:resourceURL var="downloadVCardURL" id="serveVCard">
            <portlet:param name="contactId"
                value="<%= String.valueOf(contact_.getContactId()) %>" /> 
        </portlet:resourceURL>

        <liferay-ui:icon iconCssClass="icon-download" message="download" 
            url="<%=downloadVCardURL%>" />

    </c:if>

    <c:if test="<%=hasUpdatePermission%>">

        <liferay-ui:icon iconCssClass="icon-edit" message="edit" 
            url="<%=editURL%>" />

    </c:if>

    <c:if test="<%=hasDeletePermission%>">

        <portlet:actionURL var="deleteURL">
            <portlet:param name="cmd" value="<%= Constants.DELETE %>"/>
            <portlet:param name="redirect" value="<%=currentURL%>" />
            <portlet:param name="contactId"
                value="<%=String.valueOf(contact_.getContactId())%>" />
        </portlet:actionURL>

        <liferay-ui:icon-delete message="delete" url="<%=deleteURL%>" />

    </c:if>

    <c:if test="<%= hasPermissionsPermission %>">

        <liferay-security:permissionsURL
            modelResource="<%= Contact.class.getName() %>"
            modelResourceDescription="<%= String.valueOf(contact_.getContactId()) %>"
            resourcePrimKey="<%= String.valueOf(contact_.getContactId()) %>"
            var="permissionsEntryURL"
            windowState="<%= LiferayWindowState.POP_UP.toString() %>" />

        <liferay-ui:icon iconCssClass="icon-cog" message="permissions"
            method="get" url="<%= permissionsEntryURL %>"
            useDialog="<%= true %>" />
    </c:if>

</liferay-ui:icon-menu>
