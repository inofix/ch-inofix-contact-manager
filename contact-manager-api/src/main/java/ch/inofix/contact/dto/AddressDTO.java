package ch.inofix.contact.dto;

/**
 *
 * @author Christian Berndt
 * @created 2015-05-15 20:11
 * @modified 2017-07-01 00:08
 * @version 1.0.1
 */
public class AddressDTO extends BaseDTO {

    public AddressDTO() {
        setType("work");
    }

	private String country = "";
	private String label = "";
	private String language = "";
	private String locality = "";
	private String poBox = "";
	private String postalCode = "";
	private String region = "";
	private String streetAddress = "";
	private String timezone = "";

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getPoBox() {
		return poBox;
	}

	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

}
