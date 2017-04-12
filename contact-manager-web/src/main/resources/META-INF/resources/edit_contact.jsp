<%--
    edit_contact.jsp: edit a single contact. 
    
    Created:    2015-05-07 23:40 by Christian Berndt
    Modified:   2017-04-10 16:27 by Christian Berndt
    Version:    1.1.7
--%>


<%@ include file="init.jsp"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    String backURL = ParamUtil.getString(request, "backURL", redirect);

    String windowId = "";
    windowId = ParamUtil.getString(request, "windowId");

    // Close the popup, if we are in popup mode, a redirect was provided
    // and the windowId is "editContact" (which means, viewByDefault 
    // is false.

    if (Validator.isNotNull(redirect) && themeDisplay.isStatePopUp() &&
        "editContact".equals(windowId)) {

        PortletURL closeURL = renderResponse.createRenderURL();
        closeURL.setParameter("mvcPath", "/close_popup.jsp");
        closeURL.setParameter("redirect", redirect);
        closeURL.setParameter("windowId", windowId);
        backURL = closeURL.toString();
    }

    String historyKey = ParamUtil.getString(request, "historyKey");

    String mvcPath = ParamUtil.getString(request, "mvcPath");

    // Retrieve the display settings.
    PortletPreferences preferences = renderRequest.getPreferences();

    String portletResource =
        ParamUtil.getString(request, "portletResource");

    if (Validator.isNotNull(portletResource)) {

        preferences =
            PortletPreferencesFactoryUtil.getPortletSetup(
                request, portletResource);
    }

    Contact contact_ =
        (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    String durationInMinutes = null;

    String namespace = portletDisplay.getNamespace();
    
    // TODO: re-enable permission checks 
    boolean hasUpdatePermission = true; 
//     boolean hasUpdatePermission = ContactPermission.contains(permissionChecker, contact_,
//             ContactActionKeys.UPDATE);
    boolean hasViewPermission = true; 
//     boolean hasViewPermission = ContactPermission.contains(permissionChecker, contact_,
//             ContactActionKeys.VIEW);
    boolean hasDeletePermission = true; 
//     boolean hasDeletePermission = ContactPermission.contains(permissionChecker, contact_,
//             ContactActionKeys.DELETE);
    boolean hasPermissionsPermission = true; 
//     boolean hasPermissionsPermission = ContactPermission.contains(permissionChecker, contact_, 
//             ContactActionKeys.PERMISSIONS);
%>

<portlet:actionURL name="updateContact" var="updateContactURL"
    windowState="<%=LiferayWindowState.POP_UP.toString() %>">
    <portlet:param name="mvcPath" value="/edit_contact.jsp" />
</portlet:actionURL>

<liferay-ui:header title="contact-manager" backURL="<%=backURL%>"
    showBackURL="<%=true%>" />

<aui:form method="post" action="<%=updateContactURL%>" name="fm">

    <aui:input name="userId" type="hidden"
        value="<%=String.valueOf(themeDisplay.getUserId())%>" />

    <%-- The model for this record. --%>
    <aui:model-context bean="<%=contact_%>"
        model="<%=Contact.class%>" />

    <aui:row>
        <aui:col span="6">
            <aui:fieldset>

                <aui:input name="backURL" type="hidden"
                    value="<%=backURL%>" />

                <aui:input name="redirect" type="hidden"
                    value="<%=redirect%>" />

                <aui:input name="contactId" type="hidden" />

                <aui:input name="card" 
                    disabled="<%=!hasUpdatePermission%>"
                    helpMessage="card-help" 
                    type="textarea"
                    />

            </aui:fieldset>
        </aui:col>

        <aui:col span="6">

            <aui:button-row>
                <aui:button type="submit" disabled="<%=!hasUpdatePermission%>"/>
            </aui:button-row>

        </aui:col>
    </aui:row>
</aui:form>