package ca.bcit.ecobee.algorithms.impl;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.ecobee.algorithms.IAlgorithm;
import ca.bcit.ecobee.interfaces.IReadEcobeeCSVFile;
import ca.bcit.ecobee.interfaces.impl.ReadEcobeeCSVFileImpl;
import ca.bcit.ecobee.models.EcobeeClient;
import ca.bcit.ecobee.models.EcobeeDeviceData;
import ca.bcit.ecobee.models.Result;

public class WeatherRelatedAlgorithm implements IAlgorithm {

	private List<Result> results = new ArrayList<>();
	private IReadEcobeeCSVFile read = new ReadEcobeeCSVFileImpl();
	private boolean overHeated = false;
	private boolean first = true;

	@Override
	public List<Result> simulate(List<EcobeeClient> clients) {
		List<List<EcobeeDeviceData>> generated = new ArrayList<>();
		List<EcobeeDeviceData> tmp = null;
		for (EcobeeClient client : clients) {
			List<EcobeeDeviceData> scenario = read.getDeviceDataFromFile(client.getFilename());
			int i = -1;
			tmp = new ArrayList<>();
			for (EcobeeDeviceData device : scenario) {
				EcobeeDeviceData newData = new EcobeeDeviceData();
				try {
					if (!device.getSchedule().equals("Sleep") && device.getPreference() > device.getTemperature()) {
						if (device.getPreference()
								- device.getTemperature() > (device.getWeather() - scenario.get(i + 36).getWeather())
										/ 4) {
							tmp.add(fan(device, scenario.get(i + 2), newData,
									(device.getWeather() - scenario.get(i + 2).getWeather()) / 5));
						} else if (device.getPreference()
								- device.getTemperature() > (device.getWeather() - scenario.get(i + 36).getWeather())
										/ 2) {
							tmp.add(heat(device, scenario.get(i + 2), newData,
									(device.getWeather() - scenario.get(i + 2).getWeather()) / 5));
						} else {
							tmp.add(idle(device, scenario.get(i + 2), newData,
									(device.getWeather() - scenario.get(i + 2).getWeather()) / 5));
						}
					}

				} catch (IndexOutOfBoundsException e) {
					tmp.add(idle(device, scenario.get(i + 1), newData,
							(device.getWeather() - scenario.get(i + 1).getWeather()) / 5));
				}
				i++;
			}
			generated.add(tmp);
		}
		for (List<EcobeeDeviceData> temps : generated) {
			try {
				Result result = new Result();
				int i = -1;
				for (EcobeeDeviceData device : temps) {
					result.setHeatingTime(result.getHeatingTime() + device.getHeatingTime());
					if (overHeated) {
						result.setOverHeatedTime(result.getOverHeatedTime() + 5);
					}
					if (!first && !device.getSchedule().equals("Sleep")
							&& device.getTemperature() > temps.get(i).getPreference()) {
						overHeated = true;
					} else {
						first = false;
						overHeated = false;
					}
					result.setTotalTime(result.getTotalTime() + 5);
					result.setInefficiency((result.getOverHeatedTime() / result.getTotalTime()) * 100);
					i++;
				}
				first = true;
				results.add(result);
			} catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}
		return results;

	}

	private EcobeeDeviceData heat(EcobeeDeviceData current, EcobeeDeviceData next, EcobeeDeviceData generated,
			Double weatherImpact) {
		generated.setHeatingTime(300D);
		generated.setTemperature(
				current.getTemperature() + (current.getTemperature() - next.getTemperature()) + 0.1 + weatherImpact);
		generated.setPreference(next.getPreference());
		generated.setSchedule(next.getSchedule());
		return generated;
	}

	private EcobeeDeviceData fan(EcobeeDeviceData current, EcobeeDeviceData next, EcobeeDeviceData generated,
			Double weatherImpact) {
		generated.setHeatingTime(100D);
		generated.setTemperature(
				current.getTemperature() + (current.getTemperature() - next.getTemperature()) + 0.05 + weatherImpact);
		generated.setPreference(next.getPreference());
		generated.setSchedule(next.getSchedule());
		return generated;
	}

	private EcobeeDeviceData idle(EcobeeDeviceData current, EcobeeDeviceData next, EcobeeDeviceData generated,
			Double weatherImpact) {
		generated.setHeatingTime(0D);
		generated.setTemperature(
				current.getTemperature() + (current.getTemperature() - next.getTemperature()) + weatherImpact);
		generated.setPreference(next.getPreference());
		generated.setSchedule(next.getSchedule());
		return generated;
	}

}
