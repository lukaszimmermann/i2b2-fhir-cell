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

package edu.harvard.i2b2.fhirserver.entity;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BasePathTest {
	static Logger logger = LoggerFactory.getLogger( BasePathTest.class);

	@Test
	public void test() {
		
		String url="http://localhost:8080/srv-dstu2-0.2/api/authz/i2b2login";
		//url=url.replaceAll("/(^//.+)$", "A");
		//String path = uri.getPath();
		logger.trace(url);
		url = url.substring(0,url.lastIndexOf('/') )+"";
		logger.trace(url);
	}

}
