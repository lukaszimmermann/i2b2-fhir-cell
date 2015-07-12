package wrapperHapi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.IParser;

public class WrapperHapi {
	static FhirContext ctx = FhirContext.forDstu1();
	static IParser xmlParser = ctx.newXmlParser();
	static IParser jParser = ctx.newJsonParser();

	public static String resourceXmlToJson(String xmlString) throws IOException {
		IResource r = xmlParser.parseResource(xmlString);
		jParser.setPrettyPrint(true);
		String jsonString = jParser.encodeResourceToString(r);
		// System.out.println(jsonString);
		return jsonString;
	}

	public static void main(String[] args) throws IOException {
		String xmlString = "<Patient xmlns=\"http://hl7.org/fhir\">"
				+ "<text><status value=\"generated\" /><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal</div></text>"
				+ "<identifier><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
				+ "<name><use value=\"official\" /><family value=\"Cardinal\" /><given value=\"John\" /></name>"
				+ "<gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\" /><code value=\"M\" /></coding></gender>"
				+ "<address><use value=\"home\" /><line value=\"2222 Home Street\" /></address><active value=\"true\" />"
				+ "</Patient>";
		FileInputStream inputStream = new FileInputStream(
				"/Users/***REMOVED***/tmp/new_git/res/i2b2-fhir/dstu1/xquery-1/src/main/resources/example/fhir/GeneralPatient.xml");
		xmlString = IOUtils.toString(inputStream);
		System.out.println(resourceXmlToJson(xmlString));
	}
}
