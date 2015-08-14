package edu.harvard.i2b2.fhirserver.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhirserver.entity.AccessToken;

//@Stateful
@Singleton
@Startup
public class AccessTokenBean {
	static Logger logger = LoggerFactory.getLogger(AccessTokenBean.class);
	// @PersistenceContext
	private EntityManager em;

	@PostConstruct
	public void init() {
		try {
			EntityManagerFactory factory = Persistence
					.createEntityManagerFactory("testPer");
			em = factory.createEntityManager();
			Random r = new Random();
			// createAccessToken("clientId232" + r.nextInt());
		} catch (Exception ex) {
			
			logger.error("", ex);
		}
	}

	public void createAccessToken(String resourceUserId, String i2b2Token,
			String authorizationCode, String clientRedirectUri, String clientId) {
		try {
			AccessToken tok = new AccessToken(resourceUserId, i2b2Token,
					authorizationCode, clientRedirectUri, clientId);
			logger.info("Created authToken.." + tok.toString());
			em.persist(tok);
			logger.info("Persisted authToken" + tok.toString());

		} catch (Exception ex) {
			em.getTransaction().rollback();
			logger.error("", ex);
			throw new EJBException(ex.getMessage());
		}
	}

	public List<AccessToken> getAccessTokens() {
		List<AccessToken> tokens = (List<AccessToken>) em.createNamedQuery(
				"findAllAccessTokens").getResultList();
		return tokens;
	}

	public int countAllItems() {
		int count = 0;
		try {
			count = em.createNamedQuery("findAllAccessTokens").getResultList()
					.size();
		} catch (Exception e) {
			throw new EJBException(e.getMessage());
		}
		return count;
	}

	public void removeAccessToken(AccessToken authToken) {
		try {
			em.remove(authToken);
		} catch (Exception e) {
			throw new EJBException(e.getMessage());
		}
	}

	public AccessToken authTokenByAuthorizationCode(String authCode) {
		try {
			List tokens = em
					.createQuery(
							"select a from AccessToken where AuthorizationCode = :ac ")
					.setParameter("ac", authCode).getResultList();
			if (tokens.size() > 0) {
				return (AccessToken) tokens.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new EJBException(e);
		}

	}
	
/*
	@PreDestroy
	@Transactional
	public void dropTable() {
		try {
			EntityManagerFactory factory = Persistence
					.createEntityManagerFactory("testPer");
			em.joinTransaction();
			em = factory.createEntityManager();
			em.createNativeQuery("Drop table AccessToken;").executeUpdate();
			em.createNativeQuery("shutdown;").executeUpdate();
		} catch (Exception ex) {
			em.getTransaction().rollback();
			logger.error("", ex);
		}
	}
	*/
}