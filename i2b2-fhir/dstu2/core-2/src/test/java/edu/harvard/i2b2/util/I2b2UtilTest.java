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
import java.util.List;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.AuthenticationFailure;
import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.Project;

public class I2b2UtilTest {
	Logger logger = LoggerFactory.getLogger(I2b2UtilTest.class);
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
	public void validateUser() throws XQueryUtilException, IOException, JAXBException, AuthenticationFailure {
		String pmResponse=I2b2Util.getPmResponseXml(i2b2User, i2b2Password, i2b2Url, i2b2Domain);
		String i2b2Token=I2b2Util.getToken(pmResponse);
		pmResponse=I2b2Util.getPmResponseXml(i2b2User, i2b2Token, i2b2Url, i2b2Domain);
		
		List<Project> projMap = I2b2Util.getUserProjectMap(pmResponse);
		
		Assert.assertEquals(projectId,projMap.get(0).getId());
		//Assert.assertEquals("i2b2 Demo",projMap.get(0).getName());
		//logger.info(pmResponse);
		//logger.info("i2b2Token:"+i2b2Token);
		I2b2Util.getAllPatients(i2b2User, i2b2Token, i2b2Url, i2b2Domain, projMap.get(0).getId());
		String pdoXml=I2b2Util.getAllDataForAPatient(i2b2User, i2b2Password, i2b2Url, i2b2Domain, projectId, patientId);
		Bundle b = I2b2Util.getAllDataForAPatientAsFhirBundle(pdoXml);
		logger.info("projMap:"+projMap.toString());
		logger.info("pdoAllPtDataXml:"+pdoXml);
		logger.info("Patient Bundle size:"+b.getEntry().size());
	}

	// @Test
	public void getProjectsTest() throws XQueryUtilException {
		String pmResponseXml = Utils.getFile("pmResponse.xml");
		I2b2Util.getUserProjectMap(pmResponseXml);
		// logger.info("::"+I2b2Util.getUserProjectMap(pmResponseXml));
	}

}
