/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package edu.harvard.i2b2.fhir.core;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import edu.harvard.i2b2.fhir.XQueryUtilException;

public class FhirCoreException extends Exception {

	public FhirCoreException(String string, Exception e) {
		super(string,e);
	}

	public FhirCoreException(String string) {
		super(string);
	}

	public FhirCoreException(Exception e) {
		super(e);
	}

	
}
