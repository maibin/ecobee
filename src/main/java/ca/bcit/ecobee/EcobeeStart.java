package ca.bcit.ecobee;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.ecobee.algorithms.IAlgorithm;
import ca.bcit.ecobee.algorithms.impl.EcobeeStandard;
import ca.bcit.ecobee.interfaces.IReadEcobeeCSVFile;
import ca.bcit.ecobee.interfaces.impl.ReadEcobeeCSVFileImpl;
import ca.bcit.ecobee.models.EcobeeClient;

public class EcobeeStart {

	private static IReadEcobeeCSVFile read;
	private static List<EcobeeClient> clients = new ArrayList<>();
	private static IAlgorithm algorithm;
	private static double inefficiency;
	private final static int SIZE_OF_SCENARIO = 5000;

	public static void main(String[] args) {
		read = new ReadEcobeeCSVFileImpl();
		scenarioOneStandardAlgorithm();
	}

	private static void scenarioOneStandardAlgorithm() {
		clients = read.getOnlyEcobeesWithSensors().subList(0, SIZE_OF_SCENARIO);
		algorithm = new EcobeeStandard();
		algorithm.heat(clients).stream().forEach(x -> {
			inefficiency += x.getInefficiency();
		});
		System.out.println("Inefficiency: " + inefficiency / SIZE_OF_SCENARIO + "%");
	}

}
