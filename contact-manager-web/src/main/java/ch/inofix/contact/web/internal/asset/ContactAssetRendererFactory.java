package ch.inofix.contact.web.internal.asset;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseAssetRendererFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import ch.inofix.contact.constants.ContactActionKeys;
import ch.inofix.contact.constants.PortletKeys;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactLocalService;
import ch.inofix.contact.service.permission.ContactManagerPermission;
import ch.inofix.contact.service.permission.ContactPermission;

/**
 *
 * @author Christian Berndt
 * @created 2015-05-19 17:12
 * @modified 2017-04-14 00:30
 * @version 1.0.3
 *
 */
@Component(immediate = true, property = {
        "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER }, service = AssetRendererFactory.class)
public class ContactAssetRendererFactory extends BaseAssetRendererFactory<Contact> {

    public static final String TYPE = "contact";

    public ContactAssetRendererFactory() {
        setClassName(Contact.class.getName());
        setLinkable(true);
        setPortletId(PortletKeys.CONTACT_MANAGER);
        setSearchable(true);
    }

    @Override
    public AssetRenderer<Contact> getAssetRenderer(long classPK, int type) throws PortalException {

        Contact contact = _contactLocalService.getContact(classPK);

        ContactAssetRenderer contactAssetRenderer = new ContactAssetRenderer(contact);

        contactAssetRenderer.setAssetRendererType(type);
        contactAssetRenderer.setServletContext(_servletContext);

        return contactAssetRenderer;

    }

    @Override
    public String getClassName() {
        return Contact.class.getName();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public PortletURL getURLAdd(LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse) throws PortalException {

        ThemeDisplay themeDisplay = (ThemeDisplay) liferayPortletRequest.getAttribute(WebKeys.THEME_DISPLAY);

        User user = themeDisplay.getUser();

        Group group = user.getGroup();

        if (group != null) {

            long portletPlid = PortalUtil.getPlidFromPortletId(group.getGroupId(), false, PortletKeys.CONTACT_MANAGER);

            PortletURL portletURL = PortletURLFactoryUtil.create(liferayPortletRequest, PortletKeys.CONTACT_MANAGER,
                    portletPlid, PortletRequest.RENDER_PHASE);

            portletURL.setParameter("mvcPath", "/edit_contact.jsp");

            String redirect = (String) liferayPortletRequest.getAttribute("redirect");

            if (Validator.isNotNull(redirect)) {
                portletURL.setParameter("redirect", redirect);
            }

            return portletURL;

        } else {

            return null;

        }
    }

    @Override
    public boolean hasAddPermission(PermissionChecker permissionChecker, long groupId, long classTypeId)
            throws Exception {

        return ContactManagerPermission.contains(permissionChecker, groupId, ContactActionKeys.ADD_CONTACT);
    }

    @Override
    public boolean hasPermission(PermissionChecker permissionChecker, long classPK, String actionId) throws Exception {

        Contact contact = _contactLocalService.getContact(classPK);

        return ContactPermission.contains(permissionChecker, contact.getContactId(), actionId);
    }

    @Reference(target = "(osgi.web.symbolicname=ch.inofix.contact.web)", unbind = "-")
    public void setServletContext(ServletContext servletContext) {
        _servletContext = servletContext;
    }

    @Reference(unbind = "-")
    protected void setContactLocalService(ContactLocalService contactLocalService) {
        _contactLocalService = contactLocalService;
    }

    private static final Log _log = LogFactoryUtil.getLog(ContactAssetRendererFactory.class);

    private ContactLocalService _contactLocalService;
    private ServletContext _servletContext;

}
