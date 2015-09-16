package edu.harvard.i2b2.oauth2.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity 
public class Client implements Serializable{
	private static final long serialVersionUID = 7884798170579315586L;

	static Logger logger = LoggerFactory.getLogger(Client.class);

	@Id 
	private String clientId;
	
	private String redirectUrl;
	
	private String clientSecret;

	private boolean approved;
	
	private Date registeredOn;
	
	private Date approvedOn;

	private Date lastAccess;

	private String contactName;
	
	private String contactEmail;
	
	private String description;

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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Client [clientId=" + clientId + ", redirectUrl=" + redirectUrl
				+ ", clientSecret=" + clientSecret + ", approved=" + approved
				+ ", registeredOn=" + registeredOn + ", approvedOn="
				+ approvedOn + ", lastAccess=" + lastAccess + ", contactName="
				+ contactName + ", contactEmail=" + contactEmail
				+ ", description=" + description + "]";
	}

	
}
