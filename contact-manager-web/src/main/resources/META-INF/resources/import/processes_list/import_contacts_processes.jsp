<%--
    import_contacts_processes.jsp: list of import processes
    
    Created:    2017-06-19 23:32 by Christian Berndt
    Modified:   2017-06-19 23:32 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp" %>

<%@page import="com.liferay.portal.kernel.backgroundtask.BackgroundTask"%>
<%@page import="com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker"%>
<%@page import="com.liferay.background.task.kernel.util.comparator.BackgroundTaskComparatorFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.portletfilerepository.PortletFileRepositoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.OrderByComparator"%>
<%@page import="com.liferay.portal.kernel.util.StringBundler"%>

<%
    long groupId = ParamUtil.getLong(request, "groupId");
    String displayStyle = ParamUtil.getString(request, "displayStyle");
    String navigation = ParamUtil.getString(request, "navigation");
    String orderByCol = ParamUtil.getString(request, "orderByCol");
    String orderByType = ParamUtil.getString(request, "orderByType");
    String searchContainerId = ParamUtil.getString(request, "searchContainerId");
    
    PortletURL portletURL = liferayPortletResponse.createRenderURL();
    
    portletURL.setParameter("groupId", String.valueOf(groupId));
    portletURL.setParameter("displayStyle", displayStyle);
    portletURL.setParameter("navigation", navigation);
    portletURL.setParameter("orderByCol", orderByCol);
    portletURL.setParameter("orderByType", orderByType);
    portletURL.setParameter("searchContainerId", searchContainerId);
    
    OrderByComparator<BackgroundTask> orderByComparator = BackgroundTaskComparatorFactoryUtil.getBackgroundTaskOrderByComparator(orderByCol, orderByType);
%>

<portlet:actionURL var="deleteBackgroundTasksURL">
    <portlet:param name="redirect" value="<%= currentURL.toString() %>" />
</portlet:actionURL>

<aui:form action="<%= deleteBackgroundTasksURL %>" method="get" name="fm">

    <aui:input name="<%= Constants.CMD %>" type="hidden" value="deleteBackgroundTasks" />
    <aui:input name="redirect" type="hidden" value="<%= currentURL.toString() %>" />
    <aui:input name="deleteBackgroundTaskIds" type="hidden" />
    
    <liferay-ui:search-container
        emptyResultsMessage="no-import-processes-were-found"
        id="<%= searchContainerId %>"
        iteratorURL="<%= portletURL %>"
        orderByCol="<%= orderByCol %>"
        orderByComparator="<%= orderByComparator %>"
        orderByType="<%= orderByType %>"
        rowChecker="<%= new EmptyOnClickRowChecker(liferayPortletResponse) %>"
    >
        <liferay-ui:search-container-results>
        
        <%
            int backgroundTasksCount = 0;
            List<BackgroundTask> backgroundTasks = null;

            if (navigation.equals("all")) {
                backgroundTasksCount = BackgroundTaskManagerUtil.getBackgroundTasksCount(groupId, ContactImportBackgroundTaskExecutor.class.getName());
                backgroundTasks = BackgroundTaskManagerUtil.getBackgroundTasks(groupId, ContactImportBackgroundTaskExecutor.class.getName(), searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
            }
            else {
                boolean completed = false;

                if (navigation.equals("completed")) {
                    completed = true;
                }

                backgroundTasksCount = BackgroundTaskManagerUtil.getBackgroundTasksCount(groupId, ContactImportBackgroundTaskExecutor.class.getName(), completed);
                backgroundTasks = BackgroundTaskManagerUtil.getBackgroundTasks(groupId, ContactImportBackgroundTaskExecutor.class.getName(), completed, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator());
            }

            searchContainer.setResults(backgroundTasks);
            searchContainer.setTotal(backgroundTasksCount);
        %>
    
        </liferay-ui:search-container-results>
        
        <liferay-ui:search-container-row
            className="com.liferay.portal.kernel.backgroundtask.BackgroundTask"
            keyProperty="backgroundTaskId"
            modelVar="backgroundTask"
        >

            <%
            String backgroundTaskName = backgroundTask.getName();

            if (backgroundTaskName.equals(StringPool.BLANK)) {
                backgroundTaskName = LanguageUtil.get(request, "untitled");
            }
            %>
        </liferay-ui:search-container-row>
        
        <liferay-ui:search-iterator displayStyle="<%= displayStyle %>" markupView="<%= markupView %>" />
<%--         <liferay-ui:search-iterator displayStyle="<%= displayStyle %>" markupView="lexicon" resultRowSplitter="<%= new ImportImportResultRowSplitter() %>" /> --%>        
    </liferay-ui:search-container>
</aui:form>
