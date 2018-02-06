package ca.bcit.ecobee.interfaces.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neovisionaries.i18n.CountryCode;

import ca.bcit.ecobee.interfaces.ReadEcobeeCSVFile;
import ca.bcit.ecobee.models.EcobeeClient;
import ca.bcit.ecobee.models.EcobeeDeviceData;

public class ReadEcobeeCSVFileImpl implements ReadEcobeeCSVFile {

	private static final String DIR = "/Users/Michal/Documents/ecobee/january/";
	private static final String META = "/Users/Michal/Documents/ecobee/meta_data_v3.csv";
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public List<EcobeeClient> getDeviceFileFromMetaFile(String filename) {
		List<EcobeeClient> inputList = new ArrayList<EcobeeClient>();
		try {
			File inputF = new File(filename);
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			inputList = br.lines().skip(2).map(mapToClient).collect(Collectors.toList());
			br.close();
		} catch (IOException e) {
			System.out.println("Error");
		}
		return inputList;
	}

	@Override
	public List<EcobeeClient> getOnlyEcobeesWithSensors() {
		return getDeviceFileFromMetaFile(META).stream()
				.filter((x) -> x.getId() != null && !x.getHeatPump()
						&& (x.getModel().equals("ecobee4") || x.getModel().equals("ecobee3")
								|| x.getModel().equals("ecobee3Lite"))
						&& x.getSensors() > 1 && new File(DIR, x.getFilename()).exists())
				.collect(Collectors.toList());
	}

	@Override
	public List<EcobeeDeviceData> getDeviceDataFromFile(String filename) {
		List<EcobeeDeviceData> inputList = new ArrayList<>();
		try {
			File inputF = new File(DIR + filename);
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			inputList = br.lines().skip(1).map(mapToDevice).collect(Collectors.toList());
			br.close();
		} catch (IOException e) {
			System.out.println("Error");
		}
		return inputList;
	}

	private Function<String, EcobeeClient> mapToClient = (line) -> {
		String[] p = line.split(",");// a CSV has comma separated lines
		EcobeeClient client = new EcobeeClient();
		try {
			client.setId(p[1].trim());
			client.setModel(p[2].trim());
			client.setCountryCode(CountryCode.getByCode(p[4].trim()));
			client.setFloorArea(NumberFormat.getNumberInstance(Locale.CANADA).parse(p[7].trim()).intValue());
			client.setAgeOfHome(NumberFormat.getNumberInstance(Locale.CANADA).parse(p[10].trim()).intValue());
			client.setHeatPump(Boolean.parseBoolean(p[12].trim()));
			client.setSensors(NumberFormat.getNumberInstance(Locale.CANADA).parse(p[14].trim()).intValue());
			client.setFilename(p[15].trim().substring(0, p[15].length() - 3));
		} catch (NullPointerException | ParseException e) {
			client = new EcobeeClient();
		}
		return client;
	};

	private Function<String, EcobeeDeviceData> mapToDevice = (line) -> {
		String[] p = line.split(",");// a CSV has comma separated lines
		EcobeeDeviceData device = new EcobeeDeviceData();
		try {
			device.setTime(LocalDateTime.parse(p[0].trim(), DTF));
			device.setSchedule(p[1].trim());
			device.setTemperature(NumberFormat.getNumberInstance(Locale.CANADA).parse(p[3].trim()).doubleValue());
			device.setHumidity(NumberFormat.getNumberInstance(Locale.CANADA).parse(p[6].trim()).intValue());
			device.setWeather(NumberFormat.getNumberInstance(Locale.CANADA).parse(p[11].trim()).doubleValue());
			device.setHeatingTime(NumberFormat.getNumberInstance(Locale.CANADA).parse(p[13].trim()).intValue() * 0.75
					+ NumberFormat.getNumberInstance(Locale.CANADA).parse(p[14].trim()).intValue() * 0.25);
		} catch (NullPointerException | ParseException e1) {
			device = new EcobeeDeviceData();
		}
		return device;
	};

}
