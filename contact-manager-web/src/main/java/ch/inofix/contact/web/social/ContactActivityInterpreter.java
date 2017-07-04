package ch.inofix.contact.web.social;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRenderer;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.social.kernel.model.BaseSocialActivityInterpreter;
import com.liferay.social.kernel.model.SocialActivity;
import com.liferay.social.kernel.model.SocialActivityConstants;
import com.liferay.social.kernel.model.SocialActivityInterpreter;

import ch.inofix.contact.constants.PortletKeys;
import ch.inofix.contact.model.Contact;
import ch.inofix.contact.service.ContactLocalService;
import ch.inofix.contact.service.permission.ContactPermission;
import ch.inofix.contact.social.ContactActivityKeys;

/**
 * @author Christian Berndt
 * @created 2017-07-04 16:20
 * @modified 2017-07-04 16:20
 * @version 1.0.0
 */
@Component(property = {
        "javax.portlet.name=" + PortletKeys.CONTACT_MANAGER }, service = SocialActivityInterpreter.class)
public class ContactActivityInterpreter extends BaseSocialActivityInterpreter {

    @Override
    public String[] getClassNames() {
        return _CLASS_NAMES;
    }

    @Override
    protected String getPath(SocialActivity activity, ServiceContext serviceContext) throws Exception {

        AssetRendererFactory<?> assetRendererFactory = AssetRendererFactoryRegistryUtil
                .getAssetRendererFactoryByClassName(Contact.class.getName());

        AssetRenderer<?> assetRenderer = assetRendererFactory.getAssetRenderer(activity.getClassPK());

        String path = assetRenderer.getURLViewInContext(serviceContext.getLiferayPortletRequest(),
                serviceContext.getLiferayPortletResponse(), null);

        path = HttpUtil.addParameter(path, "redirect", serviceContext.getCurrentURL());

        return path;
    }

    @Override
    protected ResourceBundleLoader getResourceBundleLoader() {
        return _resourceBundleLoader;
    }

    @Override
    protected String getTitlePattern(String groupName, SocialActivity activity) {

        int activityType = activity.getType();

        if (activityType == ContactActivityKeys.ADD_CONTACT) {
            if (Validator.isNull(groupName)) {
                return "activity-contact-add-contact";
            } else {
                return "activity-contact-add-contact-in";
            }
        } else if (activityType == SocialActivityConstants.TYPE_MOVE_TO_TRASH) {
            if (Validator.isNull(groupName)) {
                return "activity-contact-move-to-trash";
            } else {
                return "activity-contact-move-to-trash-in";
            }
        } else if (activityType == SocialActivityConstants.TYPE_RESTORE_FROM_TRASH) {

            if (Validator.isNull(groupName)) {
                return "activity-contact-restore-from-trash";
            } else {
                return "activity-contact-restore-from-trash-in";
            }
        } else if (activityType == ContactActivityKeys.UPDATE_CONTACT) {
            if (Validator.isNull(groupName)) {
                return "activity-contact-update-contact";
            } else {
                return "activity-contact-update-contact-in";
            }
        }

        return StringPool.BLANK;
    }

    @Override
    protected boolean hasPermissions(PermissionChecker permissionChecker, SocialActivity activity, String actionId,
            ServiceContext serviceContext) throws Exception {

        Contact contact = _contactLocalService.getContact(activity.getClassPK());

        return ContactPermission.contains(permissionChecker, contact.getContactId(), actionId);
    }

    @Reference(unbind = "-")
    protected void setContactLocalService(ContactLocalService contactLocalService) {
        _contactLocalService = contactLocalService;
    }

    private static final String[] _CLASS_NAMES = { Contact.class.getName() };

    private ContactLocalService _contactLocalService;
    private ResourceBundleLoader _resourceBundleLoader;

}
