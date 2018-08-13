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
package edu.harvard.i2b2.lib.mapper.rxnorm;

import java.io.IOException;
import java.util.Map;

public final class NdcToRxNormMapper {

    // No instance
    private NdcToRxNormMapper() {

        throw new AssertionError();
    }

	private static Map<String, Integer> Ndc2CuiMap;
	private static Map<Integer, String> rxCuiMap;

	static {

	    try {
            rxCuiMap = BinResourceFromRXNormData.deSerializeRxCuiMap();
            Ndc2CuiMap = BinResourceFromRXNormData.deSerializeNdc2CuiMap();
        } catch(IOException e) {

	        throw new RuntimeException(e);
        }
    }

	public static String getRxCui(String ndcString) {

		Integer intCode = Ndc2CuiMap.get(ndcString);

		if (intCode != null) {
			return Ndc2CuiMap.get(ndcString).toString();
		}
		return null;
	}

	public static String getRxCuiName(String rxCuiStr) {
		Integer rxCui = Integer.parseInt(rxCuiStr);
        return rxCuiMap.get(rxCui);
	}
}
