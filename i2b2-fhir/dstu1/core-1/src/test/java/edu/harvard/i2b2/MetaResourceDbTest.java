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


import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.ResourceReference;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class MetaResourceDbTest {

	static Logger logger = LoggerFactory.getLogger(MetaResourceDbTest.class); 
	@Test
	public void filterTest() throws DatatypeConfigurationException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, JAXBException, IOException{
		MetaResourceSet s =ResourceSetup.getPatientAndMedicationStatementEg();
	logger.info(""+JAXBUtil.toXml(s.getMetaResource().get(1).getResource()));
		
		MetaResourceSet s1 =ResourceSetup.getPatientAndMedicationStatementEg();
		Patient p=(Patient) s1.getMetaResource().get(0).getResource();
		p.setId("Patient/1000000001");
		MedicationStatement ms=(MedicationStatement) s1.getMetaResource().get(1).getResource();
		ms.setId("MedicationStatement/1000000001-1");
		ms.setPatient(FhirUtil.getResourceReferenceForRessource(p));
	
		s1.getMetaResource().get(0).setResource(p);
		s1.getMetaResource().get(1).setResource(ms);
		
		
		s.getMetaResource().add(s1.getMetaResource().get(0));
		s.getMetaResource().add(s1.getMetaResource().get(1));
		
		logger.info(""+s.getMetaResource().size());
		logger.info("0:"+s.getMetaResource().get(0).getResource().getId());
		logger.info("1:"+s.getMetaResource().get(1).getResource().getId());
		//logger.info("2:"+s.getMetaResource().get(2).getResource().getId());
		//logger.info("3:"+s.getMetaResource().get(3).getResource().getId());
		
		logger.info("bundle"+FhirUtil.getResourceBundle(s, "X/", "Y/"));
		
		
		MetaResourceDb db=new MetaResourceDb();
		db.addMetaResourceSet(s);
		assertEquals(4, db.getSize());
		assertEquals(2, db.getMetaResourceTypeCount(Patient.class));
		
	HashMap<String,String> filter= null;
	MetaResourceSet res=null;
	/*filter= new HashMap<String,String>();
		filter.put("Patient", "Patient/1000000001");
		MetaResourceSet res=db.filterMetaResources(MedicationStatement.class, filter);
		logger.info("filtered bundle:"+FhirUtil.getResourceBundle(res, "X/", "Y/") );
	
		assertEquals(1, res.getMetaResource().size());
	*/	
		filter= new HashMap<String,String>();
		filter.put("id", "MedicationStatement/1000000001-1");
		res=db.filterMetaResources(MedicationStatement.class, filter);
		logger.info("filtered bundle:"+FhirUtil.getResourceBundle(res, "X/", "Y/") );
		
		filter= new HashMap<String,String>();
		filter.put("Patient.id", "Patient/1000000005");
		res=db.filterMetaResources(MedicationStatement.class, filter);
		logger.info("filtered bundle:"+FhirUtil.getResourceBundle(res, "X/", "Y/") );
		
		
	}
	
	
}
