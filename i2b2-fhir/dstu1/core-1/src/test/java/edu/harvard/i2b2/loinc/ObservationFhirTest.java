/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

package edu.harvard.i2b2.loinc;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;
import edu.harvard.i2b2.map.MapSystemEngineTest;

public class ObservationFhirTest {

	static Logger logger = LoggerFactory.getLogger(ObservationFhirTest.class); 
	
	@Test
	public void test() throws JAXBException {
		String xml=Utils.getFile("example/fhir/MetaResourceSetLabs.xml");
		logger.trace(""+JAXBUtil.fromXml(xml, MetaResourceSet.class));
		
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
