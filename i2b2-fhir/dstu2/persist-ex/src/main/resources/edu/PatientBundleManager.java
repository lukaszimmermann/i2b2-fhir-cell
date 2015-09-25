package edu.harvard.i2b2.oauth2.core.ejb;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remove;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhirserver.ws.I2b2FhirWS;

@Singleton
@Startup
public class PatientBundleManager {
	static Logger logger = LoggerFactory.getLogger(PatientBundleManager.class);
	static HashMap<String,String> patientBundleHm=new HashMap<String,String>();
	
	@PostConstruct
	public void init(){
		patientBundleHm=new HashMap<String,String>();
	}
	
	@Lock( LockType.READ)
	public String get(String id){
		logger.info("getting:"+id);
		return patientBundleHm.get(id);
	}
	
	@Lock( LockType.WRITE)
	public String put(String id,String xml){
		logger.info("putting:"+id+"=>"+xml);
		return patientBundleHm.put(id,xml);
	}
	
	@Remove
	public void remove(){
		patientBundleHm=null;
	}
	
}
