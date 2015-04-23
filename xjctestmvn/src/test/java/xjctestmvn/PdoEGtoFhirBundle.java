package xjctestmvn;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hl7.fhir.Medication;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import core.ResourceDb;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.parser.Parser;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtil;


public class PdoEGtoFhirBundle {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	public void test() {
		String query = "declare namespace ns=\"http://hl7.org/fhir\";"
				+ "\n//(ns:Medication|ns:Patient)";
		String input = Utils.getFile("example/fhir/mixedResource.xml");

		List<String> xmlList = XQueryUtil.getStringSequence(input, query);
		List<Resource> resList = FhirUtil.xmlToResource(xmlList);
		System.out
				.println(FhirUtil.getResourceBundle(resList, "uriInfoString"));
	}

	// @Test
	public void validate() {
		// URL path=FhirUtil.class.getResource("validation.zip");
		// System.out.println(FhirUtil.isValid(Utils.getFile("example/fhir/singlePatient.xml")));
		String msg = FhirUtil.getValidatorErrorMessage(Utils
				.getFile("example/fhir/singlePatientInvalid.xml"));
		assertEquals(true, FhirUtil.isValid(Utils
				.getFile("example/fhir/singlePatient.xml")));
		assertEquals(false, FhirUtil.isValid(Utils
				.getFile("example/fhir/singlePatientInvalid.xml")));
		assertEquals(
				"Unknown Content familys @  START_TAG seen ...<use value=\"usual\"/>\n    <familys value=\"van de Heuvel\"/>... @93:37",
				msg);
	}

	// @Test
	public void search() {
		ResourceDb resDb = new ResourceDb();
		Resource r = FhirUtil.xmlToResource(Utils
				.getFile("example/fhir/singlePatient.xml"));
		// r.setId("1");
		resDb.addResource(r, Patient.class);
		resDb.addResource(r, Patient.class);
		// System.out.println(FhirUtil.resourceToXml(r));
		System.out.println(FhirUtil.resourceToXml(resDb.getResource("1",
				Patient.class)));

	}

	// @Test
	public void readBundle() throws ParseException, IOException {
		ResourceDb resDb = new ResourceDb();

		Abdera abdera = new Abdera();
		Parser parser = abdera.getParser();

		String path = Utils.getFilePath("example/fhir/PatientBundle.xml");
		URL url = new URL("file://" + path);
		Document<Feed> doc = parser.parse(url.openStream(), url.toString());
		Feed feed = doc.getRoot();
		System.out.println(feed.getTitle());
		for (Entry entry : feed.getEntries()) {
			System.out.println("\t" + entry.getTitle());
			System.out.println();
			// System.out.println("\t" + entry.getContent());
			if (!FhirUtil.isValid(entry.getContent())) {
				throw new RuntimeException(
						"entry is not a valid Fhir Resource:"
								+ entry.getId()
								+ FhirUtil.getValidatorErrorMessage(entry
										.getContent()));
			}
			Resource r = FhirUtil.xmlToResource(entry.getContent());
			resDb.addResource(r, Patient.class);

		}
		// System.out.println (feed.getAuthor());

	}

	
	@Test
	public void Test23(){
		System.out.println(defaultread());
	}
	static public String defaultread() {
		String query = Utils
				.getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMedStatement.xquery");
		String input = Utils.getFile("example/i2b2/medicationsForAPatient.xml");

		return XQueryUtil.processXQuery( query,input).toString();
	}

}