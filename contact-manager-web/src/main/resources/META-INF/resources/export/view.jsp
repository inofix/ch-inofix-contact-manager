<%--
    view.jsp: the export view of the contact-manager.
    
    Created:    2017-06-21 16:44 by Christian Berndt
    Modified:   2017-11-17 16:07 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/init.jsp" %>

<%
    String displayStyle = ParamUtil.getString(request, "displayStyle", "descriptive");
    String navigation = ParamUtil.getString(request, "navigation", "all");

    String orderByCol = ParamUtil.getString(request, "orderByCol");
    String orderByType = ParamUtil.getString(request, "orderByType");

    if (Validator.isNotNull(orderByCol) && Validator.isNotNull(orderByType)) {
        portalPreferences.setValue(PortletKeys.BACKGROUND_TASK, "entries-order-by-col", orderByCol);
        portalPreferences.setValue(PortletKeys.BACKGROUND_TASK, "entries-order-by-type", orderByType);
    } else {
        orderByCol = portalPreferences.getValue(PortletKeys.BACKGROUND_TASK, "entries-order-by-col",
                "create-date");
        orderByType = portalPreferences.getValue(PortletKeys.BACKGROUND_TASK, "entries-order-by-type", "desc");
    }

    String searchContainerId = "exportContactProcesses";
%>


<c:choose>
    <c:when test="<%= false %>">
<%--     <c:when test="<%= !GroupPermissionUtil.contains(permissionChecker, liveGroupId, ActionKeys.EXPORT_IMPORT_LAYOUTS) %>"> --%>
        <div class="alert alert-info">
            <liferay-ui:message key="you-do-not-have-permission-to-access-the-requested-resource" />
        </div>
    </c:when>
    <c:otherwise>
        <liferay-util:include page="/export/processes_list/view.jsp" servletContext="<%= application %>">
            <liferay-util:param name="displayStyle" value="<%= displayStyle %>" />
            <liferay-util:param name="navigation" value="<%= navigation %>" />
            <liferay-util:param name="orderByCol" value="<%= orderByCol %>" />
            <liferay-util:param name="orderByType" value="<%= orderByType %>" />
            <liferay-util:param name="searchContainerId" value="<%= searchContainerId %>" />
        </liferay-util:include>

        <liferay-util:include page="/export/add_button.jsp" servletContext="<%= application %>">
            <liferay-util:param name="displayStyle" value="<%= displayStyle %>" />
        </liferay-util:include>
    </c:otherwise>
</c:choose>

<aui:script use="liferay-export-import">

    <liferay-portlet:resourceURL copyCurrentRenderParameters="<%= false %>" id="exportContacts" var="exportProcessesURL">
        <portlet:param name="<%= SearchContainer.DEFAULT_CUR_PARAM %>" value="<%= ParamUtil.getString(request, SearchContainer.DEFAULT_CUR_PARAM) %>" />
        <portlet:param name="<%= SearchContainer.DEFAULT_DELTA_PARAM %>" value="<%= ParamUtil.getString(request, SearchContainer.DEFAULT_DELTA_PARAM) %>" />
        <portlet:param name="displayStyle" value="<%= displayStyle %>" />
        <portlet:param name="navigation" value="<%= navigation %>" />
        <portlet:param name="orderByCol" value="<%= orderByCol %>" />
        <portlet:param name="orderByType" value="<%= orderByType %>" />
        <portlet:param name="searchContainerId" value="<%= searchContainerId %>" />
        <portlet:param name="tabs1" value="<%= tabs1 %>" />
    </liferay-portlet:resourceURL>

    new Liferay.ExportImport(
        {
            exportLAR: true,
            incompleteProcessMessageNode: '#<portlet:namespace />incompleteProcessMessage',
            locale: '<%= locale.toLanguageTag() %>',
            namespace: '<portlet:namespace />',
            processesNode: '#exportProcessesSearchContainer',
            processesResourceURL: '<%= HtmlUtil.escapeJS(exportProcessesURL.toString()) %>'
        }
    );
</aui:script>
