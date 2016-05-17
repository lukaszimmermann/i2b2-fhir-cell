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

import org.hl7.fhir.Bundle;
import org.hl7.fhir.BundleEntry;
import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import edu.harvard.i2b2.fhir.FhirUtil;




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
	public void put(String patientId, Bundle b) {
		logger.info("putting:" + patientId + "=>" + b);
		Resource response=null;
		//createPatientRecord(patientId, b);
		//XXXX
		if(1==1) return;
		    RestTemplate restTemplate = new RestTemplate();
		    for(Resource r:FhirUtil.getResourceListFromBundle(b)){
		    	Class rc=FhirUtil.getResourceClass(r);
		    	 restTemplate.put("http:////localhost:8080//hapi-fhir-jpaserver-example//baseDstu2//"+rc.getSimpleName()+"//"+r.getId().getValue(), b);
		        logger.info(">>>>>>DID put");
		    	 //logger.info(response.toString());
			}
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
