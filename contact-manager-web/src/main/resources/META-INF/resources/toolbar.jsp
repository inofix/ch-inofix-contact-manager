<%--
    toolbar.jsp: The toolbar of the contact-manager portlet
    
    Created:    2015-07-03 19:00 by Christian Berndt
    Modified:   2017-04-13 23:25 by Christian Berndt
    Version:    1.0.2
 --%>

<%@ include file="init.jsp"%>

<aui:nav-bar>

    <aui:nav id="toolbarContainer">

        <aui:nav-item cssClass="hide" dropdown="<%=true%>"
            id="actionsButtonContainer" label="actions">

            <%
                String downloadVCardsURL = "javascript:" + renderResponse.getNamespace() + "downloadVCards();";
            %>

            <aui:nav-item href="<%=downloadVCardsURL%>" iconCssClass="icon-download"
                label="download-selected-contacts" />
            
            <%
                String deleteContactsURL = "javascript:" + renderResponse.getNamespace() + "editSet('delete');";
            %>

            <aui:nav-item>
                <liferay-ui:icon-delete url="<%= deleteContactsURL %>" label="true"
                    message="delete-selected-contacts" />
            </aui:nav-item>
        </aui:nav-item>
        

        <%-- deprecated
        <portlet:actionURL var="addURL" name="editContact"
            windowState="<%= LiferayWindowState.POP_UP.toString() %>">
            <portlet:param name="mvcPath" value="/html/edit_contact.jsp" />
            <portlet:param name="redirect" value="<%= currentURL %>" />
            <portlet:param name="windowId" value="editContact" />
        </portlet:actionURL>
        --%>

        <%
            // TODO: make window state behaviour configurable 
            String windowState = LiferayWindowState.POP_UP.toString();
        %>
        <portlet:renderURL var="editURL"
            windowState="<%= windowState %>">
            <portlet:param name="redirect" value="<%= currentURL %>" />
            <portlet:param name="mvcPath" value="/edit_contact.jsp" />
            <portlet:param name="windowId" value="editContact" />
        </portlet:renderURL>
<%
            // TODO: reactivate permission checks
%>
<%--         <c:if test='<%=ContactPortletPermission.contains( --%>
<!-- //                                 permissionChecker, scopeGroupId, -->
<%--                                 ActionKeys.ADD_CONTACT)%>'> --%>
            <%
                String taglibAddURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "editContact', title: '" + HtmlUtil.escapeJS(LanguageUtil.format(request, "edit-x", "new")) + "', uri:'" + HtmlUtil.escapeJS(editURL) + "'});";
            %>
              
            <aui:a href="<%= taglibAddURL %>" cssClass="btn btn-primary pull-left add-contact">
                <liferay-ui:message key="add-contact"/>
            </aui:a>

<%--         </c:if> --%>
    </aui:nav>
    
    <aui:nav-bar-search cssClass="pull-right">

        <portlet:renderURL var="clearURL" />

        <liferay-portlet:renderURL varImpl="searchURL">
            <portlet:param name="mvcPath" value="/view.jsp" />
        </liferay-portlet:renderURL>

        <div class="form-search">
            <aui:form action="<%=searchURL%>" method="get" name="fm1"
                cssClass="pull-right">
                <liferay-portlet:renderURLParams varImpl="searchURL" />

                <div class="search-form contact-search">
                    <span class="aui-search-bar">
                     
                        <aui:input inlineField="<%=true%>" label="" 
                            name="keywords" size="30"
                            title="search-contacts" type="text" /> 
                        <aui:button type="submit"
                            value="search" /> 
                        <aui:button value="clear"
                            href="<%=clearURL%>" />
                    </span>
                </div>
            </aui:form>
        </div>

    </aui:nav-bar-search> 

</aui:nav-bar>
