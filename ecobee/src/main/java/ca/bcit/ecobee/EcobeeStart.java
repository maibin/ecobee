package ca.bcit.ecobee;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.ecobee.interfaces.ReadEcobeeCSVFile;
import ca.bcit.ecobee.interfaces.impl.ReadEcobeeCSVFileImpl;
import ca.bcit.ecobee.models.EcobeeClient;
import ca.bcit.ecobee.models.EcobeeDeviceData;

public class EcobeeStart {

	private static ReadEcobeeCSVFile read;
	private static List<EcobeeClient> clients = new ArrayList<>();

	public static void main(String[] args) {
		read = new ReadEcobeeCSVFileImpl();
		clients = read.getOnlyEcobeesWithSensors();
		List<EcobeeDeviceData> list = read.getDeviceDataFromFile(clients.get(0).getFilename());
		System.out.println(clients.size());
		System.out.println(list.size());
	}

}
