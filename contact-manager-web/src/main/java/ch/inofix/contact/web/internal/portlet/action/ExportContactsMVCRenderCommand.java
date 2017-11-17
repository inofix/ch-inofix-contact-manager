package ch.inofix.contact.web.internal.portlet.action;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.contact.constants.ContactManagerActionKeys;
import ch.inofix.contact.constants.PortletKeys;
import ch.inofix.contact.service.permission.ContactManagerPortletPermission;

/**
 * @author Christian Berndt
 * @created 2017-11-14 18:30
 * @modified 2017-11-14 18:30
 * @version 1.0.0
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER,
        "mvc.command.name=exportContacts"
    },
    service = MVCRenderCommand.class
)
public class ExportContactsMVCRenderCommand implements MVCRenderCommand {

    protected String getPath() {
        return "/export/new_export/export_contacts.jsp";
    }

    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {

        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

        try {

            ContactManagerPortletPermission.check(themeDisplay.getPermissionChecker(), themeDisplay.getScopeGroupId(),
                    ContactManagerActionKeys.EXPORT_CONTACTS);

        } catch (Exception e) {
            if (e instanceof PrincipalException) {

                SessionErrors.add(renderRequest, e.getClass());

                return "/error.jsp";

            } else {
                throw new PortletException(e);
            }
        }

        return getPath();

    }
}
