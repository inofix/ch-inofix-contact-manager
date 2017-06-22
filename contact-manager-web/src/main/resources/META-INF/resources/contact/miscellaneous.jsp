<%--
    edit_miscellaneous.jsp: Edit the miscellaneous contact information. 
    
    Created:    2015-05-16 20:06 by Christian Berndt
    Modified:   2017-04-12 18:33 by Christian Berndt
    Version:    1.1.4
--%>

<%@ include file="/init.jsp"%>

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.SortedSet"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.TimeZone"%>

<%@page import="ch.inofix.contact.dto.ExpertiseDTO"%>
<%@page import="ezvcard.property.Gender"%>
<%@page import="ch.inofix.contact.dto.HobbyDTO"%>
<%@page import="ch.inofix.contact.dto.InterestDTO"%>
<%@page import="ch.inofix.contact.dto.LanguageDTO"%>

<%@page import="ezvcard.parameter.ExpertiseLevel"%>
<%@page import="ezvcard.parameter.HobbyLevel"%>
<%@page import="ezvcard.parameter.InterestLevel"%>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    if (contact_ == null) {
        contact_ = ContactServiceUtil.createContact();
    }

    // TODO: check permissions
    boolean hasUpdatePermission = true; 
%>

<%
    String currentTimeZone = themeDisplay.getTimeZone().getID();

    if (Validator.isNotNull(contact_.getTimezone())) {
        currentTimeZone = contact_.getTimezone();
    }

    String[] expertiseLevels = new String[] {
            ExpertiseLevel.BEGINNER.getValue(),
            ExpertiseLevel.AVERAGE.getValue(),
            ExpertiseLevel.EXPERT.getValue() };
    
    String[] genders = new String[] {
        Gender.FEMALE, 
        Gender.MALE,
        Gender.NONE, 
        Gender.OTHER,
        Gender.UNKNOWN
    }; 

    String[] hobbyLevels = new String[] { HobbyLevel.HIGH.getValue(),
            HobbyLevel.MEDIUM.getValue(), HobbyLevel.LOW.getValue(), };

    String[] interestLevels = new String[] {
            InterestLevel.HIGH.getValue(),
            InterestLevel.MEDIUM.getValue(),
            InterestLevel.LOW.getValue(), };

    List<KeyValuePair> selectedLanguages = new ArrayList<KeyValuePair>();
    List<LanguageDTO> languages = contact_.getLanguages();
    for (LanguageDTO language : languages) {
        String key = language.getKey();
        selectedLanguages.add(new KeyValuePair(key, new Locale(key)
                .getDisplayLanguage(locale)));
    }

    List<KeyValuePair> availableLanguages = new ArrayList<KeyValuePair>();
    String[] keys = Locale.getISOLanguages();
    
    Map<String, String> map = new HashMap<String, String>();
    for (String key : keys) {
        map.put( new Locale(
                key).getDisplayLanguage(locale), key); 
    }
    
    SortedSet<String> set = new TreeSet<String>(map.keySet());  

    for (String key : set) {
        KeyValuePair keyValuePair = new KeyValuePair(map.get(key), key);
        
        if (selectedLanguages.indexOf(keyValuePair) < 0) {
            availableLanguages.add(keyValuePair);
        }
    }
%>

