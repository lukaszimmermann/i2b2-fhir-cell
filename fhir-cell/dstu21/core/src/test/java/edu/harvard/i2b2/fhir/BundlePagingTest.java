
package edu.harvard.i2b2.fhir;

import static org.junit.Assert.*;


import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.Resource;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class BundlePagingTest {
	static Logger logger = LoggerFactory.getLogger(BundlePagingTest.class);

	Bundle b;

	@Before
	public void setup() {
		try {
			String xmlBundle = Utils.getFile("PDOBundle.xml");
			//logger.trace("x:"+ xmlBundle);
			b = JAXBUtil.fromXml(xmlBundle, Bundle.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Test
	public void testPaging1() throws JAXBException {
		Bundle pagedB=FhirUtil.pageBundle(b, 10, 1);
		logger.trace("pagedB:"+JAXBUtil.toXml(pagedB));
	}

}
