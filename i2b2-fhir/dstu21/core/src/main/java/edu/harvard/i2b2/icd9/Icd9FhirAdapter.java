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
 * 		July 4, 2015
 */
package edu.harvard.i2b2.icd9;

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
import org.hl7.fhir.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.query.QueryBuilder;
import edu.harvard.i2b2.Icd9.Icd9Mapper;

public class Icd9FhirAdapter {
	static Logger logger = LoggerFactory.getLogger(Icd9FhirAdapter.class);

	Icd9Mapper Icd9Mapper;
	
	public Icd9FhirAdapter() throws IOException {
		Icd9Mapper= new Icd9Mapper();
	}

	public void init() throws IOException {
	}

	public void addIcd9Name(Condition ob) throws JAXBException {
		for (Coding coding : ob.getCode().getCoding()) {
			String systemVal = coding.getSystem().getValue().toString();
			if (systemVal.equals("http://hl7.org/fhir/sid/icd-9-cm")
					) {
				
				String Icd9Number=coding.getCode().getValue().replace("Icd9:", "");
				
				String display=Icd9Mapper.getIcd9Name(Icd9Number);
				if (display==null || display.length()==0) continue;
				logger.trace("Icd9Num:"+Icd9Number+"->"+display);
				
				
				org.hl7.fhir.String displayValue = new org.hl7.fhir.String();
				displayValue.setValue(display);
				
				coding.setDisplay(displayValue);
			}
		}
	}	
		

}