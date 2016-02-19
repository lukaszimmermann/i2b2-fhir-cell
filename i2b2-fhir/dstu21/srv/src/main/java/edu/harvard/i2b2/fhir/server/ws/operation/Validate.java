package edu.harvard.i2b2.fhir.server.ws.operation;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;

public class Validate {
	static Logger logger = LoggerFactory.getLogger(Validate.class);
	
	//TODO validate against profiles
	public static String runValidate(String inTxt,String profile) throws JAXBException {
		String outTxt = "-";
		/*
		 * Resource r = JAXBUtil.fromXml(inTxt, Resource.class); Class c =
		 * FhirUtil.getResourceClass(r); logger.debug("Resource is of type:" +
		 * c); // logger.debug(" got Resource:"+JAXBUtil.toXml(r));
		 * 
		 * Bundle s = null; if (Bundle.class.isInstance(r)) { s = (Bundle) r; }
		 */
		//String ooTxt = FhirUtil.getValidatorErrorMessage(inTxt);
		String ooTxt = FhirUtil.getValidatorErrorMessageForProfile(inTxt,profile);
		outTxt = ooTxt;
	
		logger.trace("ooTxt:" + ooTxt);
		// OperationOutcome oo=JAXBUtil.fromXml(ooTxt, OperationOutcome.class);
		return outTxt;
	}
}
