<%--
    general.jsp: Edit the contact's basic contact information. 
    
    Created:    2015-05-08 18:02 by Christian Berndt
    Modified:   2017-04-12 17:05 by Christian Berndt
    Version:    1.1.3
--%>

<%@ include file="/init.jsp"%>

<%@page import="ch.inofix.contact.dto.CategoriesDTO"%>
<%@page import="ch.inofix.contact.dto.EmailDTO"%>
<%@page import="ch.inofix.contact.dto.ImppDTO"%>
<%@page import="ch.inofix.contact.dto.PhoneDTO"%>

<%@page import="ezvcard.parameter.ImppType"%>
<%@page import="ezvcard.parameter.TelephoneType"%>
<%@page import="ezvcard.property.Kind"%>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    if (contact_ == null) {
        contact_ = ContactServiceUtil.createContact();
    }

    // TODO: check permissions
    boolean hasUpdatePermission = true; 
%>

<%
    // TODO: Make the emailTypes configurable
    String[] emailTypes = new String[] { "home", "work", "other" };

    String[] imppTypes = new String[] { "other",
            ImppType.BUSINESS.getValue(), ImppType.HOME.getValue(),
            ImppType.MOBILE.getValue(), ImppType.PERSONAL.getValue(),
            ImppType.WORK.getValue() };

    // TODO: Make imppProtocols configurable
    String[] imppProtocols = new String[] { "other", "aim", "jabber",
            "yahoo", "gadu-gadu", "msn", "icq", "groupwise", "skype",
            "twitter" };

    String[] kinds = new String[] { Kind.APPLICATION, Kind.DEVICE,
            Kind.GROUP, Kind.INDIVIDUAL, Kind.LOCATION, Kind.ORG };

    // TODO: Make the phoneTypes configurable
    String[] phoneTypes = new String[] { "other",
//          TelephoneType.BBS.getValue(), 
//          TelephoneType.CAR.getValue(),
            TelephoneType.CELL.getValue(),
            TelephoneType.FAX.getValue(),
//          TelephoneType.HOME.getValue(),
//          TelephoneType.ISDN.getValue(),
//          TelephoneType.MODEM.getValue(),
//          TelephoneType.MSG.getValue(),
            TelephoneType.PAGER.getValue(),
//          TelephoneType.PCS.getValue(),
            TelephoneType.TEXT.getValue(),
            TelephoneType.TEXTPHONE.getValue(),
            TelephoneType.VIDEO.getValue(),
            TelephoneType.VOICE.getValue(),
//          TelephoneType.WORK.getValue() 
            };

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

<% // TODO re-enable popover for structured name %>
<%-- 
<div id='<portlet:namespace/>structuredNamePopover'
    class="structured-name">

    <%
        for (String snField : snFields) {
    %>
    <aui:input name="<%=snField%>" bean="<%=contact_%>"
        cssClass='<%=snField.replace(".", "-") + "-twin"%>' 
        useNamespace="false" helpMessage='<%= snField + "-help" %>'
        disabled="<%= !hasUpdatePermission %>" />
    <%
        }
    %>

</div>
--%>

