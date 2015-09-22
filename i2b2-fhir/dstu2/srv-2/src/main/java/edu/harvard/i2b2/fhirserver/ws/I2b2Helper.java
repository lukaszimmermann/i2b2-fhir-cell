/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

package edu.harvard.i2b2.fhirserver.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.hl7.fhir.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import edu.harvard.i2b2.fhir.AuthenticationFailure;
import edu.harvard.i2b2.fhir.FhirEnrich;
import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.WebServiceCall;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhirserver.ejb.PatientBundleService;

public class I2b2Helper {

	static Logger logger = LoggerFactory.getLogger(I2b2Helper.class);

	static Bundle allPatients = null;

	static Bundle initAllPatients(HttpSession session)
			throws AuthenticationFailure, FhirServerException,
			XQueryUtilException, JAXBException, IOException {
		if (session == null) {
			return null;
		}
		if (allPatients == null) {// avoid redundant run
			// if (session.getAttribute("INIT_ALL_PATIENTS") != null)
			// return;

			// MetaResourceDb md = I2b2Helper.getMetaResourceDb(session, sbb);
				allPatients = I2b2Util.getAllPatientsAsFhirBundle(session);
			logger.info("Got ResourceSet of size::"
					+ allPatients.getEntry().size());
			// md.addBundle(b);
			// I2b2Helper.saveMetaResourceDb(session, md, sbb);
		}
		return allPatients;
	}

	private static Bundle getPdo(HttpSession session, String patientId,
			 PatientBundleService service) throws InterruptedException {
			return service.getPatientBundle(session, patientId);
	}

	static String processXquery(String query, String input)
			throws XQueryUtilException {
		logger.trace("will run query:" + query + "\n input" + input);
		return XQueryUtil.processXQuery(query, input);
	}

	static String removeSpace(String input)
			throws ParserConfigurationException, SAXException, IOException {
		// return Utils.getStringFromDocument(Utils.xmltoDOM(input.replaceAll(
		// "(?m)^[ \t]*\r?\n", "")));
		return input.replaceAll("(?m)^[ \t]*\r?\n", "");
		// return input;
	}

	static Bundle parsePatientIdToFetchPDO(HttpServletRequest request,
			HttpSession session,  String resourceName,
			PatientBundleService service) throws XQueryUtilException,
			JAXBException, IOException, AuthenticationFailure,
			FhirServerException, InterruptedException {
		String patientId = I2b2Helper
				.extractPatientId(request.getQueryString());
		if (patientId == null)
			patientId = I2b2Helper.extractPatientId2(request.getRequestURL()
					.toString(), resourceName);
		logger.info("PatientId:" + patientId);
		if (patientId != null) {
			// filter.put("Patient", "Patient/" + patientId);
			return I2b2Helper.getPdo(session, patientId, service);
		} else {
			if (resourceName.equals("Patient"))
				 return I2b2Helper.initAllPatients(session);

		}
		return null;
	}

	static String extractPatientId(String input) {
		if (input == null)
			return null;
		String id = null;
		Pattern p = Pattern
				.compile("[Subject:subject|Patient|patient|_id]=([a-zA-Z0-9]+)");
		Matcher m = p.matcher(input);

		if (m.find()) {
			id = m.group(1);
			logger.trace(id);
		}
		return id;
	}

	static String extractPatientId2(String string, String resourceName) {
		logger.debug("requestUrl is:" + string);
		if (string == null)
			return null;
		String id = null;

		Pattern p = Pattern.compile(".*/([a-zA-Z0-9]+)[-]*.*$");
		Matcher m = p.matcher(string);

		if (m.find()) {
			id = m.group(1);
			logger.trace("id:" + id);
		}
		if (id.equals(resourceName))
			id = null;
		return id;
	}

}
