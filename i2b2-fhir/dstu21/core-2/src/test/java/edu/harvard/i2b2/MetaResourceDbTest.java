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


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.PropertyConfigurator;
import org.hl7.fhir.Bundle;
import org.hl7.fhir.Medication;
import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.Reference;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;


public class MetaResourceDbTest {

	static Logger logger = LoggerFactory.getLogger(MetaResourceDbTest.class); 
/*
	@Test
	public void filterTest() throws DatatypeConfigurationException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, JAXBException, IOException{
		List<Resource> s =ResourceSetup.getPatientAndMedicationStatementEg();
	logger.info(""+JAXBUtil.toXml(s.get(1)));
		
		List<Resource> s1 =ResourceSetup.getPatientAndMedicationStatementEg();
		Patient p=(Patient) s1.get(0);
		FhirUtil.setId(p,"Patient/1000000001");
		MedicationStatement ms=(MedicationStatement) s1.get(1);
		FhirUtil.setId(ms,"MedicationStatement/1000000001-1");
		ms.setPatient(FhirUtil.getReference(p));
	
		s1.add(p);
		s1.add(ms);
		
		
		s.add(s1.get(0));
		s.add(s1.get(1));
		
		logger.info(""+s.size());
		logger.info("0:"+s.get(0).getId());
		logger.info("1:"+s.get(1).getId());
		//logger.info("2:"+s.getMetaResource().get(2).getResource().getId());
		//logger.info("3:"+s.getMetaResource().get(3).getResource().getId());
		
		logger.info("bundle"+FhirUtil.getResourceBundle(s, "X/", "Y/"));
		
		
		MetaResourceDb db=new MetaResourceDb();
		db.addResourceList(s);
		assertEquals(4, db.getSize());
		assertEquals(2, db.getResourceTypeCount(Patient.class));
		
	HashMap<String,String> filter= null;
	java.util.List<Resource> res=null;
	filter= new HashMap<String,String>();
		filter.put("Patient", "Patient/1000000001");
		List<Resource> res=db.filterMetaResources(MedicationStatement.class, filter);
		logger.info("filtered bundle:"+FhirUtil.getResourceBundle(res, "X/", "Y/") );
	
		assertEquals(1, res.getMetaResource().size());
		
		filter= new HashMap<String,String>();
		filter.put("id", "MedicationStatement/1000000001-1");
		//res=db.filterResources(MedicationStatement.class, filter);
		logger.info("filtered bundle:"+FhirUtil.getResourceBundle(res, "X/", "Y/") );
		
		filter= new HashMap<String,String>();
		filter.put("Patient.id", "Patient/1000000005");
		//res=db.filterResources(MedicationStatement.class, filter);
		logger.info("filtered bundle:"+FhirUtil.getResourceBundle(res, "X/", "Y/") );
		
		
	}
	*/
	
	@Test
	public void getIncludedResourcesTest() {
		try{
			Properties props = new Properties();
			props.load(getClass().getResourceAsStream("/log4j.properties"));
			PropertyConfigurator.configure(props);
			Class c = MedicationStatement.class;
			List<String> includeResources = new ArrayList<String>();
			
			/*String xml= Utils.fileToString("/example/fhir/DSTU2/singleMedication.xml");
			logger.trace("xml:"+xml);
			Medication m=JAXBUtil.fromXml(xml, Medication.class);
			logger.trace("medication:"+JAXBUtil.toXml(m));
			
			MedicationStatement ms=JAXBUtil.fromXml(Utils.fileToString("/example/fhir/DSTU2/MedicationStatement.xml"), MedicationStatement.class);
			logger.trace("medicationS:"+JAXBUtil.toXml(ms));
			*/
			
			Bundle b=JAXBUtil.fromXml(Utils.fileToString("/example/fhir/DSTU2/MedicationStatementsToIncludeMedications.xml"), Bundle.class);
			MetaResourceDb db= new MetaResourceDb();
			db.addBundle(b);
			List<Resource> msl = db.getAll(MedicationStatement.class);
		//	logger.trace("medicationS:"+JAXBUtil.toXml(msl.get(0)));
			
			for(Resource r:db.getAll()){
				logger.trace("id:"+r.getId().getValue());
			}
			//includeResources.add("medication");
			includeResources.add("patient");
			db.getIncludedResources(MedicationStatement.class,
					 msl,  includeResources);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		/*
		List<Resource> inputSet = null;
		List<String> includeResources = null;
		
		MetaResourceDb db= new MetaResourceDb();
		
		db.getIncludedResources(c,
			 inputSet,  includeResources);
		*/
	}
	
	
}
