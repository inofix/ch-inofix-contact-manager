package ch.inofix.contact.web.servlet.taglib.ui;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorCategory;

/**
 *
 * @author Christian Berndt
 * @created 2017-04-12 16:20
 * @modified 2017-04-12 16:20
 * @version 1.0.0
 *
 */
@Component(property = { "form.navigator.category.order:Integer=20" }, service = FormNavigatorCategory.class)
public class ContactFormNavigatorCategory implements FormNavigatorCategory {

    @Override
    public String getFormNavigatorId() {
        return FormNavigatorConstants.FORM_NAVIGATOR_ID_CONTACT;
    }

    @Override
    public String getKey() {
        return "default";
    }

    @Override
    public String getLabel(Locale locale) {
        return LanguageUtil.get(locale, "default");
    }

}
