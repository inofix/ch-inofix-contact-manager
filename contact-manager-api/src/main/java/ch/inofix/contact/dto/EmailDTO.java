package ch.inofix.contact.dto;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-15 13:58
 * @modified 2015-05-15 13:58
 * @version 1.0.0
 */
public class EmailDTO extends BaseDTO {

	private String address = "";
//	private String type = "";

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

}
