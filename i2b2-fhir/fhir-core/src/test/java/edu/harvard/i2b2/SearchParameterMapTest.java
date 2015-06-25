package edu.harvard.i2b2;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hl7.fhir.Patient;
import org.hl7.fhir.StructureDefinition;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.query.SearchParameterMap;

public class SearchParameterMapTest {
	static Logger logger = LoggerFactory.getLogger(SearchParameterMapTest.class);

	@Test
	public void testInit() {
		try{
		JAXBContext context = JAXBContext.newInstance(StructureDefinition.class);
		Unmarshaller um = context.createUnmarshaller();
		StructureDefinition p = null;
			//logger.trace(">" + c.getSimpleName());
			String xml = Utils.getFile("profiles/"
					+"medicationstatement"+ ".profile.xml");

			p = (StructureDefinition) um.unmarshal(new ByteArrayInputStream(xml
					.getBytes(StandardCharsets.UTF_8)));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//SearchParameterMap m=new SearchParameterMap();
		//logger.trace("-->"+m.getParameterPath(Patient.class, "gender"));
		//logger.trace("-->"+m.getType(Patient.class, "gender"));
	}
}
