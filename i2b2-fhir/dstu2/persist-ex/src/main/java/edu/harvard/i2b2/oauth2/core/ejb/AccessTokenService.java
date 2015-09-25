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

package edu.harvard.i2b2.oauth2.core.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.Config;
import edu.harvard.i2b2.oauth2.core.entity.AccessToken;
import edu.harvard.i2b2.oauth2.core.entity.AuthToken;


@Singleton
public class AccessTokenService {
	static Logger logger = LoggerFactory.getLogger(AccessTokenService.class);

	@PersistenceContext
	EntityManager em;

	//@PostConstruct
	public void setup() {
		try {
			Random r = new Random();
			if (Config.openAccessToken != null) {
				createIfNotExistsDemoAccessToken();
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	public void deleteAllAccessTokens() {
		try {
			em.createQuery("delete from accesstoken where id!='-';")
					.executeUpdate();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new EJBException(ex.getMessage());
		}
	}

	public AccessToken createAccessToken(String authCode,
			String resourceUserId, String i2b2Token, String i2b2Project,
			String clientId, String scope) {
		try {
			AccessToken tok = new AccessToken();
			tok.setTokenString(authCode);
			tok.setResourceUserId(resourceUserId);
			tok.setI2b2Token(i2b2Token);
			tok.setI2b2Project(i2b2Project);
			tok.setClientId(clientId);
			tok.setScope(scope);
			tok.setCreatedDate(new Date());
			tok.setExpiryDate(DateUtils.addMinutes(new Date(), 30));

			logger.info("Created .." + tok.toString());
			em.persist(tok);
			logger.info("Persisted " + tok.toString());
			return tok;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new EJBException(ex.getMessage());
		}
	}

	public AccessToken createIfNotExistsDemoAccessToken() {
		try {
			AccessToken tok = null;
			logger.info("default token exists? ..");
			tok = em.find(AccessToken.class, Config.openAccessToken);
		
			if (tok == null) {

				tok = new AccessToken();
				tok.setTokenString(Config.openAccessToken);
				tok.setResourceUserId(Config.openI2b2User);
				tok.setI2b2Token(Config.openI2b2Password);
				tok.setClientId(Config.openClientId);
				tok.setScope("user *.read");
				tok.setI2b2Url(Config.i2b2Url);
				tok.setI2b2Project(Config.openI2b2Project);
				tok.setI2b2Domain(Config.i2b2Domain);
				tok.setCreatedDate(new Date());
				tok.setExpiryDate(DateUtils.addYears(new Date(), 1000));
			
				logger.info("Demo token does not exist; Hence creating .."
						+ tok.toString());
				em.persist(tok);
				logger.info("Persisted " + tok.toString());
			}
			logger.trace("returning:" + tok.toString());
			return tok;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new EJBException(ex.getMessage());
		}
	}

	/*
	 * creates accessToken and deleted the corresponding authToken
	 */
	public AccessToken createAccessTokenAndDeleteAuthToken(String authCode,
			String accessCode, String resourceUserId, String i2b2Token,
			String i2b2Project, String clientId, String scope) {
		try {
			AccessToken tok = new AccessToken();
			tok.setTokenString(accessCode);
			tok.setResourceUserId(resourceUserId);
			tok.setI2b2Token(i2b2Token);
			tok.setI2b2Project(i2b2Project);
			tok.setClientId(clientId);
			tok.setScope(scope);
			tok.setCreatedDate(new Date());
			tok.setExpiryDate(DateUtils.addMinutes(new Date(), 30));
			
			
			logger.info("Created .." + tok.toString());
			//em.getTransaction().begin();
			em.persist(tok);

			AuthToken authTok = em.find(AuthToken.class, authCode);
			if (authTok==null) throw new RuntimeException("auth Tok was not found");
			logger.info("Removing authTok " );
			em.remove(authTok);
			
			//em.getTransaction().commit();
			
			logger.info("Persisted " + tok.toString());
			return tok;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			em.getTransaction().rollback();
			throw new EJBException(ex.getMessage());
		}
	}

	public List<AccessToken> getAuthTokens() {
		try {
			List<AccessToken> tokens = em.createQuery("from AccessToken")
					.getResultList();
			em.getTransaction().commit();
			return tokens;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new EJBException(e.getMessage());
		}
	}

	public void removeAccessToken(AccessToken authToken) {
		try {
			em.getTransaction().begin();
			em.remove(authToken);
			em.getTransaction().commit();
		} catch (Exception ex) {
			em.getTransaction().rollback();
			logger.error(ex.getMessage(), ex);
			throw new EJBException(ex.getMessage());
		}
	}

	public AccessToken find(String accessCode) {
		try {
			em.getTransaction().begin();
			logger.trace("find accesstok with id:"+accessCode);
			AccessToken tok = em.find(AccessToken.class, accessCode);
			if (tok != null) {
				logger.info("returning :" + tok);
			} else {
				logger.info("NOT found");
			}
			em.getTransaction().commit();
			return tok;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			em.getTransaction().rollback();
			throw new EJBException(ex.getMessage());
		}

	}

	public void listAccessTokens() {
		try {
			em.getTransaction().begin();
			@SuppressWarnings("unchecked")
			List<AccessToken> list = em.createQuery("from AccessToken")
					.getResultList();
			for (Iterator<AccessToken> iterator = list.iterator(); iterator
					.hasNext();) {
				AccessToken a = (AccessToken) iterator.next();
				logger.info(a.toString());
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			em.getTransaction().rollback();
		}
	}

	

}