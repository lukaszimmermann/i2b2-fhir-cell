/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 17, 2015
 */
package edu.harvard.i2b2.loinc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.apache.commons.csv.CSVFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReadProc;
import au.com.bytecode.opencsv.CSVReader;

public class BinResourceFromLoincData {
	static Logger logger = LoggerFactory
			.getLogger(BinResourceFromLoincData.class);

	HashMap<String, String> loincCodeToNameMap;
	String inputDataFolder;

	public BinResourceFromLoincData(String inputDataFolder) throws IOException {

		this.inputDataFolder = inputDataFolder;
		loincCodeToNameMap = new HashMap<String, String>();
		initLoincCodeToNameMap();
		serializeLoincCodeToNameMap();

	}

	private static final CSV csv = CSV.separator(';').quote('\'').skipLines(1)
			.charset("UTF-8").create();

	public void initLoincCodeToNameMap() throws IOException {
		String inputFilePath = inputDataFolder+"loinc.csv";
		final int maxLines = Utils.countLines(inputFilePath);
		CSV csv = CSV
			    .separator(',')  // delimiter of fields
			    .quote('"')      // quote character
			    .create();       // new instance is immutable
		//File csvData = new File(inputFilePath);
		csv.read(inputFilePath, new CSVReadProc() {
		    public void procRow(int rowIndex, String... values) {
		     	List<String> arr=Arrays.asList(values);
		        //logger.trace(rowIndex+""+Arrays.asList(values));
		        if(rowIndex>0){
		        	String num=arr.get(0);
		        	String name=arr.get(29);
		        	if(num.equals("789-8"))logger.trace(num+"->"+name);
		        	loincCodeToNameMap.put(num,name);
		        }
		        if (rowIndex % 10000 == 0)
					System.out.println((rowIndex * 100) / maxLines + "%-"
							+loincCodeToNameMap.size());
		    }
		});
	}

	public void serializeLoincCodeToNameMap() throws IOException {

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream("loincCodeToNameMap.bin");

			oos = new ObjectOutputStream(fos);
			oos.writeObject(loincCodeToNameMap);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			fos.close();
			oos.close();
		}
	}

	public static HashMap<String, String> deSerializeLoincCodeToNameMap()
			throws IOException {

		HashMap<String, String> map = null;
		ObjectInputStream ois = null;
		InputStream fis = null;

		try {
			fis = BinResourceFromLoincData.class.getResourceAsStream("/loinc/loincCodeToNameMap.bin");
			ois = new ObjectInputStream(fis);
			map = (HashMap<String, String>) ois.readObject();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			ois.close();
			fis.close();
		}

		return map;
	}

	public static void main(String[] args) {

		try {
			new BinResourceFromLoincData(
					"/Users/kbw19/Downloads/LOINC_252_Text/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}