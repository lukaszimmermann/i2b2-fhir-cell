package edu.harvard.i2b2.fhirserver.ws;

import javax.xml.bind.JAXBException;

public class FhirServerException extends Exception {

	public FhirServerException(String string, JAXBException e) {
		super(string,e);
	}

	
}
