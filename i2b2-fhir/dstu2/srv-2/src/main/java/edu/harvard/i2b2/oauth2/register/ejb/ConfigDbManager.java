package edu.harvard.i2b2.oauth2.register.ejb;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Startup;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.oauth2.register.entity.ConfigDb;


@Named
@RequestScoped
			 
public class ConfigDbManager {
	static Logger logger = LoggerFactory.getLogger(ConfigDbManager.class);

	@EJB
	ConfigDbService service;

	
	@PostConstruct
	public void init(){
		
	}
	//@EJB
	//ConfigDbService service;

	
	//TODO check of the tok has a project which has the given pid
	//check if scope allows access to the patient
	
	public List<ConfigDb> list(String parName) {
		return service.list();
	}
	
}
