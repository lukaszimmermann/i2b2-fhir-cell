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
			createAuthToken("clientId232" + r.nextInt());
		} catch (Exception ex) {
			logger.error("", ex);
		}
	}

	public void createAuthToken(String clientId) {
		try {
			AuthToken tok = new AuthToken(clientId);
			logger.info("Created authToken.."+ tok.toString());
			em.persist(tok);
			logger.info("Persisted authToken"+ tok.toString());

		} catch (Exception ex) {
			logger.error("", ex);
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

	
	public AuthToken authTokenByTokenString(String tokString) {
		try {
			List tokens = em.createNamedQuery("findAuthTokenByTokenString")
					.setParameter("tokenString", tokString).getResultList();
			return (AuthToken) tokens.get(0);
		} catch (Exception e) {
			throw new EJBException(e);
		}

	}
	
	@PreDestroy
	@Transactional
	public void dropTable() {
		try {
			EntityManagerFactory factory = Persistence
					.createEntityManagerFactory("testPer");
			em.joinTransaction();
			em = factory.createEntityManager();
			em.createNativeQuery("Drop table AuthToken;").executeUpdate();
			em.createNativeQuery("shutdown;").executeUpdate();
		} catch (Exception ex) {
			logger.error("", ex);
		}
	}
}