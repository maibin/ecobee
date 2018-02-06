package ca.bcit.ecobee.interfaces.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neovisionaries.i18n.CountryCode;

import ca.bcit.ecobee.interfaces.ReadEcobeeCSVFile;
import ca.bcit.ecobee.models.EcobeeClient;

public class ReadEcobeeCSVFileImpl implements ReadEcobeeCSVFile {

	@Override
	public List<EcobeeClient> getDeviceDataFromSourceFile(String filename) {
		List<EcobeeClient> inputList = new ArrayList<EcobeeClient>();
		try {
			File inputF = new File(filename);
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			inputList = br.lines().skip(2).map(mapToItem).collect(Collectors.toList());
			br.close();
		} catch (IOException e) {
			System.out.println("Error");
		}
		return inputList;
	}

	private Function<String, EcobeeClient> mapToItem = (line) -> {
		String[] p = line.split(",");// a CSV has comma separated lines
		EcobeeClient item = new EcobeeClient();
		item.setId(p[1].trim());
		item.setModel(p[2].trim());
		item.setCountryCode(CountryCode.getByCode(p[4].trim()));
		try {
			item.setFloorArea(NumberFormat.getNumberInstance(Locale.CANADA).parse(p[7].trim()).intValue());
			item.setAgeOfHome(NumberFormat.getNumberInstance(Locale.CANADA).parse(p[10].trim()).intValue());
			item.setSensors(NumberFormat.getNumberInstance(Locale.CANADA).parse(p[14].trim()).intValue());
			item.setFilename(p[15].trim().substring(0, p[15].length()-3));
		} catch (Exception e) {
			System.out.println("Error");
		}
		return item;
	};

}
