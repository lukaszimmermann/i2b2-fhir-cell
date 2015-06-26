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
