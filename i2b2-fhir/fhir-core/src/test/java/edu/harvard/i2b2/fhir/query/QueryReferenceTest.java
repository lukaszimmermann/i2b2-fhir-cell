package edu.harvard.i2b2.fhir.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	String xmlPatient;
	String xmlMedicationStatement;
	MedicationStatement ms;
	
	QueryBuilder qb;
	Query q;
	MetaResourceDb db;

	@Before
	public void setup() throws FhirCoreException {
		xmlPatient = Utils.getFile("example/fhir/singlePatient.xml");
		p = (Patient) FhirUtil.xmlToResource(xmlPatient);
		qb = new QueryBuilder();
		xmlMedicationStatement = Utils.getFile("example/fhir/MedicationStatement.xml");
		ms = (MedicationStatement) FhirUtil.xmlToResource(xmlMedicationStatement);
		ms.setId("1-1");
		qb = new QueryBuilder();
		db=new MetaResourceDb();
		db.addMetaResource(FhirUtil.getMetaResource(p), Patient.class);
		db.addMetaResource(FhirUtil.getMetaResource(ms), MedicationStatement.class);
	
	}

	@Test
	public void testReference() throws QueryParameterException,
			QueryValueException, FhirCoreException {

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

		q = qb.setResourceClass(MedicationStatement.class).setRawParameter("patient")
				.setRawValue("example").build();
		//logger.trace("RES:"+q.match(ms));
		assertTrue(q.match(xmlMedicationStatement));
		
		
	}
}
