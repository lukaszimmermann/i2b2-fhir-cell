package harvard.i2b2.fhir.ws;

import javax.xml.bind.JAXBException;

public class FhirServerException extends Exception {

	public FhirServerException(String string, JAXBException e) {
		super(string,e);
	}

	
}
