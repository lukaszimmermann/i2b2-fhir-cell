package edu.harvard.i2b2.fhir.query;

import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryException extends Exception {

	public QueryException() {
	}

	public QueryException(String string) {
		super(string);
	}

	public QueryException(String string, Exception e) {
		super(string, e);
	}
	
	public QueryException(Exception e) {
		super(e);
	}


}
