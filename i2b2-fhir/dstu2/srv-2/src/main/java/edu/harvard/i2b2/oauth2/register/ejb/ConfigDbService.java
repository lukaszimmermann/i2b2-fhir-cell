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
		createBlank();
		list();
		find("p1");
		// remove("client1");
		//find("client1");
	}


	public void createBlank() {
		ConfigDb d=new ConfigDb();
		d.setParameter("p1");
		d.setValue("p2");
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
