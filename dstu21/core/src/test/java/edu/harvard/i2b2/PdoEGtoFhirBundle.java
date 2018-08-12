/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package edu.harvard.i2b2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.Medication;
import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.IOUtils;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.MetaData;
import edu.harvard.i2b2.fhir.query.Query;

public class PdoEGtoFhirBundle {
	static Logger logger = LoggerFactory.getLogger(PdoEGtoFhirBundle.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	public void test() throws XQueryUtilException {
		String query = "declare namespace ns=\"http://hl7.org/fhir\";"
				+ "\n//(ns:Medication|ns:Patient)";
		String input = Utils.getFile("example/fhir/mixedResource.xml");

		List<String> xmlList = XQueryUtil.getStringSequence(input, query);
		// List<Resource> resList = FhirUtil.fromXml(xmlList);
		// System.out
		// .println(FhirUtil.getResourceBundle(resList, "uriInfoString"));
	}

 @Test
	public void validate() {
		// URL path=FhirUtil.class.getResource("validation.zip");
		// System.out.println(FhirUtil.isValid(Utils.getFile("example/fhir/singlePatient.xml")));
		String msg = FhirUtil.getValidatorErrorMessage(Utils.getFile("example/fhir/DSTU2/singlePatient.xml"));
		//"/Users/kbw19/git/res/i2b2-fhir/dstu2/xquery-2/src/main/resources/example/fhir/DSTU2/singlePatientInvalid.xml");
		logger.trace("msg:"+msg);
		/*
		assertEquals(true, FhirUtil.isValid(Utils
				.getFile("example/fhir/DSTU2/singlePatient.xml")));
		assertEquals(false, FhirUtil.isValid(Utils
				.getFile("example/fhir/singlePatientInvalid.xml")));
		assertEquals(
				"Unknown Content familys @  START_TAG seen ...<use value=\"usual\"/>\n    <familys value=\"van de Heuvel\"/>... @93:37",
				msg);
				*/
	}

	// @Test
	/*
	 * public void search() { ResourceDb resDb = new ResourceDb(); Resource r =
	 * FhirUtil.xmlToResource(Utils .getFile("example/fhir/singlePatient.xml"));
	 * // r.setId("1"); resDb.addResource(r, Patient.class);
	 * resDb.addResource(r, Patient.class); //
	 * System.out.println(FhirUtil.resourceToXml(r));
	 * System.out.println(FhirUtil.resourceToXml(resDb.getResource("1",
	 * Patient.class)));
	 * 
	 * }
	 * 
	 * // @Test public void readBundle() throws ParseException, IOException {
	 * ResourceDb resDb = new ResourceDb();
	 * 
	 * Abdera abdera = new Abdera(); Parser parser = abdera.getParser();
	 * 
	 * String path = Utils.getFilePath("example/fhir/PatientBundle.xml"); URL
	 * url = new URL("file://" + path); Document<Feed> doc =
	 * parser.parse(url.openStream(), url.toString()); Feed feed =
	 * doc.getRoot(); System.out.println(feed.getTitle()); for (Entry entry :
	 * feed.getEntries()) { System.out.println("\t" + entry.getTitle());
	 * System.out.println(); // System.out.println("\t" + entry.getContent());
	 * if (!FhirUtil.isValid(entry.getContent())) { throw new RuntimeException(
	 * "entry is not a valid Fhir Resource:" + entry.getId() +
	 * FhirUtil.getValidatorErrorMessage(entry .getContent())); } Resource r =
	 * FhirUtil.xmlToResource(entry.getContent()); resDb.addResource(r,
	 * Patient.class);
	 * 
	 * } // System.out.println (feed.getAuthor());
	 * 
	 * }
	 */
	@Test
	public void TestPatient() throws XQueryUtilException {

		String patientBundle = getPatients();
		logger.trace("getPatients:" + patientBundle);
		try {
			Bundle b = JAXBUtil.fromXml(patientBundle, Bundle.class);
			logger.trace(JAXBUtil.toXml(b.getEntry().get(0).getResource()
					.getPatient()));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void Test23() throws XQueryUtilException {

		String medsBundle =null;// getMeds();
		// logger.trace("getMEds:"+medsBundle);
		try {
			Bundle b = JAXBUtil.fromXml(medsBundle, Bundle.class);
			MedicationStatement ms = b.getEntry().get(1).getResource()
					.getMedicationStatement();
			logger.trace(JAXBUtil.toXml(ms));
			logger.trace(ms.getPatient().getReference().getValue().toString());
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public String getPatients() throws XQueryUtilException {
		String query = Utils
				.getFile("transform/I2b2ToFhir/i2b2PatientToFhirPatient.xquery");
		String input = Utils.getFile("example/i2b2/AllPatients.xml");

		return XQueryUtil.processXQuery(query, input).toString();
	}

	@Test
	public void getMeds() {
		try {

			String query = IOUtils
					.toString(PdoEGtoFhirBundle.class
							.getResourceAsStream("/transform/I2b2ToFhir/i2b2MedsToFHIRMedPrescription.xquery"));
			String input = Utils
					.getFile("example/i2b2/medicationsForAPatient.xml");

			String xml = XQueryUtil.processXQuery(query, input);
			Bundle b = JAXBUtil.fromXml(xml, Bundle.class);
			logger.trace("b:"+b.getEntry().size());
			logger.trace("b:"+JAXBUtil.toXml(b));
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Test
	public void readMedBundle() throws JAXBException {
		String xmlString = Utils
				.getFile("example/fhir/DSTU2/PatientBundle.xml");
		Bundle b = JAXBUtil.fromXml(xmlString, Bundle.class);
		logger.info("b:" + JAXBUtil.toXml(b));
	}

	@Test
	public void getPatientResource() throws XQueryUtilException {
		String query = Utils
				.getFile("transform/I2b2ToFhir/i2b2PatientToFhirPatient.xquery");
		String input = Utils.getFile("example/i2b2/PDO1.xml");

		logger.info(XQueryUtil.processXQuery(query, input).toString());
	}

	@Test
	public void getPatientResource2() throws XQueryUtilException,
			JAXBException, IOException {
		Patient p = FhirUtil.getPatientResource(Utils
				.getFile("example/i2b2/PDO1.xml"));
		logger.info("" + JAXBUtil.toXml(p));
	}

}