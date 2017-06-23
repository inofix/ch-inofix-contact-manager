<%--
    configuration.jsp: configuration of the contact manager portlet.
    
    Created:    2017-04-12 17:05 by Stefan Lübbers
    Modified:   2017-06-19 16:19 by Christian Berndt
    Version:    1.0.2
--%>

<%@ include file="/init.jsp"%>

<%
    String[] columns = new String[0];
    String maxHeight = "70";
    String portraitDisplay = "circle";
    String viewByDefault = "false";
    
    if (Validator.isNotNull(contactManagerConfiguration)) {
        columns = portletPreferences.getValues("columns", contactManagerConfiguration.columns());
        markupView = portletPreferences.getValue("markup-view", contactManagerConfiguration.markupView());
        maxHeight = portletPreferences.getValue("max-height", contactManagerConfiguration.maxHeight());
        portraitDisplay = portletPreferences.getValue("portrait-display", contactManagerConfiguration.portraitDisplay());
        viewByDefault = portletPreferences.getValue("view-by-default", contactManagerConfiguration.viewByDefault());
    }
    
    PortletURL portletURL = renderResponse.createRenderURL();
    
    ContactSearch searchContainer = new ContactSearch(liferayPortletRequest, portletURL);
    List<String> headerList = searchContainer.getHeaderNames();
%>

<liferay-portlet:actionURL portletConfiguration="<%= true %>"
    var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="<%= true %>"
    var="configurationRenderURL" />

<aui:form action="<%=configurationActionURL%>" method="post" name="fm"
    onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveConfiguration();" %>'>

    <div class="portlet-configuration-body-content">

        <div class="container-fluid-1280">

            <liferay-ui:panel id="contactManagerColumnsPanel"
                title="columns" extended="true">

                <aui:input name="<%=Constants.CMD%>" type="hidden"
                    value="<%=Constants.UPDATE%>" />

                <aui:input name="redirect" type="hidden"
                    value="<%=configurationRenderURL%>" />

                <aui:input name="columns" type="hidden" />

                <%
                    Set<String> availableColumns = SetUtil.fromList(headerList);

                        List<KeyValuePair> leftList = new ArrayList<KeyValuePair>();
                        for (String column : columns) {
                            leftList.add(new KeyValuePair(column, LanguageUtil.get(request, column)));
                        }
    
                        List<KeyValuePair> rightList = new ArrayList<KeyValuePair>();
                        Arrays.sort(columns);
                        for (String column : availableColumns) {
                            if (Arrays.binarySearch(columns, column) < 0) {
                                rightList.add(new KeyValuePair(column, LanguageUtil.get(request, column)));
                            }
                        }
                        rightList = ListUtil.sort(rightList, new KeyValuePairComparator(false, true));
                %>

                <liferay-ui:input-move-boxes
                    leftBoxName="currentColumns"
                    leftList="<%=leftList%>"
                    leftReorder="<%=Boolean.TRUE.toString()%>"
                    leftTitle="current" rightBoxName="availableColumns"
                    rightList="<%=rightList%>" rightTitle="available" />

            </liferay-ui:panel>

            <liferay-ui:panel id="contactManagerAppearancePanel"
                title="appearance" extended="true">
                
                <aui:input checked="<%="lexicon".equals(markupView)%>"
                    helpMessage="markup-view-help" label="use-lexicon"
                    name="markup-view" type="checkbox" value="lexicon" />

                <aui:fieldset>
                    <aui:field-wrapper label="view-by-default-label"
                        helpMessage="view-by-default-help"
                        inlineField="false">
                        <aui:input name="view-by-default" type="radio"
                            value="true"
                            checked="<%=Validator.equals(viewByDefault, "true")%>"
                            label="Yes" inlineField="true" />

                        <aui:input name="<%="view-by-default"%>"
                            type="radio" value="false"
                            checked="<%=Validator.equals(viewByDefault, "false")%>"
                            label="No" inlineField="true" />

                    </aui:field-wrapper>
                </aui:fieldset>

                <aui:fieldset label="portait-style"
                    helpMessage="portrait-style-help"
                    inlineField="false">
                    <%
                        //TODO add editable portrait values
                    %>
                    <aui:input name="max-height" value="<%=maxHeight%>"
                        inlineField="true" />
                </aui:fieldset>
                <%--
        <aui:select name="portrait-display" label="portrait-display" 
                helpMessage="portrait-display-help" inlineField="false">
            <% //for (String diplayOption : contactManagerConfiguration.
            
            %>
        </aui:select>
 --%>
            </liferay-ui:panel>

            <aui:button-row>
                <aui:button type="submit"></aui:button>
            </aui:button-row>

        </div>

    </div>
</aui:form>

<aui:script>
    function <portlet:namespace />saveConfiguration() {
        var Util = Liferay.Util;

        var form = AUI.$(document.<portlet:namespace />fm);

        form.fm('columns').val(Util.listSelect(form.fm('currentColumns')));

        submitForm(form);
    }
</aui:script>
