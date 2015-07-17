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
package edu.harvard.i2b2.loinc;

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
import org.hl7.fhir.Observation;
import org.hl7.fhir.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.query.QueryBuilder;

public class LoincFhirAdapter {
	static Logger logger = LoggerFactory.getLogger(LoincFhirAdapter.class);

	LoincMapper loincMapper;
	
	public LoincFhirAdapter() throws IOException {
		loincMapper= new LoincMapper();
	}

	public void init() throws IOException {
	}

	public void addLoincName(Observation ob) throws JAXBException {
		for (Coding coding : ob.getName().getCoding()) {
			String systemVal = coding.getSystem().getValue().toString();
			if (systemVal.equals("http://loinc.org")) {
				String loincNumber=coding.getCode().getValue().replace("LOINC:", "");
				org.hl7.fhir.String displayValue = new org.hl7.fhir.String();
				displayValue.setValue(loincMapper.getLoincName(loincNumber));
				coding.setDisplay(displayValue);
			}
		}
	}	
		

}