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
package edu.harvard.i2b2.rxnorm;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RxNormAdapterTest {
	static Logger logger = LoggerFactory.getLogger(RxNormAdapterTest.class);

	NdcToRxNormMapper rA;

	@Before
	public void setup() throws IOException {
		rA = new NdcToRxNormMapper();
	}

	@Test
	public void test() throws IOException, JAXBException {
		assertEquals( "104097",rA.getRxCui("00002314530"));
		logger.trace(rA.getRxCuiName("753482"));
	}

}
