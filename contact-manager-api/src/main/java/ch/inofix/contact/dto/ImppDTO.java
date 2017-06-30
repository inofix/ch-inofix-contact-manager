package ch.inofix.contact.dto;

/**
 *
 * @author Christian Berndt
 * @created 2015-05-15 18:42
 * @modified 2017-07-01 00:05
 * @version 1.0.1
 */
public class ImppDTO extends BaseDTO {

    public ImppDTO() {
        setType("work");
    }

    String protocol = "";
    String uri = "";

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
