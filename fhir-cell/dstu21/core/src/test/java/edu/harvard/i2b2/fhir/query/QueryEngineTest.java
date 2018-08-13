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

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package edu.harvard.i2b2.fhir.query;

import static org.junit.Assert.assertFalse;

import org.hl7.fhir.Resource;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.hl7.fhir.Patient;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.SetupExamples;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryEngineTest {
	static Logger logger = LoggerFactory.getLogger(QueryEngineTest.class);
	Patient p;
	Patient p2;
	List<Resource> s;
	QueryEngine qe;

	@Before
	public void setup() throws JAXBException, FhirCoreException {
		String xml = Utils.getFile("example/fhir/singlePatient.xml");
		p = (Patient) JAXBUtil.fromXml(xml,Patient.class);
		FhirUtil.setId(p,"myid1");
		xml = Utils
				.getFile("example/fhir/singlePatientWithoutCodeSystemForGender.xml");
		p2 = (Patient) JAXBUtil.fromXml(xml,Patient.class);
		FhirUtil.setId(p2,"myid2");
		
		 s = new ArrayList<Resource>();
		 s.add(p);
		 }

	 @Test
	public void testQuery() throws QueryParameterException,
			QueryValueException, FhirCoreException,
			DatatypeConfigurationException, JAXBException, XQueryUtilException, QueryException {

		// qe = new QueryEngine("Patient?name=Pieter&gender=M");
		qe = new QueryEngine("Patient?name=Pieter");

		logger.trace(qe.toString());

		
		
		// s.getMetaResource().add(mr2);
		// s.getMetaResource().add(
		// SetupExamples.getEGPatient().getMetaResource().get(0));
		// logger.trace("Input:" + FhirUtil.toXml(p));

		List<Resource> s2 = qe.search(s,null);
		logger.info("Input:" + s.size());
		logger.info("Result:" + s2.size());

	}

	@Test
	public void testQueryUrl() throws QueryParameterException,
			QueryValueException, FhirCoreException, JAXBException, UnsupportedEncodingException {

		String url="http://localhost:8080/fhir-server/a/a/Patient?birthdate=%3C1966-08-29";
		logger.trace(url+"\n"+URLDecoder.decode(url,"UTF-8"));
		
	/*	qe = new QueryEngine(
		// "MedicationStatement?patient=1000000005&_include=MedicationStatement.Medication&_include=MedicationStatement.Patient"
				"Patient?birthdate=<1966-08-29");

		logger.info("qe:" + qe);
		logger.info(""+qe.search(s));
*/
		// XXX //TODO
		// http://localhost:8080/fhir-server/a/a/Patient?gender=M
		// http://localhost:8080/fhir-server/a/a/Patient?birthdate=1966-08-29
		// http://localhost:8080/fhir-server/a/a/Patient?birthdate=>1966-08-29
	}

	@Test
	public void testMultiParamQueryUrl() throws QueryParameterException,
	QueryValueException, FhirCoreException, JAXBException, UnsupportedEncodingException, XQueryUtilException, QueryException {	
		String url="Patient?gender=M&birthdate=<1965-08-29";
		qe = new QueryEngine(url);
		logger.info(""+qe);
		logger.info(""+qe.search(s,null));
	}
	
	@Test
	public void testMultiParamQueryUrlThree() throws QueryParameterException,
	QueryValueException, FhirCoreException, JAXBException, UnsupportedEncodingException {	
		String url="Patient?gender=M&birthdate=<1965-08-29&active=true";
		qe = new QueryEngine(url);
		logger.info(""+qe);
		//logger.info(""+qe.search(s));
	}
	
	public void RegexTest() {

		String s = "Patient/1000000005";
		Pattern p = Pattern.compile(".*/([^/]+)$");

		Matcher m = p.matcher(s);
		if (m.matches()) {
			logger.info("grp1:" + m.group(1));
		} else {
			logger.info("no match");
		}
	}
}
