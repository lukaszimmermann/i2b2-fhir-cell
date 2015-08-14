package edu.harvard.i2b2.fhirserver.ejb;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
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
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhirserver.entity.SessionBundle;

//@Stateful
@Singleton
@Startup
public class SessionBundleBean {
	static Logger logger = LoggerFactory.getLogger(SessionBundleBean.class);

	private EntityManager em;

	@PostConstruct
	public void init() {
		try {
			EntityManagerFactory factory = Persistence
					.createEntityManagerFactory("testPer");
			em = factory.createEntityManager();

			// Random r = new Random();
			// createAuthToken("clientId232" + r.nextInt());
			totalSessionBundle();
			// deleteOldData();
			listSessionBundles();
		} catch (Exception ex) {
			logger.error("", ex);
		}
	}

	public void listSessionBundles() {
		try {
			em.getTransaction().begin();
			@SuppressWarnings("unchecked")
			List<SessionBundle> sbl = em.createQuery("from SessionBundle")
					.getResultList();
			for (Iterator<SessionBundle> iterator = sbl.iterator(); iterator
					.hasNext();) {
				SessionBundle sb = (SessionBundle) iterator.next();
				logger.info(sb.getId() + "\n" + sb.getBundleXml());
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}

	/*
	 * @Transactional public void deleteOldData() { em.joinTransaction();
	 * em.createNativeQuery("delete from SessionBundle where id!='dummy';")
	 * .executeUpdate(); logger.debug("deleted old rows from session bundle"); }
	 */

	public SessionBundle createSessionBundle(String sessionId, Bundle s) {
		try {
			SessionBundle sb = new SessionBundle(sessionId, s);
			logger.info("Created sb.." + sb.toString());
			em.getTransaction().begin();
			em.persist(sb);
			// em.flush();
			em.getTransaction().commit();

			// em.merge(sb);

			logger.info("Persisted sb" + sb.toString());
			totalSessionBundle();
			return sb;
		} catch (Exception ex) {
			em.getTransaction().rollback();
			logger.error("", ex);
			throw new EJBException(ex.getMessage());
		}
	}
	
	

	public SessionBundle update1SessionBundle(SessionBundle sb) {
		try {
			logger.info("updating sb" + sb.toString());
				em.getTransaction().begin();
				em.merge(sb);
				em.getTransaction().commit();
			logger.info("Merged sb" + sb.toString());
			// "\ntotal"+totalSessionBundle());
			return sb;
		} catch (Exception ex) {
			em.getTransaction().rollback();
			logger.error("", ex);
			throw new EJBException(ex.getMessage());
		}
	}

	public void removeSessionBundle(SessionBundle sb) {
		try {
			em.getTransaction().begin();
			em.remove(sb);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new EJBException(e.getMessage());
		}
	}

	/*
	 * public void removeSessionBundle(String sessionId) { try {
	 * 
	 * SessionBundle sb = this.sessionBundleBySessionId(sessionId);
	 * 
	 * em.remove(sb); logger.info("removed SessionBundle:" + sessionId);
	 * 
	 * } catch (Exception e) { throw new EJBException(e.getMessage()); } }
	 */
	// initializes if not present
	public SessionBundle sessionBundleBySessionId(String sessionId) {
		try {

			// Query query =
			// em.createQuery("SELECT b FROM SESSIONBUNDLE b WHERE b.id = :id");
			// query.setParameter("id", sessionId);
			// List sessionBundles = query.getResultList();
			em.getTransaction().begin();

			SessionBundle sb = em.find(SessionBundle.class, sessionId);
			em.getTransaction().commit();
			if (sb == null) {
				sb = new SessionBundle(sessionId, new Bundle());
				em.getTransaction().begin();
				em.persist(sb);
				em.getTransaction().commit();
			}
			logger.trace("returning sb:" + sb.toString());
			return sb;
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("", e);
			throw new EJBException(e);
		}

	}

	public int totalSessionBundle() {
		try {
			em.getTransaction().begin();
			List<SessionBundle> b = em.createQuery(
					"select a from SessionBundle a").getResultList();
			em.getTransaction().commit();
			logger.trace("total sessionBundles in db:" + b.size());
			return b.size();
		} catch (Exception e) {
			em.getTransaction().rollback();
			logger.error("", e);
			throw new EJBException(e);
		}

	}

	public Bundle bundleBySessionId(String sessionId) {
		try {
			SessionBundle sessionBundle = null;
			try {
				em.getTransaction().begin();
				sessionBundle = em.find(SessionBundle.class, sessionId);
				em.getTransaction().commit();
			} catch (Exception e) {
				em.getTransaction().rollback();
				throw e;
			}
			if (sessionBundle == null)
				return new Bundle();
			else
				return sessionBundle.getBundle();
		} catch (Exception e) {

			logger.error("", e);
			throw new EJBException(e);
		}

	}
}