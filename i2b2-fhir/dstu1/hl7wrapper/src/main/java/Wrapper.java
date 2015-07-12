import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.html.parser.Parser;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Patient;
import org.hl7.fhir.instance.formats.JsonComposer;
import org.hl7.fhir.instance.formats.JsonParser;
import org.hl7.fhir.instance.formats.XmlParser;
import org.hl7.fhir.instance.model.Resource;


public class Wrapper {
	/*// Patient p =
	// JAXBUtil.fromXml(Utils.getFile("example/fhir/singlePatient.xml"),
	// Patient.class);//new Patient();
	public Wrapper() throws Exception {
		XmlParser xp = new XmlParser();
		InputStream is = new FileInputStream("example/fhir/singlePatient.xml");
		Resource r = xp.parse(is);
		System.out.print(r.getXmlId());
	}
	
	public static void main(String[] args) throws Exception{
		new Wrapper();
	}*/
	
	//static FhirContext ctx = FhirContext.forDstu1();
	//static IParser xmlParser = ctx.newXmlParser();
	//static IParser jParser = ctx.newJsonParser();

	public static String resourceXmlToJson(String xmlString) throws Exception {
		
		XmlParser xp = new XmlParser();
		FileInputStream inputStream = new FileInputStream(
				"/Users/***REMOVED***/tmp/new_git/res/i2b2-fhir/dstu1/xquery-1/src/main/resources/example/fhir/GeneralPatient.xml");
		
		Resource r =xp.parse(inputStream);
		JsonComposer jComposer=new JsonComposer();
		String jsonString = jComposer.composeString(r, true);
		return jsonString;
	}

	public static void main(String[] args) throws Exception {
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
