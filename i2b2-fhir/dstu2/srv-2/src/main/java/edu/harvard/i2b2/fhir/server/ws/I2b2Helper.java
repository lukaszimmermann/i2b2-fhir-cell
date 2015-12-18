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

package edu.harvard.i2b2.fhir.server.ws;

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
import edu.harvard.i2b2.oauth2.core.ejb.PatientBundleManager;
import edu.harvard.i2b2.oauth2.core.ejb.ProjectPatientMapManager;
import edu.harvard.i2b2.oauth2.core.entity.AccessToken;

public class I2b2Helper {

	static Logger logger = LoggerFactory.getLogger(I2b2Helper.class);

	static Bundle initAllPatients(AccessToken tok,
			ProjectPatientMapManager service) throws FhirServerException  {
		logger.info("Got request:" + tok);
		Bundle allPatients = service.getProjectPatientBundle(tok);
		logger.info("Got ResourceSet of size::" + allPatients.getEntry().size());
		return allPatients;
	}

	private static Bundle getPdo(AccessToken accessTok, String patientId,
			PatientBundleManager pBundleMgr,ProjectPatientMapManager projPatMapMgr) throws FhirServerException {
		if(projPatMapMgr.hasAccessToPatient(accessTok,patientId)){
			return pBundleMgr.getPatientBundle(accessTok, patientId);
		}else{
			throw new FhirServerException("the patientId is invalid for the given access Token: Either the patient Id is invalid or the accessToken is not credientialed to access the given patient");
		}
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

	static Bundle parsePatientIdToFetchPDO(HttpSession session,
			HttpServletRequest request, String resourceName,
			PatientBundleManager service,
			ProjectPatientMapManager ppmservice) throws XQueryUtilException,
			JAXBException, IOException, AuthenticationFailure,
			FhirServerException, InterruptedException {
		AccessToken tok = (AccessToken) session.getAttribute("accessToken");
		String patientId = FhirUtil.extractPatientId(request.getQueryString());
		if (patientId == null)
			patientId = FhirUtil.extractPatientIdFromRequestById(request.getRequestURL()
					.toString(), resourceName);
		logger.info("PatientId:" + patientId);
		if (patientId != null) {
			// filter.put("Patient", "Patient/" + patientId);
			return I2b2Helper.getPdo(tok, patientId, service,ppmservice);
		} else {
			if (resourceName.equals("Patient")){
				//throw new FhirServerException("Search on patients is not allowed. Patient id needs to be specified");
				return I2b2Helper.initAllPatients(tok,ppmservice);
			}
		}
		return null;
	}

	

}
