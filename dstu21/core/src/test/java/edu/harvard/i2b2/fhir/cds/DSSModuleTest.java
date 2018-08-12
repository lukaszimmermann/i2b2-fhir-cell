package edu.harvard.i2b2.fhir.cds;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.DecisionSupportServiceModule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;

public class DSSModuleTest {
	static Logger logger = LoggerFactory.getLogger(DSSModuleTest.class);

	@Test
	public void test() throws IOException, JAXBException {
		 DecisionSupportServiceModule dssMod=JAXBUtil.fromXml(Utils.fileToString("/example/fhir/dstu21/decisionsupportservicemodule-example.xml"),DecisionSupportServiceModule.class);
		logger.trace("dssMod:"+dssMod.getId().getValue());
		 //fail("Not yet implemented");
		logger.trace("Class:"+FhirUtil.getResourceClass(dssMod));
	}

}
