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

package edu.harvard.i2b2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.hl7.fhir.Medication;
import org.hl7.fhir.MedicationOrder;
import org.hl7.fhir.Patient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.query.SearchParameterMap;

public class FhirUtilTest {

	static Logger logger = LoggerFactory.getLogger(FhirUtilTest.class);

	@Test
	public void contain() {
		try {
			// URL path=FhirUtil.class.getResource("validation.zip");
			// System.out.println(FhirUtil.isValid(Utils.getFile("example/fhir/singlePatient.xml")));
			String mpXml = IOUtils
					.toString(FhirUtilTest.class
							.getResourceAsStream("/example/fhir/DSTU2/singleMedicationOrder.xml"));
			String mXml = IOUtils
					.toString(FhirUtilTest.class
							.getResourceAsStream("/example/fhir/DSTU2/singleMedication.xml"));

			MedicationOrder mp = JAXBUtil.fromXml(mpXml, MedicationOrder.class);
			Medication m = JAXBUtil.fromXml(mXml, Medication.class);

			// logger.info("MP:"+JAXBUtil.toXml(mp));
			// logger.info("MP:"+JAXBUtil.toXml(m));

			logger.info("after containing:"
					+ JAXBUtil.toXml(FhirUtil.containResource(mp, m)));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Test
	public void readMedicationOrder() {
		try {
			String mpXml = IOUtils
					.toString(FhirUtilTest.class
							.getResourceAsStream("/example/fhir/DSTU2/MedicationOrder.xml"));

			MedicationOrder mp = JAXBUtil.fromXml(mpXml, MedicationOrder.class);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Test
	public void extractPatientId(){
		
		String url="?Patient=123";
		String id=FhirUtil.extractPatientId(url);
		logger.info("id:"+id);
		
		url="?patient=1000000005&_include=medication";
		id=FhirUtil.extractPatientId(url);
		logger.info("id:"+id);
		
		url="?_include=medication&patient=1000000005";
		id=FhirUtil.extractPatientId(url);
		logger.info("id:"+id);
	}
	
	@Test
	public void include() throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, JAXBException, FhirCoreException{
		Bundle b1= createMedicationOrderAndMedicationBundle();
		Bundle b2= createMedicationOrderBundle();
		List<String> includeList=new ArrayList<String>();
		includeList.add("medication");
		MetaResourceDb  md1= new MetaResourceDb();
		md1.addBundle(b1);
		
		md1.getIncludedResources(MedicationOrder.class,
				FhirUtil.getResourceListFromBundle(b2),
				includeList);
		
	}
	
	private Bundle createMedicationOrderAndMedicationBundle()
			throws IOException, JAXBException {
		Bundle b= new Bundle();
		String mpXml = IOUtils
				.toString(FhirUtilTest.class
						.getResourceAsStream("/example/fhir/DSTU2/singleMedicationOrder.xml"));
		String mXml = IOUtils
				.toString(FhirUtilTest.class
						.getResourceAsStream("/example/fhir/DSTU2/singleMedication.xml"));

		MedicationOrder mp = JAXBUtil.fromXml(mpXml, MedicationOrder.class);
		Medication m = JAXBUtil.fromXml(mXml, Medication.class);
		MetaResourceDb db= new MetaResourceDb ();
		db.addResource(m);
		db.addResource(mp);
		return FhirUtil.getResourceBundle(db.getAll(), "basePath", "url");
	} 
	
	private Bundle createMedicationOrderBundle()
			throws IOException, JAXBException {
		Bundle b= new Bundle();
		String mpXml = IOUtils
				.toString(FhirUtilTest.class
						.getResourceAsStream("/example/fhir/DSTU2/singleMedicationOrder.xml"));
		
		MedicationOrder mp = JAXBUtil.fromXml(mpXml, MedicationOrder.class);
		MetaResourceDb db= new MetaResourceDb ();
		db.addResource(mp);
		return FhirUtil.getResourceBundle(db.getAll(), "basePath", "url");
	} 
}
