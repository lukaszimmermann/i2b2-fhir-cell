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

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.XQueryUtilException;

public class AuthenticationTest {

	static Logger logger = LoggerFactory.getLogger(AuthenticationTest.class);
	@Test
	public void validateI2b2UserNamePasswordPairTest() throws  XQueryUtilException {
		//fail("Not yet implemented");
		Assert.assertEquals(false, I2b2Helper.validateI2b2UserNamePasswordPair("demo","demouser1","i2b2demo", "http://services.i2b2.org/i2b2"));
		Assert.assertEquals(true,I2b2Helper.validateI2b2UserNamePasswordPair("demo","demouser","i2b2demo", "http://services.i2b2.org/i2b2"));
		//logger.trace(""+authR);
		
	}

}
