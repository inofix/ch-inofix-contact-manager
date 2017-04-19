package ch.inofix.contact.internal.xstream.configurator;

import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.liferay.exportimport.kernel.xstream.XStreamAlias;
import com.liferay.exportimport.kernel.xstream.XStreamConverter;
import com.liferay.exportimport.kernel.xstream.XStreamType;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.xstream.configurator.XStreamConfigurator;

import ch.inofix.contact.model.Contact;

/**
 *
 * @author Christian Berndt
 * @created 2017-04-18 16:08
 * @modified 2017-04-18 16:08
 * @version 1.0.0
 *
 */
@Component(immediate = true, service = XStreamConfigurator.class)
public class ContactManagerXStreamConfigurator implements XStreamConfigurator {

    @Override
    public List<XStreamType> getAllowedXStreamTypes() {
        return null;
    }

    @Override
    public List<XStreamAlias> getXStreamAliases() {
        return ListUtil.toList(_xStreamAliases);
    }

    @Override
    public List<XStreamConverter> getXStreamConverters() {
        return null;
    }

    @Activate
    protected void activate() {
        _xStreamAliases = new XStreamAlias[] { new XStreamAlias(Contact.class, "Contact") };
    }

    private XStreamAlias[] _xStreamAliases;

}
