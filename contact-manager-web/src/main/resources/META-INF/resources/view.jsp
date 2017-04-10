<%--
    view.jsp: Default view of the contact manager portlet.
    
    Created:     2017-03-30 16:44 by Stefan Luebbers
    Modified:    2017-04-10 16:12 by Christian Berndt
    Version:     1.0.1 
--%>

<%@ include file="init.jsp"%>

<%-- Import required classes --%>

<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="com.liferay.portal.kernel.exception.PortalException"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Field"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.IndexerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.kernel.util.PrefsParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringUtil"%>


<%
    String backURL = ParamUtil.getString(request, "backURL");
    int delta = ParamUtil.getInteger(request, "delta", 20);
    String displayStyle = ParamUtil.getString(request, "displayStyle", "list");
    // String[] displayViews = StringUtil.split(PrefsParamUtil.getString(portletPreferences, liferayPortletRequest, "displayViews", "descriptive,icon,list"));
    int idx = ParamUtil.getInteger(request, "cur");
    String keywords = ParamUtil.getString(request, "keywords");
    String orderByCol = ParamUtil.getString(request, "orderByCol", "name");
    String orderByType = ParamUtil.getString(request, "orderByType", "asc");
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");

    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/html/view.jsp");
    portletURL.setParameter("backURL", backURL);
%>

<%
//     Log log = LogFactoryUtil.getLog("docroot.html.view.jsp");

//     if (idx > 0) {
//         idx = idx - 1;
//     }
//     int start = delta * idx;
//     int end = delta * idx + delta;

//     SearchContext searchContext =
//         SearchContextFactory.getInstance(request);

//     boolean reverse = "desc".equals(orderByType);

//     Sort sort = new Sort(orderByCol, reverse);

//     searchContext.setKeywords(keywords);
//     searchContext.setAttribute("paginationType", "more");
//     searchContext.setStart(start);
//     searchContext.setEnd(end);
//     searchContext.setSorts(sort);

//     Indexer indexer = IndexerRegistryUtil.getIndexer(Contact.class);

//     Hits hits = indexer.search(searchContext);

//     List<Contact> contacts = new ArrayList<Contact>();

//     for (int i = 0; i < hits.getDocs().length; i++) {
//         Document doc = hits.doc(i);

//         long contactId =
//             GetterUtil.getLong(doc.get(Field.ENTRY_CLASS_PK));

//         Contact contact_ = null;

//         try {
//             contact_ = ContactLocalServiceUtil.getContact(contactId);
//         }
//         catch (PortalException pe) {
//             log.error(pe.getLocalizedMessage());
//         }
//         catch (SystemException se) {
//             log.error(se.getLocalizedMessage());
//         }

//         if (contact_ != null) {
//             contacts.add(contact_);
//         }

//     }

//     ContactChecker rowChecker =
//         new ContactChecker(liferayPortletResponse);
//     rowChecker.setCssClass("entry-selector");
%>

<div id="<portlet:namespace />contactManagerContainer">

    <liferay-ui:header backURL="<%=backURL%>" title="contact-manager" />
    
<%--     <liferay-ui:error exception="<%= PrincipalException.class %>"  
       message="you-dont-have-the-required-permissions"/>     --%>
    
    <liferay-ui:tabs
        names="browse,import-export"
        param="tabs1" url="<%= portletURL.toString() %>" />

    <c:choose>

        <c:when test='<%= tabs1.equals("import-export") %>'>
<%--             <%@include file="/html/import_vcards.jspf"%> --%>
<%--             <%@include file="/html/export_vcards.jspf"%> --%>
<%--             <%@include file="/html/delete_contacts.jspf"%> --%>
        </c:when>

        <c:otherwise>
            
            <liferay-util:include page="/toolbar.jsp" servletContext="<%= application %>" />
            
            <portlet:actionURL name="editSet" var="editSetURL">
            </portlet:actionURL>
