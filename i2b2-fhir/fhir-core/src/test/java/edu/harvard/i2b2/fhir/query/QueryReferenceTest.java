package edu.harvard.i2b2.fhir.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hl7.fhir.MedicationStatement;
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

public class QueryReferenceTest {
	static Logger logger = LoggerFactory.getLogger(QueryReferenceTest.class);
	Patient p;
	MedicationStatement ms;
	
	QueryBuilder qb;
	Query q;
	MetaResourceDb db;

	@Before
	public void setup() throws FhirCoreException {
		String xml = Utils.getFile("example/fhir/singlePatient.xml");
		p = (Patient) FhirUtil.xmlToResource(xml);
		qb = new QueryBuilder();
			xml = Utils.getFile("example/fhir/MedicationStatement.xml");
		ms = (MedicationStatement) FhirUtil.xmlToResource(xml);
		ms.setId("1-1");
		qb = new QueryBuilder();
		db=new MetaResourceDb();
		db.addMetaResource(FhirUtil.getMetaResource(p), Patient.class);
		db.addMetaResource(FhirUtil.getMetaResource(ms), MedicationStatement.class);
	
	}

	@Test
	public void testReference() throws QueryParameterException,
			QueryValueException, FhirCoreException {
		logger.info("Running tests for QueryString...");

		q = qb.setResourceClass(MedicationStatement.class).setDb(db).setRawParameter("patient")
				.setRawValue("example").build();
		//logger.trace("RES:"+q.match(ms));
		assertTrue(q.match(ms));
		
		
	}
}
