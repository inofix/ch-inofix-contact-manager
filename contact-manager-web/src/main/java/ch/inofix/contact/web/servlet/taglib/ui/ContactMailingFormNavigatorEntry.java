package ch.inofix.contact.web.servlet.taglib.ui;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;

/**
 *
 * @author Stefan Luebbers
 * @created 2017-04-24 17:16
 * @modified 2017-04-24 17:16
 * @version 1.0.0
 *
 */
@Component(property = { "form.navigator.entry.order:Integer=80" }, service = FormNavigatorEntry.class)
public class ContactMailingFormNavigatorEntry extends BaseContactFormNavigatorEntry {

    @Override
    protected String getJspPath() {
        return "/contact/mailing.jsp";
    }

    @Override
    public String getKey() {
        return FormNavigatorConstants.CATEGORY_KEY_MAILING;

    }

    @Override
    public String getLabel(Locale locale) {
        return LanguageUtil.get(locale, "mailing-address");
    }

}
