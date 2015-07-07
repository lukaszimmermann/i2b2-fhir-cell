package edu.harvard.i2b2.fhir.query;

import javax.xml.datatype.DatatypeConfigurationException;

public class QueryValueException extends Exception {

	public QueryValueException(String string) {
		super(string);
	}
	
	public QueryValueException(String string, Exception e) {
		super(string,e);
	}

}
