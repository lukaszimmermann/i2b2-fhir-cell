package edu.harvard.i2b2.fhir;

import org.apache.jackrabbit.uuid.UUID;

public class FhirUtilInstance extends FhirUtil {
	String instanceId;
	public FhirUtilInstance(){
		instanceId=UUID.randomUUID().toString();
	}
	
	
}
