<%--
    toolbar.jsp: The toolbar of the contact-manager portlet
    
    Created:    2015-07-03 19:00 by Christian Berndt
    Modified:   2017-10-28 18:28 by Christian Berndt
    Version:    1.0.5
 --%>

<%@ include file="init.jsp"%>

<%
    String orderByCol = ParamUtil.getString(request, "orderByCol", "modified-date");

    String orderByType = ParamUtil.getString(request, "orderByType", "desc");

    String searchContainerId = ParamUtil.getString(request, "searchContainerId");
    
    int total = GetterUtil.getInteger(request.getAttribute("view.jsp-total"));
    
    PortletURL portletURL = renderResponse.createRenderURL();
    portletURL.setParameters(renderRequest.getParameterMap());  
%>

<liferay-frontend:management-bar 
    disabled="<%= total == 0 %>"
    includeCheckBox="<%= true %>"
    searchContainerId="<%= searchContainerId %>">

    <liferay-frontend:management-bar-filters>
        <liferay-frontend:management-bar-sort
            orderByCol="<%= orderByCol %>"
            orderByType="<%= orderByType %>"
            orderColumns='<%= columns %>' portletURL="<%= portletURL %>" />
    </liferay-frontend:management-bar-filters>

    <liferay-frontend:management-bar-buttons>
        <%-- 
        <liferay-frontend:management-bar-button
            disabled="<%=total == 0%>"
            href="<%=downloadURL.toString()%>" icon="download"
            label="download" />
        --%>
        <liferay-util:include page="/display_style_buttons.jsp"
            servletContext="<%=application%>" />
    </liferay-frontend:management-bar-buttons>
    
    <liferay-frontend:management-bar-action-buttons>
        <%--    
        <liferay-frontend:management-bar-sidenav-toggler-button
            icon="info-circle"
            label="info"
        />
        --%>
        <liferay-frontend:management-bar-button href='<%= "javascript:" + renderResponse.getNamespace() + "deleteEntries();" %>' icon='<%= "times" %>' label='<%= "delete" %>' />
<%--         <liferay-frontend:management-bar-button href='<%= "javascript:" + renderResponse.getNamespace() + "deleteEntries();" %>' icon='<%= TrashUtil.isTrashEnabled(scopeGroupId) ? "trash" : "times" %>' label='<%= TrashUtil.isTrashEnabled(scopeGroupId) ? "recycle-bin" : "delete" %>' /> --%>
    </liferay-frontend:management-bar-action-buttons>

</liferay-frontend:management-bar>

<aui:script>
    function <portlet:namespace />deleteEntries() {
        if (confirm('<%= UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-the-selected-entries") %>')) {
            var form = AUI.$(document.<portlet:namespace />fm);

            form.attr('method', 'post');
            form.fm('<%= Constants.CMD %>').val('<%= Constants.DELETE %>');
            form.fm('deleteContactIds').val(Liferay.Util.listCheckedExcept(form, '<portlet:namespace />allRowIds'));

            submitForm(form);
        }
    }
</aui:script>
