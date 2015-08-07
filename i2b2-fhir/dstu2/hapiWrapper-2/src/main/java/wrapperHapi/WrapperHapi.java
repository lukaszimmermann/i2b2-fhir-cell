package wrapperHapi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.IParser;

public class WrapperHapi {
	static FhirContext ctx = FhirContext.forDstu2();
	static IParser xmlParser = ctx.newXmlParser();
	static IParser jParser = ctx.newJsonParser();

	public static String resourceXmlToJson(String xmlString) throws IOException {
		IResource r = resourceXmlToIResource(xmlString);
		jParser.setPrettyPrint(true);
		String jsonString = jParser.encodeResourceToString(r);
		// System.out.println(jsonString);
		return jsonString;
	}
	
	public static IResource resourceXmlToIResource(String xmlString) throws IOException {
		return xmlParser.parseResource(xmlString);
	}
	
	
	/* hapi does not recognize feed element??
	 * public static String bundleXmlToJson(String xmlString) throws IOException {
		 Bundle b = xmlParser.parseBundle(xmlString);
		jParser.setPrettyPrint(true);
		String jsonString = jParser.encodeBundleToString(b);
		// System.out.println(jsonString);
		return jsonString;
	}
	*/
	
	public static String bundleToJson(Bundle b) throws IOException {
		jParser.setPrettyPrint(true);
		String jsonString = jParser.encodeBundleToString(b);
		// System.out.println(jsonString);
		return jsonString;
	}
	
	
	public static void main(String[] args) throws IOException {
		String xmlString =null;
		FileInputStream inputStream = new FileInputStream(
				"/Users/kbw19/tmp/new_git/res/i2b2-fhir/dstu2/xquery-2/src/main/resources/example/fhir/GeneralPatient.xml");
		xmlString = IOUtils.toString(inputStream);
		System.out.println(resourceXmlToJson(xmlString));
	}
}
