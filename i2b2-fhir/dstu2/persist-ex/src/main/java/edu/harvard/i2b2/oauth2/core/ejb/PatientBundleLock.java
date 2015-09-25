package edu.harvard.i2b2.oauth2.core.ejb;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remove;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
@Startup
public class PatientBundleLock {
	static Logger logger = LoggerFactory.getLogger(PatientBundleLock.class);
	static HashMap<String,Bundle> patientBundleHm=new HashMap<String,Bundle>();
	
	@PostConstruct
	public void init(){
		patientBundleHm=new HashMap<String,Bundle>();
	}
	
	@Lock( LockType.READ)
	public Bundle get(String id){
		logger.info("getting:"+id);
		return patientBundleHm.get(id);
	}
	
	@Lock( LockType.WRITE)
	public void put(String id,Bundle b){
		logger.info("putting:"+id+"=>"+b);
		patientBundleHm.put(id,b);
	}
	
	@Remove
	public void remove(){
		patientBundleHm=null;
	}
	
}
