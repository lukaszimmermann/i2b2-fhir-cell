package edu.harvard.i2b2.fhir.server.ws;

import org.hl7.fhir.GuidanceResponse;
import org.hl7.fhir.IssueSeverity;
import org.hl7.fhir.IssueSeverityList;
import org.hl7.fhir.IssueType;
import org.hl7.fhir.IssueTypeList;
import org.hl7.fhir.OperationOutcome;
import org.hl7.fhir.OperationOutcomeIssue;

public class FhirHelper {
	static public OperationOutcome getOperationOutcome(String message, IssueTypeList issueType,
			IssueSeverityList issueSeverity) {
		OperationOutcome o = new OperationOutcome();
		OperationOutcomeIssue i = new OperationOutcomeIssue();
		IssueType iType = new IssueType();
		iType.setValue(issueType);
		i.setCode(iType);
		IssueSeverity severity = new IssueSeverity();
		severity.setValue(issueSeverity);
		i.setSeverity(severity);
		org.hl7.fhir.String s1 = new org.hl7.fhir.String();
		s1.setValue(message);
		i.setDiagnostics(s1);
		o.getIssue().add(i);

		return o;
	}
	
	static public GuidanceResponse genGuidanceResponse(){
		GuidanceResponse g=new GuidanceResponse();
		return g;
	}
}
