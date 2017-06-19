<%--
    toolbar.jsp: The toolbar of the contact-manager portlet
    
    Created:    2015-07-03 19:00 by Christian Berndt
    Modified:   2017-06-19 15:57 by Christian Berndt
    Version:    1.0.3
 --%>

<%@ include file="init.jsp"%>

<%@page import="com.liferay.trash.kernel.util.TrashUtil"%>

<%
    String[] columns = (String[])request.getAttribute("view.jsp-columns");

    String orderByCol = ParamUtil.getString(request, "orderByCol", "modified-date");

    String orderByType = ParamUtil.getString(request, "orderByType", "desc");

    String searchContainerId = ParamUtil.getString(request, "searchContainerId");
    
    int total = GetterUtil.getInteger(request.getAttribute("view.jsp-total"));
    
    PortletURL portletURL = renderResponse.createRenderURL();
    portletURL.setParameters(renderRequest.getParameterMap());
    
%>

<liferay-frontend:management-bar 
    disabled="<%= total == 0 %>"
    includeCheckBox="<%= true %>"
    searchContainerId="<%= searchContainerId %>">

    <liferay-frontend:management-bar-filters>
        <liferay-frontend:management-bar-sort
            orderByCol="<%= orderByCol %>"
            orderByType="<%= orderByType %>"
            orderColumns='<%= columns %>' portletURL="<%= portletURL %>" />
    </liferay-frontend:management-bar-filters>

    <liferay-frontend:management-bar-buttons>
        <%-- 
        <liferay-frontend:management-bar-button
            disabled="<%=total == 0%>"
            href="<%=downloadURL.toString()%>" icon="download"
            label="download" />
        --%>
        <liferay-util:include page="/display_style_buttons.jsp"
            servletContext="<%=application%>" />
    </liferay-frontend:management-bar-buttons>

</liferay-frontend:management-bar>

