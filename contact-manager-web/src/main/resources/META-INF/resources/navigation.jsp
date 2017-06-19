<%--
    navigation.jsp: Default navigation of Inofix' contact-manager.
    
    Created:     2017-06-18 23:56 by Christian Berndt
    Modified:    2017-06-18 23:56 by Christian Berndt
    Version:     1.0.0
--%>

<%@ include file="/init.jsp" %>

<%
    PortletURL portletURL = renderResponse.createRenderURL();
    portletURL.setParameter("tabs1", "contacts");
    
    PortletURL exportImportURL = renderResponse.createRenderURL();
    exportImportURL.setParameter("tabs1", "export-import"); 
%>

<aui:nav-bar cssClass="collapse-basic-search" markupView="<%= markupView %>">

    <aui:nav cssClass="navbar-nav">
        <aui:nav-item href="<%= portletURL.toString() %>" label="contacts" selected="<%= "contacts".equals(tabs1) %>" />
        <aui:nav-item href="<%= exportImportURL.toString()  %>" label="export-import" selected="<%= "export-import".equals(tabs1) %>" />
    </aui:nav>

    <aui:nav-bar-search>
        <liferay-portlet:renderURL varImpl="searchURL">
            <portlet:param name="redirect" value="<%= currentURL %>" />
        </liferay-portlet:renderURL>

        <aui:form action="<%= searchURL.toString() %>" cssClass="contact-search" name="searchFm">
        
            <liferay-frontend:management-bar-button cssClass="btn-xs" href='<%= portletURL.toString() %>' icon='times' label='clear' />
        
            <liferay-ui:search-form            
                page="/search_bar.jsp"
                servletContext="<%= application %>"/>
        </aui:form>

    </aui:nav-bar-search>
</aui:nav-bar>
