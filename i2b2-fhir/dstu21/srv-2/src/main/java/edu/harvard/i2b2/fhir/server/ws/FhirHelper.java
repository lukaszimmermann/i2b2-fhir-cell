package edu.harvard.i2b2.fhir.server.ws;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.GuidanceResponse;
import org.hl7.fhir.IssueSeverity;
import org.hl7.fhir.IssueSeverityList;
import org.hl7.fhir.IssueType;
import org.hl7.fhir.IssueTypeList;
import org.hl7.fhir.OperationOutcome;
import org.hl7.fhir.OperationOutcomeIssue;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.server.ws.operation.DSSEvaluate;
import net.sf.saxon.exslt.Random;
import net.sf.saxon.trans.XPathException;

public class FhirHelper {
	static public OperationOutcome generateOperationOutcome(String message, IssueTypeList issueTypeList,
			IssueSeverityList issueSeverityList) {
		OperationOutcome o = new OperationOutcome();
		FhirUtil.setId(o, FhirUtil.generateRandomId());
		OperationOutcomeIssue i = new OperationOutcomeIssue();
		IssueType iType = new IssueType();
		iType.setValue(issueTypeList);
		i.setCode(iType);
		IssueSeverity severity = new IssueSeverity();
		severity.setValue(issueSeverityList);
		i.setSeverity(severity);
		org.hl7.fhir.String s1 = new org.hl7.fhir.String();
		s1.setValue(message);
		i.setDiagnostics(s1);
		o.getIssue().add(i);

		return o;
	}

	static boolean isPatientDependentResource(Class c) {
		String className = c.getSimpleName();
		return (className.equals("DecisionSupportServiceModule")) ? false : true;
	}

	static public void loadTestResources(MetaResourceDb md) throws JAXBException, IOException {
		DSSEvaluate.loadDSSModules(md);

	}
}
