/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package edu.harvard.i2b2;

import static org.junit.Assert.*;

import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;

public class TestDSTU2 {
	static Logger logger = LoggerFactory.getLogger(TestDSTU2.class); 
	
	
	@Test
	public void test() throws DatatypeConfigurationException, JAXBException {
		
		List<Resource> s=ResourceSetup.getPatientAndMedicationStatementEg();
		logger.trace(""+JAXBUtil.toXml(s));
	}

}
