package edu.harvard.i2b2.fhir.server.ws;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.GuidanceResponse;
import org.hl7.fhir.IssueSeverity;
import org.hl7.fhir.IssueSeverityList;
import org.hl7.fhir.IssueType;
import org.hl7.fhir.IssueTypeList;
import org.hl7.fhir.OperationOutcome;
import org.hl7.fhir.OperationOutcomeIssue;
import org.hl7.fhir.Parameters;
import org.hl7.fhir.ParametersParameter;

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

	static public Parameters generateConceptLookUpOutput(String name, String version, String display, boolean abstractF,
			String use, String value) {
		Parameters prms = new Parameters();

		if (name != null) {
			ParametersParameter pName = new ParametersParameter();
			pName.setName(FhirUtil.generateFhirString("name"));
			pName.setValueString(FhirUtil.generateFhirString(name));
			prms.getParameter().add(pName);
		}
		if (version != null) {
			ParametersParameter pVersion = new ParametersParameter();
			pVersion.setName(FhirUtil.generateFhirString("version"));
			pVersion.setValueString(FhirUtil.generateFhirString(version));
			prms.getParameter().add(pVersion);
		}
		if (display != null) {
			ParametersParameter pDisplay = new ParametersParameter();
			pDisplay.setName(FhirUtil.generateFhirString("display"));
			pDisplay.setValueString(FhirUtil.generateFhirString(display));
			prms.getParameter().add(pDisplay);
		}
		if (value != null) {
			ParametersParameter pValue = new ParametersParameter();
			pValue.setName(FhirUtil.generateFhirString("value"));
			pValue.setValueString(FhirUtil.generateFhirString(value));
			prms.getParameter().add(pValue);
		}
		/*
		 * name 1..1 string A display name for the code system
		 * 
		 * version 0..1 string The version that these details are based on
		 * 
		 * display 1..1 string The preferred display for this concept
		 * 
		 * abstract 0..1 boolean Whether this code is an abstract concept
		 * 
		 * designation 0..* Additional representations for this concept
		 * 
		 * designation.language 0..1 code The language this designation is
		 * defined for
		 * 
		 * designation.use 0..1 Coding A code that details how this designation
		 * would be used
		 * 
		 * designation.value 1..1 string The text value for this designation
		 */
		return prms;
	}

	static boolean isPatientDependentResource(Class c) {
		String className = c.getSimpleName();
		return (className.equals("DecisionSupportServiceModule")) ? false : true;
	}

	static public void loadTestResources(MetaResourceDb md) throws JAXBException, IOException {
		DSSEvaluate.loadDSSModules(md);

	}
}
