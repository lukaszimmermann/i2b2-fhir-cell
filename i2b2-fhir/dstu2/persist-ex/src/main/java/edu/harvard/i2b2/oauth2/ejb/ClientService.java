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

package edu.harvard.i2b2.oauth2.ejb;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.oauth2.entity.Client;

@Stateless
public class ClientService {
	static Logger logger = LoggerFactory.getLogger(ClientService.class);

	@PersistenceContext
	EntityManager em;

	public void setup() {
		createBlank();
		list();
		find("client1");
		// remove("client1");
		find("client1");
	}

	public void createBlank() {
		Client c = new Client();
		c.setClientId("client1");
		c.setClientSecret("secret1");
		c.setRedirectUrl("http://google.com");
		create(c);
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
