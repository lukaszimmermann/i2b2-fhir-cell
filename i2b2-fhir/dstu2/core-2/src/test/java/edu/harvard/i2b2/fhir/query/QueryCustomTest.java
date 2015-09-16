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


public class QueryCustomTest {
	static Logger logger = LoggerFactory.getLogger(QueryCustomTest.class);
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
	public void testSingle() throws QueryParameterException, QueryValueException, FhirCoreException, JAXBException, XQueryUtilException, QueryException {
		//String url="Patient?#Patient.maritalStatus:exact=M";
		String url="Patient?gender=F&birthdate=>1966-08-29&@Patient.maritalStatus:exact=S";
		qe = new QueryEngine(url);
		logger.info(""+qe.search(s));	
	}
}
