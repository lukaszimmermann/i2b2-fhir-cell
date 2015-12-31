package edu.harvard.i2b2.fhir.server.ws.operation;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.DecisionSupportServiceModule;
import org.hl7.fhir.DecisionSupportServiceModuleParameter;
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
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.server.ws.FhirHelper;

public class DSSEvaluate {

	static Logger logger = LoggerFactory.getLogger(DSSEvaluate.class);

	// [base]/DecisionSupportServiceModule/[id]/$evaluate
	public static Resource evaluate(String id, String inTxt) throws JAXBException, IOException {
		OperationOutcome oo = null;
		Parameters reqParams = null;
		Parameters inParams = null;//this is an entry in the reqParams

		// load a few example dss module in mem
		MetaResourceDb md = new MetaResourceDb();
		DecisionSupportServiceModule d = JAXBUtil.fromXml(
				Utils.fileToString("/example/fhir/dstu21/decisionsupportservicemodule-example.xml"),
				DecisionSupportServiceModule.class);
		md.addResource(d);

		d = null;
		// find the dss module by id
		d = (DecisionSupportServiceModule) md.searchById(id, DecisionSupportServiceModule.class);

		if (d == null) {
			return FhirHelper.getOperationOutcome("Not Found DecisionSupportServiceModule with id:" + id,
					IssueTypeList.EXCEPTION, IssueSeverityList.ERROR);

		}

		try {
			reqParams = JAXBUtil.fromXml(inTxt, Parameters.class);
		} catch (JAXBException e) {
			//give back validation response to indicate errors
			logger.trace("Request was not valid");
		}
		
		if (reqParams != null) {
			for (ParametersParameter p : reqParams.getParameter()) {
				if(p.getName().getValue().equals("inputParameters")){
					inParams=p.getResource().getParameters();
				}
				logger.trace("Req pname:" + p.getName().getValue());
			}
		} else {
			logger.trace("ps is null");
			return FhirHelper.getOperationOutcome("Request has no parameters",
					IssueTypeList.EXCEPTION, IssueSeverityList.ERROR);
		}

		// run evaluate on input to generate guidance response
		// check that all required data is provided before running evaluate

		// cycle thru input parameters and check if all are present
		for (DecisionSupportServiceModuleParameter p : d.getParameter()) {
			if (!p.getUse().getValue().equals("in"))
				continue;// ignoring out put parameters
			logger.trace("required param:"+p.getName().getValue()+" ->"+p.getType().getValue());
			if(inParams!=null){
				for (ParametersParameter inP : inParams.getParameter()) {
					logger.trace("got inParam:" + inP.getName().getValue() + " ->");
				}
			}else{
				logger.trace("inParam is null");
					
			
			}
		}

		// process library of module to run logic
		/*
		 * Parameters ps = null; try { ps = JAXBUtil.fromXml(inTxt,
		 * Parameters.class); if (ps != null) { for (ParametersParameter p :
		 * ps.getParameter()) { logger.trace("pname:" + p.getName().getValue());
		 * } } else { logger.trace("ps is null"); } } catch (ClassCastException
		 * e) {
		 * 
		 * 
		 * }
		 * 
		 */
		oo=FhirHelper.getOperationOutcome("Running evaluate",
 IssueTypeList.INFORMATIONAL,
				IssueSeverityList.INFORMATION);
		return oo;

	}
}
