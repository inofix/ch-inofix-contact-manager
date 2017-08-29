package ch.inofix.contact.web.internal.asset;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.BaseJSPAssetRenderer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.trash.TrashRenderer;
import com.liferay.portal.kernel.util.PortalUtil;

import ch.inofix.contact.constants.PortletKeys;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.permission.ContactPermission;
import ch.inofix.contact.web.internal.constants.ContactManagerWebKeys;

/**
 *
 * @author Christian Berndt
 * @created 2015-05-19 17:25
 * @modified 2017-06-22 21:51
 * @version 1.1.1
 *
 */
public class ContactAssetRenderer extends BaseJSPAssetRenderer<Contact> implements TrashRenderer {

    public ContactAssetRenderer(Contact contact) {
        _contact = contact;
    }

    @Override
    public Contact getAssetObject() {
        return _contact;
    }

    @Override
    public String getClassName() {
        return Contact.class.getName();
    }

    @Override
    public long getClassPK() {
        return _contact.getContactId();
    }

    @Override
    public long getGroupId() {
        return _contact.getGroupId();
    }

    @Override
    public String getJspPath(HttpServletRequest request, String template) {
        if (template.equals(TEMPLATE_ABSTRACT) || template.equals(TEMPLATE_FULL_CONTENT)) {
            return "/asset/" + template + ".jsp";
        } else {
            return null;
        }
    }

    @Override
    public String getPortletId() {
        AssetRendererFactory<Contact> assetRendererFactory = getAssetRendererFactory();

        return assetRendererFactory.getPortletId();
    }

    @Override
    public int getStatus() {
        return _contact.getStatus();
    }

    @Override
    public String getSummary(PortletRequest portletRequest, PortletResponse portletResponse) {

        // TODO
        return "TODO: contact summary";
        // return _contact.getDescription();
    }

    @Override
    public String getTitle(Locale locale) {

        return "TODO: _contact.getName()";
        // return _contact.getName();

    }

    @Override
    public String getType() {
        return ContactAssetRendererFactory.TYPE;
    }

    @Override
    public PortletURL getURLEdit(LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse) throws Exception {

        PortletURL portletURL = locateContactManager(liferayPortletRequest);

        return portletURL;

    }

    @Override
    public String getURLView(LiferayPortletResponse liferayPortletResponse, WindowState windowState) {

        try {

            long portletPlid = PortalUtil.getPlidFromPortletId(_contact.getGroupId(), false,
                    PortletKeys.CONTACT_MANAGER);

            PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(portletPlid,
                    PortletKeys.CONTACT_MANAGER, PortletRequest.RENDER_PHASE);

            portletURL.setParameter("mvcPath", "/edit_contact.jsp");

            portletURL.setParameter("contactId", String.valueOf(_contact.getContactId()));

            return portletURL.toString();

        } catch (Exception e) {
            _log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public String getURLViewInContext(LiferayPortletRequest liferayPortletRequest,
            LiferayPortletResponse liferayPortletResponse, String noSuchEntryRedirect) {

        try {

            PortletURL portletURL = locateContactManager(liferayPortletRequest);

            return portletURL.toString();

        } catch (Exception e) {
            _log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public long getUserId() {
        return _contact.getUserId();
    }

    @Override
    public String getUserName() {
        return _contact.getUserName();
    }

    @Override
    public String getUuid() {
        return _contact.getUuid();
    }

    @Override
    public boolean hasViewPermission(PermissionChecker permissionChecker) {

        return ContactPermission.contains(permissionChecker, _contact, ActionKeys.VIEW);
    }

    @Override
    public boolean include(HttpServletRequest request, HttpServletResponse response, String template) throws Exception {

        request.setAttribute(ContactManagerWebKeys.CONTACT, _contact);

        return super.include(request, response, template);
    }

    private PortletURL locateContactManager(LiferayPortletRequest liferayPortletRequest) throws PortalException {

        long portletPlid = PortalUtil.getPlidFromPortletId(_contact.getGroupId(), false, PortletKeys.CONTACT_MANAGER);

        PortletURL portletURL = PortletURLFactoryUtil.create(liferayPortletRequest, PortletKeys.CONTACT_MANAGER,
                portletPlid, PortletRequest.RENDER_PHASE);

        portletURL.setParameter("mvcPath", "/edit_contact.jsp");

        portletURL.setParameter("contactId", String.valueOf(_contact.getContactId()));

        return portletURL;
    }

    private static final Log _log = LogFactoryUtil.getLog(ContactAssetRenderer.class);

    private final Contact _contact;
}
