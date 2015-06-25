package edu.harvard.i2b2;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.hl7.fhir.Patient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class TestDSTU2 {
	static Logger logger = LoggerFactory.getLogger(TestDSTU2.class); 
	
	
	@Test
	public void test() throws DatatypeConfigurationException, JAXBException {
		
		MetaResourceSet s=ResourceSetup.getPatientAndMedicationStatementEg();
		logger.trace(""+JAXBUtil.toXml(s));
	}

}
