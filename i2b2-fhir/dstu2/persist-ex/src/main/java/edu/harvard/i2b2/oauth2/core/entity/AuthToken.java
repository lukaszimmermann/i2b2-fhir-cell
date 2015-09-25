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

package edu.harvard.i2b2.oauth2.core.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Table(name = "Authtoken")
//@NamedQuery(name = "findAllAuthTokens", query = "SELECT a FROM AuthToken a "
//	+ "ORDER BY id")
@Entity
public class AuthToken {
	static Logger logger = LoggerFactory.getLogger(AuthToken.class);

	// private static final long serialVersionUID = 6582105865012174694L;
	@Id
	private String authorizationCode;// authorizationTokenString;

	private String state;

	private String clientId;

	private String clientRedirectUri;

	private String resourceUserId;// resourceOwnerUsername;

	private String i2b2Token;// resourceOwnerAuthKey

	private String scope;
	
	private String i2b2Project;
	
	private String i2b2Url;
	
	private String i2b2Domain;
	
	private Date createdDate;
	
	private Date expiryDate;

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientRedirectUri() {
		return clientRedirectUri;
	}

	public void setClientRedirectUri(String clientRedirectUri) {
		this.clientRedirectUri = clientRedirectUri;
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

	public String getI2b2Project() {
		return i2b2Project;
	}

	public void setI2b2Project(String i2b2Project) {
		this.i2b2Project = i2b2Project;
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
		return "AuthToken [authorizationCode=" + authorizationCode + ", state="
				+ state + ", clientId=" + clientId + ", clientRedirectUri="
				+ clientRedirectUri + ", resourceUserId=" + resourceUserId
				+ ", i2b2Token=" + i2b2Token + ", scope=" + scope
				+ ", i2b2Project=" + i2b2Project + ", i2b2Url=" + i2b2Url
				+ ", i2b2Domain=" + i2b2Domain + ", createdDate=" + createdDate
				+ ", expiryDate=" + expiryDate + "]";
	}

	
	
}
