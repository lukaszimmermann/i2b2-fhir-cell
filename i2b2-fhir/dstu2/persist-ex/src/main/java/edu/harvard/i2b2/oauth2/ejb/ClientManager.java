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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.oauth2.entity.Client;
import edu.harvard.i2b2.oauth2.entity.User;

@Named
@RequestScoped 
public class ClientManager {
	static Logger logger = LoggerFactory.getLogger(ClientManager.class);

	private Client client;
	
	

	@EJB
	ClientService service;
	
	@PostConstruct
	public void init(){
		client=new Client();
		client.setClientId(RandomStringUtils.randomAlphanumeric(20));
		client.setClientSecret(RandomStringUtils.randomAlphanumeric(20));
	}
	
	public void save(){
		if(service.find(client.getClientId())!=null){
			service.update(client);
		}else{
			service.create(client);
		}
		
	}
	
	public void update(){
		service.update(this.client);
	}
	
	public void delete(){
		service.delete(client);
	}

	public List<Client> list(){
		List<Client> subList= new ArrayList<Client>();
		List<Client> fullList=new ArrayList<Client>();
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
		if(sessionMap.containsKey("authenticatedUser")){
			User authorizedUser=(User) sessionMap.get("authenticatedUser");
			logger.debug("Found authenticatedUser: "+authorizedUser);
			fullList= service.list();
			
			for(Client c:fullList){
				if (c.getUser().getId()==authorizedUser.getId()){
					subList.add(c);
				}
			}
		}
		logger.debug("fullList:"+fullList+"\nsubList:"+subList.toString());
		return subList;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	
}
