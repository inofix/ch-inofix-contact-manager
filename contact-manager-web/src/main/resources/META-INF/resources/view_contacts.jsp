<%--
    view_contacts.jsp: search-container of Inofix' contact-manager.
    
    Created:     2017-06-19 16:02 by Christian Berndt
    Modified:    2017-06-19 16:02 by Christian Berndt
    Version:     1.0.0
--%>

<%@ include file="/init.jsp"%>

<%@page import="ch.inofix.contact.web.internal.search.EntriesChecker"%>

<%
    ContactSearch searchContainer = (ContactSearch) request.getAttribute("view.jsp-searchContainer");

    EntriesChecker entriesChecker = new EntriesChecker(liferayPortletRequest, liferayPortletResponse);

    searchContainer.setRowChecker(entriesChecker);

    String displayStyle = GetterUtil.getString((String) request.getAttribute("view.jsp-displayStyle"));

    String searchContainerId = ParamUtil.getString(request, "searchContainerId");
%>

<liferay-ui:search-container id="contacts"
    searchContainer="<%=searchContainer%>"
    var="contactSearchContainer">
    
    <liferay-ui:search-container-row
        className="ch.inofix.contact.model.Contact"
        modelVar="contact_" keyProperty="contactId">

        <portlet:renderURL var="editURL">
            <portlet:param name="redirect" value="<%=currentURL%>" />
            <portlet:param name="contactId"
                value="<%=String.valueOf(contact_.getContactId())%>" />
            <portlet:param name="mvcPath" value="/edit_contact.jsp" />
        </portlet:renderURL>

        <portlet:renderURL var="viewURL">
            <portlet:param name="redirect" value="<%=currentURL%>" />
            <portlet:param name="contactId"
                value="<%=String.valueOf(contact_.getContactId())%>" />
            <portlet:param name="mvcPath" value="/edit_contact.jsp" />
        </portlet:renderURL>

        <%
            request.setAttribute("editURL", editURL.toString());
            request.setAttribute("viewURL", viewURL.toString());

            boolean hasUpdatePermission = ContactPermission.contains(permissionChecker,
                    contact_.getContactId(), ContactActionKeys.UPDATE);

            boolean hasViewPermission = ContactPermission.contains(permissionChecker,
                    contact_.getContactId(), ContactActionKeys.VIEW);
            
            String detailURL = null;

            if (hasUpdatePermission) {
                detailURL = editURL.toString();
            } else if (hasViewPermission) {
                detailURL = viewURL.toString();
            }
        %>
                
        <%@ include file="/search_columns.jspf"%>

        <liferay-ui:search-container-column-jsp cssClass="entry-action"
            path="/contact_action.jsp" valign="top" />
    
    </liferay-ui:search-container-row>

    <liferay-ui:search-iterator displayStyle="<%=displayStyle%>"
        markupView="<%=markupView%>" />

</liferay-ui:search-container>
