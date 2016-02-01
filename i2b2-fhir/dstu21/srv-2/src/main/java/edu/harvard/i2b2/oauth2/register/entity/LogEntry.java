package edu.harvard.i2b2.oauth2.register.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
 * fhir cell config from database
 */

@Entity
public class LogEntry {
	@Id
	@GeneratedValue
	int id;
	
	
	Date dateAccessed;

	String sessionId;

	String accessToken;

	int userId;

	String clientId;
	@Column(columnDefinition = "TEXT")
	String url;

	@Column(columnDefinition = "TEXT")
	String textComment;

	

	

	public Date getDateAccessed() {
		return dateAccessed;
	}

	public void setDateAccessed(Date dateAccessed) {
		this.dateAccessed = dateAccessed;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTextComment() {
		return textComment;
	}

	public void setTextComment(String textComment) {
		this.textComment = textComment;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return "LogEntry [id=" + id + ", dateAccessed=" + dateAccessed + ", sessionId=" + sessionId + ", accessToken="
				+ accessToken + ", userId=" + userId + ", clientId=" + clientId + ", url=" + url + ", textComment="
				+ textComment + "]";
	}

	
	
}
