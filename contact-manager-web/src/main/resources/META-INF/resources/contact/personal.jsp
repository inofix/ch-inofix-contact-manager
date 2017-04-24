<%--
    personal.jsp: Edit the contact's personal information. 
    
    Created:    2015-05-08 18:02 by Christian Berndt
    Modified:   2017-04-21 14:29 by Stefan Luebbers
    Version:    1.1.3
--%>

<%@ include file="/init.jsp"%>

<%@ page import="ch.inofix.contact.dto.UrlDTO" %>
<%@ page import="ch.inofix.contact.dto.UriDTO" %>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    if (contact_ == null) {
        contact_ = ContactServiceUtil.createContact();
    }

    // TODO: check permissions
    boolean hasUpdatePermission = true; 
%>

<%
    String[] urlTypes = new String[] { "private", "work", "facebook", "twitter", "blog", "video-chat"};

    String[] snFields = new String[] { "structuredName.prefix",
            "structuredName.given", "structuredName.additional",
            "structuredName.family", "structuredName.suffix" };
%>

<%-- The values of the hidden snFields are managed --%>
<%-- by the structured-name popover below.         --%>
<%
    for (String snField : snFields) {
%>
<aui:input name="<%=snField%>" bean="<%=contact_%>"
    cssClass='<%=snField.replace(".", "-") %>' type="hidden" 
    disabled="<%= !hasUpdatePermission %>"/>
<%
    }
%>



<aui:fieldset label="job">
    <aui:container>
        <aui:row>
            <aui:col width="50">
                <aui:input name="title" bean="<%=contact_%>"
                    inlineField="true" helpMessage="title-help" 
                    disabled="<%= !hasUpdatePermission %>" required="false"/>
            </aui:col>
            <aui:col width="50">
                <aui:input name="role" bean="<%=contact_%>"
                    helpMessage="role-help" disabled="<%= !hasUpdatePermission %>" />
            </aui:col>
        </aui:row>
        <aui:row>
            <aui:col width="50">
                <aui:input name="company" bean="<%=contact_%>"
                    inlineField="true" helpMessage="company-help" 
                    disabled="<%= !hasUpdatePermission %>" required="false"/>
            </aui:col>
            <aui:col width="50">
                <aui:input name="department" bean="<%=contact_%>"
                    helpMessage="department-help" disabled="<%= !hasUpdatePermission %>" />
            </aui:col>
        </aui:row>
        <aui:row>
            <aui:col width="50">
                <aui:input name="office" bean="<%=contact_%>"
                    inlineField="true" helpMessage="office-help" 
                    disabled="<%= !hasUpdatePermission %>" required="false"/>
            </aui:col>
        </aui:row>
    </aui:container>
</aui:fieldset>


<aui:fieldset label="web-adresses" id="webAdresses">
    <aui:container>
        <%
            List<UrlDTO> urls = contact_.getUrls();

            for (UrlDTO url : urls) {
        %>
        <aui:row>
            <aui:col span="12">
                <div class="lfr-form-row">
                    <div class="row-fields">
                    
                       <div class="sort-handle"></div>
                    
                        <aui:select name="url.type" label="" inlineField="true"
                            disabled="<%=!hasUpdatePermission%>">
                            <%
                                for (String urlType : urlTypes) {
                            %>
                            <aui:option value="<%=urlType%>" label="<%=urlType%>"
                                selected="<%=urlType.equalsIgnoreCase(url.getType())%>" />
                            <%
                                }
                            %>
                        </aui:select>
                        <aui:input name="url.address" inlineField="true"
                            value="<%=url.getAddress()%>" label=""
                            disabled="<%=!hasUpdatePermission%>" />

                        <liferay-ui:icon-help message="url.address-help" />
                    </div>
                </div>
            </aui:col>
        </aui:row>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="calendar-requests" id="calendarRequests">
    <aui:container>
        <%
            List<UriDTO> calendarRequests = contact_.getCalendarRequestUris();

            for (UriDTO calendar : calendarRequests) {
        %>
        <aui:row>
            <aui:col span="12">
                <div class="lfr-form-row">
                    <div class="row-fields">
	                    <div class="sort-handle"></div>
	                
	                        <aui:input name="calendar.uri" inlineField="true"
	                            value="<%=calendar.getUri()%>" label="calendar-request-uri"
	                            disabled="<%=!hasUpdatePermission%>" />
	
	                    <liferay-ui:icon-help message="calendar-request.uri-help" />
	                </div>
                </div>
            </aui:col>
        </aui:row>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="free-busy" id="freeBusy">
    <aui:container>
        <%
        List<UrlDTO> freeBusyUrls = contact_.getFreeBusyUrls();

            for (UrlDTO freeBusyUrl : freeBusyUrls) {
        %>
        <aui:row>
            <aui:col span="12">
                <div class="lfr-form-row">
                    <div class="row-fields">
                        <div class="sort-handle"></div>
                    
                            <aui:input name="freeBusyUrl.url" inlineField="true"
                                value="<%=freeBusyUrl.getAddress()%>" label="free-busy-url"
                                disabled="<%=!hasUpdatePermission%>" />
    
                        <liferay-ui:icon-help message="free-busy.url-help" />
                    </div>
                </div>
            </aui:col>
        </aui:row>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="calendars" id="calendars">
    <aui:container>
        <%
        List<UriDTO> calendarUris = contact_.getCalendarUris();

            for (UriDTO calendarUri : calendarUris) {
        %>
        <aui:row>
            <aui:col span="12">
                <div class="lfr-form-row">
                    <div class="row-fields">
                        <div class="sort-handle"></div>
                    
                            <aui:input name="calendar.uri" inlineField="true"
                                value="<%=calendarUri.getUri()%>" label="calendar-uri"
                                disabled="<%=!hasUpdatePermission%>" />
    
                        <liferay-ui:icon-help message="calendar.uri-help" />
                    </div>
                </div>
            </aui:col>
        </aui:row>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="miscellaneous">
    <aui:container>
        <aui:row>
            <aui:col width="50">
                <aui:input name="birthday" bean="<%=contact_%>"
                    inlineField="true" helpMessage="birthday-help" 
                    disabled="<%= !hasUpdatePermission %>" required="false"/>
            </aui:col>
            <aui:col width="50">
                <aui:input name="anniversary" bean="<%=contact_%>"
                    helpMessage="anniversary-help" disabled="<%= !hasUpdatePermission %>" />
            </aui:col>
        </aui:row>
        <aui:row>
            <aui:col width="50">
                <aui:input name="birthplace" bean="<%=contact_%>"
                    inlineField="true" helpMessage="birthplace-help" 
                    disabled="<%= !hasUpdatePermission %>" required="false"/>
            </aui:col>
        </aui:row>
    </aui:container>
</aui:fieldset>

<%-- Configure auto-fields --%>
<aui:script use="liferay-auto-fields">

    var urlAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />webAdresses',
        namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
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
        sortableHandle: '.sort-handle',
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
        sortableHandle: '.sort-handle',
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
        sortableHandle: '.sort-handle',
        on : {
            'clone' : function(event) {
                restoreOriginalNames(event);
            }
        }
    }).render();

</aui:script>