<%--
            <aui:form action="<%= editSetURL %>" name="fm" 
                onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "editSet();" %>'>
                
                <aui:input name="cmd" type="hidden" value="view"/>
                
                <div id="<portlet:namespace />entriesContainer">
                    <liferay-ui:search-container
                        searchContainer="<%= new ContactSearch(renderRequest, portletURL) %>"
                        var="contactSearchContainer"
                        rowChecker="<%= rowChecker %>"
                        >
        
                        <liferay-ui:search-container-results results="<%= contacts %>"
                            total="<%= hits.getLength() %>" />
        
                        <liferay-ui:search-container-row
                            className="ch.inofix.contact.model.Contact"
                            modelVar="contact_" keyProperty="contactId">
        
                            <portlet:actionURL var="deleteURL" name="deleteContact">
                                <portlet:param name="contactId"
                                    value="<%= String.valueOf(contact_.getContactId()) %>" />
                                <portlet:param name="backURL" value="<%= currentURL %>" />
                                <portlet:param name="mvcPath" value="/html/view.jsp" />
                            </portlet:actionURL>
        
                            <portlet:resourceURL var="downloadVCardURL" id="serveVCard">
                                <portlet:param name="contactId"
                                    value="<%= String.valueOf(contact_.getContactId()) %>" />
                            </portlet:resourceURL>
        
                            <portlet:actionURL var="editURL" name="editContact"
                                windowState="<%= LiferayWindowState.POP_UP.toString() %>">
                                <portlet:param name="redirect" value="<%= currentURL %>" />
                                <portlet:param name="contactId"
                                    value="<%= String.valueOf(contact_.getContactId()) %>" />
                                <portlet:param name="mvcPath" value="/html/edit_contact.jsp" />
                                <portlet:param name="windowId" value="editContact" />
                            </portlet:actionURL>
        
                            <%
                                StringBuilder sb = new StringBuilder(); 
                            
                                sb.append(LanguageUtil.get(pageContext, "permissions-of-contact")); 
                                sb.append(" "); 
                                sb.append(contact_.getFullName(true)); 
                            
                                String modelResourceDescription = sb.toString(); 
                            %>
        
                            <liferay-security:permissionsURL
                                modelResource="<%= Contact.class.getName() %>"
                                modelResourceDescription="<%= modelResourceDescription %>"
                                resourcePrimKey="<%= String.valueOf(contact_.getContactId()) %>"
                                var="permissionsURL" />
        
                            <portlet:actionURL var="viewURL" name="viewContact"
                                windowState="<%= LiferayWindowState.POP_UP.toString() %>">
                                <portlet:param name="redirect" value="<%= currentURL %>" />
                                <portlet:param name="contactId"
                                    value="<%= String.valueOf(contact_.getContactId()) %>" />
                                <portlet:param name="mvcPath" value="/html/view_contact.jsp" />
                                <portlet:param name="windowId" value="viewContact" />
                            </portlet:actionURL>
        
                            <%
                            
                                String taglibEditURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "editContact', title: '" + HtmlUtil.escapeJS(LanguageUtil.format(pageContext, "edit-x", HtmlUtil.escape(contact_.getFullName(true)))) + "', uri:'" + HtmlUtil.escapeJS(editURL) + "'});";
                                String taglibViewURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "viewContact', title: '" + HtmlUtil.escapeJS(LanguageUtil.format(pageContext, "view-x", HtmlUtil.escape(contact_.getFullName(true)))) + "', uri:'" + HtmlUtil.escapeJS(viewURL) + "'});";
                            
                                boolean hasDeletePermission = ContactPermission.contains(permissionChecker,
                                        contact_.getContactId(), ActionKeys.DELETE);   
                                boolean hasPermissionsPermission = ContactPermission.contains(permissionChecker,
                                        contact_.getContactId(), ActionKeys.PERMISSIONS);  
                                boolean hasUpdatePermission = ContactPermission.contains(permissionChecker,
                                        contact_.getContactId(), ActionKeys.UPDATE);
                                boolean hasViewPermission = ContactPermission.contains(permissionChecker,
                                        contact_.getContactId(), ActionKeys.VIEW);
        
                                String detailURL = null;
        
                                if (hasUpdatePermission) {
                                    
                                    if (!viewByDefault) {
                                        detailURL = taglibEditURL; 
                                    } else {
                                        detailURL = taglibViewURL;                                  
                                    }
                                    
                                } else if (hasViewPermission) {
                                    detailURL = taglibViewURL;  
                                }
                            %>
        
                            <%@ include file="/html/search_columns.jspf"%>
        
                            <liferay-ui:search-container-column-text align="right">
        
                                <liferay-ui:icon-menu>
        
                                    <c:if test="<%= hasUpdatePermission %>">
                                        <liferay-ui:icon image="edit" url="<%=taglibEditURL%>" />
                                    </c:if>
                                    <c:if test="<%= hasPermissionsPermission %>">
                                        <liferay-ui:icon image="permissions" url="<%= permissionsURL %>" />
                                    </c:if>
                                    <c:if test="<%= hasViewPermission %>">
                                        <liferay-ui:icon image="view" url="<%=taglibViewURL%>" />
                                    </c:if>
                                    <c:if test="<%= hasViewPermission %>">
                                        <liferay-ui:icon image="download" url="<%= downloadVCardURL %>" />
                                    </c:if>
                                    <c:if test="<%= hasDeletePermission %>">
                                        <liferay-ui:icon-delete url="<%=deleteURL%>" />
                                    </c:if>
        
                                </liferay-ui:icon-menu>
        
                            </liferay-ui:search-container-column-text>
                                    
                        </liferay-ui:search-container-row>
        
                        <liferay-ui:search-iterator />
        
                    </liferay-ui:search-container>
                </div>
            </aui:form>
            
            <aui:script>
                Liferay.provide(
                    window,
                    '<portlet:namespace />toggleActionsButton',
                    function() {
                        var A = AUI();
            
                        var actionsButton = A.one('#<portlet:namespace />actionsButtonContainer');
            
                        var hide = (Liferay.Util.listCheckedExcept(document.<portlet:namespace />fm, '<portlet:namespace /><%= RowChecker.ALL_ROW_IDS %>Checkbox').length == 0);
                        
                        if (actionsButton) {
                                            
                            actionsButton.toggle(!hide);
                            
                        }
                    },
                    ['liferay-util-list-fields']
                );
            
                <portlet:namespace />toggleActionsButton();
                
            </aui:script>
            
            <aui:script use="contact-manager-navigation">
            
                new Liferay.Portlet.ContactManagerNavigation(
                    {
                        displayStyle: '<%= HtmlUtil.escapeJS(displayStyle) %>',
                        namespace: '<portlet:namespace />',
                        portletId: '<%= portletDisplay.getId() %>',
                        rowIds: '<%= RowChecker.ROW_IDS %>',
                        select: {
            
                           <%
                           String[] escapedDisplayViews = new String[displayViews.length];
            
                           for (int i = 0; i < displayViews.length; i++) {
                               escapedDisplayViews[i] = HtmlUtil.escapeJS(displayViews[i]);
                           }
                           %>
            
                           displayViews: ['<%= StringUtil.merge(escapedDisplayViews, "','") %>']
                       }
                    }); 
            </aui:script>
            
            <aui:script>
                Liferay.provide(window, '<portlet:namespace />editSet', 
                    function(cmd) {
                    
                        document.<portlet:namespace />fm.<portlet:namespace />cmd.value = cmd; 
                                    
                        submitForm(document.<portlet:namespace />fm);
                
                    }, ['liferay-util-list-fields']
                );
            </aui:script>
            
            <portlet:resourceURL var="downloadVCardsURL" id="serveVCards"/>
            
             <aui:script>
                 Liferay.provide(window, '<portlet:namespace />downloadVCards', 
                     function() {
                     
                         var A = AUI();
    
                         // Get the rowChecker-boxes
                         var inputs = A.all('.entry-selector input');
                         var values = []; 
                         
                         // Filter for checked boxes
                         inputs.each(function() {
                             
                            if (this.get('checked') == true) {
                                values.push(this.get('value')); 
                            }
                            
                         });
                            
                         // Append checked boxes' values to resource-request
                         var rowIds = ""; 
                         
                         for (i=0; i<values.length; i++) {
                             rowIds = rowIds + '&<portlet:namespace />rowIds=' + values[i]; 
                         }
                         
                         // Download checked vCards from server
                         window.location.href= '<%= downloadVCardsURL %>' + rowIds;  
                 
                     }
                 );
             </aui:script> --%>
            
            
        </c:otherwise>

    </c:choose>

    <hr>
    
    <ifx-util:build-info/>
    
</div>
