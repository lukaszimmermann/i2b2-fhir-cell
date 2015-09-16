package edu.harvard.i2b2.oauth2.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton

public class StartupConfig {
	@EJB
	ClientService service;
	
	@PostConstruct
	public void init(){
		service.setup();
	}
}
