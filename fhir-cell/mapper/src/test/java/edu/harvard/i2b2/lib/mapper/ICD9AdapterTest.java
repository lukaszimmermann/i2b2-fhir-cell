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

package edu.harvard.i2b2.lib.mapper;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import edu.harvard.i2b2.lib.mapper.Icd9.Icd9Mapper;
import org.junit.Assert;
import org.junit.Test;

public class ICD9AdapterTest {

	@Test
	public void Icd9CodeToNameTest() throws IOException, JAXBException {
		Assert.assertEquals("Retained plastic fragments", Icd9Mapper.getIcd9Name("V902"));
		//
		//logger.trace("->"+rA.getIcd9Name("V25.41"));
		assertEquals("Surveillance of contraceptive pill", Icd9Mapper.getIcd9Name("V25.41"));
		//logger.trace("->"+rA.getIcd9Name("379.93"));;
		assertEquals("Redness or discharge of eye",Icd9Mapper.getIcd9Name("379.93"));
	}
}
