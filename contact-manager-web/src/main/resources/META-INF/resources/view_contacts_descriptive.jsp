<%--
    view_contacts_descriptive.jsp: descriptive display stylle
    of Inofix' contact-manager.
    
    Created:     2017-08-30 21:47 by Christian Berndt
    Modified:    2017-08-30 21:47 by Christian Berndt
    Version:     1.0.0
--%>

<%@ include file="/init.jsp" %>

<%@page import="java.util.Date"%>

<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>

<%
    ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

    Contact contact_ = (Contact) row.getObject();

    PortletURL rowURL = liferayPortletResponse.createRenderURL();
    
    Date modifiedDate = contact_.getModifiedDate();

    String modifiedDateDescription = LanguageUtil.getTimeDescription(request, System.currentTimeMillis() - modifiedDate.getTime(), true);

%>

<h5 class="text-default">
    <liferay-ui:message arguments="<%= new String[] {HtmlUtil.escape(contact_.getName()), modifiedDateDescription} %>" key="x-modified-x-ago" />
</h5>

<h4>
    <aui:a href="<%= rowURL.toString() %>">
        <%= contact_.getName() %>
    </aui:a>
</h4>

<h5 class="text-default">
    <aui:workflow-status markupView="<%= markupView %>" showIcon="<%= false %>" showLabel="<%= false %>" status="<%= contact_.getStatus() %>" />
</h5>
