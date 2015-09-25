package edu.harvard.i2b2.oauth2.core.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.oauth2.core.entity.AccessToken;


@Stateful
public class PatientBundleService {
	static Logger logger = LoggerFactory.getLogger(PatientBundleService.class);

	@EJB
	PatientBundleLock mgr;

	@EJB
	PatientBundleStatus status;

	// public getPDO(String pid)

	// putPDO(String pid)

	public Bundle getPatientBundle(AccessToken tok, String pid)
			throws InterruptedException {

		if (status.isComplete(pid)) {
			return getPatientBundleLocking(pid);
		}

		if (!(status.isProcessing(pid) || status.isComplete(pid))) {
			fetchPatientBundle(tok, pid);
		}

		while (status.isProcessing(pid)) {
			logger.info("waiting on complete status");
			Thread.sleep(1000);
		}
		return getPatientBundle(tok, pid);
	}

	private void fetchPatientBundle(AccessToken tok, String pid) {
		status.markProcessing(pid);
		try{
			String i2b2Xml = I2b2Util.getAllDataForAPatient(tok.getResourceUserId(), tok.getI2b2Token(), tok.getI2b2Url(),tok.getI2b2Domain(), tok.getI2b2Project(), pid);
			Bundle b=I2b2Util.getAllDataForAPatientAsFhirBundle(i2b2Xml);
			mgr.put(pid, b);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		status.markComplete(pid);
	}

	private Bundle getPatientBundleLocking(String pid) {
			return mgr.get(pid);
	}

	
}
