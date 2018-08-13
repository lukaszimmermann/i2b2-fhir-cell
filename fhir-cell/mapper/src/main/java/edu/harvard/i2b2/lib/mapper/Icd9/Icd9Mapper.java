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
package edu.harvard.i2b2.lib.mapper.Icd9;

import java.io.IOException;
import java.util.Map;

public final class Icd9Mapper {

    private Icd9Mapper() {

        throw new AssertionError();
    }

	private static Map<String, String> Icd9CodeToNameMap;
    static {

        try {

            Icd9CodeToNameMap=BinResourceFromIcd9Data.deSerializeIcd9CodeToNameMap();
        }
        catch(IOException e) {

            throw new RuntimeException(e);
        }
    }

	public static String getIcd9Name(String icd9Number) {

		icd9Number = icd9Number.replace(".","");
        return Icd9CodeToNameMap.get(icd9Number);
	}
}
