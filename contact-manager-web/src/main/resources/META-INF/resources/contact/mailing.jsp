<%--
    edit_mailing_address.jsp: Edit the contact's mailing addresses. 
    
    Created:    2015-05-11 18:30 by Christian Berndt
    Modified:   2017-07-03 17:16 by Christian Berndt
    Version:    1.1.0
--%>

<%@ include file="/init.jsp"%>

<%@page import="ezvcard.parameter.AddressType"%>
<%@page import="ch.inofix.contact.dto.AddressDTO"%>

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
	// TODO: make the list of address-types configurable
	String[] addressTypes = new String[] { "other",
			// AddressType.DOM.getValue(),       // Not longer supported in v.4.0
			AddressType.HOME.getValue(),
			// AddressType.INTL.getValue(),      // Not longer supported in v.4.0
			// AddressType.PARCEL.getValue(),    // Not longer supported in v.4.0
			// AddressType.POSTAL.getValue(),    // Not longer supported in v.4.0
			AddressType.WORK.getValue() };
%>

<aui:row>
    <aui:fieldset id="<%= namespace + "address" %>" cssClass="address">
		<%
		    for (AddressDTO address : contact_.getAddresses()) {
		%>
            <div class="lfr-form-row">
            
                <div class="row-fields">
                
                    <div class="sort-handle"></div>
                    
                    <div class="col-md-6">
                    
                        <aui:input name="address.streetAddress" type="textarea"
                            label="street" 
                            value="<%=address.getStreetAddress()%>"
                            helpMessage="address.street-address-help"
                            disabled="<%=!hasUpdatePermission%>" />
                            
                        <aui:input name="address.poBox" value="<%=address.getPoBox()%>"
                            label="po-box" helpMessage="address.po-box-help"
                            disabled="<%=!hasUpdatePermission%>" />
                            
                        <aui:select name="address.type" label="type"
                            helpMessage="address.type-help"
                            disabled="<%=!hasUpdatePermission%>">
                            
                            <%
                                for (String type : addressTypes) {
                            %>
                            <aui:option value="<%=type%>" label="<%=type%>"
                                selected="<%=type.equalsIgnoreCase(address
                                                .getType())%>" />
                            <%
                                }
                            %>
                        </aui:select>
                    
                    </div>
                    
                    <div class="col-md-6">

                        <aui:input name="address.locality" label="city"
                            value="<%=address.getLocality()%>"
                            helpMessage="address.locality-help"
                            disabled="<%=!hasUpdatePermission%>" />
                            
                        <aui:input name="address.postalCode" label="postal-code"
                            value="<%=address.getPostalCode()%>"
                            helpMessage="address.postal-code-help"
                            disabled="<%=!hasUpdatePermission%>" />
                            
                        <aui:input name="address.region" label="region"
                            value="<%=address.getRegion()%>"
                            helpMessage="address.region-help"
                            disabled="<%=!hasUpdatePermission%>" />
                            
                        <aui:input name="address.country" label="country"
                            value="<%=address.getCountry()%>"
                            helpMessage="address.country-help"
                            disabled="<%=!hasUpdatePermission%>" />
                        
                    </div>
                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>
</aui:row>


<%-- Configure auto-fields --%>
<aui:script use="liferay-auto-fields">

    var addressAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />address',
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
