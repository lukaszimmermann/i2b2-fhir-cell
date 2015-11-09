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

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.oauth2.register.ejb.ConfigDbManager;
import edu.harvard.i2b2.oauth2.register.ejb.ConfigDbService;
import edu.harvard.i2b2.oauth2.register.entity.ConfigDb;


@Stateless
@DependsOn("ConfigDbService")
public class ServerConfigs {

	@EJB
	private ConfigDbService configDbService;

	static private boolean openAccess;
	static private int maxQueryThreads;

	
	static CompositeConfiguration configC = null;

	Logger logger = LoggerFactory.getLogger(ServerConfigs.class);

	@PostConstruct
	public void init(){
		
	}
	
	public ServerConfigs() {
		if (configC == null) {
			try {
				configC = new CompositeConfiguration();

				configC.addConfiguration(new SystemConfiguration());
				configC.addConfiguration(new PropertiesConfiguration(
						"application.properties"));
				// config.addConfiguration(new PropertiesConfiguration(
				// "/Users/***REMOVED***/Syncplicity/confidential_data/config_local_pcori/confidential.properties"));

				openAccess = Boolean
						.parseBoolean(GetString(ConfigParameter.openAccess));
				maxQueryThreads = Integer.parseInt(GetString(ConfigParameter.maxQueryThreads));
				
				logger.info("initialized:"+configC.toString());
				/*logger.info("initialized:" + "\ni2b2Url:" + i2b2Url
						+ "\ndemoAccessToken:" + openAccessToken
						+ "\n openAccess:" + openAccess + "\nmaxQueryThreads"
						+ maxQueryThreads + "\ndemoConfidentialClientId:"
						+ demoConfidentialClientId
						+ "\ndemoConfidentialClientSecret"
						+ demoConfidentialClientSecret);*/
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public String GetString(ConfigParameter configParameter) {
		String value=null;
		String parName=configParameter.toString();
		
		logger.info("parName:"+parName);
		if(configDbService==null) {logger.warn("configDbService is null");}
		else{
			ConfigDb p=configDbService.find(parName);
			if(p!=null) value=p.getValue();
		}
		if(value==null){value=configC.getString(parName);}
		return value;
	}


	public boolean isOpenAccess() {
		return openAccess;
	}

	public void setOpenAccess(boolean openAccess) {
		ServerConfigs.openAccess = openAccess;
	}

	
	
	public int getMaxQueryThreads() {
		return maxQueryThreads;
	}

	public void setMaxQueryThreads(int maxQueryThreads) {
		ServerConfigs.maxQueryThreads = maxQueryThreads;
	}

	

}
