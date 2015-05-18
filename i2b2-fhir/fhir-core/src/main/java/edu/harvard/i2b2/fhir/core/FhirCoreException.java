package edu.harvard.i2b2.fhir.core;

import javax.xml.bind.JAXBException;

public class FhirCoreException extends Exception {

	public FhirCoreException(String string, JAXBException e) {
		super(string,e);
	}

	public FhirCoreException(String string) {
		super(string);
	}
}
