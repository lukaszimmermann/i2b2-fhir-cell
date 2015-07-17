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

public class LoincMapper {
	static Logger logger = LoggerFactory.getLogger(LoincMapper.class);


	HashMap<String, String> loincCodeToNameMap;

	public LoincMapper() throws IOException {
		init();
	}

	private void init() throws IOException {
		loincCodeToNameMap=BinResourceFromLoincData.deSerializeLoincCodeToNameMap();
	}

	

	
	public String getLoincName(String loincNumber) {
		if (loincNumber != null) {
			return loincCodeToNameMap.get(loincNumber);
		} else {
			return null;
		}
	}
	
	
}