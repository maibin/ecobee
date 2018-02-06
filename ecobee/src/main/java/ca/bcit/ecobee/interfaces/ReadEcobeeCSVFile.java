package ca.bcit.ecobee.interfaces;

import java.util.List;

import ca.bcit.ecobee.models.EcobeeClient;

public interface ReadEcobeeCSVFile {
	
	List<EcobeeClient> getDeviceDataFromSourceFile(String filename);
	
	List<EcobeeClient> getOnlyEcobeesWithSensors();

}
