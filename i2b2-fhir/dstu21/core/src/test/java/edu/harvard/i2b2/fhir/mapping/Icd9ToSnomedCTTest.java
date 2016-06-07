package edu.harvard.i2b2.fhir.mapping;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.Condition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.BundlePagingTest;
import edu.harvard.i2b2.fhir.FhirEnrich;
import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.icd9.Icd9FhirAdapter;

public class icd9ToSnomedCTTest {

	static Logger logger = LoggerFactory.getLogger(icd9ToSnomedCTTest.class);

	Bundle b;

	@Before
	public void setup() {
		try {
			String xmlBundle = Utils.getFile("test-examples/fhir/dstu21/ConditionBundle.xml");
			logger.trace("x:" + xmlBundle);
			b = JAXBUtil.fromXml(xmlBundle, Bundle.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Test
	public void test() {
		Condition c = (Condition) FhirUtil.getResourceFromContainer(b.getEntry().get(1).getResource());
		try {
			FhirEnrich.enrich(c);
			logger.trace("c:" + JAXBUtil.toXml(c));
			Assert.assertEquals("45007003",Icd9FhirAdapter.getSnomedCTCode(c));
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
