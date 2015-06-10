package edu.harvard.i2b2.rxnorm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Code;
import org.hl7.fhir.Coding;
import org.hl7.fhir.Medication;
import org.hl7.fhir.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.query.QueryBuilder;

public class RxNormAdapter {
	static Logger logger = LoggerFactory.getLogger(RxNormAdapter.class);

	HashMap<String, Integer> Ndc2CuiMap;

	public RxNormAdapter() throws IOException {
		init();
	}

	public void init() throws IOException {
		Ndc2CuiMap = new HashMap<String, Integer>();
		String filePath = Utils.getFilePath("rxnorm/RXNSAT_NDC.RRF");
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		// logger.trace("read line:"+reader.readLine());
		String line = reader.readLine();
		while (line != null) {

			String[] arr = line.split("\\|");
			Ndc2CuiMap.put(arr[1], Integer.parseInt(arr[0]));
			// System.out.println(Ndc2CuiMap.get(arr[1]));
			// logger.trace("read line:"+arr[0]);
			line = reader.readLine();

		}
		;
	}

	public String getRxCui(String ndcString) {
		Integer intCode = Ndc2CuiMap.get(ndcString);
		if (intCode != null) {
			return Ndc2CuiMap.get(ndcString).toString();
		} else {
			return null;
		}
	}

	public void addRxCui(Medication m) throws JAXBException {
		String ndcString = "123";

		Coding c = new Coding();
		Uri uri = new Uri();
		uri.setValue("rxnorm");

		c.setSystem(uri);

		Code cd = new Code();

		cd.setValue(getRxCui(ndcString));
		c.setCode(cd);
		m.getCode().getCoding().add(c);
		logger.trace(JAXBUtil.toXml(m));
	}

}