package edu.harvard.i2b2.fhirserver.ejb;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class PatientBundleManager {
	static HashMap<String,String> patientBundleHm=new HashMap<String,String>();
	
	@PostConstruct
	public void init(){
		patientBundleHm=new HashMap<String,String>();
	}
	public String get(String id){
		return patientBundleHm.get(id);
	}
	
	public String put(String id,String xml){
		return patientBundleHm.put(id,xml);
	}
	
	@Remove
	public void remove(){
		patientBundleHm=null;
	}
	
}
