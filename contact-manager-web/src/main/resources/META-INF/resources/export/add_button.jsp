<%--
    add_button.jsp: create a new export process
    
    Created:    2017-06-21 16:42 by Christian Berndt
    Modified:   2017-11-19 13:55 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/init.jsp"%>

<%    
    boolean hasExportPermission = ContactManagerPortletPermission.contains(permissionChecker, scopeGroupId,
            ContactManagerActionKeys.EXPORT_CONTACTS);
%>

<liferay-frontend:add-menu>

    <portlet:renderURL var="addExportProcessURL">
        <portlet:param name="groupId" value="<%=String.valueOf(scopeGroupId)%>" />
        <portlet:param name="mvcRenderCommandName" value="exportContacts" />
        <portlet:param name="redirect" value="<%= currentURL %>"/>
        <portlet:param name="tabs1" value="export-import" />
    </portlet:renderURL>

    <c:if test="<%= hasExportPermission %>">
        <liferay-frontend:add-menu-item  title='<%= LanguageUtil.get(request, "new-export-process") %>' url="<%= addExportProcessURL.toString() %>" />
    </c:if>
        
</liferay-frontend:add-menu>
