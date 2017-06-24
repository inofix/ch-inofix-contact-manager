package ch.inofix.contact.dto;

/**
 *
 * @author Christian Berndt
 * @created 2017-06-24 12:02
 * @modified 2017-06-24 12:02
 * @version 1.0.0
 *
 */
public class StructuredNameDTO extends BaseDTO {

    private String additional = "";
    private String family = "";
    private String given = "";
    private String prefix = "";
    private String suffix = "";

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGiven() {
        return given;
    }

    public void setGiven(String given) {
        this.given = given;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
