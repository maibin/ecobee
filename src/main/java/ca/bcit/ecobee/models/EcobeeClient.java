package ca.bcit.ecobee.models;

import com.neovisionaries.i18n.CountryCode;

import lombok.Data;

@Data
public class EcobeeClient {
	
	private String id;
	private String model;
	private CountryCode countryCode;
	private Integer floorArea;
	private Integer ageOfHome;
	private Boolean heatPump;
	private Integer sensors;
	private String filename;
}