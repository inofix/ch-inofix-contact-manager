package ch.inofix.contact.dto;

/**
 *
 * @author Christian Berndt
 * @created 2015-05-15 13:58
 * @modified 2017-06-30 23:59
 * @version 1.0.1
 */
public class PhoneDTO extends BaseDTO {

    public PhoneDTO() {
        setType("work");
    }

	private String number = "";

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
