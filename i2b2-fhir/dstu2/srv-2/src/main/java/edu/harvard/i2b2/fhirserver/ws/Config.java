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

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	static public String i2b2Url ;
	static public String demoAccessToken;
	static public boolean openAccess;

	static Logger logger = LoggerFactory.getLogger(Config.class);
	
	static {
		try {
			CompositeConfiguration config = new CompositeConfiguration();
			config.addConfiguration(new SystemConfiguration());
			config.addConfiguration(new PropertiesConfiguration(
					"application.properties"));
			
			i2b2Url =config.getString("i2b2Url");
			demoAccessToken =config.getString("demoAccessToken");
			openAccess=Boolean.parseBoolean(config.getString("openAccess"));
			
			logger.info("initialized:"+"\ni2b2Url:"+i2b2Url
					+"\ndemoAccessToken:"+demoAccessToken+"\n openAccess:"+openAccess);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	
	
	
}
