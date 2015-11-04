package edu.harvard.i2b2.oauth2.register.ejb;


import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.oauth2.register.entity.ConfigDb;


@Stateless
public class ConfigDbManager {
	static Logger logger = LoggerFactory.getLogger(ConfigDbManager.class);

	@PersistenceContext
	EntityManager em;

	
	@PostConstruct
	public void init(){
		
	}
	//@EJB
	//ConfigDbService service;

	
	//TODO check of the tok has a project which has the given pid
	//check if scope allows access to the patient
	
	public ConfigDb find(String parName) {
		return new ConfigDb();
		//return service.find(parName);
	}
	
}
