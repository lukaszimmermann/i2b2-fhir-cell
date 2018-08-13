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

import edu.harvard.i2b2.lib.mapper.rxnorm.NdcToRxNormMapper;
import org.junit.Test;



public class RxNormAdapterTest {

	@Test
	public void test() {
		assertEquals( "104097",NdcToRxNormMapper.getRxCui("00002314530"));
	}
}

