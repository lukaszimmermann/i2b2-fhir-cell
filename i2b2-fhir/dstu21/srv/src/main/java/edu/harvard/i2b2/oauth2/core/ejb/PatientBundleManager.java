package edu.harvard.i2b2.oauth2.core.ejb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.hl7.fhir.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ranges.RangeException;

import edu.harvard.i2b2.fhir.DiagnosticReportGenerator;
import edu.harvard.i2b2.fhir.FhirEnrich;
import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.I2b2UtilByCategory;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.ObservationCategoryGenerator;
import edu.harvard.i2b2.fhir.core.CoreConfig;
import edu.harvard.i2b2.fhir.server.ConfigParameter;
import edu.harvard.i2b2.fhir.server.ServerConfigs;
import edu.harvard.i2b2.fhir.server.ws.FhirServerException;
import edu.harvard.i2b2.oauth2.core.entity.AccessToken;

@Stateful
public class PatientBundleManager {
	static Logger logger = LoggerFactory.getLogger(PatientBundleManager.class);
	int timeOutInSecs;// will time out after
	Date callReceiptDt;
	Date timeOutDt;

	@EJB
	PatientBundleService service;

	@EJB
	BundleStatus status;

	@Inject
	ServerConfigs serverConfig;

	@PostConstruct
	void init() {
		timeOutInSecs = Integer.parseInt(serverConfig.GetString(ConfigParameter.patientBundleTimeOut));
	}
	// TODO check of the tok has a project which has the given pid
	// check if scope allows access to the patient

	public Bundle getPatientBundle(AccessToken tok, String pid) throws FhirServerException {
		callReceiptDt = new Date();
		timeOutDt = ((Date) callReceiptDt.clone());
		timeOutDt.setSeconds(callReceiptDt.getSeconds() + timeOutInSecs);
		if (status.isComplete(pid)) {
			return getPatientBundleLocking(pid);
		}

		if (!(status.isProcessing(pid) || status.isComplete(pid))) {
			fetchPatientBundle(tok, pid);
		}

		while (status.isProcessing(pid)) {
			logger.info("waiting on complete status");
			try {
				Date now = new Date();
				if (now.after(timeOutDt)) {
					throw new InterruptedException(
							"Waiting time execeed patientBundleTimeOut parameter of:" + timeOutInSecs);
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new FhirServerException(e.getMessage(), e);
			}
		}
		if (status.isFailed(pid)) {
			throw new FhirServerException("Processing has Failed");
		}
		return getPatientBundle(tok, pid);
	}

	private void fetchPatientBundle(AccessToken tok, String pid) throws FhirServerException {
		status.markProcessing(pid);
		Bundle b = new Bundle();
		try {
			if (tok == null)
				logger.error("AccessToken is null");
			ServerConfigs sConfig = new ServerConfigs();
			logger.trace("fetching PDO for pid:" + pid + " and tok" + tok + " for categories:"
					+ sConfig.GetString(ConfigParameter.resourceCategoriesList));

			HashMap<String, String> map = new HashMap<String, String>();

			for (String cat : Arrays.asList(sConfig.GetString(ConfigParameter.resourceCategoriesList).split("-"))) {
				switch (cat) {
				case "medications":
					map.put("medications", sConfig.GetString(ConfigParameter.medicationPath));
					break;
				case "labs":
					map.put("labs", sConfig.GetString(ConfigParameter.labsPath));
					break;
				case "diagnoses":
					map.put("diagnoses", sConfig.GetString(ConfigParameter.diagnosesPath));
					break;
				case "reports":
					map.put("reports", sConfig.GetString(ConfigParameter.reportsPath));
					break;
				}
			}
			b = I2b2UtilByCategory.getAllDataForAPatientAsFhirBundle(tok.getResourceUserId(), tok.getI2b2Token(),
					tok.getI2b2Url(), tok.getI2b2Domain(), tok.getI2b2Project(), pid, map,
					serverConfig.GetString(ConfigParameter.ontologyType));

			if (sConfig.GetString(ConfigParameter.enrichEnabled).equals("true")) {
				FhirEnrich.enrich(b);
				b=ObservationCategoryGenerator.addObservationCategoryToObservationBundle(b);
			}
			
			if(sConfig.GetString(ConfigParameter.createDiagnosticReportsFromObservations).equals("true")) {
				logger.trace("createDiagnosticReportsFromObservations");
				b=DiagnosticReportGenerator.generateAndAddDiagnosticReports(b);
			}else{
				logger.trace("createDiagnosticReportsFromObservations is false");
			}
			//	logger.trace("bundlexML:"+JAXBUtil.toXml(b));
			logger.trace("fetched bundle of size:" + b.getEntry().size());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			status.markFailed(pid);
			throw new FhirServerException(e);
		}
		service.put(pid, b);
		status.markComplete(pid);
	}

	
	
	private Bundle getPatientBundleLocking(String pid) {
		Bundle b = service.get(pid);
		try {
			logger.trace("returning Bundle:" + JAXBUtil.toXml(b));
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		}
		return b;
	}

	public void resetCache() {
		service.deleteAll();
		status.resetAll();
	}
}