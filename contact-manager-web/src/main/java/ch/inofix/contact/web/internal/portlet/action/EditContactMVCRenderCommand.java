package ch.inofix.contact.web.internal.portlet.action;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;

import ch.inofix.contact.constants.PortletKeys;
import ch.inofix.contact.web.internal.portlet.action.GetContactMVCRenderCommand;

/**
 * 
 * @author Christian Berndt
 * @created 2017-11-14 15:42
 * @modified 2017-11-14 15:42
 * @version 1.0.0
 *
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER,
        "mvc.command.name=editContact"
    },
    service = MVCRenderCommand.class
)
public class EditContactMVCRenderCommand extends GetContactMVCRenderCommand {

    @Override
    protected String getPath() {

        return "/edit_contact.jsp";
    }
}
