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

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.hl7.fhir.Patient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryTest {

	static Logger logger = LoggerFactory.getLogger(QueryTest.class); 
	Patient p;
	String xmlPatient;
	QueryBuilder qb;
	Query q;
	
	@Before
	public void setup() throws FhirCoreException, JAXBException {
		xmlPatient = Utils.getFile("example/fhir/singlePatient.xml");
		p = (Patient) JAXBUtil.fromXml(xmlPatient,Patient.class);
		qb = new QueryBuilder();
	}
	
	@Test
	public void testDate() throws QueryParameterException, QueryValueException, FhirCoreException, XQueryUtilException, QueryException {
		logger.info("Running tests for QueryDate...");
		try{
			q=qb.setResourceClass(Patient.class).setRawParameter("birthdate").setRawValue("05-15-2015").build();
		}catch(QueryValueException e){
			assert(true);
		}
		try{
			q=qb.setResourceClass(Patient.class).setRawParameter("undefined").setRawValue("2015-05-15").build();
		}catch(QueryParameterException e){
			assert(true);
		}
		
		q=qb.setResourceClass(Patient.class).setRawParameter("birthdate").setRawValue("<2015-05-15").build();
		logger.trace("Q:"+q);
		logger.trace("RES:"+q.match(xmlPatient,null,null));
		assertTrue(q.match(xmlPatient,null,null));
		q=qb.setResourceClass(Patient.class).setRawParameter("birthdate").setRawValue(">1941-05-15").build();
		//logger.trace("RES:"+q.match(xmlPatient));
		//assertTrue(q.match(xmlPatient));
		q=qb.setResourceClass(Patient.class).setRawParameter("birthdate").setRawValue("<1941-05-15").build();
		//logger.trace("RES:"+q.match(xmlPatient));
		assertFalse(q.match(xmlPatient,null,null));
		q=qb.setResourceClass(Patient.class).setRawParameter("birthdate").setRawValue("=<1944-11-17").build();
		//logger.trace("RES:"+q.match(xmlPatient));
		assertTrue(q.match(xmlPatient,null,null));
	}
	
	
	@Test
	public void testTokenCode() throws QueryParameterException, QueryValueException, FhirCoreException, JAXBException, XQueryUtilException, QueryException {
		q=qb.setResourceClass(Patient.class).setRawParameter("gender").setRawValue("M").build();
		//logger.trace("RES:"+q.match(xmlPatient));
		assertTrue(q.match(xmlPatient,null,null));
		
		q=qb.setResourceClass(Patient.class).setRawParameter("gender:text").setRawValue("M").build();
		assertTrue(q.match(xmlPatient,null,null));
		
		q=qb.setResourceClass(Patient.class).setRawParameter("gender:text").setRawValue("F").build();
		assertFalse(q.match(xmlPatient,null,null));
		
		
		q=qb.setResourceClass(Patient.class).setRawParameter("gender").setRawValue("F").build();
		assertFalse(q.match(xmlPatient,null,null));
		q=qb.setResourceClass(Patient.class).setRawParameter("gender").setRawValue("http://hl7.org/fhir/v3/AdministrativeGender|M").build();
		assertTrue(q.match(xmlPatient,null,null));
		q=qb.setResourceClass(Patient.class).setRawParameter("gender:text").setRawValue("http://hl7.org/fhir/v3/AdministrativeGender|M").build();
		assertTrue(q.match(xmlPatient,null,null));
		q=qb.setResourceClass(Patient.class).setRawParameter("gender:text").setRawValue("http://hl7.org/fhir/v3/AdministrativeGender|Male").build();
		assertTrue(q.match(xmlPatient,null,null));
	
		
		q=qb.setResourceClass(Patient.class).setRawParameter("gender:text").setRawValue("http://hl7.org/fhir/v3/AdministrativeGender|F").build();
		assertFalse(q.match(xmlPatient,null,null));
	
		
		q=qb.setResourceClass(Patient.class).setRawParameter("language").setRawValue("urn:ietf:bcp:47|nl").build();
		assertTrue(q.match(xmlPatient,null,null));
		
		q=qb.setResourceClass(Patient.class).setRawParameter("language:text").setRawValue("Dutch").build();
		assertTrue(q.match(xmlPatient,null,null));
		
		
		q=qb.setResourceClass(Patient.class).setRawParameter("gender").setRawValue("http://hl7.org/fhir/v3/AdministrativeGender|F").build();
		assertFalse(q.match(xmlPatient,null,null));
	
		
		
		String xml=Utils.getFile("example/fhir/singlePatientWithoutCodeSystemForGender.xml");
		p=(Patient) JAXBUtil.fromXml(xml,Patient.class);
		q=qb.setResourceClass(Patient.class).setRawParameter("gender").setRawValue("|M").build();
		assertTrue(q.match(xmlPatient,null,null));
	}
	
	@Test
	public void testTokenIdentifier() throws QueryParameterException, QueryValueException, FhirCoreException, XQueryUtilException, QueryException {
	
		q=qb.setResourceClass(Patient.class).setRawParameter("identifier").setRawValue("738472983").build();
		assertTrue(q.match(xmlPatient,null,null));
		
		q=qb.setResourceClass(Patient.class).setRawParameter("identifier").setRawValue("738472981").build();
		assertFalse(q.match(xmlPatient,null,null));
		
		
		q=qb.setResourceClass(Patient.class).setRawParameter("identifier").setRawValue("urn:oid:2.16.840.1.113883.2.4.6.3|738472983").build();
		assertTrue(q.match(xmlPatient,null,null));
	}
	
	@Test
	public void testTokenSimpleElements() throws QueryParameterException, QueryValueException, FhirCoreException, XQueryUtilException, QueryException {
	
		q=qb.setResourceClass(Patient.class).setRawParameter("active").setRawValue("true").build();
		assertTrue(q.match(xmlPatient,null,null));
	}	
	
	
}