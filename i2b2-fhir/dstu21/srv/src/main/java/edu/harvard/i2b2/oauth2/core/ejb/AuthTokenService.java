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

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.server.ConfigParameter;
import edu.harvard.i2b2.fhir.server.ServerConfigs;
import edu.harvard.i2b2.oauth2.core.entity.AuthToken;


//@Stateful
@Singleton
//@Startup
public class AuthTokenService {
	static Logger logger = LoggerFactory.getLogger(AuthTokenService.class);
	@PersistenceContext
	private EntityManager em;

	@Inject
	ServerConfigs serverConfig;
	
	//@PostConstruct
	public void setup() {
		try {
			//EntityManagerFactory factory = Persistence
					//.createEntityManagerFactory("testPer");
			//em = factory.createEntityManager();
			Random r = new Random();
			String rs = Integer.toString(r.nextInt());
			createAuthToken(rs, rs, rs, rs, rs, rs, rs, rs,rs);
			logger.info("total:" + totalCount());
			// createAuthToken("clientId232" + r.nextInt());
		} catch (Exception ex) {
			logger.error("", ex);
		}
	}

	public AuthToken createAuthToken(String authCode, String resourceUserId,
			String i2b2Token, String clientRedirectUri, String clientId,
			String state, String scope, String i2b2Project, String patientId) {
		try {
			AuthToken tok = new AuthToken();
			tok.setAuthorizationCode(authCode);
			tok.setResourceUserId(resourceUserId);
			tok.setI2b2Token(i2b2Token);
			tok.setClientRedirectUri(clientRedirectUri);
			tok.setClientId(clientId);
			tok.setState(state);
			tok.setScope(scope);
			tok.setI2b2Project(i2b2Project);
			tok.setCreatedDate(new Date());
			tok.setExpiryDate(DateUtils.addMinutes(new Date(), 30));
			tok.setI2b2Url(serverConfig.GetString(ConfigParameter.i2b2Url));
			tok.setI2b2Domain(serverConfig.GetString(ConfigParameter.i2b2Domain));
			tok.setPatient(patientId);
			logger.info("Created authToken.." + tok.toString());
			//em.getTransaction().begin();
			em.persist(tok);
			//em.getTransaction().commit();
			logger.info("Persisted authToken" + tok.toString());
			return tok;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			//em.getTransaction().rollback();
			throw new EJBException(ex.getMessage());
		}
	}

	public List<AuthToken> getAuthTokens() {
		try {
			//em.getTransaction().begin();
			List<AuthToken> tokens = em.createQuery("from AuthToken")
					.getResultList();
			//em.getTransaction().commit();
			return tokens;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//em.getTransaction().rollback();
			throw new EJBException(e.getMessage());
		}
	}

	public void removeAuthToken(AuthToken authToken) {
		try {
			//em.getTransaction().begin();
			logger.info("deleting from db:" + authToken);
			em.remove(authToken);

			//em.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//em.getTransaction().rollback();
			throw new EJBException(e.getMessage());
		}
	}

	public AuthToken find(String authCode) {
		try {
			if (authCode==null) throw new IllegalArgumentException("search parameter:auth code is null");
			//em.getTransaction().begin();
			AuthToken tok = em.find(AuthToken.class, authCode);
			// .createQuery(
			// "select a from AuthToken a where AuthorizationCode = :ac ")
			// .setParameter("ac", authCode).getResultList();
			//em.getTransaction().commit();
			logger.info("found token:" + tok);
			return tok;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//em.getTransaction().rollback();
			throw new EJBException(e.getMessage());
		}

	}

	public void listAuthTokens() {
		try {
			//em.getTransaction().begin();
			@SuppressWarnings("unchecked")
			List<AuthToken> list = em.createQuery("from AuthToken")
					.getResultList();
			for (Iterator<AuthToken> iterator = list.iterator(); iterator
					.hasNext();) {
				AuthToken a = (AuthToken) iterator.next();
				logger.info(a.toString());
			}
			//em.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//em.getTransaction().rollback();
		}
	}

	public int totalCount() {
		int count = getAuthTokens().size();
		logger.trace("AuthToken count:" + count);
		return count;
	}

	public AuthToken findByClientId(String clientId) {
		try {
			AuthToken tok = null;
			//em.getTransaction().begin();
			List<AuthToken> list = em
					.createQuery(
							"select a from AuthToken a where clientId = :ac ")
					.setParameter("ac", clientId).getResultList();
			//em.getTransaction().commit();
			if (list.size() > 0)
				tok = list.get(0);
			return tok;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//em.getTransaction().rollback();
			throw new EJBException(e.getMessage());
		}

	}

	//@PreDestroy
	// @Transactional
	public void shutdownDb() {
		try {
			em.createQuery("SHUTDOWN").executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//em.getTransaction().rollback();
			throw new EJBException(e.getMessage());
		}

	}

	/*
	 * public void dropTable() { try { EntityManagerFactory factory =
	 * Persistence .createEntityManagerFactory("testPer"); em.joinTransaction();
	 * em = factory.createEntityManager();
	 * em.createNativeQuery("Drop table AuthToken;").executeUpdate();
	 * em.createNativeQuery("shutdown;").executeUpdate(); } catch (Exception ex)
	 * { logger.error(ex.getMessage(),ex); logger.error("", ex); } }
	 */
}