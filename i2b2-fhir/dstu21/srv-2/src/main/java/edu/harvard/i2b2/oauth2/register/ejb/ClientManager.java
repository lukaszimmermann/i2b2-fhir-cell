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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;


import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.oauth2.register.entity.Client;
import edu.harvard.i2b2.oauth2.register.entity.User;

@Named
@RequestScoped
public class ClientManager {
	static Logger logger = LoggerFactory.getLogger(ClientManager.class);

	private Client client;

	@EJB
	ClientService service;

	@PostConstruct
	public void newClient() {
		client = new Client();
		client.setClientId(RandomStringUtils.randomAlphanumeric(20));
		client.setClientSecret(RandomStringUtils.randomAlphanumeric(20));
	}

	public String save() {
		if(client.getUser()==null) {client.setUser(getSessionUser() );}
		if (service.find(client.getClientId()) != null) {
			service.update(client);

		} else {
			service.create(client);
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "client saved successfully!", null));
		return "list";
	}

	public void update() {
		service.update(this.client);
	}

	public void delete() {
		service.delete(client);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "client deleted successfully!", null));
	}

	public List<Client> list() {
		List<Client> subList = new ArrayList<Client>();
		List<Client> fullList = new ArrayList<Client>();
		User authorizedUser = getSessionUser();
		logger.debug("Found authenticatedUser: " + authorizedUser);
		fullList = service.list();

		for (Client c : fullList) {
			if (c != null && authorizedUser != null
					&& c.getUser().getId() == authorizedUser.getId()) {
				subList.add(c);
			}
		}
		logger.debug("fullList:" + fullList + "\nsubList:" + subList.toString());
		return subList;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public User getSessionUser() {
		User authorizedUser = null;
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = context.getExternalContext()
				.getSessionMap();
		if (sessionMap.containsKey("authenticatedUser")) {
			authorizedUser = (User) sessionMap.get("authenticatedUser");
		}
		logger.debug("session user:" + authorizedUser);
		return authorizedUser;
	}
	
	
	
	
}
