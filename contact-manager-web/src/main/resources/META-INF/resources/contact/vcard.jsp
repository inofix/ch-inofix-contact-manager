<%-- 
    contact/vcard.jsp: Edit the vCard String of the contact.
    
    Created:    2015-05-08 15:42 by Christian Berndt
    Modified:   2017-06-24 13:45 by Christian Berndt
    Version:    1.0.5
--%>

<%@ include file="/init.jsp"%>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    if (contact_ == null) {
        contact_ = ContactServiceUtil.createContact();
    }

    boolean hasUpdatePermission = ContactPermission.contains(permissionChecker, contact_, ActionKeys.UPDATE);
%>

<aui:row>
    <aui:fieldset cssClass="col-md-12" helpMessage="v-card-help" label="v-card" markupView="<%= markupView %>">

		<aui:input name="vCard" type="textarea" cssClass="v-card"
			value="<%=contact_.getCard()%>" disabled="true" label="" />

        <%-- 
		<portlet:resourceURL var="serveVCardURL" id="serveVCard">
			<portlet:param name="contactId"
				value="<%= String.valueOf(contact_.getContactId()) %>" />
		</portlet:resourceURL>

		<aui:button href="<%=serveVCardURL%>" value="download" />
        --%>
        
    </aui:fieldset>
</aui:row>
