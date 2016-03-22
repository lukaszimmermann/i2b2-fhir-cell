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

import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.hl7.fhir.Coding;
import org.hl7.fhir.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.Icd9.Icd9Mapper;
import edu.harvard.i2b2.Icd9ToSnomedCT.Icd9ToSnomedCTMapper;

public class Icd9FhirAdapter {
	static Logger logger = LoggerFactory.getLogger(Icd9FhirAdapter.class);

	Icd9Mapper Icd9Mapper;
	Icd9ToSnomedCTMapper Icd9ToSnomedCTMapper;

	public Icd9FhirAdapter() throws IOException {
		Icd9Mapper = new Icd9Mapper();
		Icd9ToSnomedCTMapper = new Icd9ToSnomedCTMapper();
	}

	public void init() throws IOException {
	}

	public void addIcd9Name(Condition ob) throws JAXBException {
		for (Coding coding : ob.getCode().getCoding()) {
			String systemVal = coding.getSystem().getValue().toString();
			if (systemVal.equals("http://hl7.org/fhir/sid/icd-9-cm")) {

				String Icd9Number = coding.getCode().getValue().replace("Icd9:", "");

				String display = Icd9Mapper.getIcd9Name(Icd9Number);
				if (display == null || display.length() == 0)
					continue;
				logger.trace("Icd9Num:" + Icd9Number + "->" + display);

				org.hl7.fhir.String displayValue = new org.hl7.fhir.String();
				displayValue.setValue(display);

				coding.setDisplay(displayValue);
			}
		}
	}

	static public String getIcd9Code(Condition ob) {
		for (Coding coding : ob.getCode().getCoding()) {
			String systemVal = coding.getSystem().getValue().toString();
			if (systemVal.equals("http://hl7.org/fhir/sid/icd-9-cm")) {
				return coding.getCode().getValue().replace("Icd9:", "");
			}
		}
		return null;
	}

	public static String getSnomedCTCode(Condition ob) {
		for (Coding coding : ob.getCode().getCoding()) {
			String systemVal = coding.getSystem().getValue().toString();
			if (systemVal.equals("http://snomed.info/sct")) {
				return coding.getCode().getValue();
			}
		}
		return null;
	}

	public void translateIcd9ToSnomedCT(Condition ob) throws JAXBException {
		String icd9Code = getIcd9Code(ob);
		String snomedCTCode = getSnomedCTCode(ob);
		if (icd9Code != null || snomedCTCode == null) {
			snomedCTCode = Icd9ToSnomedCTMapper.getSnomedCTCode(icd9Code);
			if (snomedCTCode != null) {
				Coding coding = FhirUtil.createCoding(snomedCTCode, "http://snomed.info/sct", "-");
				ob.getCode().getCoding().add(coding);
			}
		}
	}
}