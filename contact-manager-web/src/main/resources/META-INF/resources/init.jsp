<%--
    init.jsp: Common setup code for the contact manager portlet.

    Created:     2017-03-30 16:44 by Stefan Luebbers
    Modified:    2017-11-14 19:49 by Christian Berndt
    Version:     1.1.3
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

<%@page import="ch.inofix.contact.background.task.ContactExportBackgroundTaskExecutor"%>
<%@page import="ch.inofix.contact.background.task.ContactImportBackgroundTaskExecutor"%>
<%@page import="ch.inofix.contact.constants.ContactManagerActionKeys"%>
<%@page import="ch.inofix.contact.constants.PortletKeys"%>
<%@page import="ch.inofix.contact.dto.AddressDTO"%>
<%@page import="ch.inofix.contact.dto.CategoriesDTO"%>
<%@page import="ch.inofix.contact.dto.EmailDTO"%>
<%@page import="ch.inofix.contact.dto.ExpertiseDTO"%>
<%@page import="ch.inofix.contact.dto.FileDTO" %>
<%@page import="ch.inofix.contact.dto.HobbyDTO"%>
<%@page import="ch.inofix.contact.dto.ImppDTO"%>
<%@page import="ch.inofix.contact.dto.InterestDTO"%>
<%@page import="ch.inofix.contact.dto.LanguageDTO"%>
<%@page import="ch.inofix.contact.dto.NoteDTO"%>
<%@page import="ch.inofix.contact.dto.PhoneDTO"%>
<%@page import="ch.inofix.contact.dto.UrlDTO"%>
<%@page import="ch.inofix.contact.dto.UriDTO"%>
<%@page import="ch.inofix.contact.model.Contact"%>
<%@page import="ch.inofix.contact.service.permission.ContactManagerPortletPermission"%>
<%@page import="ch.inofix.contact.service.permission.ContactPermission"%>
<%@page import="ch.inofix.contact.service.ContactServiceUtil"%>
<%@page import="ch.inofix.contact.service.util.ContactUtil"%>
<%@page import="ch.inofix.contact.web.configuration.ContactManagerConfiguration"%>
<%@page import="ch.inofix.contact.web.internal.constants.ContactManagerWebKeys"%>
<%@page import="ch.inofix.contact.web.internal.search.ContactDisplayTerms"%>
<%@page import="ch.inofix.contact.web.internal.search.ContactSearch"%>
<%@page import="ch.inofix.contact.web.internal.search.ContactSearchTerms"%>
<%@page import="ch.inofix.contact.web.internal.search.EntriesChecker"%>
<%@page import="ch.inofix.contact.web.servlet.taglib.ui.FormNavigatorConstants"%>

<%@page import="com.liferay.background.task.kernel.util.comparator.BackgroundTaskComparatorFactoryUtil"%>
<%@page import="com.liferay.exportimport.kernel.exception.LARFileException"%>
<%@page import="com.liferay.exportimport.kernel.exception.LARFileNameException"%>
<%@page import="com.liferay.exportimport.kernel.exception.LARFileSizeException"%>
<%@page import="com.liferay.exportimport.kernel.exception.LARTypeException"%>
<%@page import="com.liferay.exportimport.kernel.lar.PortletDataHandlerKeys"%>
<%@page import="com.liferay.exportimport.kernel.lar.ExportImportHelper"%>
<%@page import="com.liferay.exportimport.kernel.lar.ExportImportHelperUtil"%>
<%@page import="com.liferay.exportimport.kernel.model.ExportImportConfiguration"%>
<%@page import="com.liferay.exportimport.kernel.service.ExportImportConfigurationLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.backgroundtask.BackgroundTask"%>
<%@page import="com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants"%>
<%@page import="com.liferay.portal.kernel.backgroundtask.BackgroundTaskManagerUtil"%>
<%@page import="com.liferay.portal.kernel.backgroundtask.BackgroundTaskStatusRegistryUtil"%>
<%@page import="com.liferay.portal.kernel.backgroundtask.BackgroundTaskStatus"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@page import="com.liferay.portal.kernel.dao.search.RowChecker"%>
<%@page import="com.liferay.portal.kernel.dao.search.SearchContainer"%>
<%@page import="com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker"%>
<%@page import="com.liferay.portal.kernel.exception.PortalException"%>
<%@page import="com.liferay.portal.kernel.exception.SystemException"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="com.liferay.portal.kernel.model.Group"%>
<%@page import="com.liferay.portal.kernel.model.Portlet"%>
<%@page import="com.liferay.portal.kernel.model.Ticket"%>
<%@page import="com.liferay.portal.kernel.model.TicketConstants"%>
<%@page import="com.liferay.portal.kernel.model.User"%>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<%@page import="com.liferay.portal.kernel.portlet.PortalPreferences"%>
<%@page import="com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.portlet.PortletURLUtil"%>
<%@page import="com.liferay.portal.kernel.portletfilerepository.PortletFileRepositoryUtil"%>
<%@page import="com.liferay.portal.kernel.repository.model.FileEntry"%>
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
<%@page import="com.liferay.portal.kernel.security.permission.ActionKeys"%>
<%@page import="com.liferay.portal.kernel.security.permission.ResourceActionsUtil"%>
<%@page import="com.liferay.portal.kernel.service.GroupLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.service.PortletLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.service.ServiceContext"%>
<%@page import="com.liferay.portal.kernel.service.TicketLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.service.UserLocalServiceUtil"%>
<%@page import="com.liferay.portal.kernel.servlet.SessionMessages"%>
<%@page import="com.liferay.portal.kernel.util.CalendarFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.CamelCaseUtil"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.DateUtil"%>
<%@page import="com.liferay.portal.kernel.util.FastDateFormatFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.util.HttpUtil"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePair"%>
<%@page import="com.liferay.portal.kernel.util.KeyValuePairComparator"%>
<%@page import="com.liferay.portal.kernel.util.ListUtil"%>
<%@page import="com.liferay.portal.kernel.util.OrderByComparator"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.PortalUtil"%>
<%@page import="com.liferay.portal.kernel.util.PrefsPropsUtil"%>
<%@page import="com.liferay.portal.kernel.util.PropsKeys"%>
<%@page import="com.liferay.portal.kernel.util.SetUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringBundler"%><%@page import="com.liferay.portal.kernel.util.PrefsParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="com.liferay.portal.kernel.util.StringUtil"%>
<%@page import="com.liferay.portal.kernel.util.TextFormatter"%>
<%@page import="com.liferay.portal.kernel.util.Time"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.workflow.WorkflowConstants"%>
<%@page import="com.liferay.trash.kernel.util.TrashUtil"%>

