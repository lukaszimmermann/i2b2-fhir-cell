package edu.harvard.i2b2.fhir.server.ws.operation;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.Response;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.Parameters;
import org.hl7.fhir.ParametersParameter;
import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.server.ws.FhirServerException;

public class CdsHook {
	static Logger logger = LoggerFactory.getLogger(CdsHook.class);

	String activity = null;// 1..1
	String activityInstance = null;// 1..1 valueString,
	String fhirServer = null;// 0..1 valueUri,
	URI redirect = null;// 1..1 valueUri,
	String user = null;// 0..1 valueString,
	String patient = null;// 0..1 valueId,
	String encounter = null;// 0..1 valueId,
	Resource context = null;// 0..* resource(Any),
	Bundle preFetchData = null;// 0..1 resource(Bundle)

	public CdsHook(Parameters parameters) throws URISyntaxException {

		for (ParametersParameter p : parameters.getParameter()) {
			String paramName = p.getName().getValue();

			switch (paramName) {
				case "activity":
					this.activity = p.getValueString().getValue();
					break;
				case "activityInstance":
					this.activityInstance = p.getValueString().getValue();
					break;
				case "fhirServer":
					this.fhirServer = p.getValueString().getValue();
					break;
				case "redirect":
					this.redirect = new URI(p.getValueString().getValue());
					break;
		
			}
		}
		
		
	}

	void validateInputParameters() throws FhirServerException {
		if(this.activity==null) throw new FhirServerException("required input parameter:activity, is not provided for operation cds-hook");
		if(this.activityInstance==null) throw new FhirServerException("required input parameter:activityInstance, is not provided for operation cds-hook");
		if(this.redirect==null) throw new FhirServerException("required input parameter:redirect, is not provided for operation cds-hook");
			
	}

	public Resource execute() throws FhirServerException {
		Resource outR = null;
		validateInputParameters(); 
		return outR;
	}

}
