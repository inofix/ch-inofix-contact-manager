package ch.inofix.contact.web.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 *
 * @author Christian Berndt
 * @author Stefan Luebbers
 * @created 2017-03-30 19:56
 * @modified 2017-10-28 18:13
 * @version 1.0.2
 *
 */
@Meta.OCD(
    id = "ch.inofix.contact.web.configuration.ContactManagerConfiguration", 
    localization = "content/Language", name = "contact.configuration.name"
)
public interface ContactManagerConfiguration {

    @Meta.AD(deflt = "full-name|email|phone|modified-date", required = false)
    public String[] columns();

    @Meta.AD(deflt = "70", required = false)
    public String maxHeight();

    @Meta.AD(deflt = "lexicon", required = false)
    public String markupView();

    @Meta.AD(deflt = "circle", optionValues = { "circle", "rounded-edges", "polaroid" }, required = false)
    public String portraitDisplay();

    @Meta.AD(deflt = "false", required = false)
    public String viewByDefault();

}
