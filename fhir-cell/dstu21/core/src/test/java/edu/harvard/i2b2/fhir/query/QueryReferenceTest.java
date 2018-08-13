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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryReferenceTest {
	static Logger logger = LoggerFactory.getLogger(QueryReferenceTest.class);
	Patient p;
	String xmlPatient;
	String xmlMedicationStatement;
	MedicationStatement ms;
	List<Resource>s;
	
	QueryBuilder qb;
	Query q;
	MetaResourceDb db;
	QueryEngine qe;
	@Before
	public void setup() throws FhirCoreException, JAXBException, IOException {
		xmlPatient = Utils.getFile("example/fhir/DSTU2/marriedPatient.xml");
		p = (Patient) JAXBUtil.fromXml(xmlPatient,Patient.class);
		qb = new QueryBuilder();
		xmlMedicationStatement = Utils.getFile("example/fhir/DSTU2/MedicationStatement.xml");
		ms = (MedicationStatement) JAXBUtil.fromXml(xmlMedicationStatement,MedicationStatement.class);
		FhirUtil.setId(ms,"1-1");
		qb = new QueryBuilder();
		db=new MetaResourceDb();
		db.addResource(p);
		db.addResource(ms);
		s=db.getAll(Patient.class);
	}

	@Test
	public void testReference() throws QueryParameterException,
			QueryValueException, FhirCoreException, XQueryUtilException, JAXBException, QueryException {

		/*String url="MedicationStatement?patient=1000000005&_include=MedicationStatement.Medication&_include=MedicationStatement.Patient";
		Pattern p = Pattern.compile( FhirUtil.RESOURCE_LIST_REGEX+"\\?*([^\\?]*)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(url);
		if (m.matches()) {
			logger.info("true");
			}
		else{
			logger.info("false");
			
		}
		*/logger.info("Running tests for QueryString...");

		//q = qb.setResourceClass(MedicationStatement.class).setRawParameter("patient")
			//	.setRawValue("example").build();
		//logger.trace("RES:"+q.match(ms));
		//assertTrue(q.match(xmlMedicationStatement,null,null));
		
		logger.info(JAXBUtil.toXml(s.get(0)));	
		
		String url="Patient?gender=female&birthdate=>1966-08-29&@Patient.maritalStatus.coding.code:exact=M";
		qe = new QueryEngine(url);
		logger.info(""+qe.search(s,db));	
		
		
	}
}
