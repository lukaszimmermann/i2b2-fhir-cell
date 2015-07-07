package edu.harvard.i2b2.fhir.query;

import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryParameterException extends Exception {

	public QueryParameterException() {
	}

	public QueryParameterException(String string) {
		super(string);
	}

	public QueryParameterException(String string, FhirCoreException e) {
		super(string, e);
	}

}
