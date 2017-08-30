<%--
    display_styles_buttons.jsp: Select the display style of the Timetracker.
    
    Created:    2017-06-19 16:55 by Christian Berndt
    Modified:   2017-08-30 21:44 by Christian Berndt
    Version:    1.0.1
--%>

<%@ include file="/init.jsp" %>

<%
    String navigation = ParamUtil.getString(request, "navigation", "all");

    String displayStyle = ParamUtil.getString(request, "displayStyle");

    if (Validator.isNull(displayStyle)) {
        displayStyle = portalPreferences.getValue(PortletKeys.CONTACT_MANAGER, "display-style", "list");
    }
    
    PortletURL displayStyleURL = renderResponse.createRenderURL();
%>

<liferay-frontend:management-bar-display-buttons
    displayViews='<%= new String[] {"icon", "descriptive", "list"} %>'
    portletURL="<%= displayStyleURL %>"
    selectedDisplayStyle="<%= displayStyle %>"
/>
