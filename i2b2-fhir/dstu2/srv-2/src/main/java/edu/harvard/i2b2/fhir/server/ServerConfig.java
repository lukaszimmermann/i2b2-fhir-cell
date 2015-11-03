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

package edu.harvard.i2b2.fhir.server;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ServerConfig {
	static private String i2b2Url ;
	static private String i2b2Domain ;
	static private String openAccessToken;
	static private boolean openAccess;
	static private String openI2b2User;
	static private String openI2b2Password;
	static private String openI2b2Project;
	static private String openClientId; 
	private static String demoPatientId;
	private static int maxQueryThreads;

	private static String demoConfidentialClientId;
	private static String demoConfidentialClientSecret;
	
	
	static Logger logger = LoggerFactory.getLogger(ServerConfig.class);
	
	
	static {
		try {
			CompositeConfiguration config = new CompositeConfiguration();
			config.addConfiguration(new SystemConfiguration());
			config.addConfiguration(new PropertiesConfiguration(
					"application.properties"));
			//config.addConfiguration(new PropertiesConfiguration(
			//		"/Users/***REMOVED***/Syncplicity/confidential_data/config_local_pcori/confidential.properties"));
			
			i2b2Url =config.getString("i2b2Url");
			i2b2Domain =config.getString("i2b2Domain");
			openAccessToken =config.getString("openAccessToken");
			openAccess=Boolean.parseBoolean(config.getString("openAccess"));
			openI2b2User =config.getString("openI2b2User");
			openI2b2Password =config.getString("openI2b2Password");
			openI2b2Project =config.getString("openI2b2Project");
			openClientId =config.getString("openClientId");
			demoPatientId =config.getString("demoPatientId");
			maxQueryThreads =config.getInt("maxQueryThreads");
			demoConfidentialClientId=config.getString("demoConfidentialClientId");
			demoConfidentialClientSecret=config.getString("demoConfidentialClientSecret");
			
			logger.info("initialized:"+"\ni2b2Url:"+i2b2Url
					+"\ndemoAccessToken:"+openAccessToken+"\n openAccess:"+openAccess
					+"\nmaxQueryThreads"+ maxQueryThreads
					+"\ndemoConfidentialClientId:"+demoConfidentialClientId
					+"\ndemoConfidentialClientSecret"+demoConfidentialClientSecret);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}


	public static String getI2b2Url() {
		return i2b2Url;
	}


	public static void setI2b2Url(String i2b2Url) {
		ServerConfig.i2b2Url = i2b2Url;
	}


	public static String getI2b2Domain() {
		return i2b2Domain;
	}


	public static void setI2b2Domain(String i2b2Domain) {
		ServerConfig.i2b2Domain = i2b2Domain;
	}


	public static String getOpenAccessToken() {
		return openAccessToken;
	}


	public static void setOpenAccessToken(String openAccessToken) {
		ServerConfig.openAccessToken = openAccessToken;
	}


	public static boolean isOpenAccess() {
		return openAccess;
	}


	public static void setOpenAccess(boolean openAccess) {
		ServerConfig.openAccess = openAccess;
	}


	public static String getOpenI2b2User() {
		return openI2b2User;
	}


	public static void setOpenI2b2User(String openI2b2User) {
		ServerConfig.openI2b2User = openI2b2User;
	}


	public static String getOpenI2b2Password() {
		return openI2b2Password;
	}


	public static void setOpenI2b2Password(String openI2b2Password) {
		ServerConfig.openI2b2Password = openI2b2Password;
	}


	public static String getOpenI2b2Project() {
		return openI2b2Project;
	}


	public static void setOpenI2b2Project(String openI2b2Project) {
		ServerConfig.openI2b2Project = openI2b2Project;
	}


	public static String getOpenClientId() {
		return openClientId;
	}


	public static void setOpenClientId(String openClientId) {
		ServerConfig.openClientId = openClientId;
	}


	public static String getDemoPatientId() {
		return demoPatientId;
	}


	public static void setDemoPatientId(String demoPatientId) {
		ServerConfig.demoPatientId = demoPatientId;
	}


	public static int getMaxQueryThreads() {
		return maxQueryThreads;
	}


	public static void setMaxQueryThreads(int maxQueryThreads) {
		ServerConfig.maxQueryThreads = maxQueryThreads;
	}


	public static String getDemoConfidentialClientId() {
		return demoConfidentialClientId;
	}


	public static String getDemoConfidentialClientSecret() {
		return demoConfidentialClientSecret;
	}

	
}
