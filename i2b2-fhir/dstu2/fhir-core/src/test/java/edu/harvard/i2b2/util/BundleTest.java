package edu.harvard.i2b2.util;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.BundleEntry;
import org.hl7.fhir.Patient;
import org.hl7.fhir.ResourceContainer;
import org.hl7.fhir.SearchParameter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.query.SearchParameterMap;

public class BundleTest {

	static Logger logger = LoggerFactory.getLogger(BundleTest.class);

	@Test
	public void testBundleToResourceList() {
		try {
			String bundleString = Utils
					.getFile("profiles/search-parameters.xml");

			Bundle b = JAXBUtil.fromXml(bundleString, Bundle.class);
			logger.trace("" + JAXBUtil.toXml(b));

			// fail("Not yet implemented");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testReadBundle() {
		try {
			String bundleString = Utils.getFile("example/bundle1.xml");

			Bundle b = JAXBUtil.fromXml(bundleString, Bundle.class);
			logger.trace("" + JAXBUtil.toXml(b));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Bundle exampleBundle() {
		Bundle b = new Bundle();
		BundleEntry e = new BundleEntry();
		e.setId("123");
		ResourceContainer rc = new ResourceContainer();
		Patient p = new Patient();
		SearchParameter sp = new SearchParameter();
		sp.setName(FhirUtil.getFhirString("name23"));
		// rc.setPatient(p);
		rc.setSearchParameter(sp);
		e.setResource(rc);
		b.getEntry().add(e);
		// logger.trace(""+JAXBUtil.toXml(b));
		return b;
	}
}