<aui:fieldset label="name">
    <aui:container>
        <aui:row>
            <aui:col width="50">
                <aui:input name="formattedName" bean="<%=contact_%>"
                    inlineField="true" helpMessage="formatted-name-help" 
                    disabled="<%= !hasUpdatePermission %>" required="true"/>
                <% // TODO re-enable popover for structured name %>
                <%-- 
                <aui:button name="structuredNameBtn" value="structured-name"
                    cssClass="btn" />
                --%>
            </aui:col>
            <aui:col width="50">
                <aui:input name="nickname" bean="<%=contact_%>"
                    helpMessage="nickname-help" disabled="<%= !hasUpdatePermission %>" />
            </aui:col>
        </aui:row>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="email" id="email">
    <aui:container>
        <%
            List<EmailDTO> emails = contact_.getEmails();

            for (EmailDTO email : emails) {
        %>
        <aui:row>
            <aui:col span="12">
                <div class="lfr-form-row">
                    <div class="row-fields">
                    
                       <div class="sort-handle"></div>
                    
                        <aui:select name="email.type" label="" inlineField="true"
                            disabled="<%=!hasUpdatePermission%>">
                            <%
                                for (String emailType : emailTypes) {
                            %>
                            <aui:option value="<%=emailType%>" label="<%=emailType%>"
                                selected="<%=emailType
                                                .equalsIgnoreCase(email
                                                        .getType())%>" />
                            <%
                                }
                            %>
                        </aui:select>
                        <aui:input name="email.address" inlineField="true"
                            value="<%=email.getAddress()%>" label=""
                            disabled="<%=!hasUpdatePermission%>" />

                        <liferay-ui:icon-help message="email.address-help" />
                    </div>
                </div>
            </aui:col>
        </aui:row>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="phone" id="phone">
    <aui:container>
        <%
            List<PhoneDTO> phones = contact_.getPhones(); 
                        
            for (PhoneDTO phone : phones) {
        %>
        <aui:row>
            <aui:col span="12">
                <div class="lfr-form-row">
                    <div class="row-fields">
                    
                        <div class="sort-handle"></div>
                       
                        <aui:select name="phone.type" label="" inlineField="true"
                            disabled="<%=!hasUpdatePermission%>">
                            <%
                                for (String phoneType : phoneTypes) {
                            %>
                            <aui:option value="<%=phoneType%>" label="<%=phoneType%>"
                                selected="<%=phoneType
                                                .equalsIgnoreCase(phone
                                                        .getType())%>" />
                            <%
                                }
                            %>
                        </aui:select>
                        <aui:input name="phone.number" inlineField="true"
                            value="<%=phone.getNumber()%>" label=""
                            disabled="<%=!hasUpdatePermission%>" />

                        <liferay-ui:icon-help message="phone.number-help" />

                    </div>
                </div>

            </aui:col>
        </aui:row>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="instant-messaging" id="impp">
    <aui:container>
        <%
            List<ImppDTO> impps = contact_.getImpps();
                                        
            for (ImppDTO impp : impps) {
        %>
        <aui:row>
            <aui:col span="12">
                <div class="lfr-form-row">
                    <div class="row-fields">
                    
                        <div class="sort-handle"></div>
                        
                        <aui:select name="impp.type" label="" inlineField="true"
                            disabled="<%=!hasUpdatePermission%>">
                            <%
                                for (String imppType : imppTypes) {
                            %>
                            <aui:option value="<%=imppType%>" label="<%=imppType%>"
                                selected="<%=imppType
                                                .equalsIgnoreCase(impp
                                                        .getType())%>" />
                            <%
                                }
                            %>
                        </aui:select>
                        
                        <aui:select name="impp.protocol" label="" inlineField="true"
                            disabled="<%=!hasUpdatePermission%>">
                            <%
                                for (String imppProtocol : imppProtocols) {
                            %>
                            <aui:option value="<%=imppProtocol%>" label="<%=imppProtocol%>"
                                selected="<%=imppProtocol
                                                .equalsIgnoreCase(impp
                                                        .getProtocol())%>" />
                            <%
                                }
                            %>
                        </aui:select>

                        <aui:input name="impp.uri" inlineField="true"
                            value="<%=impp.getUri()%>" label=""
                            disabled="<%=!hasUpdatePermission%>" />

                        <liferay-ui:icon-help message="impp.uri-help" />
                    </div>
                </div>
            </aui:col>
        </aui:row>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="categories" id="categories">
    <aui:container>
        <%
            List<CategoriesDTO> categoriesList = contact_.getCategoriesList(); 

            for (CategoriesDTO categories : categoriesList) {
        %>
        <aui:row>
            <aui:col span="12">
                <div class="lfr-form-row">
                    <div class="row-fields">
                    
                        <div class="sort-handle"></div>
                        
                        <aui:input name="categories.type" inlineField="true" label="type"
                            value="<%=categories.getType()%>"
                            disabled="<%=!hasUpdatePermission%>" />
                        
                        <aui:input name="categories.value" inlineField="true" label=""
                            value="<%=categories.getValue()%>"
                            disabled="<%=!hasUpdatePermission%>" />
                            
                        <liferay-ui:icon-help message="categories-help" />

                    </div>
                </div>
            </aui:col>
        </aui:row>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="kind">
    <aui:select name="kind" title="kind-help" label="" inlineField="true"
        disabled="<%= !hasUpdatePermission %>">
        <%
            for (String kind : kinds) {
        %>
        <aui:option value="<%=kind%>" label="<%=kind%>"
            selected="<%=kind.equalsIgnoreCase(contact_.getKind())%>" />
        <%
            }
        %>
    </aui:select>
    
    <liferay-ui:icon-help message="kind-help"/>

</aui:fieldset>

<%-- Configure auto-fields --%>
<aui:script use="liferay-auto-fields">

    var emailAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />email',
        namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
        on : {
            'clone' : function(event) {
                restoreOriginalNames(event);
            }
        }
    }).render();

    var phoneAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />phone',
        namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
        on : {
            'clone' : function(event) {
                restoreOriginalNames(event);
            }
        }
    }).render();
    
    var imppAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />impp',
        namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
        on : {
            'clone' : function(event) {
                restoreOriginalNames(event);
            }
        }
    }).render();
       
    var categoriesAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />categories',
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