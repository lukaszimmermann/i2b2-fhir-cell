package edu.harvard.i2b2.fhirserver.ejb;

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

import edu.harvard.i2b2.fhirserver.entity.AccessToken;
import edu.harvard.i2b2.fhirserver.entity.AuthToken;

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

	public AccessToken createAccessToken(String authCode,String resourceUserId, String i2b2Token,
			String i2b2Project, String clientId,String scope) {
		try {
			AccessToken tok = new AccessToken();
			tok.setTokenString(authCode);
			tok.setResourceUserId(resourceUserId);
			tok.setI2b2Token(i2b2Token);
			tok.setI2b2Project(i2b2Project);
			tok.setClientId(clientId);
			tok.setScope(scope);
			tok.setCreatedDate(new Date());
			tok.setExpiryDate(DateUtils.addMinutes(new Date(),30));
			
			logger.info("Created .." + tok.toString());
			em.getTransaction().begin();
			em.persist(tok);
			em.getTransaction().commit();
			logger.info("Persisted " + tok.toString());
			return tok;
		} catch (Exception ex) {
			em.getTransaction().rollback();
			logger.error("", ex);
			throw new EJBException(ex.getMessage());
		}
	}
	
	/*
	 * creates accessToken and deleted the corresponding authToken
	 */
	public AccessToken createAccessTokenAndDeleteAuthToken(String authCode,String accessCode,String resourceUserId, String i2b2Token,
			String i2b2Project, String clientId,String scope) {
		try {
			AccessToken tok = new AccessToken();
			tok.setTokenString(authCode);
			tok.setResourceUserId(resourceUserId);
			tok.setI2b2Token(i2b2Token);
			tok.setI2b2Project(i2b2Project);
			tok.setClientId(clientId);
			tok.setScope(scope);
			tok.setCreatedDate(new Date());
			tok.setExpiryDate(DateUtils.addMinutes(new Date(),30));
			
			logger.info("Created .." + tok.toString());
			em.getTransaction().begin();
			em.persist(tok);
			AuthToken authTok=em.find(AuthToken.class, authCode);
			em.remove(authTok);
			em.getTransaction().commit();
			logger.info("Persisted " + tok.toString());
			return tok;
		} catch (Exception ex) {
			em.getTransaction().rollback();
			logger.error("", ex);
			throw new EJBException(ex.getMessage());
		}
	}

	public List<AccessToken> getAuthTokens() {
		try {
			em.getTransaction().begin();
			List<AccessToken> tokens = em.createQuery("from AccessToken").getResultList();
			em.getTransaction().commit();
			return tokens;
		} catch (Exception e) {
			em.getTransaction().rollback();
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
			AccessToken tok = em.find(AccessToken.class, accessCode);
					//.createQuery(
						//	"select a from AuthToken a where AuthorizationCode = :ac ")
					//.setParameter("ac", authCode).getResultList();
			em.getTransaction().commit();
			return tok;
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error(e.getMessage(), e);
			throw new EJBException(e.getMessage());
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
				logger.info( a.toString());
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
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