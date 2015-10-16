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

package edu.harvard.i2b2.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.AuthenticationFailure;
import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.I2b2UtilByCategory;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.WebServiceCall;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.Project;
import edu.harvard.i2b2.Config;

public class I2b2UtilByCategoryTest {
	Logger logger = LoggerFactory.getLogger(I2b2UtilByCategoryTest.class);
	String i2b2User;
	String i2b2Password;
	String i2b2Url;
	String i2b2Domain;
	String projectId;
	String patientId;
	
	@Before
	public void init() {
		i2b2User = "demo";//pcori
		i2b2Password = "demouser";
		i2b2Url = "http://services.i2b2.org:9090/i2b2";
		i2b2Domain = "i2b2demo";
		projectId="Demo";//"pcori";
		patientId="1000000005";
		}

	@Test
	public void getAllData() throws XQueryUtilException, IOException, JAXBException, AuthenticationFailure, FhirCoreException {
		I2b2UtilByCategory.getAllDataForAPatientAsFhirBundle(i2b2User, i2b2Password, i2b2Url, i2b2Domain, projectId, patientId, null);
	}
	
}
