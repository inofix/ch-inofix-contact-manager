<%--
    full_content.jsp: full-content contact asset-renderer template

    Created:     2017-06-23 14:21 by Christian Berndt
    Modified:    2017-06-23 14:21 by Christian Berndt
    Version:     1.0.0
--%>

<%@ include file="/init.jsp" %>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    Portlet portlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), portletDisplay.getId());
    
    // TODO: add css config for full-content view
%>

<%-- 
<liferay-util:html-top outputKey="blogs_common_main_css">
    <link href="<%= PortalUtil.getStaticResourceURL(request, application.getContextPath() + "/blogs/css/common_main.css", portlet.getTimestamp()) %>" rel="stylesheet" type="text/css" />
</liferay-util:html-top>
--%>

<div class="portlet-contact-manager">

    <div class="entry-body">
        <%
            // TODO: i
            String subtitle = null; // no subtitle
        %>

        <c:if test="<%= Validator.isNotNull(subtitle) %>">
            <div class="entry-subtitle">
                <p><%= HtmlUtil.escape(subtitle) %></p>
            </div>
        </c:if>

        <div class="entry-date icon-calendar">
            <span class="hide-accessible"><liferay-ui:message key="create-date" /></span>

            <%= dateFormatDateTime.format(contact_.getCreateDate()) %>
        </div>
        
        <div class="entry-date icon-calendar">
            <span class="hide-accessible"><liferay-ui:message key="modified-date" /></span>

            <%= dateFormatDateTime.format(contact_.getModifiedDate()) %>
        </div>

        <%= contact_.getVCard().writeHtml() %>  
        
    </div>
</div>
