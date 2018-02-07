package ca.bcit.ecobee.algorithms.impl;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.ecobee.algorithms.IAlgorithm;
import ca.bcit.ecobee.interfaces.IReadEcobeeCSVFile;
import ca.bcit.ecobee.interfaces.impl.ReadEcobeeCSVFileImpl;
import ca.bcit.ecobee.models.EcobeeClient;
import ca.bcit.ecobee.models.EcobeeDeviceData;
import ca.bcit.ecobee.models.Result;

public class EcobeeStandard implements IAlgorithm {

	private List<Result> results = new ArrayList<>();
	private IReadEcobeeCSVFile read = new ReadEcobeeCSVFileImpl();
	private boolean overHeated = false;
	private boolean first = true;

	@Override
	public List<Result> heat(List<EcobeeClient> clients) {
		for (EcobeeClient client : clients) {
			List<EcobeeDeviceData> scenario = read.getDeviceDataFromFile(client.getFilename());
			try {
				int i = -1;
				Result result = new Result();
				for (EcobeeDeviceData device : scenario) {
					if (overHeated) {
						result.setOverHeatedTime(result.getOverHeatedTime() + 5);
					}
					if (!first && !device.getSchedule().equals("Sleep")
							&& device.getTemperature() > scenario.get(i).getPreference()) {
						overHeated = true;
					} else {
						first = false;
						overHeated = false;
					}
					result.setTotalTime(result.getTotalTime() + 5);
					i++;
				}
				first = true;
				result.setInefficiency((result.getOverHeatedTime() / result.getTotalTime()) * 100);
				results.add(result);
			} catch (NullPointerException e) {
				continue;
			}
		}
		return results;
	}

}
