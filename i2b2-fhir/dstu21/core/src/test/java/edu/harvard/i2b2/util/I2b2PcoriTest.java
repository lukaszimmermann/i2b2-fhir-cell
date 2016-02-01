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

import org.apache.commons.configuration.ConfigurationException;
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
import edu.harvard.i2b2.fhir.core.CoreConfig;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.Project;
import edu.harvard.i2b2.Config;

public class I2b2PcoriTest {
	Logger logger = LoggerFactory.getLogger(I2b2PcoriTest.class);
	String i2b2User;
	String i2b2Password;
	String i2b2Url;
	String i2b2Domain;
	String projectId;
	String patientId;
String medPath;
	@Before
	public void init() {
		try {
			i2b2User = CoreConfig.getStringProperty("pcori.i2b2User");
			i2b2Password  = CoreConfig.getStringProperty("pcori.i2b2Password");
			i2b2Url = CoreConfig.getStringProperty("pcori.i2b2Url");

			i2b2Domain  = CoreConfig.getStringProperty("pcori.i2b2Domain");

			projectId  = CoreConfig.getStringProperty("pcori.projectId");
			patientId  = CoreConfig.getStringProperty("pcori.patientId");
			medPath  = CoreConfig.getStringProperty("pcori.medPath");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	@Test
	public void getDataForAPatient1() {
		try {
			I2b2UtilByCategory.getI2b2ResponseXmlForAResourceCategory(i2b2User,
					i2b2Password, i2b2Url, i2b2Domain, projectId, patientId,
					"medications", medPath);
			// \\\\PCORI_MED\\PCORI\\MEDICATION\\RXNORM_CUI\\
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
