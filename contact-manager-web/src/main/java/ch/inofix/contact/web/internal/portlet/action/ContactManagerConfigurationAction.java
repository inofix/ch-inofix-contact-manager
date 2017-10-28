package ch.inofix.contact.web.internal.portlet.action;

import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;

import ch.inofix.contact.constants.PortletKeys;
import ch.inofix.contact.web.configuration.ContactManagerConfiguration;

/**
 * Configuration of Inofix' contact manager.
 *
 * @author Stefan Luebbers
 * @author Christian Berndt
 * @created 2017-04-12 17:35
 * @modified 2017-10-28 18:21
 * @version 1.0.2
 */

@Component(
    configurationPid = "ch.inofix.contact.web.configuration.ContactManagerConfiguration",
    configurationPolicy = ConfigurationPolicy.OPTIONAL, 
    immediate = true,
    property = { "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER }, 
    service = ConfigurationAction.class
)
public class ContactManagerConfigurationAction extends DefaultConfigurationAction {

    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse)
            throws Exception {

        String columns = ParamUtil.getString(actionRequest, "columns");

        setPreference(actionRequest, "columns", columns.split(","));

        super.processAction(portletConfig, actionRequest, actionResponse);
    }

    @Override
    public void include(PortletConfig portletConfig, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws Exception {

        httpServletRequest.setAttribute(ContactManagerConfiguration.class.getName(), _contactManagerConfiguration);

        super.include(portletConfig, httpServletRequest, httpServletResponse);
    }

    @Activate
    @Modified
    protected void activate(Map<Object, Object> properties) {

        _contactManagerConfiguration = ConfigurableUtil.createConfigurable(
                ContactManagerConfiguration.class, properties);
        
    }

    private volatile ContactManagerConfiguration _contactManagerConfiguration;

}
