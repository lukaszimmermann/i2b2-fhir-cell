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

package edu.harvard.i2b2.oauth2.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class User implements Serializable {
	private static final long serialVersionUID = 7884798140579415586L;

	static Logger logger = LoggerFactory.getLogger(User.class);

	@Id
	private int Id;

	private UserType userType;

	private String firstName;
	
	private String lastName;

	private String email;

	private String password;

	private boolean emailConfirmed;

	private boolean approved;// by Admin

	private String emailConfirmationUrl;

	private String notes; // by admin

	private Date registeredOn;

	private Date approvedOn;

	private Date lastAccess;

	/*@MapsId
	@OneToMany
	@JoinColumn*/
	Client client;

	public int getId() {
		return Id;
	}

	public void setId(int Id) {
		this.Id = Id;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEmailConfirmed() {
		return emailConfirmed;
	}

	public void setEmailConfirmed(boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getEmailConfirmationUrl() {
		return emailConfirmationUrl;
	}

	public void setEmailConfirmationUrl(String emailConfirmationUrl) {
		this.emailConfirmationUrl = emailConfirmationUrl;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "User [Id=" + Id + ", userType=" + userType
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", password=" + password
				+ ", emailConfirmed=" + emailConfirmed + ", approved="
				+ approved + ", emailConfirmationUrl=" + emailConfirmationUrl
				+ ", notes=" + notes + ", registeredOn=" + registeredOn
				+ ", approvedOn=" + approvedOn + ", lastAccess=" + lastAccess
				+ ", client=" + client + "]";
	}
	
	

	
}
