package edu.harvard.i2b2.oauth2.core.ejb;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.hl7.fhir.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ranges.RangeException;

import edu.harvard.i2b2.fhir.FhirEnrich;
import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.I2b2UtilByCategory;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.core.CoreConfig;
import edu.harvard.i2b2.fhir.server.ws.FhirServerException;
import edu.harvard.i2b2.oauth2.core.entity.AccessToken;


@Stateful
public class PatientBundleManager {
	static Logger logger = LoggerFactory.getLogger(PatientBundleManager.class);

	@EJB
	PatientBundleService service;

	@EJB
	BundleStatus status;

	//TODO check of the tok has a project which has the given pid
	//check if scope allows access to the patient
	
	public Bundle getPatientBundle(AccessToken tok, String pid) throws FhirServerException {

		if (status.isComplete(pid)) {
			return getPatientBundleLocking(pid);
		}

		if (!(status.isProcessing(pid) || status.isComplete(pid))) {
			fetchPatientBundle(tok, pid);
		}

		while (status.isProcessing(pid)) {
			logger.info("waiting on complete status");
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				logger.error(e.getMessage(),e);
			}
		}
		if(status.isFailed(pid)){
			throw new FhirServerException("Processing has Failed");
		}
		return getPatientBundle(tok, pid);
	}

	private void fetchPatientBundle(AccessToken tok, String pid) throws FhirServerException {
		status.markProcessing(pid);
		Bundle b= new Bundle();
		try{
			if(tok==null) logger.error("AccessToken is null");
			logger.trace("fetching PDO for pid:"+pid+" and tok"+tok);
			//HashMap<String,String>map=null;
			HashMap<String,String>map=new HashMap<String,String>();
			map.put("medications",CoreConfig.getMedicationPath());
			map.put("labs",CoreConfig.getLabsPath());
			map.put("diagnoses",CoreConfig.getDiagnosesPath());
			b=I2b2UtilByCategory.getAllDataForAPatientAsFhirBundle(tok.getResourceUserId(), tok.getI2b2Token(), tok.getI2b2Url(),tok.getI2b2Domain(), tok.getI2b2Project(), pid,map);
			
			FhirEnrich.enrich(b);
			logger.trace("fetched bundle of size:"+b.getEntry().size());
					
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			status.markFailed(pid);
			throw new FhirServerException(e);
		}
		service.put(pid, b);
		status.markComplete(pid);
	}

	private Bundle getPatientBundleLocking(String pid) {
			Bundle b=service.get(pid);
			try{
				logger.trace("returning Bundle:"+JAXBUtil.toXml(b));
			}catch(JAXBException e){
				logger.error(e.getMessage(),e);
			}
			return b;
	}

	
}
