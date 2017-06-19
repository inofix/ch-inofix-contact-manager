<%--
    error.jsp: error page of the contact-manager portlet.

    Created:     2017-06-19 17:43 by Christian Berndt
    Modified:    2017-06-19 17:43 by Christian Berndt
    Version:     1.0.0
--%>


<%@ include file="/init.jsp"%>

<%@page import="ch.inofix.contact.exception.NoSuchContactException"%>

<liferay-ui:error-header />

<liferay-ui:error exception="<%= NoSuchContactException.class %>"
    message="the-contact-could-not-be-found" />

<liferay-ui:error-principal />
