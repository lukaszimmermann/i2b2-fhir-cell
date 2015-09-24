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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtilException;

public class I2b2UtilTest {
	Logger logger = LoggerFactory.getLogger(I2b2UtilTest.class);

	@Test
	public void getProjectsTest() throws  XQueryUtilException {
		String pmResponseXml=Utils.getFile("pmResponse.xml");
		//logger.info(pmResponseXml);
		//logger.info("::"+I2b2Util.getUserProjects(pmResponseXml));
		I2b2Util.getUserProjectMap(pmResponseXml);
		//logger.info("::"+I2b2Util.getUserProjectMap(pmResponseXml));
	}

}
