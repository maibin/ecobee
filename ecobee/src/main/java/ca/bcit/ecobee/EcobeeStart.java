package ca.bcit.ecobee;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.ecobee.interfaces.ReadEcobeeCSVFile;
import ca.bcit.ecobee.interfaces.impl.ReadEcobeeCSVFileImpl;
import ca.bcit.ecobee.models.EcobeeClient;

public class EcobeeStart {

	private static ReadEcobeeCSVFile read;
	private static List<EcobeeClient> clients = new ArrayList<>();

	public static void main(String[] args) {
		read = new ReadEcobeeCSVFileImpl();
		clients = read.getOnlyEcobeesWithSensors();
		System.out.println(clients.size());
	}

}
