<%--
    export/processes_list/view.jsp: list export processes
    
    Created:    2017-06-21 16:48 by Christian Berndt
    Modified:   2017-11-14 18:35 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp"%>

<%
    String displayStyle = ParamUtil.getString(request, "displayStyle", "descriptive");
    String navigation = ParamUtil.getString(request, "navigation", "all");
    String orderByCol = ParamUtil.getString(request, "orderByCol");
    String orderByType = ParamUtil.getString(request, "orderByType");
    String searchContainerId = ParamUtil.getString(request, "searchContainerId");
%>

<div id="<portlet:namespace />exportProcessesSearchContainer">
    <liferay-util:include page="/export_import_toolbar.jsp" servletContext="<%= application %>">
        <liferay-util:param name="mvcRenderCommandName" value="exportContacts" />
            <liferay-util:param name="groupId" value="<%= String.valueOf(scopeGroupId) %>" />
        <liferay-util:param name="displayStyle" value="<%= displayStyle %>" />
        <liferay-util:param name="navigation" value="<%= navigation %>" />
        <liferay-util:param name="orderByCol" value="<%= orderByCol %>" />
        <liferay-util:param name="orderByType" value="<%= orderByType %>" />
        <liferay-util:param name="searchContainerId" value="<%= searchContainerId %>" />
    </liferay-util:include>

    <div class="container-fluid-1280" id="<portlet:namespace />processesContainer">
        <liferay-util:include page="/export/processes_list/export_contacts_processes.jsp" servletContext="<%= application %>">
            <liferay-util:param name="groupId" value="<%= String.valueOf(scopeGroupId) %>" />
            <liferay-util:param name="displayStyle" value="<%= displayStyle %>" />
            <liferay-util:param name="navigation" value="<%= navigation %>" />
            <liferay-util:param name="orderByCol" value="<%= orderByCol %>" />
            <liferay-util:param name="orderByType" value="<%= orderByType %>" />
            <liferay-util:param name="searchContainerId" value="<%= searchContainerId %>" />
        </liferay-util:include>
    </div>
</div>
