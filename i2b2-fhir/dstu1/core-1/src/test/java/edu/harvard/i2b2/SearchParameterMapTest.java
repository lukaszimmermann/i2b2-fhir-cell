package edu.harvard.i2b2;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Patient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.query.SearchParameterMap;

public class SearchParameterMapTest {
	static Logger logger = LoggerFactory.getLogger(SearchParameterMapTest.class);

	@Test
	public void testInit() throws  FhirCoreException{
		SearchParameterMap m=new SearchParameterMap();
		logger.trace("-->"+m.getParameterPath(Patient.class, "gender"));
		logger.trace("-->"+m.getType(Patient.class, "gender"));
	}
}
