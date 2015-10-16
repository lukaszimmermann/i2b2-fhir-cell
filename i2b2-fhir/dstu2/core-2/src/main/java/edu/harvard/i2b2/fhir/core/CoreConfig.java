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

package edu.harvard.i2b2.fhir.core;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CoreConfig {
	static private String resourceCategoriesList ;//comma seperated
	static private String medicationPath ;
	static private String labsPath ;
	static private String diagnosesPath ;
	static private String reportsPath ;
	

	static Logger logger = LoggerFactory.getLogger(CoreConfig.class);
	
	static {
		try {
			CompositeConfiguration config = new CompositeConfiguration();
			config.addConfiguration(new SystemConfiguration());
			config.addConfiguration(new PropertiesConfiguration(
					"application.properties"));
			
			resourceCategoriesList  =config.getString("resourceCategoriesList");
			labsPath  =config.getString("labsPath");
			medicationPath  =config.getString("medicationPath");
			diagnosesPath  =config.getString("diagnosesPath");
			reportsPath  =config.getString("reportsPath");
	
			logger.info("initialized:"+toStaticString());
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}


	public static String getResourceCategoriesList() {
		return resourceCategoriesList;
	}
	


	public static String getMedicationPath() {
		return medicationPath;
	}



	public static String getLabsPath() {
		return labsPath;
	}



	public static String getDiagnosesPath() {
		return diagnosesPath;
	}



	public static String getReportsPath() {
		return reportsPath;
	}



	public static String toStaticString() {
		return "resourceCategoriesList:"+resourceCategoriesList
				+"\nmedicationPath:"+medicationPath
				+"\nlabsPath:"+labsPath
				+"\ndiagnosesPath:"+diagnosesPath
				+"\nreportsPath:"+reportsPath;
	}


	
	

}
