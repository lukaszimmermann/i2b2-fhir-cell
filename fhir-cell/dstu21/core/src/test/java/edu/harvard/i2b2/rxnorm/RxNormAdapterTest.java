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

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package edu.harvard.i2b2.rxnorm;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Medication;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;

public class RxNormAdapterTest {
	static Logger logger = LoggerFactory.getLogger(RxNormAdapterTest.class);

	RxNormAdapter rA;

	@Before
	public void setup() throws IOException {
		rA = new RxNormAdapter();
	}

	@Test
	public void test() throws IOException, JAXBException {
		assertEquals( "104097",rA.getRxCui("00002314530"));
		Medication  m=JAXBUtil.fromXml(Utils.getFile("example/fhir/Medication.xml"), Medication.class);
		//rA.addRxCui(m);
		rA.addRxCui();
		
		logger.trace(rA.getRxCuiName("753482"));
	}

}
