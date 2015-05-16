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
import edu.harvard.i2b2.fhir.Utils;

public class QueryTest {

	static Logger logger = LoggerFactory.getLogger(QueryTest.class); 
	Patient p;
	QueryBuilder qb;
	Query q;
	
	@Before
	public void setup(){
		String xml=Utils.getFile("example/fhir/singlePatient.xml");
		p=(Patient) FhirUtil.xmlToResource(xml);
		qb=new QueryBuilder();
	}
	
	@Test
	public void testDate() throws QueryParameterException, QueryValueException {
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
		logger.trace("RES:"+q.match(p));
		assertTrue(q.match(p));
		q=qb.setResourceClass(Patient.class).setRawParameter("birthdate").setRawValue(">1941-05-15").build();
		logger.trace("RES:"+q.match(p));
		assertTrue(q.match(p));
		q=qb.setResourceClass(Patient.class).setRawParameter("birthdate").setRawValue("<1941-05-15").build();
		logger.trace("RES:"+q.match(p));
		assertFalse(q.match(p));
		q=qb.setResourceClass(Patient.class).setRawParameter("birthdate").setRawValue("=<1944-11-17").build();
		logger.trace("RES:"+q.match(p));
		assertTrue(q.match(p));
	}
	
	
	//@Test
	public void testToken() throws QueryParameterException, QueryValueException {
		q=qb.setResourceClass(Patient.class).setRawParameter("gender").setRawValue("M").build();
		logger.trace("q:"+q);
		logger.trace("RES:"+q.match(p));
		//assertTrue(q.match(p));
	}
}