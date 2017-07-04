<%--
    view.jsp: Default view of the contact manager portlet.
    
    Created:     2017-03-30 16:44 by Stefan Luebbers
    Modified:    2017-06-22 16:47 by Christian Berndt
    Version:     1.0.6
--%>

<%@ include file="/init.jsp"%>

<%
    String[] columns = new String[]{"full-name", "create-date", "modified-date"};

	int maxHeight = 70;
	boolean viewByDefault = false;
	String portraitDisplay = "circle";

	if (Validator.isNotNull(contactManagerConfiguration)) {
		columns = portletPreferences.getValues("columns", contactManagerConfiguration.columns());
		maxHeight = Integer.parseInt(portletPreferences.getValue("max-height", contactManagerConfiguration.maxHeight()));
		viewByDefault = Boolean.parseBoolean(portletPreferences.getValue("view-by-default", contactManagerConfiguration.viewByDefault()));
		portraitDisplay = portletPreferences.getValue("portrait-display", contactManagerConfiguration.portraitDisplay());
	}

	String backURL = ParamUtil.getString(request, "backURL");
	String displayStyle = ParamUtil.getString(request, "displayStyle", "list");
	String keywords = ParamUtil.getString(request, "keywords");

	PortletURL portletURL = renderResponse.createRenderURL();

	portletURL.setParameter("tabs1", tabs1);

	ContactSearch searchContainer = new ContactSearch(renderRequest, "cur", portletURL);

	int status = ParamUtil.getInteger(request, "status");

	boolean reverse = false;
	if (searchContainer.getOrderByType().equals("desc")) {
		reverse = true;
	}

	Sort sort = new Sort(searchContainer.getOrderByCol(), reverse);

	ContactSearchTerms searchTerms = (ContactSearchTerms) searchContainer.getSearchTerms();

	Hits hits = null;

	if (searchTerms.isAdvancedSearch()) {

		hits = ContactServiceUtil.search(themeDisplay.getUserId(), scopeGroupId, 0, searchTerms.getCompany(),
				searchTerms.getFullName(), status, null, searchTerms.isAndOperator(),
				searchContainer.getStart(), searchContainer.getEnd(), sort);

	} else {
		hits = ContactServiceUtil.search(themeDisplay.getUserId(), themeDisplay.getScopeGroupId(), keywords,
				searchContainer.getStart(), searchContainer.getEnd(), sort);

	}

	List<Contact> contacts = ContactUtil.getContacts(hits);

	searchContainer.setResults(contacts);
	searchContainer.setTotal(hits.getLength());

	request.setAttribute("view.jsp-columns", columns);

	request.setAttribute("view.jsp-displayStyle", displayStyle);

	request.setAttribute("view.jsp-searchContainer", searchContainer);

	request.setAttribute("view.jsp-total", hits.getLength());
%>

<%
    // TODO: add trash bin support
%>

<liferay-util:include page="/navigation.jsp"
    servletContext="<%=application%>" />

<c:choose>
    <c:when test="<%="export-import".equals(tabs1)%>">
        <liferay-util:include page="/export_import.jsp"
            servletContext="<%=application%>" />
    </c:when>
    <c:otherwise>

        <liferay-util:include page="/toolbar.jsp"
            servletContext="<%=application%>">
            <liferay-util:param name="searchContainerId"
                value="contacts" />
        </liferay-util:include>

        <div class="container-fluid-1280">

            <div id="<portlet:namespace />contactManagerContainer">

                <liferay-ui:error
                    exception="<%= PrincipalException.class %>"
                    message="you-dont-have-the-required-permissions" />

                <portlet:actionURL var="editSetURL" />

                <aui:form action="<%=editSetURL%>" name="fm"
                    onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "editSet();" %>'>

                    <aui:input name="<%=Constants.CMD%>" type="hidden" />
                    <aui:input name="redirect" type="hidden"
                        value="<%=currentURL%>" />
                    <aui:input name="deleteContactIds" type="hidden" />

                    <liferay-util:include page="/view_contacts.jsp"
                        servletContext="<%=application%>" />

                </aui:form>
            </div>
        </div>

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

        
		<liferay-util:include page="/add_button.jsp"
            servletContext="<%=application%>" />

    </c:otherwise>
</c:choose>

<%
    // TODO
%>
<!--     <ifx-util:build-info/> -->
