<%--
    view_contacts_descriptive.jsp: descriptive display stylle
    of Inofix' contact-manager.
    
    Created:     2017-08-30 21:47 by Christian Berndt
    Modified:    2017-09-21 19:11 by Christian Berndt
    Version:     1.0.1
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

<h4>
    <aui:a href="<%= rowURL.toString() %>">
        <%= contact_.getName() %>
    </aui:a>
</h4>

<c:if test="<%= Validator.isNotNull(contact_.getEmail().getAddress())  %>">
    <div class="email">
        <span class="icon-envelope"></span>
        <a href="mailto:<%= contact_.getEmail().getAddress() %>"><%= contact_.getEmail().getAddress() %></a>
    </div>
</c:if>

<c:if test="<%= Validator.isNotNull(contact_.getPhone().getNumber())  %>">
    <div class="phone">
        <span class="icon-phone"></span>
        <%= contact_.getPhone().getNumber() %>
    </div>
</c:if>

<c:if test="<%= Validator.isNotNull(contact_.getUrl())  %>">
    <div class="url">
        <span class="icon-globe"></span>
        <a href="<%= contact_.getUrl() %>" target="_blank"><%= contact_.getUrl() %></a>
    </div>
</c:if>

<h5 class="text-default">
    <aui:workflow-status markupView="<%= markupView %>" showIcon="<%= false %>" showLabel="<%= false %>" status="<%= contact_.getStatus() %>" />
</h5>
