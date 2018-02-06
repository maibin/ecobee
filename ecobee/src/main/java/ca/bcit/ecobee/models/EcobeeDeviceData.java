package ca.bcit.ecobee.models;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EcobeeDeviceData {

	private LocalDateTime time;
	private String schedule;
	private Double temperature;
	private Integer humidity;
	private Double weather;
	private Double heatingTime;
}
