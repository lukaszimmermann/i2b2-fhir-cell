/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package edu.harvard.i2b2.rxnorm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
	HashMap<Integer, String> rxCuiMap;

	public RxNormAdapter() throws IOException {
		init();
	}

	public void init() throws IOException {
		initNdc2CuiMap();
		initRxCuiMap();
	}

	public void initRxCuiMap() throws IOException {
		rxCuiMap = BinResourceFromRXNormData.deSerializeRxCuiMap();

		/*
		 * InputStream fileIS = null; try { rxCuiMap = new HashMap<Integer,
		 * String>(); fileIS = Utils.getInputStream("rxnorm/RXNCONSO_NORM.RRF");
		 * BufferedReader reader = new BufferedReader(new InputStreamReader(
		 * fileIS)); // logger.trace("read line:"+reader.readLine());
		 * 
		 * String line = reader.readLine(); while (line != null) {
		 * 
		 * String[] arr = line.split("\\|"); String name = arr[2]; //give
		 * preference to longer name Integer cuiCode = Integer.parseInt(arr[0]);
		 * String oldName = rxCuiMap.get(cuiCode); if (oldName == null ||
		 * oldName.length() < name.length()) { rxCuiMap.put(cuiCode, name); } //
		 * System.out.println(Ndc2CuiMap.get(arr[1])); //
		 * logger.trace("read line:"+arr[0]); line = reader.readLine();
		 * 
		 * } } catch (Exception e) {
		 * 
		 * } finally { fileIS.close(); }
		 */
	}

	public void initNdc2CuiMap() throws IOException {
		Ndc2CuiMap = BinResourceFromRXNormData.deSerializeNdc2CuiMap();
		/*
		 * InputStream fileIS = null; try { Ndc2CuiMap = new HashMap<String,
		 * Integer>(); fileIS = Utils.getInputStream("rxnorm/RXNSAT_NDC.RRF");
		 * BufferedReader reader = new BufferedReader(new InputStreamReader(
		 * fileIS)); // logger.trace("read line:"+reader.readLine());
		 * 
		 * String line = reader.readLine(); while (line != null) {
		 * 
		 * String[] arr = line.split("\\|"); Ndc2CuiMap.put(arr[1],
		 * Integer.parseInt(arr[0])); //
		 * System.out.println(Ndc2CuiMap.get(arr[1])); //
		 * logger.trace("read line:"+arr[0]); line = reader.readLine();
		 * 
		 * } } catch (Exception e) {
		 * 
		 * } finally { fileIS.close(); }
		 */
	}

	public String getRxCui(String ndcString) {
		Integer intCode = Ndc2CuiMap.get(ndcString);
		if (intCode != null) {
			return Ndc2CuiMap.get(ndcString).toString();
		} else {
			return null;
		}
	}

	public String getRxCuiName(String rxCuiStr) {
		Integer rxCui = Integer.parseInt(rxCuiStr);
		if (rxCui != null) {
			return rxCuiMap.get(rxCui);
		} else {
			return null;
		}
	}

	public void addRxCui(Medication m) throws JAXBException {
	String ndcString = getNDCCodeString(m);
		String rxCui = getRxCui(ndcString);
		String rxCuiName = getRxCuiName(rxCui);

		if (getRxNormCoding(m) != null) {
			logger.trace("already contains rxnorm code");
			return;
		}

		Coding c = new Coding();
		Uri uri = new Uri();
		uri.setValue("http://www.nlm.nih.gov/research/umls/rxnorm");

		c.setSystem(uri);

		Code cd = new Code();
		logger.trace("rxCui:"+rxCui);
		cd.setValue(rxCui);
		c.setCode(cd);
		org.hl7.fhir.String displayValue = new org.hl7.fhir.String();
		displayValue.setValue(rxCuiName);
		c.setDisplay(displayValue);
		m.getCode().getCoding().add(c);

		logger.trace(JAXBUtil.toXml(m));
	}

	public String getNDCCodeString(Medication m) throws JAXBException {
		for (Coding coding : m.getCode().getCoding()) {
			Uri s = coding.getSystem();
			if (s.getValue().contains("NDC")) {
				Code code = coding.getCode();
				if (code != null) {
					return coding.getCode().getValue();
				} else
					return null;
			}
		}
		return null;
	}

	public Coding getRxNormCoding(Medication m) throws JAXBException {
		for (Coding coding : m.getCode().getCoding()) {
			Uri s = coding.getSystem();
			if (s.getValue().contains("rxnorm")) {
				return coding;
			}
		}
		return null;
	}

}