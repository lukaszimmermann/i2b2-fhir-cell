package wrapperHapi;

import static org.junit.Assert.*;

import java.io.IOException;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;

public class MedicationPrescriptionTest {
	static Logger logger = LoggerFactory.getLogger(MedicationPrescriptionTest.class);
	@Test
	public void test() throws IOException {
		//String inputXml=Utils.getFile("MedicationPrescriptionWithContainedMedication.xml");
		//FhirContext ctx = FhirContext.forDstu2();
		//IBaseResource parsed = ctx.newXmlParser().parseResource(inputXml);
		//String outputJson = ctx.newJsonParser().encodeResourceToString(parsed);
		//logger.trace(""+outputJson);
		logger.trace(""+WrapperHapi.resourceXmlToJson(Utils.getFile("BundleContained4.xml")));
		

	}

}
