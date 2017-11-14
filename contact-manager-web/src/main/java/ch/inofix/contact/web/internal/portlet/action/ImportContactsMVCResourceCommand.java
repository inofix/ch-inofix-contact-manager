package ch.inofix.contact.web.internal.portlet.action;

import javax.portlet.PortletRequestDispatcher;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;

import ch.inofix.contact.constants.PortletKeys;

/**
 * 
 * @author Christian Berndt
 * @created 2017-11-14 17:37
 * @modified 2017-11-14 17:37
 * @version 1.0.0
 *
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER,
        "mvc.command.name=importContacts"
    },
    service = MVCResourceCommand.class
)
public class ImportContactsMVCResourceCommand extends BaseMVCResourceCommand {

    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
            throws Exception {
        
        _log.info("doServeResource()");

        String cmd = ParamUtil.getString(resourceRequest, Constants.CMD);

        _log.info("cmd = " + cmd);

        PortletRequestDispatcher portletRequestDispatcher = null;

        if (cmd.equals(Constants.IMPORT)) {
            portletRequestDispatcher = getPortletRequestDispatcher(resourceRequest, "/import/processes_list/view.jsp");
        } else {
            portletRequestDispatcher = getPortletRequestDispatcher(resourceRequest,
                    "/import/new_import/import_contacts_resources.jsp");
        }

        portletRequestDispatcher.include(resourceRequest, resourceResponse);
    }
    
    private static Log _log = LogFactoryUtil.getLog(ImportContactsMVCResourceCommand.class.getName());

}
