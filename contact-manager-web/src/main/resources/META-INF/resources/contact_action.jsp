<%--
    task_record_action.jsp: The action menu of the timetrackers's default view.
    
    Created:    2017-04-11 16:00 by Stefan Luebbers
    Modified:   2017-04-12 10:47 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="init.jsp"%>

<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>

<%
    ResultRow row = (ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

    Contact contact_ = (Contact) row.getObject();
    
    String editURL = (String) request.getAttribute("editURL");
    String viewURL = (String) request.getAttribute("viewURL");
    
    editURL = HttpUtil.setParameter(editURL, renderResponse.getNamespace() + "contactId", contact_.getContactId()); 
    viewURL = HttpUtil.setParameter(viewURL, renderResponse.getNamespace() + "contactId", contact_.getContactId()); 
%>

<%
    String taglibEditURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "editTaskRecord', title: '" + HtmlUtil.escapeJS(LanguageUtil.format(request, "edit-x", contact_.getContactId())) + "', uri:'" + HtmlUtil.escapeJS(editURL) + "'});";            
    String taglibViewURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "viewTaskRecord', title: '" + HtmlUtil.escapeJS(LanguageUtil.format(request, "view-x", contact_.getContactId())) + "', uri:'" + HtmlUtil.escapeJS(viewURL) + "'});";

%>

<%
    boolean hasDeletePermission = ContactPermission.contains(permissionChecker,
            contact_.getContactId(), ContactActionKeys.DELETE);   
    boolean hasPermissionsPermission = ContactPermission.contains(permissionChecker,
            contact_.getContactId(), ContactActionKeys.PERMISSIONS);  
    boolean hasUpdatePermission = ContactPermission.contains(permissionChecker,
            contact_.getContactId(), ContactActionKeys.UPDATE);
    boolean hasViewPermission = ContactPermission.contains(permissionChecker,
            contact_.getContactId(), ContactActionKeys.VIEW);;
%>
<%--                             <liferay-ui:search-container-column-text align="right"> --%>
        
<%--                                 <liferay-ui:icon-menu> --%>
        
<%--                                     <c:if test="<%= hasUpdatePermission %>"> --%>
<%--                                         <liferay-ui:icon image="edit" url="<%=taglibEditURL%>" /> --%>
<%--                                     </c:if> --%>
<%--                                     <c:if test="<%= hasPermissionsPermission %>"> --%>
<%--                                         <liferay-ui:icon image="permissions" url="<%= permissionsURL %>" /> --%>
<%--                                     </c:if> --%>
<%--                                     <c:if test="<%= hasViewPermission %>"> --%>
<%--                                         <liferay-ui:icon image="view" url="<%=taglibViewURL%>" /> --%>
<%--                                     </c:if> --%>
<%--                                     <c:if test="<%= hasViewPermission %>"> --%>
<%--                                         <liferay-ui:icon image="download" url="<%= downloadVCardURL %>" /> --%>
<%--                                     </c:if> --%>
<%--                                     <c:if test="<%= hasDeletePermission %>"> --%>
<%--                                         <liferay-ui:icon-delete url="<%=deleteURL%>" /> --%>
<%--                                     </c:if> --%>
        
<%--                                 </liferay-ui:icon-menu> --%>
        
<%--                             </liferay-ui:search-container-column-text> --%>
<liferay-ui:icon-menu showWhenSingleIcon="true">

    <c:if test="<%=hasViewPermission%>">

        <liferay-ui:icon iconCssClass="icon-eye-open" message="view" 
            url="<%=taglibViewURL%>" />

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
            url="<%=taglibEditURL%>" />

    </c:if>

    <c:if test="<%=hasDeletePermission%>">

        <portlet:actionURL var="deleteURL" name="deleteContact">
            <portlet:param name="redirect" value="<%=currentURL%>" />
            <portlet:param name="contactId"
                value="<%=String.valueOf(contact_.getContactId())%>" />
        </portlet:actionURL>

        <liferay-ui:icon-delete message="delete" url="<%=deleteURL%>" />

    </c:if>

    <c:if test="<%= hasPermissionsPermission %>">
<% // TODO %>
<%--         <liferay-security:permissionsURL --%>
<%--             modelResource="<%= Contact.class.getName() %>" --%>
<%--             modelResourceDescription="<%= String.valueOf(contact_.getContactId()) %>" --%>
<%--             resourcePrimKey="<%= String.valueOf(contact_.getContactId()) %>" --%>
<%--             var="permissionsEntryURL" --%>
<%--             windowState="<%= LiferayWindowState.POP_UP.toString() %>" /> --%>

<%-- -
        <liferay-ui:icon iconCssClass="icon-cog" message="permissions"
            method="get" url="<%= permissionsEntryURL %>"
            useDialog="<%= true %>" />
--%>
    </c:if>

</liferay-ui:icon-menu>
