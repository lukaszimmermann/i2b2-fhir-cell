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

import org.hl7.fhir.Medication;
import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.Resource;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
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

public class QueryIdTest {
	static Logger logger = LoggerFactory.getLogger(QueryIdTest.class);
	Patient p;
	String xmlPatient;
	String xmlMedicationStatement;
	MedicationStatement ms;
	
	QueryBuilder qb;
	Query q;
	MetaResourceDb db;
	QueryEngine qe;
	List<Resource> s;
	@Before
	public void setup() throws FhirCoreException, JAXBException, IOException {
		xmlPatient = Utils.getFile("example/fhir/DSTU2/singlePatient.xml");
		p = (Patient) JAXBUtil.fromXml(xmlPatient,Patient.class);
		qb = new QueryBuilder();
		db=new MetaResourceDb();
		db.addResource(p);
		s=db.getAll();
	}

	@Test
	public void testSingle() throws QueryParameterException, QueryValueException, FhirCoreException, JAXBException, XQueryUtilException, QueryException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//logger.trace("ms:"+JAXBUtil.toXml(ms));
		//logger.trace("id:"+FhirUtil.getChildThruChain(ms,"Patient.id", s));
		String url="Patient?_id=example";
		qe = new QueryEngine(url);
		logger.trace("qe:"+qe);
		List<Resource> resSet=qe.search(s,db);
		//logger.info("res:"+JAXBUtil.toXml(resSet));	
		assertTrue(resSet.size()>0);
		
	
		}
}
