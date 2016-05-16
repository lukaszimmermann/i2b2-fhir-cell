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

import java.util.Date;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
@Startup
public class AuthenticationService {
	static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	@EJB
	AccessTokenService accessTokenBean;

	public boolean authenticate(String authHeaderContent) {
		if (authHeaderContent == null){
			logger.info("authfailure: authHeaderContent is null");
			return false;
		}

		
		AccessToken tok=getHttpAccessTokenString(authHeaderContent);
		boolean res= (tok != null && (!isExpired(tok.getExpiryDate()))) ? true
				: false;
		logger.info("auth is"+res);
		return res;
	}
	
	public AccessToken getHttpAccessTokenString(String authHeaderContent) {
		if (authHeaderContent == null) return null;
		String accessTokenId= authHeaderContent.replaceAll("Bearer\\s*", "");

		return accessTokenBean.find(accessTokenId);
	}

	private boolean isExpired(Date inputDate) {

		Date current = new Date();
		boolean result = current.after(inputDate) ? true : false;
		logger.info("current Date:" + current.toGMTString() + "\ninputDate:"
				+ inputDate.toGMTString() + "\nisExpired?:" + result);
		return result;
	}
	
	public void authenticateSession(String authHeaderContent,HttpSession session){
		AccessToken tok=getHttpAccessTokenString(authHeaderContent);
		logger.trace("inserting accessToken into Session:"+tok+"\nsession:"+session);
		session.setAttribute("accessToken",tok);
	}
	
	
}
