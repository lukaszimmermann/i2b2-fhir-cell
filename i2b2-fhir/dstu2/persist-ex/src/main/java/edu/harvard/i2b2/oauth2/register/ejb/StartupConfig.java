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

import edu.harvard.i2b2.oauth2.core.ejb.AccessTokenService;
import edu.harvard.i2b2.oauth2.core.ejb.AuthTokenService;

@Startup
@Singleton

public class StartupConfig {
	@EJB
	ClientService clientService;
	@EJB
	UserService userService;
	
	@EJB
	AccessTokenService accessTokenService;
	
	
	@PostConstruct
	public void init(){
		userService.setup();
		clientService.setup();
		accessTokenService.setup();
	}
}
