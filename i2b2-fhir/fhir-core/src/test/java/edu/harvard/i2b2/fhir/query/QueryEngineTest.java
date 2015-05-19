package edu.harvard.i2b2.fhir.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hl7.fhir.Patient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryEngineTest {
	static Logger logger = LoggerFactory.getLogger(QueryEngineTest.class);
	Patient p;
	QueryEngine qe;
	
	@Before
	public void setup() {
		String xml = Utils.getFile("example/fhir/singlePatient.xml");
		p = (Patient) FhirUtil.xmlToResource(xml);
	}

	@Test
	public void testQueryUrl() throws QueryParameterException, QueryValueException, FhirCoreException  {
		qe=new QueryEngine("Patient?name=pieter&gender=M");
		logger.trace(qe.toString());
	}
}
