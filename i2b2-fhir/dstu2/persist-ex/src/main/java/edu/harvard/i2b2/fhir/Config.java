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

package edu.harvard.i2b2.fhir;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Config {
	static public String i2b2Url ;
	static public String i2b2Domain ;
	static public String openAccessToken;
	static public boolean openAccess;
	static public String openI2b2User;
	static public String openI2b2Password;
	static public String openI2b2Project;
	static public String openClientId; 
	public static String demoPatientId;

	static Logger logger = LoggerFactory.getLogger(Config.class);
	
	
	static {
		try {
			CompositeConfiguration config = new CompositeConfiguration();
			config.addConfiguration(new SystemConfiguration());
			config.addConfiguration(new PropertiesConfiguration(
					"application.properties"));
			
			i2b2Url =config.getString("i2b2Url");
			i2b2Domain =config.getString("i2b2Domain");
			openAccessToken =config.getString("openAccessToken");
			openAccess=Boolean.parseBoolean(config.getString("openAccess"));
			openI2b2User =config.getString("openI2b2User");
			openI2b2Password =config.getString("openI2b2Password");
			openI2b2Project =config.getString("openI2b2Project");
			openClientId =config.getString("openClientId");
			demoPatientId =config.getString("demoPatientId");
			
			logger.info("initialized:"+"\ni2b2Url:"+i2b2Url
					+"\ndemoAccessToken:"+openAccessToken+"\n openAccess:"+openAccess);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	
	
	
}
