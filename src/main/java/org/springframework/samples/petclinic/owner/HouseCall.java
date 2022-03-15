package org.springframework.samples.petclinic.owner;

import javax.persistence.MappedSuperclass;
import java.time.Duration;

@MappedSuperclass
public class HouseCall extends Visit {
	private String zipcode;
	private String street;
	private Integer houseNumber;
	private String houseCallDetails;
	private Duration expectedDuration;
	private Duration endOfVisit;

	public static HouseCall getHouseCall(String zipcode){
		if(zipcode.toCharArray().length == 7){
			HouseCall houseCall = new HouseCall();
			houseCall.setZipcode(zipcode);
			return houseCall;
		} else {
			return null;
		}
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		if(zipcode.toCharArray().length == 7){
			this.zipcode = zipcode;
		} else {
			this.zipcode = "";
		}
	}

	public void setLocation(String street, Number houseNumber){
		this.street = street;
		if(houseNumber instanceof Integer){
			this.houseNumber = houseNumber.intValue();
		} else {
			this.houseNumber = -1;
		}
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(Integer houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getHouseCallDetails() {
		return houseCallDetails;
	}

	public void setHouseCallDetails(String houseCallDetails) {
		this.houseCallDetails = houseCallDetails;
	}

	public Duration getExpectedDuration() {
		return expectedDuration;
	}

	public void setExpectedDuration(Duration expectedDuration) {
		this.expectedDuration = expectedDuration;
	}

	public Duration getEndOfVisit() {
		return endOfVisit;
	}

	public void setEndOfVisit(Duration endOfVisit) {
		this.endOfVisit = endOfVisit;
	}
}
