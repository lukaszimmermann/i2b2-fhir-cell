package xjctestmvn;

import static org.junit.Assert.*;

import java.util.List;

import org.hl7.fhir.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.FhirUtil;
import utils.Utils;
import utils.XQueryUtil;

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

	@Test
	public void test() {
	 String query="declare namespace ns=\"http://hl7.org/fhir\";"+"\n//(ns:Medication|ns:Patient)";
	 String input=Utils.getFile("example/fhir/mixedResource.xml");

	List<String> xmlList=XQueryUtil.getStringSequence(input, query);
	List<Resource> resList=FhirUtil.xmlToResource(xmlList);
	System.out.println(FhirUtil.getResourceBundle(resList,"uriInfoString"));
	
	}

}
