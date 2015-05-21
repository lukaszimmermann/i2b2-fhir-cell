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
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryStringTest {
	static Logger logger = LoggerFactory.getLogger(QueryStringTest.class);
	Patient p;
	String xmlPatient;
	QueryBuilder qb;
	Query q;
	
	
	@Before
	public void setup() throws FhirCoreException {
		String xmlPatient = Utils.getFile("example/fhir/singlePatient.xml");
		p = (Patient) FhirUtil.xmlToResource(xmlPatient);
		qb = new QueryBuilder();
	}

	@Test
	public void testString() throws QueryParameterException,
			QueryValueException, FhirCoreException {
		logger.info("Running tests for QueryString...");

		q = qb.setResourceClass(Patient.class).setRawParameter("name")
				.setRawValue("pieter").build();
		// logger.trace("RES:"+q.match(xmlPatient));
		assertTrue(q.match(xmlPatient));
		q = qb.setResourceClass(Patient.class).setRawParameter("name:exact")
				.setRawValue("Pieter").build();
		assertTrue(q.match(xmlPatient));

		try {
			q = qb.setResourceClass(Patient.class)
					.setRawParameter("name1:exact").setRawValue("Pieter")
					.build();
		} catch (QueryParameterException e) {
			assert (true);
		}

		q = qb.setResourceClass(Patient.class).setRawParameter("family:exact")
				.setRawValue("van de Heuvel").build();
		assertTrue(q.match(xmlPatient));

		q = qb.setResourceClass(Patient.class).setRawParameter("family")
				.setRawValue(" van  de").build();
		assertTrue(q.match(xmlPatient));

		q = qb.setResourceClass(Patient.class).setRawParameter("family:exact")
				.setRawValue("van de Heuvel1").build();
		assertFalse(q.match(xmlPatient));

	}
	
	@Test
	public void testParserUrl() throws QueryParameterException{
		MetaResourceDb db= new MetaResourceDb();
		new QueryBuilder("patient?name=pieter");
		new QueryBuilder("medication?name=pieter");
		logger.trace(""+FhirUtil.getResourceList());
		
	}
}
