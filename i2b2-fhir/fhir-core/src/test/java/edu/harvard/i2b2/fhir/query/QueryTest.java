package edu.harvard.i2b2.fhir.query;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.hl7.fhir.Patient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryTest {

	static Logger logger = LoggerFactory.getLogger(QueryTest.class); 
	Patient p;
	QueryBuilder qb;
	Query q;
	MetaResourceDb db;

	@Before
	public void setup() throws FhirCoreException {
		String xml = Utils.getFile("example/fhir/singlePatient.xml");
		p = (Patient) FhirUtil.xmlToResource(xml);
		qb = new QueryBuilder();
		db=new MetaResourceDb();
		db.addMetaResource(FhirUtil.getMetaResource(p), Patient.class);
	}
	
	@Test
	public void testDate() throws QueryParameterException, QueryValueException, FhirCoreException {
		logger.info("Running tests for QueryDate...");
		try{
			q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("birthdate").setRawValue("05-15-2015").build();
		}catch(QueryValueException e){
			assert(true);
		}
		try{
			q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("undefined").setRawValue("2015-05-15").build();
		}catch(QueryParameterException e){
			assert(true);
		}
		
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("birthdate").setRawValue("<2015-05-15").build();
		logger.trace("Q:"+q);
		logger.trace("RES:"+q.match(p));
		assertTrue(q.match(p));
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("birthdate").setRawValue(">1941-05-15").build();
		//logger.trace("RES:"+q.match(p));
		//assertTrue(q.match(p));
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("birthdate").setRawValue("<1941-05-15").build();
		//logger.trace("RES:"+q.match(p));
		assertFalse(q.match(p));
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("birthdate").setRawValue("=<1944-11-17").build();
		//logger.trace("RES:"+q.match(p));
		assertTrue(q.match(p));
	}
	
	
	@Test
	public void testTokenCode() throws QueryParameterException, QueryValueException, FhirCoreException {
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("gender").setRawValue("M").build();
		//logger.trace("RES:"+q.match(p));
		assertTrue(q.match(p));
		
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("gender:text").setRawValue("M").build();
		assertTrue(q.match(p));
		
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("gender:text").setRawValue("F").build();
		assertFalse(q.match(p));
		
		
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("gender").setRawValue("F").build();
		assertFalse(q.match(p));
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("gender").setRawValue("http://hl7.org/fhir/v3/AdministrativeGender|M").build();
		assertTrue(q.match(p));
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("gender:text").setRawValue("http://hl7.org/fhir/v3/AdministrativeGender|M").build();
		assertTrue(q.match(p));
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("gender:text").setRawValue("http://hl7.org/fhir/v3/AdministrativeGender|Male").build();
		assertTrue(q.match(p));
	
		
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("gender:text").setRawValue("http://hl7.org/fhir/v3/AdministrativeGender|F").build();
		assertFalse(q.match(p));
	
		
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("language").setRawValue("urn:ietf:bcp:47|nl").build();
		assertTrue(q.match(p));
		
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("language:text").setRawValue("Dutch").build();
		assertTrue(q.match(p));
		
		
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("gender").setRawValue("http://hl7.org/fhir/v3/AdministrativeGender|F").build();
		assertFalse(q.match(p));
	
		
		
		String xml=Utils.getFile("example/fhir/singlePatientWithoutCodeSystemForGender.xml");
		p=(Patient) FhirUtil.xmlToResource(xml);
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("gender").setRawValue("|M").build();
		assertTrue(q.match(p));
	}
	
	@Test
	public void testTokenIdentifier() throws QueryParameterException, QueryValueException, FhirCoreException {
	
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("identifier").setRawValue("738472983").build();
		assertTrue(q.match(p));
		
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("identifier").setRawValue("738472981").build();
		assertFalse(q.match(p));
		
		
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("identifier").setRawValue("urn:oid:2.16.840.1.113883.2.4.6.3|738472983").build();
		assertTrue(q.match(p));
	}
	
	@Test
	public void testTokenSimpleElements() throws QueryParameterException, QueryValueException, FhirCoreException {
	
		q=qb.setResourceClass(Patient.class).setDb(db).setRawParameter("active").setRawValue("true").build();
		assertTrue(q.match(p));
	}	
	
	
}