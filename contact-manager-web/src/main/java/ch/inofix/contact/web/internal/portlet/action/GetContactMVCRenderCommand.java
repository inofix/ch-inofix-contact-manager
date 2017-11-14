package ch.inofix.contact.web.internal.portlet.action;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;

import ch.inofix.contact.exception.NoSuchContactException;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.web.internal.constants.ContactManagerWebKeys;
import ch.inofix.contact.web.internal.portlet.action.ActionUtil;
import ch.inofix.contact.web.internal.portlet.action.GetContactMVCRenderCommand;

/**
 * 
 * @author Christian Berndt
 * @created 2017-11-14 15:41
 * @modified 2017-11-14 15:41
 * @version 1.0.0
 *
 */
public abstract class GetContactMVCRenderCommand implements MVCRenderCommand {

    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {

        _log.info("render()");

        try {
            Contact contact = ActionUtil.getContact(renderRequest);

            renderRequest.setAttribute(ContactManagerWebKeys.CONTACT, contact);
        } catch (Exception e) {
            if (e instanceof NoSuchContactException || e instanceof PrincipalException) {

                SessionErrors.add(renderRequest, e.getClass());

                return "/error.jsp";

            } else {
                throw new PortletException(e);
            }
        }

        return getPath();
    }

    protected abstract String getPath();

    private static Log _log = LogFactoryUtil.getLog(GetContactMVCRenderCommand.class.getName());

}
