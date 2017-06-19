<%--
    init.jsp: Common setup code for the contact manager portlet.

    Created:     2017-03-30 16:44 by Stefan Luebbers
    Modified:    2017-06-19 15:45 by Christian Berndt
    Version:     1.0.4
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend"%>
<%@taglib uri="http://liferay.com/tld/aui" prefix="aui"%>
<%@taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%>
<%@taglib uri="http://liferay.com/tld/security" prefix="liferay-security"%>
<%@taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@taglib uri="http://liferay.com/tld/util" prefix="liferay-util"%>

<%@page import="ch.inofix.contact.constants.ContactActionKeys"%>
<%@page import="ch.inofix.contact.constants.PortletKeys"%>
<%@page import="ch.inofix.contact.model.Contact"%>
<%@page import="ch.inofix.contact.service.permission.ContactPermission"%>
<%@page import="ch.inofix.contact.service.ContactServiceUtil"%>
<%@page import="ch.inofix.contact.service.util.ContactUtil"%>
<%@page import="ch.inofix.contact.web.configuration.ContactManagerConfiguration"%>
<%@page import="ch.inofix.contact.web.internal.constants.ContactManagerWebKeys"%>
<%@page import="ch.inofix.contact.web.internal.search.ContactDisplayTerms"%>
<%@page import="ch.inofix.contact.web.internal.search.ContactSearch"%>
<%@page import="ch.inofix.contact.web.internal.search.ContactSearchTerms"%>

<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="com.liferay.portal.kernel.exception.PortalException"%>
<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.model.User"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.portlet.PortalPreferences"%>
<%@page import="com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Document"%>
<%@page import="com.liferay.portal.kernel.search.Field"%>
<%@page import="com.liferay.portal.kernel.search.Hits"%>
<%@page import="com.liferay.portal.kernel.search.IndexerRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.search.Indexer"%>
<%@page import="com.liferay.portal.kernel.search.SearchContextFactory"%>
<%@page import="com.liferay.portal.kernel.search.SearchContext"%>
<%@page import="com.liferay.portal.kernel.search.Sort"%>
<%@page import="com.liferay.portal.kernel.search.SortFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.security.auth.PrincipalException"%>
<%@page import="com.liferay.portal.kernel.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.DateUtil"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.util.HttpUtil"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePairComparator"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="com.liferay.portal.kernel.util.PrefsParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.PortalUtil"%>
<%@page import="com.liferay.portal.kernel.util.SetUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.StringUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.workflow.WorkflowConstants"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>

<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="javax.portlet.ResourceURL"%>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
    PortalPreferences portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(request);

    String markupView = "lexicon";

    String tabs1 = ParamUtil.getString(request, "tabs1", "timetracker");
    String tabs2 = ParamUtil.getString(request, "tabs2", "export");

    ContactManagerConfiguration contactManagerConfiguration = (ContactManagerConfiguration) renderRequest
            .getAttribute(ContactManagerConfiguration.class.getName());

    if (Validator.isNotNull(contactManagerConfiguration)) {

        markupView = portletPreferences.getValue("markup-view", contactManagerConfiguration.markupView());

        // because of current checkbox configuration
        if ("false".equals(markupView)) {
            markupView = "";
        }
    }
%>