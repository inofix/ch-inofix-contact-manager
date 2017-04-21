<%--
    view.jsp: Default view of the contact manager portlet.
    
    Created:     2017-03-30 16:44 by Stefan Luebbers
    Modified:    2017-04-18 13:15 by Stefan Luebbers
    Version:     1.0.4
--%>

<%@ include file="/init.jsp"%>

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
    String [] columns = new String[] {"full-name", "create-date", "modified-date"};
    int maxHeight = 70;
    boolean viewByDefault = false;
    String portraitDisplay = "circle";
    
    if (Validator.isNotNull(contactManagerConfiguration)) {
        columns = portletPreferences.getValues("columns", contactManagerConfiguration.columns());
        maxHeight = Integer.parseInt(portletPreferences.getValue("max-height", contactManagerConfiguration.maxHeight()));
        viewByDefault = Boolean.parseBoolean(portletPreferences.getValue("view-by-default", contactManagerConfiguration.viewByDefault()));
        portraitDisplay = portletPreferences.getValue("portrait-display", contactManagerConfiguration.portraitDisplay());
    }
    
    int delta = ParamUtil.getInteger(request, "delta", 20);
    String displayStyle = ParamUtil.getString(request, "displayStyle", "list");
    // String[] displayViews = StringUtil.split(PrefsParamUtil.getString(portletPreferences, liferayPortletRequest, "displayViews", "descriptive,icon,list"));
    int idx = ParamUtil.getInteger(request, "cur");
    
    String backURL = ParamUtil.getString(request, "backURL");
    String keywords = ParamUtil.getString(request, "keywords");
    String orderByCol = ParamUtil.getString(request, "orderByCol", "name");
    String orderByType = ParamUtil.getString(request, "orderByType", "asc");
    String tabs1 = ParamUtil.getString(request, "tabs1", "browse");

    portletURL.setParameter("tabs1", tabs1);
    portletURL.setParameter("mvcPath", "/view.jsp");
    portletURL.setParameter("backURL", backURL);
    
    ContactSearch contactSearch = new ContactSearch(renderRequest, "cur", portletURL);
    
    boolean reverse = false; 
    if (contactSearch.getOrderByType().equals("desc")) {
        reverse = true;
    }
    
    Sort sort = new Sort(contactSearch.getOrderByCol(), reverse);
    
    ContactSearchTerms searchTerms = (ContactSearchTerms) contactSearch.getSearchTerms();

    Hits hits = ContactServiceUtil.search(themeDisplay.getUserId(), themeDisplay.getScopeGroupId(), keywords,
            contactSearch.getStart(), contactSearch.getEnd(), sort);
            
    List<Document> documents = ListUtil.toList(hits.getDocs());
        
    List<Contact> contacts = new ArrayList<Contact>();
    
    for (Document document : documents) {
        try {
            long contactId = GetterUtil.getLong(document.get("entryClassPK"));

            Contact contact_ = ContactServiceUtil.getContact(contactId);
            contacts.add(contact_); 
        } catch (Exception e) {
            // TODO: use log
            System.out.println("ERROR: contactmanager/view.jsp Failed to getContact: " + e); 
        }
    }
    contactSearch.setResults(contacts); 
    contactSearch.setTotal(hits.getLength());

    // TODO: enable rowChecker
//     ContactChecker rowChecker =
//         new ContactChecker(liferayPortletResponse);
//     rowChecker.setCssClass("entry-selector");
%>

<div id="<portlet:namespace />contactManagerContainer">

    <liferay-ui:error exception="<%= PrincipalException.class %>"
        message="you-dont-have-the-required-permissions" />
    
    <liferay-ui:tabs
        names="browse,import-export"
        param="tabs1" url="<%= portletURL.toString() %>" />

    <c:choose>

        <c:when test='<%= tabs1.equals("import-export") %>'>
