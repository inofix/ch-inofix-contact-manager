<%--
    add_button.jsp: create a new import process
    
    Created:    2017-06-21 16:42 by Christian Berndt
    Modified:   2017-11-17 16:08 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp"%>

<%    
    // TODO: check permissions
%>

<liferay-frontend:add-menu>

    <portlet:renderURL var="addExportProcessURL">
        <portlet:param name="groupId" value="<%=String.valueOf(scopeGroupId)%>" />
        <portlet:param name="mvcPath" value="/export/new_export/export_contacts.jsp" />
        <portlet:param name="redirect" value="<%= currentURL %>"/>
        <portlet:param name="tabs1" value="export-import" />
    </portlet:renderURL>

    <liferay-frontend:add-menu-item title='<%= LanguageUtil.get(request, "new-export-process") %>' url="<%= addExportProcessURL.toString() %>" />
    
</liferay-frontend:add-menu>
