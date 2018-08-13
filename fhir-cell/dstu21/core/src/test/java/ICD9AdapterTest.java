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
 * 		July 4, 2015
 */



import java.io.IOException;

import javax.xml.bind.JAXBException;

import edu.harvard.i2b2.lib.mapper.Icd9.Icd9Mapper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ICD9AdapterTest {
	static Logger logger = LoggerFactory.getLogger(ICD9AdapterTest.class);


	@Test
	public void Icd9CodeToNameTest() throws IOException, JAXBException {
		Assert.assertEquals( "Retained plastic fragments", Icd9Mapper.getIcd9Name("V902"));
		logger.trace("->"+Icd9Mapper.getIcd9Name("V902"));
	}

}
