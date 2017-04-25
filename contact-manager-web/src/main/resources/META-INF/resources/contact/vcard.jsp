<%-- 
    edit_contact/edit_vcard.jsp: Edit the vCard String of the contact.
    
    Created:    2015-05-08 15:42 by Christian Berndt
    Modified:   2017-04-25 22:56 by Stefan Luebbers
    Version:    1.0.4
--%>

<%@ include file="/init.jsp"%>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    if (contact_ == null) {
        contact_ = ContactServiceUtil.createContact();
    }

    // TODO: check permissions
    boolean hasUpdatePermission = true; 
%>

<aui:fieldset label="v-card">

	<aui:row>
		<aui:col span="10">
			<aui:input name="vCard" type="textarea" cssClass="v-card"
				value="<%=contact_.getCard()%>" disabled="true" label=""
				inlineField="true" />
			<liferay-ui:icon-help message="v-card-help" />
		</aui:col>
		<aui:col span="2">

			<portlet:resourceURL var="serveVCardURL" id="serveVCard">
				<portlet:param name="contactId"
					value="<%= String.valueOf(contact_.getContactId()) %>" />
			</portlet:resourceURL>

			<aui:button href="<%=serveVCardURL%>" value="download" />

		</aui:col>
	</aui:row>
</aui:fieldset>
