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


import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.Icd9.Icd9Mapper;


public class ICD9AdapterTest {
	static Logger logger = LoggerFactory.getLogger(ICD9AdapterTest.class);

	Icd9Mapper rA;

	@Before
	public void setup() throws IOException {
		rA = new Icd9Mapper();
	}

	@Test
	public void Icd9CodeToNameTest() throws IOException, JAXBException {
		assertEquals( "Retained plastic fragments",rA.getIcd9Name("V902"));
		logger.trace("->"+rA.getIcd9Name("V902"));
	}

}
