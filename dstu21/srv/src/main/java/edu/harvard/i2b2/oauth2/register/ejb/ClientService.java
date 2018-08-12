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
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.server.ConfigParameter;
import edu.harvard.i2b2.fhir.server.ServerConfigs;
import edu.harvard.i2b2.oauth2.register.entity.Client;

@Stateless
public class ClientService {
	static Logger logger = LoggerFactory.getLogger(ClientService.class);

	
	@PersistenceContext
	EntityManager em;

	@EJB
	UserService userService;
	
	@EJB
	ServerConfigs serverConfig;
	
	
	//@javax.ws.rs.core.Context
    //private ServletContext servletContext;

	public void setup() {
		createBlank();
		list();
		find("client0");
		// remove("client1");
		find("client0");
	}

	public void createBlank() {
		//logger.info("servletContext.getContextPath():"+servletContext.getContextPath());
		
		Client c = new Client();
		c.setClientId("client0");
		c.setClientSecret("secret0");
		c.setRedirectUrl("http://google.com");
		c.setUser(userService.find(1));
		save(c);

		c = new Client();
		c.setClientId("client1");
		c.setClientSecret("secret1");
		c.setRedirectUrl("http://localhost:8080");
		c.setUser(userService.find(1));
		save(c);
		
		c = new Client();
		c.setClientId("smartapp1");
		c.setClientSecret("1b6sf3bs72bs73bd73h3bs8ok8fb3bbftd7");
		c.setRedirectUrl("http://localhost:8080/persist-ex-dstu2-0.2/fhir-app1/index.html");
		c.setUser(userService.find(1));
		save(c);
		
		
		if (serverConfig.GetString(ConfigParameter.demoConfidentialClientId) != null) {
			c = new Client();
			c.setClientId(serverConfig.GetString(ConfigParameter.demoConfidentialClientId));
			c.setClientSecret(serverConfig.GetString(ConfigParameter.demoConfidentialClientSecret));
			c.setRedirectUrl("http://localhost:8080");
			c.setUser(userService.find(2));
			save(c);
		}
		
		
		
		if (serverConfig.GetString(ConfigParameter.openClientId) != null) {
			c = new Client();
			c.setClientId(serverConfig.GetString(ConfigParameter.openClientId) );
			c.setClientSecret("dummysecret");
			c.setRedirectUrl("http://localhost:8080");
			c.setUser(userService.find(2));
			save(c);
		}
	}

	public void create(Client client) {
		logger.info("persisting:" + client.toString());
		client.setRegisteredOn(new Date());
		em.persist(client);
		logger.info("persisted:" + client.toString());
	}

	public List<Client> list() {
		List<Client> list = em.createQuery(" SELECT s FROM Client s")
				.getResultList();
		String msg = "";
		for (Object x : list) {
			msg += x.toString() + "\n";
		}
		logger.info("list:" + msg);
		return list;
	}

	public Client find(String id) {
		Client c = em.find(Client.class, id);
		if (c != null) {
			logger.info("found:" + c.toString());
		} else {
			logger.info("no client with id:" + id);
		}
		return c;
	}

	public void delete(Client client) {
		em.remove(em.contains(client) ? client : em.merge(client));
		logger.info("removed client");
	}

	public void update(Client client) {
		em.merge(client);
		logger.trace("updated:" + client);
	}

	public void save(Client client) {
		if (em.contains(client)) {
			update(client);
		} else {
			create(client);
		}
		logger.trace("updated:" + client);

	}

}