<%--             <liferay-util:include page="/import_vcards.jsp" servletContext="<%= application %>"  /> --%>
<%--             <liferay-util:include page="/export_vcards.jspf" servletContext="<%= application %>"  /> --%>
<%--             <liferay-util:include page="/delete_contacts.jspf" servletContext="<%= application %>"  /> --%>
        </c:when>

        <c:otherwise>
            
            <liferay-util:include page="/toolbar.jsp" servletContext="<%= application %>" />
            
            <portlet:actionURL name="editSet" var="editSetURL">
            </portlet:actionURL>

            <aui:form action="<%= editSetURL %>" name="fm" 
                onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "editSet();" %>'>
                
                <aui:input name="cmd" type="hidden" value="view"/>
                
                <div id="<portlet:namespace />entriesContainer">

                    <liferay-ui:search-container
                        id="contacts"
                        searchContainer="<%= contactSearch %>"
                        var="contactSearchContainer" >
               
                        <liferay-ui:search-container-row
                            className="ch.inofix.contact.model.Contact"
                            modelVar="contact_" keyProperty="contactId">
        
                            
                            <portlet:renderURL var="editURL"
                                windowState="<%=LiferayWindowState.POP_UP.toString()%>">
                                <portlet:param name="redirect"
                                    value="<%=currentURL%>" />
                                <portlet:param name="contactId"
                                    value="<%=String.valueOf(contact.getContactId())%>" />
                                <portlet:param name="mvcPath"
                                    value="/edit_contact.jsp" />
                                <portlet:param name="windowId"
                                    value="editContact" />
                            </portlet:renderURL>
        
                            <%--
                                StringBuilder sb = new StringBuilder(); 
                            
                                sb.append(LanguageUtil.get(pageContext, "permissions-of-contact")); 
                                sb.append(" "); 
                                sb.append(contact_.getFullName(true)); 
                            
                                String modelResourceDescription = sb.toString(); 
                             --%> 
                            <portlet:renderURL var="viewURL"
                                windowState="<%=LiferayWindowState.POP_UP.toString()%>">
                                <portlet:param name="redirect"
                                    value="<%=currentURL%>" />
                                <portlet:param name="contactId"
                                    value="<%=String.valueOf(contact_.getContactId())%>" />
                                <portlet:param name="mvcPath"
                                    value="/view_contact.jsp" />
                                <portlet:param name="windowId"
                                    value="viewContact" />
                            </portlet:renderURL>

                            <%
                                String taglibEditURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "editContact', title: '" + HtmlUtil.escapeJS(LanguageUtil.format(request, "edit-x", contact_.getFullName(true))) + "', uri:'" + HtmlUtil.escapeJS(editURL) + "'});";            
                                String taglibViewURL = "javascript:Liferay.Util.openWindow({id: '" + renderResponse.getNamespace() + "viewContact', title: '" + HtmlUtil.escapeJS(LanguageUtil.format(request, "view-x", contact_.getFullName(true))) + "', uri:'" + HtmlUtil.escapeJS(viewURL) + "'});";
                                
                                request.setAttribute("editURL", editURL.toString()); 
                                request.setAttribute("viewURL", viewURL.toString()); 

                                boolean hasDeletePermission = ContactPermission.contains(permissionChecker,
                                        contact_.getContactId(), ContactActionKeys.DELETE);   
                                boolean hasPermissionsPermission = ContactPermission.contains(permissionChecker,
                                        contact_.getContactId(), ContactActionKeys.PERMISSIONS);  
                                boolean hasUpdatePermission = ContactPermission.contains(permissionChecker,
                                        contact_.getContactId(), ContactActionKeys.UPDATE);
                                boolean hasViewPermission = ContactPermission.contains(permissionChecker,
                                        contact_.getContactId(), ContactActionKeys.VIEW);

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
                            
                            <liferay-ui:search-container-column-text value="<%= contact_.getCard() %>"/>
        
<%--                              <%@ include file="/search_columns.jspf"%>  --%>
                            
                            <liferay-ui:search-container-column-jsp
                                cssClass="entry-action"
                                path="/contact_action.jsp"
                                valign="top" />
                            
                        </liferay-ui:search-container-row>
        
                        <liferay-ui:search-iterator />
        
                    </liferay-ui:search-container>
                </div>
            </aui:form>
            
            
            <%
                ResourceURL resourceURL = liferayPortletResponse.createResourceURL();

                resourceURL.setResourceID("getSum");

                // Copy render parameters to resourceRequest
                resourceURL.setParameters(renderRequest.getParameterMap());
            %>
            
<%--        <aui:script>
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
    
    <% // TODO %>
<!--     <ifx-util:build-info/> -->
    
</div>
