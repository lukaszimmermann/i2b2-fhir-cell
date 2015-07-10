package edu.harvard.i2b2;

import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.MetaData;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class JsonTest {
	static Logger logger = LoggerFactory.getLogger(JsonTest.class);
	
	//@Test
	public  void jsonPatientTest()
			throws DatatypeConfigurationException, JSONException, JAXBException {
		
		
		Patient p = JAXBUtil.fromXml(Utils.getFile("example/fhir/singlePatient.xml"), Patient.class);//new Patient();
		p.setId("Patient/1000000005");
		//JSONObject j=FhirUtil.resourceToJson(p);
		//logger.trace(""+j.toString(2));
		
	}
	
	@Test
	public  void jsonSimplifiedPatientTest()
			throws DatatypeConfigurationException, JSONException, JAXBException {
		
		
		Patient p = JAXBUtil.fromXml(Utils.getFile("example/fhir/simplifiedGeneralPatient.xml"), Patient.class);//new Patient();
		//logger.trace(""+JAXBUtil.toXml(p));
		String s=FhirUtil.resourceToJsonString(p);
		logger.info("->"+s);
		
	}
}
