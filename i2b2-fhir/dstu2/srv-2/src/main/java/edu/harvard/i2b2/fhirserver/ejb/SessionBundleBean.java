package edu.harvard.i2b2.fhirserver.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedQuery;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhirserver.entity.AuthToken;
import edu.harvard.i2b2.fhirserver.entity.SessionBundle;

//@Stateful
@Singleton
@Startup
public class SessionBundleBean {
	static Logger logger = LoggerFactory.getLogger(SessionBundleBean.class);
	// @PersistenceContext
	private EntityManager em;

	@PostConstruct
	public void init() {
		try {
			EntityManagerFactory factory = Persistence
					.createEntityManagerFactory("testPer");
			em = factory.createEntityManager();
			// Random r = new Random();
			// createAuthToken("clientId232" + r.nextInt());
		} catch (Exception ex) {
			logger.error("", ex);
		}
	}

	public SessionBundle createSessionBundle(String sessionId, Bundle s) {
		try {
			SessionBundle sb = new SessionBundle(sessionId, s);
			logger.info("Created sb.." + sb.toString());
			em.persist(sb);
			//em.merge(sb);
			
			//em.flush(); 
			logger.info("Persisted sb" + sb.toString());
			return sb;
		} catch (Exception ex) {
			logger.error("", ex);
			throw new EJBException(ex.getMessage());
		}
	}
	
	public SessionBundle updateSessionBundle(String sessionId,Bundle b) {
		try {
			SessionBundle sb=sessionBundleBySessionId(sessionId);
			if(b==null) b=new Bundle();
			sb.setBundleXml(JAXBUtil.toXml(b));
			em.merge(sb);
			//em.flush(); 
			logger.info("Merged sb" + sb.toString());
			return sb;
		} catch (Exception ex) {
			logger.error("", ex);
			throw new EJBException(ex.getMessage());
		}
	}

	public void removeSessionBundle(SessionBundle sb) {
		try {
			em.remove(sb);
		} catch (Exception e) {
			throw new EJBException(e.getMessage());
		}
	}

	// initializes if not present
	public SessionBundle sessionBundleBySessionId(String sessionId) {
		try {
			SessionBundle b = null;
			// Query query =
			// em.createQuery("SELECT b FROM SESSIONBUNDLE b WHERE b.id = :id");
			// query.setParameter("id", sessionId);
			// List sessionBundles = query.getResultList();
			b = em.find(SessionBundle.class, sessionId);
			if (b==null) {
				b = createSessionBundle(sessionId, new Bundle());
			}
			logger.trace("returning sb:"+b.toString());
			return b;
		} catch (Exception e) {
			throw new EJBException(e);
		}

	}

	public Bundle bundleBySessionId(String sessionId) {
		try {

			SessionBundle sessionBundle = sessionBundleBySessionId(sessionId);
			if (sessionBundle == null)
				return new Bundle();
			else
				return sessionBundle.getBundle();
		} catch (Exception e) {
			throw new EJBException(e);
		}

	}
}