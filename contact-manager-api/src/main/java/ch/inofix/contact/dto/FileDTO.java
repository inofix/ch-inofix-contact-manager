package ch.inofix.contact.dto;

/**
 * 
 * @author Christian Berndt
 * @created 2015-06-25 22:47
 * @modified 2015-06-25 22:47
 * @version 1.0.0
 */
public class FileDTO extends BaseDTO {

	private String data = "";
	private String url = "";

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
