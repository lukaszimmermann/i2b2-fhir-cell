package edu.harvard.i2b2.fhir;

import javax.xml.datatype.DatatypeConfigurationException;

public class I2b2FhirException extends Exception {

	public I2b2FhirException(String string, DatatypeConfigurationException e) {
		super(string,e);
	}

}
