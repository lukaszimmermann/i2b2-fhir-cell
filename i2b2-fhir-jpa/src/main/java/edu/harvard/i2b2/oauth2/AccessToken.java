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

package edu.harvard.i2b2.oauth2;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class AccessToken {
	static Logger logger = LoggerFactory.getLogger(AccessToken.class);

	// private static final long serialVersionUID = 6582105865012174694L;
	@Id
	private String tokenString;
	
	private String clientId;
	
	private String resourceUserId;// resourceOwnerUsername;
	
	private String i2b2Token;// resourceOwnerAuthKey

	private String i2b2Project;
	
	private String i2b2Url;
	
	private String i2b2Domain;
	
	private String scope;
	
	private Date createdDate;
	
	private Date expiryDate;

	public String getTokenString() {
		return tokenString;
	}

	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getResourceUserId() {
		return resourceUserId;
	}

	public void setResourceUserId(String resourceUserId) {
		this.resourceUserId = resourceUserId;
	}

	public String getI2b2Token() {
		return i2b2Token;
	}

	public void setI2b2Token(String i2b2Token) {
		this.i2b2Token = i2b2Token;
	}

	public String getI2b2Project() {
		return i2b2Project;
	}

	public void setI2b2Project(String i2b2Project) {
		this.i2b2Project = i2b2Project;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getI2b2Url() {
		return i2b2Url;
	}

	public void setI2b2Url(String i2b2Url) {
		this.i2b2Url = i2b2Url;
	}

	public String getI2b2Domain() {
		return i2b2Domain;
	}

	public void setI2b2Domain(String i2b2Domain) {
		this.i2b2Domain = i2b2Domain;
	}

	@Override
	public String toString() {
		return "AccessToken [tokenString=" + tokenString + ", clientId="
				+ clientId + ", resourceUserId=" + resourceUserId
				+ ", i2b2Token=" + i2b2Token + ", i2b2Project=" + i2b2Project
				+ ", i2b2Url=" + i2b2Url + ", i2b2Domain=" + i2b2Domain
				+ ", scope=" + scope + ", createdDate=" + createdDate
				+ ", expiryDate=" + expiryDate + "]";
	}

	
	
}
