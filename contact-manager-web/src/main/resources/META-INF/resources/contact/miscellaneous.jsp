<%--
    edit_miscellaneous.jsp: Edit the miscellaneous contact information. 
    
    Created:    2015-05-16 20:06 by Christian Berndt
    Modified:   2017-10-30 19:13 by Christian Berndt
    Version:    1.1.9
--%>

<%@ include file="/init.jsp"%>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    boolean hasUpdatePermission = false;

    if (contact_ == null) {

        contact_ = ContactServiceUtil.createContact();
        hasUpdatePermission = true;

    } else {

        hasUpdatePermission = ContactPermission.contains(permissionChecker, contact_, ActionKeys.UPDATE);
    }

    String namespace = liferayPortletResponse.getNamespace();

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

<aui:row>
    <aui:fieldset cssClass="col-md-6" helpMessage="expertise-help" label="expertise" id="<%= namespace + "expertise" %>" markupView="<%= markupView %>">

        <%
            List<ExpertiseDTO> expertises = contact_.getExpertises();

                for (ExpertiseDTO expertise : expertises) {
        %>

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
                            selected="<%=expertiseLevel.equalsIgnoreCase(expertise.getLevel())%>" />
                        <%
                            }
                        %>
                    </aui:select>

                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>
    
    <aui:fieldset cssClass="col-md-6" helpMessage="hobby-help" label="hobbies" id="<%= namespace + "hobby" %>" markupView="<%= markupView %>">
    
        <%
            List<HobbyDTO> hobbies = contact_.getHobbies();

                for (HobbyDTO hobby : hobbies) {
        %>
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
                            selected="<%=hobbyLevel.equalsIgnoreCase(hobby.getLevel())%>" />
                        <%
                            }
                        %>
                    </aui:select>

                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>
    
    <aui:fieldset cssClass="col-md-6" helpMessage="interest-help" label="interests" id="<%= namespace + "interest" %>" markupView="<%= markupView %>">
        <%
            List<InterestDTO> interests = contact_.getInterests();

            for (InterestDTO interest : interests) {
        %>
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
                            selected="<%=interestLevel.equalsIgnoreCase(interest.getLevel())%>" />
                        <%
                            }
                        %>
                    </aui:select>

                </div>
            </div>
        <%
            }
        %>
    </aui:fieldset>
</aui:row>

<aui:row>
    <aui:fieldset cssClass="col-md-12" helpMessage="languages-help" label="languages" id="<%= namespace + "languages" %>" markupView="<%= markupView %>">

        <liferay-ui:input-move-boxes rightList="<%= availableLanguages %>"
            rightTitle="available" leftBoxName="selectedLanguages"
            leftList="<%= selectedLanguages %>" rightBoxName="availableLanguages"
            leftTitle="current" leftReorder="true" />
            
    </aui:fieldset>
</aui:row>

<aui:row>
    <aui:fieldset cssClass="col-md-6" helpMessage="gender-help" label="gender"  markupView="<%= markupView %>">
        <aui:select name="gender" label=""
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
    </aui:fieldset>

    <aui:fieldset cssClass="col-md-6" helpMessage="time-zone-help" label="time-zone" markupView="<%= markupView %>">
        <aui:select name="timezone" label="" 
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
    </aui:fieldset>
</aui:row>

<aui:row>
    <aui:fieldset cssClass="col-md-12" label="related-assets" helpMessage="related-assets-help" markupView="<%= markupView %>">
    
        <liferay-ui:input-asset-links
            className="<%= Contact.class.getName() %>"
            classPK="<%= contact_.getContactId() %>" />
    
    </aui:fieldset>
</aui:row>

<%-- Configure autofields --%>
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
