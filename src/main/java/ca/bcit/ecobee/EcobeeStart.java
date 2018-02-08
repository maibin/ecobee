package ca.bcit.ecobee;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.ecobee.algorithms.IAlgorithm;
import ca.bcit.ecobee.algorithms.impl.EcobeeAlgorithm;
import ca.bcit.ecobee.algorithms.impl.WeatherRelatedAlgorithm;
import ca.bcit.ecobee.interfaces.IReadEcobeeCSVFile;
import ca.bcit.ecobee.interfaces.impl.ReadEcobeeCSVFileImpl;
import ca.bcit.ecobee.models.EcobeeClient;

public class EcobeeStart {

	private static IReadEcobeeCSVFile read;
	private static List<EcobeeClient> clients = new ArrayList<>();
	private static IAlgorithm algorithm;
	private static double inefficiency, heatingTime;
	private final static int SIZE_OF_SCENARIO = 1000;

	public static void main(String[] args) {
		read = new ReadEcobeeCSVFileImpl();
		scenarioOneStandardAlgorithm();
		scenarioTwoWeatherRelatedAlgorithm();
	}

	private static void scenarioOneStandardAlgorithm() {
		inefficiency = 0.0;
		heatingTime = 0.0;
		clients = read.getOnlyEcobeesWithSensors().subList(0, SIZE_OF_SCENARIO);
		algorithm = new EcobeeAlgorithm();
		algorithm.simulate(clients).stream().forEach(x -> {
			if (!Double.isNaN(x.getInefficiency())) {
				inefficiency += x.getInefficiency();
				heatingTime += x.getHeatingTime();
			}
		});
		System.out.println("Inefficiency: " + inefficiency / SIZE_OF_SCENARIO + "%");
		System.out.println("Heating Time: " + heatingTime / SIZE_OF_SCENARIO / 60 + "m");
	}

	private static void scenarioTwoWeatherRelatedAlgorithm() {
		inefficiency = 0.0;
		heatingTime = 0.0;
		clients = read.getOnlyEcobeesWithSensors().subList(0, SIZE_OF_SCENARIO);
		algorithm = new WeatherRelatedAlgorithm();
		algorithm.simulate(clients).stream().forEach(x -> {
			if (!Double.isNaN(x.getInefficiency())) {
				inefficiency += x.getInefficiency();
				heatingTime += x.getHeatingTime();
			}
		});
		System.out.println("Inefficiency: " + inefficiency / SIZE_OF_SCENARIO + "%");
		System.out.println("Heating Time: " + heatingTime / SIZE_OF_SCENARIO / 60 + "m");
	}

}