<aui:fieldset label="expertise" id="expertise">
    <aui:container>
        <%
            List<ExpertiseDTO> expertises = contact_.getExpertises();

            for (ExpertiseDTO expertise : expertises) {
        %>
        <aui:row>
            <aui:col span="12">
                <div class="lfr-form-row">
                    <div class="row-fields">
                    
                       <div class="sort-handle"></div>
                    
                        <aui:input name="expertise" inlineField="true" label=""
                            value="<%=expertise.getValue()%>"
                            disabled="<%=!hasUpdatePermission%>" />
                            
                        <aui:select name="expertise.level" inlineField="true" label=""
                            disabled="<%=!hasUpdatePermission%>">
                            <%
                                for (String expertiseLevel : expertiseLevels) {
                            %>
                            <aui:option value="<%=expertiseLevel%>"
                                label="<%=expertiseLevel%>"
                                selected="<%=expertiseLevel
                                                .equalsIgnoreCase(expertise
                                                        .getLevel())%>" />
                            <%
                                }
                            %>
                        </aui:select>

                        <liferay-ui:icon-help message="expertise-help" />
                    </div>
                </div>
            </aui:col>
        </aui:row>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="hobbies" id="hobby">
    <aui:container>
        <%
            List<HobbyDTO> hobbies = contact_.getHobbies();

                    for (HobbyDTO hobby : hobbies) {
        %>
        <aui:row>
            <aui:col span="12">
                <div class="lfr-form-row">
                    <div class="row-fields">
                    
                        <div class="sort-handle"></div>
                    
                        <aui:input name="hobby" inlineField="true" label=""
                            value="<%=hobby.getValue()%>"
                            disabled="<%=!hasUpdatePermission%>" />
                            
                        <aui:select name="hobby.level" inlineField="true" label=""
                            disabled="<%=!hasUpdatePermission%>">
                            <%
                                for (String hobbyLevel : hobbyLevels) {
                            %>
                            <aui:option value="<%=hobbyLevel%>" label="<%=hobbyLevel%>"
                                selected="<%=hobbyLevel
                                                .equalsIgnoreCase(hobby
                                                        .getLevel())%>" />
                            <%
                                }
                            %>
                        </aui:select>

                        <liferay-ui:icon-help message="hobby-help" />

                    </div>
                </div>
            </aui:col>
        </aui:row>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>


<aui:fieldset label="interests" id="interest">
    <aui:container>
        <%
            List<InterestDTO> interests = contact_.getInterests();

            for (InterestDTO interest : interests) {
        %>
        <aui:row>
            <aui:col span="12">
                <div class="lfr-form-row">
                    <div class="row-fields">
                    
                        <div class="sort-handle"></div>
                        
                        <aui:input name="interest" inlineField="true" label=""
                            value="<%=interest.getValue()%>"
                            disabled="<%=!hasUpdatePermission%>" />
                            
                        <aui:select name="interest.level" inlineField="true" label=""
                            disabled="<%=!hasUpdatePermission%>">
                            <%
                                for (String interestLevel : interestLevels) {
                            %>
                            <aui:option value="<%=interestLevel%>" label="<%=interestLevel%>"
                                selected="<%=interestLevel
                                                .equalsIgnoreCase(interest
                                                        .getLevel())%>" />
                            <%
                                }
                            %>
                        </aui:select>

                        <liferay-ui:icon-help message="interest-help" />

                    </div>
                </div>
            </aui:col>
        </aui:row>
        <%
            }
        %>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="languages" id="languages" helpMessage="languages-help">

    <liferay-ui:input-move-boxes rightList="<%= availableLanguages %>"
        rightTitle="available" leftBoxName="selectedLanguages"
        leftList="<%= selectedLanguages %>" rightBoxName="availableLanguages"
        leftTitle="current" leftReorder="true" />
        
</aui:fieldset>

<aui:select name="gender" helpMessage="gender-help"
        disabled="<%= !hasUpdatePermission %>">
    <%
        for (String gender : genders) {
    %>
    <aui:option value="<%=gender%>" label='<%="gender-" + gender.toLowerCase() %>'
        selected="<%=gender.equalsIgnoreCase(contact_.getGender())%>" />
    <%
        }
    %>
</aui:select>

<aui:fieldset label="time-zone">
    <aui:container>
        <aui:row>
            <aui:col span="12">
                <aui:select name="timezone" label="" inlineField="true"
                    disabled="<%= !hasUpdatePermission %>">
                    <%
                        String[] timezones = TimeZone.getAvailableIDs();
                        for (String timezone : timezones) {
                    %>
                    <aui:option value="<%=timezone%>" label="<%=timezone%>"
                        selected="<%=timezone.equalsIgnoreCase(currentTimeZone)%>" />
                    <%
                        }
                    %>
                </aui:select>
                <liferay-ui:icon-help message="timezone-help"/>
            </aui:col>
        </aui:row>
    </aui:container>
</aui:fieldset>

<aui:fieldset label="related-assets" helpMessage="related-assets-help">

    <liferay-ui:input-asset-links
        className="<%= Contact.class.getName() %>"
        classPK="<%= contact_.getContactId() %>" />

</aui:fieldset>


<%-- Configure autofields 
<aui:script use="liferay-auto-fields">

    var expertiseAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />expertise',
        namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
        on : {
            'clone' : function(event) {
                restoreOriginalNames(event);
            }
        }
    }).render();

    var hobbyAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />hobby',
        namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
        on : {
            'clone' : function(event) {
                restoreOriginalNames(event);
            }
        }
    }).render();
    
    var interestAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />interest',
        namespace : '<portlet:namespace />',
        sortable : true,
        sortableHandle: '.sort-handle',
        on : {
            'clone' : function(event) {
                restoreOriginalNames(event);
            }
        }
    }).render();

</aui:script>
--%>
