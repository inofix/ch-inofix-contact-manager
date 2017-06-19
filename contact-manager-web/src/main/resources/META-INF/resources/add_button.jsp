<%--
    add_button.jsp: add a new contact 
    
    Created:    2017-06-19 16:34 by Christian Berndt
    Modified:   2017-06-19 16:34 by Christian Berndt
    Version:    1.0.0
--%>

<%@page import="ch.inofix.contact.service.permission.ContactManagerPermission"%>
<%@ include file="/init.jsp" %>

<c:if test="<%=ContactManagerPermission.contains(permissionChecker, scopeGroupId,
                        ContactActionKeys.ADD_CONTACT)%>">

    <liferay-frontend:add-menu>

        <portlet:renderURL var="addContactURL">
            <portlet:param name="redirect" value="<%=currentURL%>" />
            <portlet:param name="mvcPath" value="/edit_contact.jsp" />
        </portlet:renderURL>

        <liferay-frontend:add-menu-item
            title='<%=LanguageUtil.get(request, "add-contact")%>'
            url="<%=addContactURL.toString()%>" />

    </liferay-frontend:add-menu>

</c:if>
