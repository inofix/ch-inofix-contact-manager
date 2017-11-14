<%--
    general.jsp: Edit the contact's basic contact information. 
    
    Created:    2015-05-08 18:02 by Christian Berndt
    Modified:   2017-11-14 16:31 by Christian Berndt
    Version:    1.2.7
--%>

<%@ include file="/init.jsp"%>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    boolean hasUpdatePermission = false;
    boolean isDownloadDisabled = false;

    if (contact_ == null) {

        contact_ = ContactServiceUtil.createContact();
        hasUpdatePermission = true;
        isDownloadDisabled = true;


    } else {

        hasUpdatePermission = ContactPermission.contains(permissionChecker, contact_, ActionKeys.UPDATE);
    }

    String namespace = liferayPortletResponse.getNamespace();
%>

<%
    // TODO: Make the emailTypes configurable
    String[] emailTypes = new String[] { "home", "work", "other" };

    String[] imppTypes = new String[] { "other",
            ImppType.BUSINESS.getValue(), ImppType.HOME.getValue(),
            ImppType.MOBILE.getValue(), ImppType.PERSONAL.getValue(),
            ImppType.WORK.getValue() };

    // TODO: Make imppProtocols configurable
    String[] imppProtocols = new String[] { "aim", "jabber",
            "yahoo", "gadu-gadu", "msn", "icq", "groupwise", "skype",
            "twitter", "other" };

    String[] kinds = new String[] { Kind.APPLICATION, Kind.DEVICE,
            Kind.GROUP, Kind.INDIVIDUAL, Kind.LOCATION, Kind.ORG };

    // TODO: Make the phoneTypes configurable
    String[] phoneTypes = new String[] { 
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
            TelephoneType.WORK.getValue(),
            "other"
            };
%>

<aui:row>
    <aui:fieldset cssClass="col-md-12 kind" markupView="<%= markupView %>">
    
        <aui:select helpMessage="kind-help" inlineField="true" name="kind" title="kind-help"
            disabled="<%= !hasUpdatePermission %>" onChange="<%= liferayPortletResponse.getNamespace() + "submitForm()" %>">
            <%
                for (String kind : kinds) {
            %>
            <aui:option value="<%=kind%>" label="<%=kind%>"
                selected="<%=kind.equalsIgnoreCase(contact_.getKind())%>" />
            <%
                }
            %>
        </aui:select>

        <portlet:resourceURL var="downloadVCardURL" id="exportContacts">
            <portlet:param name="<%= Constants.CMD %>" value="download" />
            <portlet:param name="contactId" value="<%=String.valueOf(contact_.getContactId())%>" />
        </portlet:resourceURL>

        <aui:button cssClass="btn-download pull-right"
            disabled="<%=isDownloadDisabled%>"
            href="<%=downloadVCardURL%>" icon="icon-download"
            value="download" />

    </aui:fieldset>
</aui:row>

