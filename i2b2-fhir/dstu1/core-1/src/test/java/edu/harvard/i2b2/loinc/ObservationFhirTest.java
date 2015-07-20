package edu.harvard.i2b2.loinc;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;
import edu.harvard.i2b2.map.MapSystemEngineTest;

public class ObservationFhirTest {

	static Logger logger = LoggerFactory.getLogger(ObservationFhirTest.class); 
	
	@Test
	public void test() throws JAXBException {
		String xml=Utils.getFile("example/fhir/MetaResourceSetLabs.xml");
		logger.trace(""+JAXBUtil.fromXml(xml, MetaResourceSet.class));
		
	}

}
