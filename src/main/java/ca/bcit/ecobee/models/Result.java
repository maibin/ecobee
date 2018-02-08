package ca.bcit.ecobee.models;

import lombok.Data;

@Data
public class Result {

	private Double totalTime = 0.0;
	private Double overHeatedTime = 0.0;
	private Double inefficiency = 0.0;
	private Double heatingTime = 0.0;

}