<aui:row>

    <aui:fieldset cssClass="col-md-6" label="name" id="<%= namespace + "name" %>" markupView="<%= markupView %>">
    
        <c:choose>
            <c:when test="<%= Kind.ORG.equals(contact_.getKind()) %>">
                
                <aui:input name="company" bean="<%=contact_%>"
                    helpMessage="company-help"
                    disabled="<%=!hasUpdatePermission%>" />
        
                <aui:input name="department" bean="<%=contact_%>"
                    helpMessage="department-help"
                    disabled="<%=!hasUpdatePermission%>" />
        
                <aui:input name="office" bean="<%=contact_%>"
                    helpMessage="office-help"
                    disabled="<%=!hasUpdatePermission%>" />
                 
            </c:when>
            <c:otherwise>
    
                <aui:input name="formattedName" bean="<%=contact_%>"
                    helpMessage="formatted-name-help"
                    disabled="<%= true %>" />
                    
                <%                
                    for (String snField : snFields) {
                %>
                <aui:input helpMessage='<%= CamelCaseUtil.fromCamelCase(snField) + "-help" %>'
                    name="<%=snField%>" bean="<%=contact_%>"
                    disabled="<%= !hasUpdatePermission %>"/>
                <%
                    }
                %>
            
                <aui:input name="nickname" bean="<%=contact_%>"
                    helpMessage="nickname-help" disabled="<%= !hasUpdatePermission %>" />
            
            </c:otherwise>
        </c:choose>

    </aui:fieldset>
    
    <aui:fieldset cssClass="col-md-6" label="email" helpMessage="email.address-help" id="<%= namespace + "email" %>" markupView="<%= markupView %>">
        <%
            List<EmailDTO> emails = contact_.getEmails();
    
            for (EmailDTO email : emails) {
        %>
            <div class="lfr-form-row">
                <div class="row-fields">
                
                   <div class="sort-handle"></div>
                
                    <aui:select name="email.type" label="" inlineField="true"
                        disabled="<%=!hasUpdatePermission%>">
                        <%
                            for (String emailType : emailTypes) {
                        %>
                        <aui:option value="<%=emailType%>" label="<%=emailType%>"
                            selected="<%=emailType.equalsIgnoreCase(email.getType())%>" />
                        <%
                            }
                        %>
                    </aui:select>
                    <aui:input name="email.address" inlineField="true"
                        value="<%=email.getAddress()%>" label=""
                        disabled="<%=!hasUpdatePermission%>" />
                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>

    <aui:fieldset cssClass="col-md-6" label="phone" helpMessage="phone.number-help" id="<%= namespace + "phone" %>" markupView="<%= markupView %>">
        <%
            List<PhoneDTO> phones = contact_.getPhones(); 
                        
            for (PhoneDTO phone : phones) {
        %>
            <div class="lfr-form-row">
                <div class="row-fields">
                
                    <div class="sort-handle"></div>
                   
                    <aui:select name="phone.type" label="" inlineField="true"
                        disabled="<%=!hasUpdatePermission%>">
                        <%
                            for (String phoneType : phoneTypes) {
                        %>
                        <aui:option value="<%=phoneType%>" label="<%=phoneType%>"
                            selected="<%=phoneType.equalsIgnoreCase(phone.getType())%>" />
                        <%
                            }
                        %>
                    </aui:select>
                    <aui:input name="phone.number" inlineField="true"
                        value="<%=phone.getNumber()%>" label=""
                        disabled="<%=!hasUpdatePermission%>" />
    
                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>
    
    <aui:fieldset cssClass="col-md-6" label="instant-messaging" helpMessage="impp.uri-help" id="<%= namespace + "impp" %>" markupView="<%= markupView %>">
        <%
            List<ImppDTO> impps = contact_.getImpps();
                                        
            for (ImppDTO impp : impps) {
        %>
            <div class="lfr-form-row">
                <div class="row-fields">
                
                    <div class="sort-handle"></div>
                    
                    <aui:select name="impp.type" label="" inlineField="true"
                        disabled="<%=!hasUpdatePermission%>">
                        <%
                            for (String imppType : imppTypes) {
                        %>
                        <aui:option value="<%=imppType%>" label="<%=imppType%>"
                            selected="<%=imppType.equalsIgnoreCase(impp.getType())%>" />
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
                            selected="<%=imppProtocol.equalsIgnoreCase(impp.getProtocol())%>" />
                        <%
                            }
                        %>
                    </aui:select>
    
                    <aui:input name="impp.uri" inlineField="true"
                        value="<%=impp.getUri()%>" label=""
                        disabled="<%=!hasUpdatePermission%>" />
                        
                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>
    
    <aui:fieldset cssClass="col-md-6" label="categories" helpMessage="categories-help" id="<%= namespace + "categories" %>" markupView="<%= markupView %>">
        <%
            List<CategoriesDTO> categoriesList = contact_.getCategoriesList(); 
    
            for (CategoriesDTO categories : categoriesList) {
        %>
            <div class="lfr-form-row">
                <div class="row-fields">
                
                    <div class="sort-handle"></div>
                    
                    <aui:input name="categories.type" inlineField="true" label="type"
                        value="<%=categories.getType()%>"
                        disabled="<%=!hasUpdatePermission%>" />
                    
                    <aui:input name="categories.value" inlineField="true" label=""
                        value="<%=categories.getValue()%>"
                        disabled="<%=!hasUpdatePermission%>" />
                        
                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>
    
</aui:row>

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

<aui:script>
    function <portlet:namespace />submitForm() {
    
        var form = AUI.$(document.<portlet:namespace />fm);
    
        submitForm(form);
    }
</aui:script>
