<%--
    view_contacts.jsp: search-container of Inofix' contact-manager.
    
    Created:     2017-06-19 16:02 by Christian Berndt
    Modified:    2017-11-18 17:06 by Christian Berndt
    Version:     1.0.5
--%>

<%@ include file="/init.jsp"%>

<%
    ContactSearch searchContainer = (ContactSearch) request.getAttribute("view.jsp-searchContainer");

    EntriesChecker entriesChecker = new EntriesChecker(liferayPortletRequest, liferayPortletResponse);

    searchContainer.setRowChecker(entriesChecker);

    String displayStyle = GetterUtil.getString((String) request.getAttribute("view.jsp-displayStyle"));

    String searchContainerId = ParamUtil.getString(request, "searchContainerId");
%>

<liferay-ui:search-container 
    id="contacts"
    searchContainer="<%=searchContainer%>"    
    var="contactSearchContainer">
        
    <liferay-ui:search-container-row
        className="ch.inofix.contact.model.Contact"
        modelVar="contact_" keyProperty="contactId">

        <portlet:renderURL var="editURL">
            <portlet:param name="contactId" value="<%=String.valueOf(contact_.getContactId())%>" />
            <portlet:param name="mvcRenderCommandName" value="editContact" />
            <portlet:param name="redirect" value="<%=currentURL%>" />
        </portlet:renderURL>

        <portlet:renderURL var="viewURL">
            <portlet:param name="contactId" value="<%=String.valueOf(contact_.getContactId())%>" />
            <portlet:param name="mvcRenderCommandName" value="editContact" />
            <portlet:param name="redirect" value="<%=currentURL%>" />
        </portlet:renderURL>

        <%
            request.setAttribute("editURL", editURL.toString());
            request.setAttribute("viewURL", viewURL.toString());

            boolean hasUpdatePermission = ContactPermission.contains(permissionChecker,
                    contact_.getContactId(), ContactManagerActionKeys.UPDATE);

            boolean hasViewPermission = ContactPermission.contains(permissionChecker,
                    contact_.getContactId(), ContactManagerActionKeys.VIEW);
            
            String detailURL = null;

            if (hasUpdatePermission) {
                detailURL = editURL.toString();
            } else if (hasViewPermission) {
                detailURL = viewURL.toString();
            }
        %>
            
        <c:choose>
            <c:when test='<%= displayStyle.equals("descriptive") %>'>
            
                <liferay-ui:search-container-column-jsp
                    colspan="2"
                    path="/view_contacts_descriptive.jsp"
                />
                
                <liferay-ui:search-container-column-jsp align="right" cssClass="entry-action"
                    path="/contact_action.jsp" valign="top"/>            
            </c:when>
            
            <c:when test='<%= displayStyle.equals("icon") %>'>
                <liferay-ui:app-view-entry
                    assetCategoryClassName="<%= Contact.class.getName() %>"
                    assetCategoryClassPK="<%= contact_.getContactId() %>"
                    displayStyle="<%= displayStyle %>"
                    showCheckbox="false"
                    title="<%= contact_.getName() %>"                    
                />          
            </c:when>
            
            <c:otherwise>
            
                <liferay-ui:search-container-column-user
                    cssClass="user-icon-lg"
                    showDetails="<%= false %>"
                    userId="<%= contact_.getUserId() %>"
                />

                <%@ include file="/search_columns.jspf"%>
                
                <liferay-ui:search-container-column-jsp align="right" cssClass="entry-action"
                    path="/contact_action.jsp" valign="top"/> 
                                       
            </c:otherwise>
        </c:choose>
    
    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator displayStyle="<%=displayStyle%>"
        markupView="<%=markupView%>" />

</liferay-ui:search-container>
