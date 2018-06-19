
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;

@Embeddable
@Access(AccessType.PROPERTY)
public class Coordinate {

	private String	longitude;
	private String	latitude;


	public Coordinate() {
		super();
	}

	@Pattern(regexp = "^((\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?)))|()$")
	public String getLongitude() {
		return this.longitude;
	}
	public void setLongitude(final String longitude) {

		this.longitude = longitude;
	}

	@Pattern(regexp = "^((\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?)))|()$")
	public String getLatitude() {
		return this.latitude;
	}
	public void setLatitude(final String latitude) {
		this.latitude = latitude;
	}

	//	@Pattern(regexp = "^((\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?)))|()$")
	//	public String getLongitude() {
	//		return (this.longitude!=null)?this.longitude.toString():"";
	//	}
	//	public void setLongitude(final String longitude) {
	//		
	//		String fpRegex = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$";
	//		if (java.util.regex.Pattern.matches(fpRegex, longitude)){
	//			this.longitude = Double.valueOf(longitude); // Will not throw NumberFormatException
	//		} else {
	//		   this.longitude = null;
	//		}
	//	}
	//
	//	@Pattern(regexp = "^((\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?)))|()$")
	//	public String getLatitude() {
	//		return (this.latitude!=null)?this.latitude.toString():"";
	//	}
	//	public void setLatitude(final String latitude) {
	//		String fpRegex = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$";
	//		if (java.util.regex.Pattern.matches(fpRegex, latitude)){
	//			this.latitude = Double.valueOf(latitude); // Will not throw NumberFormatException
	//		} else {
	//		   this.latitude = null;
	//		}
	//		
	//	}

}
