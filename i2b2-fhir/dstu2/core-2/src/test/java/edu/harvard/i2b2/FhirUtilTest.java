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

package edu.harvard.i2b2;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Medication;
import org.hl7.fhir.MedicationOrder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;

public class FhirUtilTest {
	
	static Logger logger = LoggerFactory.getLogger(FhirUtilTest.class); 
	@Test
	public void contain() throws JAXBException, IOException {
		// URL path=FhirUtil.class.getResource("validation.zip");
		// System.out.println(FhirUtil.isValid(Utils.getFile("example/fhir/singlePatient.xml")));
		String mpXml=IOUtils.toString(FhirUtilTest.class.getResourceAsStream("/example/fhir/DSTU2/singleMedicationOrder.xml"));
		String mXml=IOUtils.toString(FhirUtilTest.class.getResourceAsStream("/example/fhir/DSTU2/singleMedication.xml"));
		
		MedicationOrder mp= JAXBUtil.fromXml(mpXml,MedicationOrder.class);
		Medication m= JAXBUtil.fromXml(mXml,Medication.class);
		
		//logger.info("MP:"+JAXBUtil.toXml(mp));
		//logger.info("MP:"+JAXBUtil.toXml(m));
			
		logger.info("after containing:"+JAXBUtil.toXml(FhirUtil.containResource(mp, m)));
	}
}
