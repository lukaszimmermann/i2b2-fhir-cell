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
import edu.harvard.i2b2.oauth2.register.entity.LogEntry;

import javax.ejb.Singleton;
import javax.ejb.Startup;


@Singleton
@Startup

public class LogEntryService {
	static Logger logger = LoggerFactory.getLogger(LogEntryService.class);

	@PersistenceContext
	EntityManager em;

	@PostConstruct
	public void init() {
		// patientList<String>Hm=new HashMap<String,List<String>>();
	}
	public void setup() {
		createDefault();
		list();
		find(123456);
	}


	public void createDefault() {
		LogEntry le=new LogEntry();
		le.setAccessToken("accessToken");
		le.setDateAccessed(new Date());
		le.setUrl("http://test");
		le.setUserId(123456);
		create(le);
	}
	
	

	public List<LogEntry> list() {
		List<LogEntry> list = em.createQuery(" SELECT s FROM LogEntry s")
				.getResultList();
		String msg = "";
		for (Object x : list) {
			msg += x.toString() + "\n";
		}
		logger.info("list:" + msg);
		return list;
	}

	public LogEntry find(int id) {
		LogEntry u = em.find(LogEntry.class,id);
		if (u != null) {
			logger.info("found:" + u.toString());
		} else {
			logger.info("no LogEntry with id:" + id);
		}
		return u;
	}

	public void delete(LogEntry u) {
		em.remove(em.contains(u) ? u : em.merge(u));
		logger.info("removed ConfigDb");
	}

	public void update(LogEntry u) {
		em.merge(u);
		logger.trace("updated:" + u);
	}

	public void save(LogEntry u) {
		if (em.contains(u)) {
			update(u);
		} else {
			create(u);
		}
		logger.trace("updated:" + u);

	}
	public void create(LogEntry u) {
		logger.info("persisting:" + u.toString());
		em.persist(u);
		logger.info("persisted:" + u.toString());
		
	}


	

}
