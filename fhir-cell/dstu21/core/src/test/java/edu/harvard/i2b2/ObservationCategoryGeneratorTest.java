package edu.harvard.i2b2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.apache.log4j.PropertyConfigurator;
import org.hl7.fhir.Bundle;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.DiagnosticReportGenerator;
import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.ObservationCategoryGenerator;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class ObservationCategoryGeneratorTest {
static Logger logger = LoggerFactory.getLogger(ObservationCategoryGeneratorTest.class);
	@Test
	public void test() {
		//fail("Not yet implemented");
		//loadLogConfiguration();
		logger.trace("loaded");
		try {
			Bundle obsB=JAXBUtil.fromXml(Utils.fileToString("/example/fhir/dstu21/ObservationBundleForObservationCategories.xml"),Bundle.class);
			Bundle obsWithCatB=ObservationCategoryGenerator.addObservationCategoryToObservationBundle(obsB);
			logger.trace(JAXBUtil.toXml(obsB));
			
		} catch (FhirCoreException | JAXBException | IOException e) {
			logger.error(e.getMessage(),e);
		}
	}



static {
	Properties props = new Properties();
	try {
		props.load(ObservationCategoryGeneratorTest.class.getResourceAsStream("/log4j.properties"));
	} catch (IOException e) {
		e.printStackTrace();
		logger.error(e.getMessage(),e);
	}
	PropertyConfigurator.configure(props);
}
}