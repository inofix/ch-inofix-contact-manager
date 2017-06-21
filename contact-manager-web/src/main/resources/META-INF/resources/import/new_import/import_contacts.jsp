<%--
    import_contacts.jsp: configure the contacts import.
    
    Created:    2017-06-19 23:44 by Christian Berndt
    Modified:   2017-06-20 18:33 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp" %>

<%@page import="javax.portlet.PortletRequest"%>

<%
    long groupId = ParamUtil.getLong(request, "groupId");
    boolean validate = ParamUtil.getBoolean(request, "validate", true);

    String[] tempFileNames = ContactServiceUtil.getTempFileNames(groupId, ExportImportHelper.TEMP_FOLDER_NAME);

    portletDisplay.setShowBackIcon(true);

    PortletURL importProcessesURL = PortalUtil.getControlPanelPortletURL(request,
            PortletKeys.CONTACT_MANAGER, PortletRequest.RENDER_PHASE);

    importProcessesURL.setParameter("mvcPath", "/import/view.jsp");

    portletDisplay.setURLBack(importProcessesURL.toString());

    renderResponse.setTitle(LanguageUtil.get(request, "new-import-process"));
%>

<div class="container-fluid-1280" id="<portlet:namespace />exportImportOptions">

    <%
    int incompleteBackgroundTaskCount = BackgroundTaskManagerUtil.getBackgroundTasksCount(groupId, ContactImportBackgroundTaskExecutor.class.getName(), false);
    %>

    <div class="<%= (incompleteBackgroundTaskCount == 0) ? "hide" : "in-progress" %>" id="<portlet:namespace />incompleteProcessMessage">
        <liferay-util:include page="/incomplete_processes_message.jsp" servletContext="<%= application %>">
            <liferay-util:param name="incompleteBackgroundTaskCount" value="<%= String.valueOf(incompleteBackgroundTaskCount) %>" />
        </liferay-util:include>
    </div>

    <c:choose>
        <c:when test="<%= (tempFileNames.length > 0) && !validate %>">
            <liferay-util:include page="/import/new_import/import_contacts_resources.jsp" servletContext="<%= application %>" />
        </c:when>
        <c:otherwise>
            <liferay-util:include page="/import/new_import/import_contacts_validation.jsp" servletContext="<%= application %>" />
        </c:otherwise>
    </c:choose>
</div>
