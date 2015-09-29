package edu.harvard.i2b2.oauth2.core.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remove;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.oauth2.core.entity.PatientBundleRecord;

@Singleton
@Startup
public class PatientBundleService {
	static Logger logger = LoggerFactory.getLogger(PatientBundleService.class);
	// static HashMap<String,Bundle> patientBundleHm=new
	// HashMap<String,Bundle>();

	@PersistenceContext
	EntityManager em;

	@PostConstruct
	public void init() {
		// patientBundleHm=new HashMap<String,Bundle>();
	}

	@Lock(LockType.READ)
	public Bundle get(String patientId) {
		logger.info("getting: bundle for pid:" + patientId);

		// return patientBundleHm.get(id);
		return find(patientId);
	}

	private Bundle find(String patientId) {
		PatientBundleRecord r = em.find(PatientBundleRecord.class, patientId);
		if(r==null) return null;
		String bundleXml = r.getBundleXml();
		Bundle b = null;
		try {
			b = JAXBUtil.fromXml(bundleXml, Bundle.class);
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		}
		logger.trace("found PatientBundleRecord:" + r.toString());
		return b;
	}

	@Lock(LockType.WRITE)
	public void put(String patientId, Bundle b) {
		logger.info("putting:" + patientId + "=>" + b);
		createPatientRecord(patientId, b);
		// patientBundleHm.put(id,b);

	}
	
	@Remove
	public void remove(){
		 //patientBundleHm=null;
	}

	private PatientBundleRecord createPatientRecord(String patientId, Bundle b){
			
		PatientBundleRecord r = new PatientBundleRecord();
		r.setPatientId(patientId);
		try {
			r.setBundleXml(JAXBUtil.toXml(b));
		} catch (JAXBException e) {
			logger.error(e.getMessage(),e);
		}
		em.persist(r);
		logger.trace("created PatientBundleRecord:" + r.toString());
		return r;
	}

}
