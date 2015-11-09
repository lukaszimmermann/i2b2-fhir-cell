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

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.server.ServerConfigs;
import edu.harvard.i2b2.oauth2.register.entity.ConfigDb;

import javax.ejb.Singleton;
import javax.ejb.Startup;


@Singleton
@Startup

public class ConfigDbService {
	static Logger logger = LoggerFactory.getLogger(ConfigDbService.class);

	@PersistenceContext
	EntityManager em;

	@PostConstruct
	public void init() {
		// patientList<String>Hm=new HashMap<String,List<String>>();
	}
	public void setup() {
		createDefault();
		list();
		find("p1");
		// remove("client1");
		//find("client1");
	}


	public void createDefault() {
		ServerConfigs sconfig=new ServerConfigs();
		for(String key:sconfig.getKeys()){
			//logger.trace("creating ConfigDb for:"+key);
			create(key,sconfig.getFileConfigValue(key));
		}
		/*
		create("p1","v1");
		create("i2b2Url","http://services.i2b2.org:9090/i2b2");
		create("i2b2Domain","i2b2demo");
		create("openAccessToken","1f4ffead29414d1977fba44e2bf4d8b7");
		create("openAccess","true");
		create("openI2b2User","demo");
		create("openI2b2Password","demouser");
		create("openI2b2Project","Demo");
		create("openClientId","my_web_app");
		create("demoPatientId","1000000005");
		create("maxQueryThreads","2");
		create("resourceCategoriesList","medications-labs-diagnoses-reports");
		create("medicationPath","\\\\i2b2_MEDS\\i2b2\\Medications\\");
		create("labsPath","\\\\i2b2_LABS\\i2b2\\Labtests\\");
		create("diagnosesPath","\\\\i2b2_DIAG\\i2b2\\Diagnoses\\");
		create("reportsPath","\\\\i2b2_REP\\i2b2\\Reports\\");
		create("demoConfidentialClientId","webclient");
		create("demoConfidentialClientSecret","1b6sg3bs72bs73bd73h3bs8ok8fb3bbftd7");
		*/
	}
	
	public void create(String parName,String value ) {
		ConfigDb d=new ConfigDb();
		d.setParameter(parName);
		d.setValue(value);
		create(d);
	}

	public void create(ConfigDb u) {
		logger.info("persisting:" + u.toString());
		em.persist(u);
		logger.info("persisted:" + u.toString());
	}

	public List<ConfigDb> list() {
		List<ConfigDb> list = em.createQuery(" SELECT s FROM ConfigDb s")
				.getResultList();
		String msg = "";
		for (Object x : list) {
			msg += x.toString() + "\n";
		}
		logger.info("list:" + msg);
		return list;
	}

	public ConfigDb find(String parName) {
		ConfigDb u = em.find(ConfigDb.class, parName);
		if (u != null) {
			logger.info("found:" + u.toString());
		} else {
			logger.info("no ConfigDb with id:" + parName);
		}
		return u;
	}

	public void delete(ConfigDb u) {
		em.remove(em.contains(u) ? u : em.merge(u));
		logger.info("removed ConfigDb");
	}

	public void update(ConfigDb u) {
		em.merge(u);
		logger.trace("updated:" + u);
	}

	public void save(ConfigDb u) {
		if (em.contains(u)) {
			update(u);
		} else {
			create(u);
		}
		logger.trace("updated:" + u);

	}


	

}
