package edu.harvard.i2b2.fhir.core;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

public class FhirCoreException extends Exception {

	public FhirCoreException(String string, Exception e) {
		super(string,e);
	}

	public FhirCoreException(String string) {
		super(string);
	}

	
}
