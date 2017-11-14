<%--
    edit_contact.jsp: edit a single contact. 
    
    Created:    2015-05-07 23:40 by Christian Berndt
    Modified:   2017-11-14 15:27 by Christian Berndt
    Version:    1.2.7
--%>

<%@ include file="init.jsp"%>

<%
    String cmd = Constants.ADD; 

    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    String title = LanguageUtil.get(request, "new-contact");

    boolean hasUpdatePermission = false;
    boolean hasViewPermission = false;
    boolean hasDeletePermission = false;
    boolean hasPermissionsPermission = false;

    if (contact_ == null) {
        
        contact_ = ContactServiceUtil.createContact();
        hasUpdatePermission = true;
        
    } else {
        
        cmd = Constants.UPDATE;    
        title = contact_.getName();
        hasUpdatePermission = ContactPermission.contains(permissionChecker, contact_, ContactManagerActionKeys.UPDATE);
        hasViewPermission = ContactPermission.contains(permissionChecker, contact_, ContactManagerActionKeys.VIEW);
        hasDeletePermission = ContactPermission.contains(permissionChecker, contact_, ContactManagerActionKeys.DELETE);
        hasPermissionsPermission = ContactPermission.contains(permissionChecker, contact_,
                ContactManagerActionKeys.PERMISSIONS);
    }

    String redirect = ParamUtil.getString(request, "redirect");

    String backURL = ParamUtil.getString(request, "backURL", redirect);

    portletDisplay.setShowBackIcon(true);
    portletDisplay.setURLBack(redirect);

    renderResponse.setTitle(title);

    request.setAttribute("showTitle", "true"); // used by the inofix-theme
%>

<div class="container-fluid-1280">

    <portlet:actionURL name="editContact" var="updateContactURL">
        <portlet:param name="mvcRenderCommandName" value="editContact" />
    </portlet:actionURL>

    <aui:form method="post" action="<%=updateContactURL%>" name="fm">

        <aui:input name="<%=Constants.CMD%>" type="hidden"
            value="<%=cmd%>" />

        <aui:input name="contactId" type="hidden"
            value="<%=String.valueOf(contact_.getContactId())%>" />

        <aui:input name="redirect" type="hidden" 
            value="<%=redirect%>" />

        <div class="lfr-form-content">

            <liferay-ui:form-navigator
                id="<%=FormNavigatorConstants.FORM_NAVIGATOR_ID_CONTACT%>"
                markupView="<%=markupView%>"
                showButtons="<%=hasUpdatePermission%>" />

        </div>

    </aui:form>

</div>
