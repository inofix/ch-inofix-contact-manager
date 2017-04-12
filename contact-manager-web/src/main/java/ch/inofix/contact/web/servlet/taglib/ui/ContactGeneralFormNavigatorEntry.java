package ch.inofix.contact.web.servlet.taglib.ui;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;

/**
 *
 * @author Christian Berndt
 * @created 2017-04-12 16:49
 * @modified 2017-04-12 16:49
 * @version 1.0.0
 *
 */
@Component(property = { "form.navigator.entry.order:Integer=100" }, service = FormNavigatorEntry.class)
public class ContactGeneralFormNavigatorEntry extends BaseContactFormNavigatorEntry {

    @Override
    protected String getJspPath() {
        return "/contact/general.jsp";
    }

    @Override
    public String getKey() {
        return FormNavigatorConstants.CATEGORY_KEY_GENERAL;

    }

    @Override
    public String getLabel(Locale locale) {
        return LanguageUtil.get(locale, "general");
    }

}
