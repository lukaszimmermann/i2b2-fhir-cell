package edu.harvard.i2b2.fhirserver.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirEnrich;
import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.WebServiceCall;
import edu.harvard.i2b2.fhirserver.ws.I2b2Helper;

@Stateful
public class PatientBundleService {
	static Logger logger = LoggerFactory.getLogger(PatientBundleService.class);

	@EJB
	PatientBundleManager mgr;

	@EJB
	PatientBundleStatus status;

	// public getPDO(String pid)

	// putPDO(String pid)

	public Bundle getPatientBundle(HttpSession session, String pid)
			throws InterruptedException {

		if (status.isComplete(pid)) {
			return getPatientBundleLocking(pid);
		}

		if (!(status.isProcessing(pid) || status.isComplete(pid))) {
			fetchPatientBundle(session, pid);
		}

		while (status.isProcessing(pid)) {
			logger.info("waiting on complete status");
			Thread.sleep(1000);
		}
		return getPatientBundle(session, pid);
	}

	private void fetchPatientBundle(HttpSession session, String pid) {
		status.markProcessing(pid);

		String xml = getPdo(session, pid);
		mgr.put(pid, xml);
		// fetch PDO from i2b2
		// translate to FhirResource
		// put in db
		status.markComplete(pid);
	}

	private Bundle getPatientBundleLocking(String pid) {
		try {
			return JAXBUtil.fromXml(mgr.get(pid), Bundle.class);
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private static String getPdo(HttpSession session, String patientId) {
		try {
			String requestStr = IOUtils
					.toString(I2b2Helper.class
							.getResourceAsStream("/i2b2query/i2b2RequestAllDataForAPatient.xml"));
			requestStr = I2b2Util.insertSessionParametersInXml(requestStr,
					session);

			if (patientId != null)
				requestStr = requestStr.replaceAll("PATIENTID", patientId);

			String i2b2Url = (String) session.getAttribute("i2b2domainUrl");

			logger.info("fetching from i2b2host...");
			String i2b2XmlResponse = WebServiceCall.run(i2b2Url
					+ "/services/QueryToolService/pdorequest", requestStr);
			logger.trace("got i2b2 response:" + i2b2XmlResponse);
			logger.info("running transformation...");

			Bundle b = FhirUtil
					.convertI2b2ToFhirForAParticularPatient(i2b2XmlResponse);
			FhirEnrich.enrich(b);
			logger.trace("bundle:" + JAXBUtil.toXml(b));
			logger.trace("list size:" + b.getEntry().size());
			logger.info("adding to memory...");
			return JAXBUtil.toXml(b);

		} catch (Exception e) {
			logger.error("ERROR MSG:" + e.getMessage(), e);
		}
		return null;
	}
}
