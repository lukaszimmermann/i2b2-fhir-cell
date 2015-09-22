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

package edu.harvard.i2b2.fhirserver.ws;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhirserver.ws.I2b2FhirWS;
import edu.harvard.i2b2.fhirserver.ws.I2b2Helper;

public class I2b2HelperTest {
	static Logger logger = LoggerFactory.getLogger(I2b2HelperTest.class);
	@Test
	public void testExtractPatientIdFromParticularUrl() {
		//logger.info(I2b2Helper.extractPatientId2("http://localhost:8080/srv-dstu2-0.2/api/Observation/1000000005-1"));
	}

}
