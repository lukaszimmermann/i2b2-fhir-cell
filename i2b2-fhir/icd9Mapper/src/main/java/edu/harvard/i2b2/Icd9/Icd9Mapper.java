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
package edu.harvard.i2b2.Icd9;

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

public class Icd9Mapper {
	static Logger logger = LoggerFactory.getLogger(Icd9Mapper.class);


	HashMap<String, String> Icd9CodeToNameMap;

	public Icd9Mapper() throws IOException {
		init();
	}

	private void init() throws IOException {
		Icd9CodeToNameMap=BinResourceFromIcd9Data.deSerializeIcd9CodeToNameMap();
	}

	

	
	public String getIcd9Name(String icd9Number) {
		icd9Number=icd9Number.replace(".","");
		logger.trace("icd9Number:<"+icd9Number+">");
		if (icd9Number != null) {
			return Icd9CodeToNameMap.get(icd9Number);
		} else {
			return null;
		}
	}
	
	
}