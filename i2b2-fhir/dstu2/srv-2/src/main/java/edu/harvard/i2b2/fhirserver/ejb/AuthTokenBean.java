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

import edu.harvard.i2b2.fhirserver.entity.AuthToken;

//@Stateful
@Singleton
@Startup
public class AuthTokenBean {
	static Logger logger = LoggerFactory.getLogger(AuthTokenBean.class);
	// @PersistenceContext
	private EntityManager em;

	@PostConstruct
	public void init() {
		try {
			EntityManagerFactory factory = Persistence
					.createEntityManagerFactory("testPer");
			em = factory.createEntityManager();
			Random r = new Random();
			createAuthToken("!null"+r, "!null"+r,
					"authorizationCode"+r, "redirectURI"+r, "clientId"+r,"null"+r,"!null"+r);
			logger.info("total:"+totalCount());
			// createAuthToken("clientId232" + r.nextInt());
		} catch (Exception ex) {
			logger.error("", ex);
		}
	}

	public void createAuthToken(String resourceUserId, String i2b2Token,
			String authorizationCode, String clientRedirectUri,
			String clientId, String state, String scope) {
		try {
			AuthToken tok = new AuthToken(resourceUserId, i2b2Token,
					authorizationCode, clientRedirectUri, clientId, state,
					scope);
			logger.info("Created authToken.." + tok.toString());
			//em.getTransaction();
			em.persist(tok);
			//em.flush();
			logger.info("Persisted authToken" + tok.toString() );
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new EJBException(ex.getMessage());
		}
	}

	public List<AuthToken> getAuthTokens() {
		List<AuthToken> tokens = (List<AuthToken>) em.createNamedQuery(
				"findAllAuthTokens").getResultList();
		return tokens;
	}

	public int countAllItems() {
		int count = 0;
		try {
			count = em.createNamedQuery("findAllAuthTokens").getResultList()
					.size();
		} catch (Exception e) {
			throw new EJBException(e.getMessage());
		}
		return count;
	}

	public void removeAuthToken(AuthToken authToken) {
		try {
			em.remove(authToken);
		} catch (Exception e) {
			throw new EJBException(e.getMessage());
		}
	}

	public AuthToken authTokenByAuthorizationCode(String authCode) {
		try {
			List tokens = em
					.createQuery(
							"select a from AuthToken a where AuthorizationCode = :ac ")
					.setParameter("ac", authCode).getResultList();
			if (tokens.size() > 0) {
				AuthToken atok = (AuthToken) tokens.get(0);
				logger.trace("returning:" + atok);
				return atok;
			} else {
				logger.trace("returning:null");
				return null;
			}
		} catch (Exception e) {
			throw new EJBException(e);
		}

	}

	public int totalCount() {
		try {
			List tokens = em.createQuery("select a from AuthToken a ")
					.getResultList();
			return tokens.size();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new EJBException(e);
		}
	}

	public AuthToken authTokenByClientId(String clientId) {
		try {
			logger.info("search by clientid:"+clientId);
			List tokens = em
					.createQuery(
							"select a from AuthToken a where clientId = :ac ")
					.setParameter("ac", clientId).getResultList();
			if (tokens.size() > 0) {
				AuthToken atok = (AuthToken) tokens.get(0);
				logger.trace("returning:" + atok);
				return atok;
			} else {
				logger.trace("returning:null");
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new EJBException(e);
		}
	}

	//@PreDestroy
	//@Transactional
	public void dropTable() {
		try {
			EntityManagerFactory factory = Persistence
					.createEntityManagerFactory("testPer");
			em.joinTransaction();
			em = factory.createEntityManager();
			em.createNativeQuery("Drop table AuthToken;").executeUpdate();
			em.createNativeQuery("shutdown;").executeUpdate();
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			logger.error("", ex);
		}
	}

}