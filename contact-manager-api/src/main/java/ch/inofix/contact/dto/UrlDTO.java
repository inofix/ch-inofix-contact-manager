package ch.inofix.contact.dto;

/**
 *
 * @author Christian Berndt
 * @created 2015-05-16 16:05
 * @modified 2017-07-01 00:03
 * @version 1.0.1
 */
public class UrlDTO extends BaseDTO {

    public UrlDTO() {
        setType("work");
    }

    private String address = "";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
