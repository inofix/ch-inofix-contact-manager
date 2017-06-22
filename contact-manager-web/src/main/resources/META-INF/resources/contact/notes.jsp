<%-- 
    edit_contact/edit_notes.jsp: Edit the vCard's notes of the contact.
    
    Created:    2015-05-11 18:48 by Christian Berndt
    Modified:   2017-04-25 14:09 by Stefan Luebbers
    Version:    1.0.8
--%>

<%@ include file="/init.jsp"%>

<%@page import="ch.inofix.contact.dto.NoteDTO"%>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    if (contact_ == null) {
        contact_ = ContactServiceUtil.createContact();
    }

    // TODO: check permissions
    boolean hasUpdatePermission = true; 
%>

<aui:fieldset label="notes" id="note">

    <%
    List<NoteDTO> notes = contact_.getNotes();

        for (NoteDTO note : notes) {
    %>
    <aui:row>
        <aui:col span="12">
            <div class="lfr-form-row">
                <div class="row-fields">
                    <div class="sort-handle"></div>

                    <aui:input name="note" 
                        autoSize="true"
                        type="textarea" 
                        value="<%=note.getValue()%>" 
                        inlineField="false" 
                        label=""
                        disabled="<%=!hasUpdatePermission%>" 
                        helpMessage="note-help"/>
                </div>
            </div>
        </aui:col>
    </aui:row>
    <%
        }
    %>
</aui:fieldset>

<%-- Configure auto-fields 
<aui:script use="liferay-auto-fields">

    var noteAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />note',
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

