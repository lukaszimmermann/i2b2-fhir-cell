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

package edu.harvard.i2b2.oauth2.register.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity 
public class Client implements Serializable{
	private static final long serialVersionUID = 7884798170579315586L;

	static Logger logger = LoggerFactory.getLogger(Client.class);

	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	@Id 
	private String clientId;
	
	private String redirectUrl;
	
	private String clientSecret;

	private boolean approved; //by user
	
	private Date registeredOn;
	
	private Date approvedOn;

	private Date lastAccess;

	private String description;
	
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public Date getRegisteredOn() {
		return registeredOn;
	}

	public void setRegisteredOn(Date registeredOn) {
		this.registeredOn = registeredOn;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public Date getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Client [clientId=" + clientId + ", redirectUrl=" + redirectUrl
				+ ", clientSecret=" + clientSecret + ", approved=" + approved
				+ ", registeredOn=" + registeredOn + ", approvedOn="
				+ approvedOn + ", lastAccess=" + lastAccess + ", description="
				+ description + ", user=" + user + "]";
	}

	


	
}
