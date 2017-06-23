<%--
    search.jsp: The extended search of the contact-manager portlet.

    Created:     2017-06-18 00:04 by Christian Berndt
    Modified:    2017-06-23 17:47 by Christian Berndt
    Version:     1.0.2
--%>

<%@ include file="/init.jsp" %>

<%@page import="java.util.Collections"%>

<%@page import="javax.portlet.WindowState"%>

<%@page import="com.liferay.portal.kernel.service.UserServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.comparator.UserLastNameComparator"%>

<%
    ContactDisplayTerms displayTerms = new ContactDisplayTerms(renderRequest);
    int status = ParamUtil.getInteger(request, "status", -1);
    
    List<User> users = Collections.emptyList();
    
    if (themeDisplay.isSignedIn()) {
        users = UserServiceUtil.getGroupUsers(scopeGroupId); 
        Collections.sort(users, new UserLastNameComparator(false));
    }
%>

<liferay-ui:search-toggle
    autoFocus="<%=windowState.equals(WindowState.MAXIMIZED)%>"
    buttonLabel="search" displayTerms="<%=displayTerms%>"
    id="toggle_id_contact_search" markupView="<%=markupView%>">
    
    <aui:fieldset>
        
        <aui:input inlineField="<%=true%>"
            name="<%=ContactDisplayTerms.FULL_NAME%>" size="20"
            value="<%=displayTerms.getFullName()%>" />
            
        <aui:input inlineField="<%=true%>"
            name="<%=ContactDisplayTerms.COMPANY%>" size="20"
            value="<%=displayTerms.getCompany()%>" />

            <%--   
        
        <aui:select name="<%=ContactDisplayTerms.OWNER_USER_ID%>" inlineField="<%= true %>">
            <aui:option value="" label="any-user"/>
            <% for (User selectUser : users) { %>
                <aui:option value="<%= selectUser.getUserId() %>" label="<%= selectUser.getFullName() %>"/>
            <% } %>
        </aui:select>  
         
    
        <aui:select name="status" inlineField="<%= true %>"
            last="true">
            <aui:option
                value="<%=WorkflowConstants.STATUS_ANY%>"
                selected="<%=WorkflowConstants.STATUS_ANY == status%>">
                <liferay-ui:message key="any" />
            </aui:option>
            <aui:option
                value="<%=WorkflowConstants.STATUS_APPROVED%>"
                selected="<%=WorkflowConstants.STATUS_APPROVED == status%>">
                <liferay-ui:message key="approved" />
            </aui:option>
            <aui:option
                value="<%=WorkflowConstants.STATUS_DENIED%>"
                selected="<%=WorkflowConstants.STATUS_DENIED == status%>">
                <liferay-ui:message key="denied" />
            </aui:option>
            <aui:option
                value="<%=WorkflowConstants.STATUS_DRAFT%>"
                selected="<%=WorkflowConstants.STATUS_DRAFT == status%>">
                <liferay-ui:message key="draft" />
            </aui:option>
            <aui:option
                value="<%=WorkflowConstants.STATUS_INACTIVE%>"
                selected="<%= WorkflowConstants.STATUS_INACTIVE == status %>">
                <liferay-ui:message key="inactive" />
            </aui:option>
            <aui:option
                value="<%= WorkflowConstants.STATUS_INCOMPLETE %>"
                selected="<%= WorkflowConstants.STATUS_INCOMPLETE == status %>">
                <liferay-ui:message key="incomplete" />
            </aui:option>
            <aui:option
                value="<%= WorkflowConstants.STATUS_PENDING %>"
                selected="<%= WorkflowConstants.STATUS_PENDING == status %>">
                <liferay-ui:message key="pending" />
            </aui:option>
        </aui:select>  
        
         --%>      
              
    </aui:fieldset>
    
</liferay-ui:search-toggle>
