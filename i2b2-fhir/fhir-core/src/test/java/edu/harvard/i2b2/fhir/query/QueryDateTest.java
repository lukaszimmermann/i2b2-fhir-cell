package edu.harvard.i2b2.fhir.query;

import static org.junit.Assert.*;

import org.hl7.fhir.Patient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		
		Query q=qb.setResourceClass(Patient.class).setParameter("birthdate").setValue("2015-05-15").build();
		
		logger.info(""+q);
	}

}