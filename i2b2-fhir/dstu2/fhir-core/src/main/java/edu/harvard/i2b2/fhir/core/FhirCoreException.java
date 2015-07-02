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
