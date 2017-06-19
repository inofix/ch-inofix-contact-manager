<%--
    view.jsp: the import view of the contact-manager.
    
    Created:    2017-06-19 23:16 by Christian Berndt
    Modified:   2017-06-19 23:16 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp" %>

<%
    String displayStyle = ParamUtil.getString(request, "displayStyle", "list");
    long groupId = scopeGroupId;
    String navigation = ParamUtil.getString(request, "navigation", "all");
    String orderByCol = ParamUtil.getString(request, "orderByCol", "create-date");
    String orderByType = ParamUtil.getString(request, "orderByType", "desc");
    String searchContainerId = "importContacthProcesses";
    tabs1 = ParamUtil.getString(request, "tabs1"); 
    tabs2 = ParamUtil.getString(request, "tabs2"); 

//     PortletURL portletURL = renderResponse.createRenderURL();

//     portletURL.setParameter("groupId", String.valueOf(groupId));
//     portletURL.setParameter("displayStyle", displayStyle);
//     portletURL.setParameter("mvcPath", "/import/view.jsp"); 
//     portletURL.setParameter("navigation", navigation);
//     portletURL.setParameter("orderByCol", orderByCol);
//     portletURL.setParameter("orderByType", orderByType);
//     portletURL.setParameter("searchContainerId", searchContainerId);
//     portletURL.setParameter("tabs1", tabs1);
//     portletURL.setParameter("tabs2", tabs2);
    
%>

<c:choose>
    <c:when test="<%=!ContactManagerPermission.contains(permissionChecker, scopeGroupId, ContactActionKeys.EXPORT_IMPORT_CONTACTS)%>">
        <div class="alert alert-info">
            <liferay-ui:message key="you-do-not-have-permission-to-access-the-requested-resource" />
        </div>
    </c:when>
    <c:otherwise>
        <liferay-util:include page="/import/processes_list/view.jsp" servletContext="<%= application %>">
            <liferay-util:param name="displayStyle" value="<%= displayStyle %>" />
            <liferay-util:param name="navigation" value="<%= navigation %>" />
            <liferay-util:param name="orderByCol" value="<%= orderByCol %>" />
            <liferay-util:param name="orderByType" value="<%= orderByType %>" />
            <liferay-util:param name="searchContainerId" value="<%= searchContainerId %>" />
        </liferay-util:include>

        <liferay-util:include page="/import/add_button.jsp" servletContext="<%= application %>">
            <liferay-util:param name="groupId" value="<%= String.valueOf(groupId) %>" />
            <liferay-util:param name="displayStyle" value="<%= displayStyle %>" />
        </liferay-util:include>
    </c:otherwise>
</c:choose>