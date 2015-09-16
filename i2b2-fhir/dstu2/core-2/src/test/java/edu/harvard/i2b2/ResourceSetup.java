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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.hl7.fhir.Id;
import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.hl7.fhir.Reference;
import org.hl7.fhir.String;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.core.MetaData;

public class ResourceSetup {
	
	static public  List<Resource> getEGPatient()
			throws DatatypeConfigurationException {
		Patient p = new Patient();
		Id id=new Id();
		id.setValue("Patient/1000000005");;
		p.setId(id);

		MetaData md1 = new MetaData();
		GregorianCalendar gc = new GregorianCalendar();
		md1.setLastUpdated(DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(gc));

		// md1.setId(p.getId());

	//	p.setMeta(value);
		
		List<Resource> s1 = new ArrayList<Resource>();
		s1.add(p);
		return s1;
	}

	
	
	
	static public  List<Resource> getPatientAndMedicationStatementEg()
			throws DatatypeConfigurationException {
		List<Resource> s1 = new ArrayList<Resource>();

		Patient p = new Patient();
		FhirUtil.setId(p,"Patient/1000000005");

		MedicationStatement ms = new MedicationStatement();
		FhirUtil.setId(ms,"MedicationStatement/1000000005-1");
		ms.setPatient(FhirUtil.getReference(p));
		
		//MetaData md1 = new MetaData();
		
		// md1.setId(p.getId());
		//GregorianCalendar gc = new GregorianCalendar();
		//md1.setLastUpdated(DatatypeFactory.newInstance()
			//	.newXMLGregorianCalendar(gc));
	//	p.setMeta(value);
		s1.add(p);

		//md2.setLastUpdated(DatatypeFactory.newInstance()
		//		.newXMLGregorianCalendar(gc));

		s1.add(ms);
		return s1;
		}
 	
	 	
}
