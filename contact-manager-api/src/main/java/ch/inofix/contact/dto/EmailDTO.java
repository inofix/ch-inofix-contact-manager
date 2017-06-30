package ch.inofix.contact.dto;

/**
 *
 * @author Christian Berndt
 * @created 2015-05-15 13:58
 * @modified 2017-06-30 23:56
 * @version 1.0.1
 */
public class EmailDTO extends BaseDTO {

    public EmailDTO() {
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
