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
	private static boolean first = true;
	private static boolean overHeated = false;
	private static long totalTime, overHeatedTime;

	public static void main(String[] args) {
		read = new ReadEcobeeCSVFileImpl();
		clients = read.getOnlyEcobeesWithSensors().subList(0, 5000);
		for (EcobeeClient client : clients) {
			List<EcobeeDeviceData> list = read.getDeviceDataFromFile(client.getFilename());
			try {
				int i = -1;
				for (EcobeeDeviceData device : list) {
					if (overHeated) {
						overHeatedTime += 5;
					}
					if (!first && !device.getSchedule().equals("Sleep")
							&& device.getTemperature() > list.get(i).getPreference()) {
						overHeated = true;
					} else {
						first = false;
						overHeated = false;
					}
					totalTime += 5;
					i++;
				}
				first = true;
			} catch (NullPointerException e) {
				continue;
			}
		}
		System.out.println("Total time: " + totalTime);
		System.out.println("Overheated time: " + overHeatedTime);
		System.out.println("Inefficiency: " + overHeatedTime * 100 / totalTime + "%");
	}

}
