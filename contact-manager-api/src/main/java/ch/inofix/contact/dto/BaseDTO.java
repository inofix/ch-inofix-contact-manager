package ch.inofix.contact.dto;

/**
 * 
 * @author Christian Berndt
 * @created 2015-05-15 20:11
 * @modified 2015-05-15 20:11
 * @version 1.0.0
 */
public abstract class BaseDTO {

	private String type = "";
	private String value = ""; 

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
