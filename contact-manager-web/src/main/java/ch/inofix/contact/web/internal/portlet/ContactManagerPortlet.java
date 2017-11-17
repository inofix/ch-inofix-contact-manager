package ch.inofix.contact.web.internal.portlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import ch.inofix.contact.constants.PortletKeys;

/**
 * View Controller of Inofix' contact-manager.
 *
 * @author Stefan Luebbers
 * @author Christian Berndt
 * @created 2017-03-30 19:52
 * @modified 2017-11-14 23:34
 * @version 1.1.5
 */
@Component(
    configurationPid = "ch.inofix.contact.web.configuration.ContactManagerConfiguration", 
    immediate = true, 
    property = { 
        "com.liferay.portlet.css-class-wrapper=ifx-portlet portlet-contact-manager",
        "com.liferay.portlet.display-category=category.inofix",
        "com.liferay.portlet.footer-portlet-javascript=/js/main.js",
        "com.liferay.portlet.header-portlet-css=/css/main.css", 
        "com.liferay.portlet.instanceable=false",
        "javax.portlet.display-name=Contact Manager", 
        "javax.portlet.init-param.template-path=/",
        "javax.portlet.init-param.view-template=/view.jsp",
        "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER,        
        "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.security-role-ref=power-user,user" 
    }, 
    service = Portlet.class
)
public class ContactManagerPortlet extends MVCPortlet {
}