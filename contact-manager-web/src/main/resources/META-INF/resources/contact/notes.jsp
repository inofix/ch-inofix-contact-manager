<%-- 
    edit_contact/edit_notes.jsp: Edit the vCard's notes of the contact.
    
    Created:    2015-05-11 18:48 by Christian Berndt
    Modified:   2017-07-09 13:22 by Christian Berndt
    Version:    1.1.1
--%>

<%@ include file="/init.jsp"%>

<%@page import="ch.inofix.contact.dto.NoteDTO"%>

<%
    Contact contact_ = (Contact) request.getAttribute(ContactManagerWebKeys.CONTACT);

    if (contact_ == null) {
        contact_ = ContactServiceUtil.createContact();
    }

    boolean hasUpdatePermission = ContactPermission.contains(permissionChecker, contact_, ActionKeys.UPDATE);
    
    String namespace = liferayPortletResponse.getNamespace();
    namespace = ""; // not required in 7.0.2 but in 7.0.3?
%>

    <aui:fieldset cssClass="col-md-6" label="notes" id="<%= namespace + "notes" %>" markupView="<%= markupView %>">

    <%
        List<NoteDTO> notes = contact_.getNotes();
    
            for (NoteDTO note : notes) {
    %>
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
    <%
        }
    %>
</aui:fieldset>

<%-- Configure auto-fields --%>
<aui:script use="liferay-auto-fields">

    var noteAutoFields = new Liferay.AutoFields({
        contentBox : 'fieldset#<portlet:namespace />notes',
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
