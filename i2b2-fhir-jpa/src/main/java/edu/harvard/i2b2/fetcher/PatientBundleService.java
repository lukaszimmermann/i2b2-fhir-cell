package edu.harvard.i2b2.fetcher;


import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remove;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.model.dstu2.resource.Bundle;


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

	
	@Lock
	public void put(String patientId, ca.uhn.fhir.model.dstu2.resource.Bundle b) {
		logger.info("putting:" + patientId + "=>" + b);
		//createPatientRecord(patientId, b);
		//XXXX
	}

	@Remove
	public void remove(PatientBundleRecord r) {
		em.remove(em.contains(r) ? r : em.merge(r));
		logger.info("removed client");
	}


	public Bundle get(String pid) {
		// TODO Auto-generated method stub
		return null;
	}

	

	

	
}
