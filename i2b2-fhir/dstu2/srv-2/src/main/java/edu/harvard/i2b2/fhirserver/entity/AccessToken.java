package edu.harvard.i2b2.fhirserver.entity;

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

//@Table(name = "Authtoken")
//@NamedQuery(name = "findAllAuthTokens", query = "SELECT a FROM AuthToken a "
//	+ "ORDER BY id")
@Entity
public class AccessToken {
	static Logger logger = LoggerFactory.getLogger(AccessToken.class);

	// private static final long serialVersionUID = 6582105865012174694L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String accessToken;

	private String authorizationCode;// authorizationTokenString;

	private String clientId;
	private String clientRedirectUri;

	private String resourceUserId;// resourceOwnerUsername;
	private String i2b2Token;// resourceOwnerAuthKey

	private String scope;
	
	private Date createdDate;
	private Date expiryDate;

	public AccessToken() {
		init();
	}

	public AccessToken(String resourceUserId, String i2b2Token,
			String authorizationCode, String clientRedirectUri, String clientId) {
		this.resourceUserId = resourceUserId;
		this.i2b2Token = i2b2Token;
		this.authorizationCode = authorizationCode;
		this.clientRedirectUri = clientRedirectUri;
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
		// this.id = r.nextInt();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// @Temporal(TIMESTAMP)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	// @Temporal(TIMESTAMP)
	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
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

	@Override
	public String toString() {
		return "AccessToken [id=" + id + ", accessToken=" + accessToken
				+ ", authorizationCode=" + authorizationCode + ", clientId="
				+ clientId + ", clientRedirectUri=" + clientRedirectUri
				+ ", resourceUserId=" + resourceUserId + ", i2b2Token="
				+ i2b2Token + ", createdDate=" + createdDate + ", expiryDate="
				+ expiryDate + "]";
	}

}
