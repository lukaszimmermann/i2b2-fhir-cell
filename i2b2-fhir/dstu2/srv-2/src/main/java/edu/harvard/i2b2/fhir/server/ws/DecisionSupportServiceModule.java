package edu.harvard.i2b2.fhir.server.ws;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.IssueSeverityList;
import org.hl7.fhir.IssueTypeList;
import org.hl7.fhir.OperationOutcome;
import org.hl7.fhir.Parameters;
import org.hl7.fhir.ParametersParameter;
import org.hl7.fhir.Resource;
import org.hl7.fhir.dstu21.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu21.model.OperationOutcome.IssueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;

public class DecisionSupportServiceModule {

	static Logger logger = LoggerFactory.getLogger(DecisionSupportServiceModule.class);

	
	// [base]/DecisionSupportServiceModule/[id]/$evaluate
	public static Resource evaluate(String inTxt) throws JAXBException {
		OperationOutcome oo = new OperationOutcome();
		Parameters ps = null;
		try {
			ps = JAXBUtil.fromXml(inTxt, Parameters.class);
			if (ps != null) {
				for (ParametersParameter p : ps.getParameter()) {
					logger.trace("pname:" + p.getName().getValue());
				}
			} else {
				logger.trace("ps is null");
			}
		} catch (ClassCastException e) {

			
		}

		FhirHelper.getOperationOutcome("message", IssueTypeList.INFORMATIONAL, IssueSeverityList.INFORMATION);
		return oo;
		

	}
}
