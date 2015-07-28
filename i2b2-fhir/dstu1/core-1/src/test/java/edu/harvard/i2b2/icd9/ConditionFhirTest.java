package edu.harvard.i2b2.icd9;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.Icd9.Utils;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;
import edu.harvard.i2b2.map.MapSystemEngineTest;

public class ConditionFhirTest {

	static Logger logger = LoggerFactory.getLogger(ConditionFhirTest.class); 
	
	@Test
	public void test() throws JAXBException {
		String xml=Utils.getFile("example/fhir/MetaResourceSetDiagCondition.xml");
		MetaResourceSet ms=JAXBUtil.fromXml(xml, MetaResourceSet.class);
		logger.trace(""+JAXBUtil.toXml(ms));
		
	}

	@Test
	public void testParameterPattern() throws JAXBException {
		
		String input="Patient:Patient";
		input="subject:Patient";
		Pattern p = Pattern.compile("(\\w+):(\\w+):*(\\w+)");
		Matcher m=p.matcher(input);
		
		m.matches();
		String typeResource=m.group(1);
		logger.trace(""+m.matches()+"\ntypeResource:"+typeResource);
	}
}
