package ch.inofix.contact.web.internal.portlet.action;

import java.io.IOException;

import javax.portlet.PortletRequestDispatcher;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;

import ch.inofix.contact.constants.PortletKeys;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactService;

/**
 * 
 * @author Christian Berndt
 * @created 2017-11-14 16:05
 * @modified 2017-11-17 18:38
 * @version 1.0.1
 *
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER,
        "mvc.command.name=exportContacts"
    },
    service = MVCResourceCommand.class
)
public class ExportContactsMVCResourceCommand extends BaseMVCResourceCommand {

    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
            throws Exception {

        String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);

        if (cmd.equals("download")) {

            download(resourceRequest, resourceResponse);

        }

        else {
            
            PortletRequestDispatcher portletRequestDispatcher = getPortletRequestDispatcher(resourceRequest,
                    "/export/processes_list/view.jsp");

            portletRequestDispatcher.include(resourceRequest, resourceResponse);
        }
    }

    protected void download(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
            throws PortalException, IOException {

        long contactId = ParamUtil.getLong(resourceRequest, "contactId");

        Contact contact = _contactService.getContact(contactId);

        String card = contact.getCard();
        String name = contact.getFullName(true);

        PortletResponseUtil.sendFile(resourceRequest, resourceResponse, name + ".vcf", card.getBytes(),
                ContentTypes.TEXT_PLAIN_UTF8);

    }

    @Reference
    protected void setContactService(ContactService contactService) {
        this._contactService = contactService;
    }

    private ContactService _contactService;

}
