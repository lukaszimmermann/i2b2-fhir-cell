/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package edu.harvard.i2b2.fhirserver.ws;

import javax.xml.bind.JAXBException;

public class FhirServerException extends Exception {

	public FhirServerException(String string, Exception e) {
		super(string,e);
	}
	
	public FhirServerException(String string) {
		super(string);
	}

	
}
