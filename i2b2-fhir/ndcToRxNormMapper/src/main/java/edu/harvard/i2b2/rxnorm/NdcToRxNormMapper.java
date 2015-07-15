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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NdcToRxNormMapper {
	static Logger logger = LoggerFactory.getLogger(NdcToRxNormMapper.class);

	HashMap<String, Integer> Ndc2CuiMap;
	HashMap<Integer, String> rxCuiMap;

	public NdcToRxNormMapper() throws IOException {
		init();
	}

	private void init() throws IOException {
		initNdc2CuiMap();
		initRxCuiMap();
	}

	private void initRxCuiMap() throws IOException {
		rxCuiMap=BinResourceFromRXNormData.deSerializeRxCuiMap();
	
	}

	private void initNdc2CuiMap() throws IOException {
		Ndc2CuiMap=BinResourceFromRXNormData.deSerializeNdc2CuiMap();
	}

	public String getRxCui(String ndcString) {
		Integer intCode = Ndc2CuiMap.get(ndcString);
		if (intCode != null) {
			return Ndc2CuiMap.get(ndcString).toString();
		} else {
			return null;
		}
	}
	
	public String getRxCuiName(String rxCuiStr) {
		Integer rxCui=Integer.parseInt(rxCuiStr);
		if (rxCui!= null) {
			return rxCuiMap.get(rxCui);
		} else {
			return null;
		}
	}
	
	
}