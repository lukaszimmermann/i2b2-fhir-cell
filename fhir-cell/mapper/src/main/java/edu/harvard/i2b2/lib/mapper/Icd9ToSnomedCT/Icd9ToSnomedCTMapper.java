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
package edu.harvard.i2b2.lib.mapper.Icd9ToSnomedCT;

import java.io.IOException;
import java.util.HashMap;


public class Icd9ToSnomedCTMapper {


    private Icd9ToSnomedCTMapper() {

        throw new AssertionError();
    }


	private static HashMap<String, String> map;

	static {
		try {
			map = BinResourceFromIcd9ToSnomedCTMap.deSerializeIcd9CodeToNameMap();
		} catch (IOException e) {

			throw new RuntimeException(e);
		}
	}

	public static String getSnomedCTCode(String icd9Code) {

	    return map.get(icd9Code);
	}
}
