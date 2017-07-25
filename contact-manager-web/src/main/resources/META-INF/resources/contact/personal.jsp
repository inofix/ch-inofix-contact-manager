<%--
    personal.jsp: Edit the contact's personal information. 
    
    Created:    2015-05-08 18:02 by Christian Berndt
    Modified:   2017-07-25 22:27 by Christian Berndt
    Version:    1.2.1
--%>

<%@ include file="/init.jsp"%>

<%@ page import="ch.inofix.contact.dto.UrlDTO"%>
<%@ page import="ch.inofix.contact.dto.UriDTO"%>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    boolean hasUpdatePermission = false;

    if (contact_ == null) {

        contact_ = ContactServiceUtil.createContact();
        hasUpdatePermission = true;

    } else {

        hasUpdatePermission = ContactPermission.contains(permissionChecker, contact_, ActionKeys.UPDATE);
    }

    String namespace = liferayPortletResponse.getNamespace();
%>

<%
    String[] urlTypes = new String[]{"work", "private", "facebook", "twitter", "blog", "video-chat"};
%>

<aui:row>
        
    <c:choose>
        <c:when test="<%=Kind.ORG.equals(contact_.getKind())%>">

            <%
                // in org-mode, reset any structured name data
                for (String snField : snFields) {
            %>
            <aui:input bean="<%=contact_%>"
                disabled="<%=!hasUpdatePermission%>"
                name="<%=snField%>" type="hidden" value="" />
            <%
                }
            %>

            <aui:input disabled="<%=!hasUpdatePermission%>"
                name="nickname" bean="<%=contact_%>" type="hidden"
                value="" />

        </c:when>
        <c:otherwise>
            
            <aui:fieldset cssClass="fieldset col-md-6" label="job"
                markupView="<%=markupView%>">
            
                <aui:input name="title" bean="<%=contact_%>"
                    helpMessage="title-help"
                    disabled="<%=!hasUpdatePermission%>" />
        
                <aui:input name="role" bean="<%=contact_%>"
                    helpMessage="role-help" disabled="<%=!hasUpdatePermission%>" />
                    
                <aui:input name="company" bean="<%=contact_%>"
                    helpMessage="company-help"
                    disabled="<%=!hasUpdatePermission%>" />
        
                <aui:input name="department" bean="<%=contact_%>"
                    helpMessage="department-help"
                    disabled="<%=!hasUpdatePermission%>" />
        
                <aui:input name="office" bean="<%=contact_%>"
                    helpMessage="office-help"
                    disabled="<%=!hasUpdatePermission%>" /> 
                                            
            </aui:fieldset>
    
        </c:otherwise>
        
    </c:choose>



    <aui:fieldset cssClass="col-md-6" label="web-addresses" id="<%= namespace + "webAddresses" %>" markupView="<%= markupView %>">
        <%
            List<UrlDTO> urls = contact_.getUrls();

				for (UrlDTO url : urls) {
        %>
            <div class="lfr-form-row">
                <div class="row-fields">

                    <div class="sort-handle"></div>

                    <aui:select name="url.type" label=""
                        inlineField="true"
                        disabled="<%=!hasUpdatePermission%>">
                        <%
                            for (String urlType : urlTypes) {
                        %>
                        <aui:option value="<%=urlType%>"
                            label="<%=urlType%>"
                            selected="<%=urlType.equalsIgnoreCase(url.getType())%>" />
                        <%
                            }
                        %>
                    </aui:select>
                    <aui:input name="url.address" inlineField="true"
                        value="<%=url.getAddress()%>" label=""
                        disabled="<%=!hasUpdatePermission%>"
                        helpMessage="url.address-help" />
                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>
    
    <aui:fieldset cssClass="col-md-6" label="calendar-requests" id="<%= namespace + "calendarRequests" %>" markupView="<%= markupView %>">
        <%
            List<UriDTO> calendarRequests = contact_.getCalendarRequestUris();

				for (UriDTO calendar : calendarRequests) {
        %>
            <div class="lfr-form-row">
                <div class="row-fields">
                    <div class="sort-handle"></div>

                    <aui:input name="calendar.uri"
                        inlineField="true" inlineLabel="left"
                        value="<%=calendar.getUri()%>"
                        label=""
                        disabled="<%=!hasUpdatePermission%>"
                        helpMessage="calendar-request.uri-help" />
                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>
    
    <aui:fieldset cssClass="col-md-6" label="free-busy" id="<%= namespace + "freeBusy" %>" markupView="<%= markupView %>">
        <%
            List<UrlDTO> freeBusyUrls = contact_.getFreeBusyUrls();

				for (UrlDTO freeBusyUrl : freeBusyUrls) {
        %>
            <div class="lfr-form-row">
                <div class="row-fields">
                    <div class="sort-handle"></div>

                    <aui:input name="freeBusyUrl.url"
                        inlineLabel="left"
                        value="<%=freeBusyUrl.getAddress()%>"
                        label=""
                        disabled="<%=!hasUpdatePermission%>"
                        helpMessage="free-busy.url-help" />

                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>
    
    <aui:fieldset cssClass="col-md-6" label="calendars" id="<%= namespace + "calendars" %>" markupView="<%= markupView %>">
        <%
            List<UriDTO> calendarUris = contact_.getCalendarUris();

				for (UriDTO calendarUri : calendarUris) {
        %>
            <div class="lfr-form-row">
                <div class="row-fields">
                    <div class="sort-handle"></div>

                    <aui:input name="calendar.uri"
                        inlineField="true" inlineLabel="left"
                        value="<%=calendarUri.getUri()%>"
                        label=""
                        disabled="<%=!hasUpdatePermission%>"
                        helpMessage="calendar.uri-help" />
                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>

    <aui:fieldset cssClass="fieldset col-md-6" label="miscellaneous"
        markupView="<%=markupView%>">

        <aui:input name="birthday" bean="<%=contact_%>"
            helpMessage="birthday-help"
            disabled="<%=!hasUpdatePermission%>" required="false" />

        <aui:input name="anniversary" bean="<%=contact_%>"
            helpMessage="anniversary-help"
            disabled="<%=!hasUpdatePermission%>" />

        <aui:input name="birthplace" bean="<%=contact_%>"
            helpMessage="birthplace-help"
            disabled="<%=!hasUpdatePermission%>" required="false" />

    </aui:fieldset>
    
</aui:row>

<%-- Configure auto-fields --%>
<aui:script use="liferay-auto-fields">

	var urlAutoFields = new Liferay.AutoFields({
		contentBox : 'fieldset#<portlet:namespace />webAddresses',
		namespace : '<portlet:namespace />',
		sortable : true,
		sortableHandle : '.sort-handle',
		on : {
			'clone' : function(event) {
				restoreOriginalNames(event);
			}
		}
	}).render();

	var calendarRequestAutoFields = new Liferay.AutoFields({
		contentBox : 'fieldset#<portlet:namespace />calendarRequests',
		namespace : '<portlet:namespace />',
		sortable : true,
		sortableHandle : '.sort-handle',
		on : {
			'clone' : function(event) {
				restoreOriginalNames(event);
			}
		}
	}).render();

	var freeBusyAutoFields = new Liferay.AutoFields({
		contentBox : 'fieldset#<portlet:namespace />freeBusy',
		namespace : '<portlet:namespace />',
		sortable : true,
		sortableHandle : '.sort-handle',
		on : {
			'clone' : function(event) {
				restoreOriginalNames(event);
			}
		}
	}).render();

	var calendarAutoFields = new Liferay.AutoFields({
		contentBox : 'fieldset#<portlet:namespace />calendars',
		namespace : '<portlet:namespace />',
		sortable : true,
		sortableHandle : '.sort-handle',
		on : {
			'clone' : function(event) {
				restoreOriginalNames(event);
			}
		}
	}).render();
</aui:script>
