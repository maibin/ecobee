package ca.bcit.ecobee.interfaces;

import java.util.List;

import ca.bcit.ecobee.models.EcobeeClient;
import ca.bcit.ecobee.models.EcobeeDeviceData;

public interface IReadEcobeeCSVFile {
	
	List<EcobeeClient> getDeviceFileFromMetaFile(String filename);
	
	List<EcobeeDeviceData> getDeviceDataFromFile(String filename);
	
	List<EcobeeClient> getOnlyEcobeesWithSensors();

}
