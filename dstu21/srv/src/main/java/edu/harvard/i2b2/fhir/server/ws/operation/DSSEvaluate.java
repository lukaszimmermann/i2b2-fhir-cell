package edu.harvard.i2b2.fhir.server.ws.operation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.DecisionSupportServiceModule;
import org.hl7.fhir.DecisionSupportServiceModuleParameter;
import org.hl7.fhir.GuidanceResponse;
import org.hl7.fhir.GuidanceResponseStatus;
import org.hl7.fhir.GuidanceResponseStatusList;
import org.hl7.fhir.IssueSeverityList;
import org.hl7.fhir.IssueTypeList;
import org.hl7.fhir.OperationOutcome;
import org.hl7.fhir.Parameters;
import org.hl7.fhir.ParametersParameter;
import org.hl7.fhir.Reference;
import org.hl7.fhir.Resource;
import org.hl7.fhir.ResourceContainer;
import org.hl7.fhir.dstu21.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.dstu21.model.OperationOutcome.IssueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;

import edu.harvard.i2b2.fhir.server.ws.FhirHelper;

public class DSSEvaluate {

	static Logger logger = LoggerFactory.getLogger(DSSEvaluate.class);

	// [base]/DecisionSupportServiceModule/[id]/$evaluate
	public static Resource evaluate(String id, String inTxt) throws JAXBException, IOException {
		OperationOutcome oo = null;
		Parameters reqParams = null;
		Parameters inParams = null;// this is an entry in the reqParams

		// load a few example dss module in mem
		MetaResourceDb md = new MetaResourceDb();
		loadDSSModules(md);

		// find the dss module by id
		DecisionSupportServiceModule d = (DecisionSupportServiceModule) md.searchById(id,
				DecisionSupportServiceModule.class);

		if (d == null) {
			oo = FhirHelper.generateOperationOutcome("Not Found DecisionSupportServiceModule with id:" + id,
					IssueTypeList.EXCEPTION, IssueSeverityList.ERROR);
			return genGuidanceResponse(reqParams.getId().getValue(), null, GuidanceResponseStatusList.FAILURE, oo);

		}

		try {
			reqParams = JAXBUtil.fromXml(inTxt, Parameters.class);
		} catch (JAXBException e) {
			// give back validation response to indicate errors
			logger.trace("Request was not valid");
		}

		if (reqParams != null) {
			for (ParametersParameter p : reqParams.getParameter()) {
				if (p.getName().getValue().equals("inputParameters")) {
					inParams = p.getResource().getParameters();
				}
				logger.trace("Req pname:" + p.getName().getValue());
			}
		} else {
			logger.trace("request param is null");
			oo = FhirHelper.generateOperationOutcome("Request has no parameters", IssueTypeList.EXCEPTION,
					IssueSeverityList.ERROR);
			return genGuidanceResponse(
					((reqParams!=null)?reqParams.getId().getValue():null), (
							(d!=null)?d.getId().getValue():null),
					GuidanceResponseStatusList.FAILURE, oo);
		}

		if (inParams != null) {
			for (ParametersParameter p : reqParams.getParameter()) {
				logger.trace("Input pname:" + p.getName().getValue());
			}
		} else {
			logger.trace("input param is null");
			oo = FhirHelper.generateOperationOutcome("Request has no Parameter named as inputParameters",
					IssueTypeList.EXCEPTION, IssueSeverityList.ERROR);
			return genGuidanceResponse(reqParams.getId().getValue(), d.getId().getValue(),
					GuidanceResponseStatusList.DATA_REQUIRED.SUCCESS, oo);

		}

		// run evaluate on input to generate guidance response
		// check that all required data is provided before running evaluate

		// cycle thru input parameters of Module and check if all are present in the request
		List<DecisionSupportServiceModuleParameter> notFoundList = new ArrayList<>();
		for (DecisionSupportServiceModuleParameter p : d.getParameter()) {
			if (!(p.getUse().getValue().equals("in")))
				continue;// ignoring out put parameters
			logger.trace("required param:" + p.getName().getValue() + " ->" + p.getType().getValue());

			boolean paramFoundF = false;
			for (ParametersParameter inP : inParams.getParameter()) {
				logger.trace("got inParam:" + inP.getName().getValue() + " ->");
				Resource inR = FhirUtil.getResourceFromContainer(inP.getResource());
				if (FhirUtil.getResourceClass(inR).getSimpleName().toLowerCase()
						.equals(p.getName().getValue().toLowerCase())) {
					paramFoundF = true;
					// logger.error("Mismatch:"+FhirUtil.getResourceClass(inR).getSimpleName().toLowerCase()+"--"+p.getName().getValue().toLowerCase());

				}
			}
			if (!paramFoundF) {
				notFoundList.add(p);
				logger.error("Required Parameter :" + p.getName().getValue() + " was not present in Request");
			}
		}

		if (notFoundList.size() > 0) {
			String listTxt = "";
			for (DecisionSupportServiceModuleParameter p : notFoundList) {
				listTxt += "," + p.getName().getValue();
			}
			listTxt=listTxt.substring(1);
			oo= FhirHelper.generateOperationOutcome(
					"The following parameters are missing in the Request. These are required by DecisionSupportServiceModule id(" 
							+ d.getId().getValue()+"):"+ listTxt,
					IssueTypeList.EXCEPTION, IssueSeverityList.ERROR);
			 return genGuidanceResponse(reqParams.getId().getValue(), d.getId().getValue(),
						GuidanceResponseStatusList.DATA_REQUIRED.SUCCESS, oo);
		}

		return genGuidanceResponse(reqParams.getId().getValue(), d.getId().getValue(),
				GuidanceResponseStatusList.SUCCESS, oo);
		/*
		 * oo = FhirHelper.generateOperationOutcome("Running evaluate",
		 * IssueTypeList.INFORMATIONAL, IssueSeverityList.INFORMATION); return
		 * oo;
		 */
	}

	public static void loadDSSModules(MetaResourceDb md) throws JAXBException, IOException {
		DecisionSupportServiceModule d = JAXBUtil.fromXml(
				 edu.harvard.i2b2.fhir.Utils.fileToString("/example/fhir/dstu21/decisionsupportservicemodule-example.xml"),
				DecisionSupportServiceModule.class);
		md.addResource(d);

	}

	static private GuidanceResponse genGuidanceResponse(String requestId, String moduleId,
			GuidanceResponseStatusList statusValue, OperationOutcome oo//,Action a
			) throws JAXBException {

		GuidanceResponse g = new GuidanceResponse();
		g.setRequestId(FhirUtil.generateFhirString(requestId));

		if (moduleId != null) {
			Reference ref = new Reference();
			ref.setReference(FhirUtil.generateFhirString("DecisionSupportServiceModule/" + moduleId));
			g.setModule(ref);
		}
		
		
		GuidanceResponseStatus status = new GuidanceResponseStatus();
		status.setValue(statusValue);
		g.setStatus(status);
		if (oo != null) {
			Reference ref = new Reference();
			ref.setReference(FhirUtil.generateFhirString("OperationOutcome/" + oo.getId().getValue()));
			g.getEvaluationMessage().add(ref);
			g = (GuidanceResponse) FhirUtil.containResource(g, oo,oo.toString());
			logger.trace("called contain");
		}
		return g;
	}
}
