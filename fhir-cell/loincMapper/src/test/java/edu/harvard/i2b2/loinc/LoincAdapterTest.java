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

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoincAdapterTest {
	static Logger logger = LoggerFactory.getLogger(LoincAdapterTest.class);

	LoincMapper rA;

	@Before
	public void setup() throws IOException {
		rA = new LoincMapper();
	}

	@Test
	public void loincCodeToNameTest() throws IOException, JAXBException {
		assertEquals( "Erythrocytes [#/volume] in Blood by Automated count",rA.getLoincName("789-8"));
		//logger.trace("->"+rA.getLoincName("789-8"));
	}

}
