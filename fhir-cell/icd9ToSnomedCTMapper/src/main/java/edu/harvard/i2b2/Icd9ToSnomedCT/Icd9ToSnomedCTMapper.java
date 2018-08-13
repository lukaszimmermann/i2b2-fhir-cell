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
package edu.harvard.i2b2.Icd9ToSnomedCT;

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

public class Icd9ToSnomedCTMapper {
	static Logger logger = LoggerFactory.getLogger(Icd9ToSnomedCTMapper.class);

	static HashMap<String, String> map;

	public Icd9ToSnomedCTMapper() throws IOException {
		init();
	}

	private void init() throws IOException {

		if (map == null)
			map = BinResourceFromIcd9ToSnomedCTMap.deSerializeIcd9CodeToNameMap();

	}

	public String getSnomedCTCode(String icd9Code) {

		if (icd9Code != null) {
			String sno = map.get(icd9Code);
			logger.trace("icd9Number:<" + icd9Code + ">" + " -> snomed-ct:<" + sno + ">");
			return sno;
		} else {
			return null;
		}
	}

}