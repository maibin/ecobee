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
		long start = System.nanoTime();
		clients = read.getDeviceDataFromSourceFile("/Users/Michal/Documents/ecobee/meta_data_v3.csv");
		long elapsedTime = System.nanoTime() - start;
		System.out.println("#: " + clients.size() + ", time: " + elapsedTime);
	}

}
