package ch.inofix.contact.web.servlet.taglib.ui;

import com.liferay.portal.kernel.servlet.taglib.ui.BaseJSPFormNavigatorEntry;

import ch.inofix.contact.model.Contact;

/**
 *
 * @author Christian Berndt
 * @created 2017-04-12 16:24
 * @modified 2017-04-12 16:24
 * @version 1.0.0
 *
 */
public abstract class BaseContactFormNavigatorEntry extends BaseJSPFormNavigatorEntry<Contact> {

    @Override
    public String getCategoryKey() {
        return "default";
    }

    @Override
    public String getFormNavigatorId() {

        return "contact.form";
    }

}
