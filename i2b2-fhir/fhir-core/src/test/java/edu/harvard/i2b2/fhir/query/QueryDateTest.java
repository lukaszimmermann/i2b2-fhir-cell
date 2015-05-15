package edu.harvard.i2b2.fhir.query;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.hl7.fhir.Patient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.Utils;

public class QueryDateTest {

	static Logger logger = LoggerFactory.getLogger(QueryDateTest.class); 
	
	@Test
	public void test() throws QueryParameterException, QueryValueException {
		QueryBuilder qb=new QueryBuilder();
		try{
			Query q=qb.setResourceClass(Patient.class).setParameter("birthdate").setValue("05-15-2015").build();
		}catch(QueryValueException e){
			assert(true);
		}
		try{
			Query q=qb.setResourceClass(Patient.class).setParameter("undefined").setValue("2015-05-15").build();
		}catch(QueryParameterException e){
			assert(true);
		}
		
		
		String xml=Utils.getFile("example/fhir/singlePatient.xml");
		Patient p=(Patient) FhirUtil.xmlToResource(xml);
		
		Query q=qb.setResourceClass(Patient.class).setParameter("birthdate").setValue("<2015-05-15").build();
		logger.trace("RES:"+q.match(p));
		assertTrue(q.match(p));
		q=qb.setResourceClass(Patient.class).setParameter("birthdate").setValue(">1941-05-15").build();
		logger.trace("RES:"+q.match(p));
		assertTrue(q.match(p));
		q=qb.setResourceClass(Patient.class).setParameter("birthdate").setValue("<1941-05-15").build();
		logger.trace("RES:"+q.match(p));
		assertFalse(q.match(p));
		q=qb.setResourceClass(Patient.class).setParameter("birthdate").setValue("=<1944-11-17").build();
		logger.trace("RES:"+q.match(p));
		assertTrue(q.match(p));
		
		
		
	}

}