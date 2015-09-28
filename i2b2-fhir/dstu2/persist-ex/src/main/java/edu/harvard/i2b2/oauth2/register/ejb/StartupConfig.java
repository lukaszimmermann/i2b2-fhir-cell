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

package edu.harvard.i2b2.oauth2.register.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.Config;
import edu.harvard.i2b2.oauth2.core.ejb.AccessTokenService;
import edu.harvard.i2b2.oauth2.core.ejb.AuthTokenService;
import edu.harvard.i2b2.oauth2.core.ejb.PatientBundleService;

@Startup
@Singleton

public class StartupConfig {
	static Logger logger = LoggerFactory.getLogger( StartupConfig.class);

	
	@EJB
	ClientService clientService;
	@EJB
	UserService userService;
	
	@EJB
	AccessTokenService accessTokenService;
	
	@EJB
	PatientBundleService patientBundleService;
	
	
	@PostConstruct
	public void init(){
		userService.setup();
		clientService.setup();
		accessTokenService.setup();
		try{
		//patientBundleService.getPatientBundle(accessTokenService.find(Config.openAccessToken), (String)Config.demoPatientId);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		}
}
