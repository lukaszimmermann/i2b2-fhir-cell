package edu.harvard.i2b2.fhirserver.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import static javax.persistence.TemporalType.TIMESTAMP;

import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhirserver.ws.I2b2FhirWS;


//@Table(name = "Authtoken")
//@NamedQuery(name = "findAllAuthTokens", query = "SELECT a FROM AuthToken a "
	//	+ "ORDER BY id")
@Entity
public class AuthToken {
	static Logger logger = LoggerFactory.getLogger(AuthToken.class);

	//private static final long serialVersionUID = 6582105865012174694L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String tokenString;

	private String clientId;

	private String resourceOwnerUsername;
	private String resourceOwnerPassword;
	private String resourceOwnerAuthKey;

	private Date createdDate;
	private Date expiryDate;

	public AuthToken() {
		init();
	}

	public AuthToken(String clientId) {
		this.clientId = clientId;
		init();
	}

	private void init() {
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		this.createdDate = c.getTime();
		c.add(Calendar.HOUR, 1);
		this.expiryDate = c.getTime();
		String tokenString = UUID.randomUUID().toString();
		Random r = new Random();
		//this.id = r.nextInt();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//@Temporal(TIMESTAMP)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	//@Temporal(TIMESTAMP)
	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getTokenString() {
		return tokenString;
	}

	public void setTokenString(String tokenString) {
		this.tokenString = tokenString;
	}

	public String getResourceOwnerUsername() {
		return resourceOwnerUsername;
	}

	public void setResourceOwnerUsername(String resourceOwnerUsername) {
		this.resourceOwnerUsername = resourceOwnerUsername;
	}

	public String getResourceOwnerPassword() {
		return resourceOwnerPassword;
	}

	public void setResourceOwnerPassword(String resourceOwnerPassword) {
		this.resourceOwnerPassword = resourceOwnerPassword;
	}

	public String getResourceOwnerAuthKey() {
		return resourceOwnerAuthKey;
	}

	public void setResourceOwnerAuthKey(String resourceOwnerAuthKey) {
		this.resourceOwnerAuthKey = resourceOwnerAuthKey;
	}

	@Override
	public String toString() {
		return "AuthToken [ clientId=" + clientId + "]";
	}
	
	

}
