<%--
    export_import.jsp: Export and import contacts
    
    Created:    2017-06-19 16:08 by Christian Berndt
    Modified:   2017-06-19 16:08 by Christian Berndt
    Version:    1.0.0
--%>

<%@ include file="/init.jsp"%>

<%
    String redirect = ParamUtil.getString(request, "redirect");

    PortletURL portletURL = renderResponse.createRenderURL();

    portletURL.setParameter("mvcPath", "/view.jsp");
    portletURL.setParameter("redirect", redirect);
    portletURL.setParameter("tabs1", "export-import");

    // TODO: check export-import permissions
%>

TODO: export_import.jsp