<%@page import="ezvcard.parameter.AddressType"%>
<%@page import="ezvcard.parameter.ImppType"%>
<%@page import="ezvcard.parameter.TelephoneType"%>
<%@page import="ezvcard.parameter.ExpertiseLevel"%>
<%@page import="ezvcard.parameter.HobbyLevel"%>
<%@page import="ezvcard.parameter.InterestLevel"%>
<%@page import="ezvcard.property.Gender"%>
<%@page import="ezvcard.property.Kind"%>

<%@page import="java.io.Serializable"%>

<%@page import="java.text.Format"%>
<%@page import="java.text.DecimalFormatSymbols"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.SortedSet"%>

<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="javax.portlet.PortletRequest"%>
<%@page import="javax.portlet.PortletURL"%>
<%@page import="javax.portlet.ResourceURL"%>

<%@page import="ezvcard.property.Kind"%>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
    PortalPreferences portalPreferences = PortletPreferencesFactoryUtil.getPortalPreferences(request);

    String[] columns = portletPreferences.getValues("columns", new String[] { "portrait", "name", "city", "country", "url", "user-name", "modified-date" });
    Format dateFormatDate = FastDateFormatFactoryUtil.getDate(locale, timeZone);
    Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);
    String markupView = portletPreferences.getValue("markupView", "lexicon");
    String maxHeight = portletPreferences.getValue("maxHeight", "70");
    String portraitDisplay = portletPreferences.getValue("portraitDisplay", "circle");

    String[] snFields = new String[] { "structuredName.prefix", "structuredName.given",
            "structuredName.additional", "structuredName.family", "structuredName.suffix" };

    String tabs1 = ParamUtil.getString(request, "tabs1", "contacts");
    String tabs2 = ParamUtil.getString(request, "tabs2", "export");

    String viewByDefault = portletPreferences.getValue("viewByDefault", "false");

    ContactManagerConfiguration contactManagerConfiguration = (ContactManagerConfiguration) request
            .getAttribute(ContactManagerConfiguration.class.getName());

    if (Validator.isNotNull(contactManagerConfiguration)) {

        columns = portletPreferences.getValues("columns", contactManagerConfiguration.columns());
        markupView = portletPreferences.getValue("markupView", contactManagerConfiguration.markupView());
        maxHeight = portletPreferences.getValue("maxHeight", contactManagerConfiguration.maxHeight());
        portraitDisplay = portletPreferences.getValue("portraitDisplay", contactManagerConfiguration.portraitDisplay());
        viewByDefault = portletPreferences.getValue("viewByDefault", contactManagerConfiguration.viewByDefault());

        // because of current checkbox configuration
        if ("false".equals(markupView)) {
            markupView = "";
        }
    }
%>