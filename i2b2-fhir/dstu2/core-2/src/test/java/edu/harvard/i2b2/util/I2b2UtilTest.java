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

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtilException;

public class I2b2UtilTest {
	Logger logger = LoggerFactory.getLogger(I2b2UtilTest.class);
	String i2b2User;
	String i2b2Password;
	String i2b2Url;
	String i2b2domain;

	@Before
	public void init() {
		i2b2User = "demo";
		i2b2Password = "demouser";
		i2b2Url = "http://services.i2b2.org:9090/i2b2";
		i2b2domain = "i2b2demo";
	}

	@Test
	public void validateUser() throws XQueryUtilException, IOException {
		String pmResponse=I2b2Util.getPmResponseXml(i2b2User, i2b2Password, i2b2Url, i2b2domain);
		logger.info(pmResponse);
	}

	// @Test
	public void getProjectsTest() throws XQueryUtilException {
		String pmResponseXml = Utils.getFile("pmResponse.xml");
		I2b2Util.getUserProjectMap(pmResponseXml);
		// logger.info("::"+I2b2Util.getUserProjectMap(pmResponseXml));
	}

}
