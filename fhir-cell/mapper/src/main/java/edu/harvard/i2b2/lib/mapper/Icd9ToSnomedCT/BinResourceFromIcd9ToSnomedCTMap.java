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
package edu.harvard.i2b2.lib.mapper.Icd9ToSnomedCT;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import edu.harvard.i2b2.lib.mapper.Utils;
import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVReadProc;

/*
Download data from https://www.nlm.nih.gov/research/umls/mapping_projects/icd9cm_to_snomedct.html

to get codes from metathesaurus!
*/	
public class BinResourceFromIcd9ToSnomedCTMap {

	HashMap<String, String> Icd9ToSnomedCTMap;
	String inputDataFolder;
	final static String binFileName="/Icd9ToSnomedCTMap.bin";

	public BinResourceFromIcd9ToSnomedCTMap(String inputDataFolder) throws IOException {

		this.inputDataFolder = inputDataFolder;
		Icd9ToSnomedCTMap = new HashMap<String, String>();
		initIcd9SnomedCTMap();
		serializeIcd9CodeToNameMap();

	}

	private static final CSV csv = CSV.separator(';').quote('\'').skipLines(1)
			.charset("UTF-8").create();


	public void initIcd9SnomedCTMap() throws IOException {
		String inputFilePath = inputDataFolder+"ICD9CM_SNOMED_MAP_1TO1_201512.txt";
		final int maxLines = Utils.countLines(inputFilePath);
		CSV csv = CSV
			    .separator('\t')  // delimiter of fields
			    .quote('"')      // quote character
			    .create();       // new instance is immutable
		//File csvData = new File(inputFilePath);
		csv.read(inputFilePath, new CSVReadProc() {
		    public void procRow(int rowIndex, String... values) {
		     	List<String> arr=Arrays.asList(values);
		     	//if(rowIndex==1)logger.trace(rowIndex+"->"+Arrays.asList(values).get(0)+"-"+Arrays.asList(values).get(7));
		        if(rowIndex>0){
		        	String icd=arr.get(0).trim();
		        	String sno=arr.get(7).trim();
		        	//if(icd.contains("427.31"))logger.trace("<"+icd+">-><"+sno+">");
		        	//logger.trace(num+"->"+name);
		        	Icd9ToSnomedCTMap.put(icd,sno);
		        }
		        if (rowIndex % 1000 == 0)
					System.out.println((rowIndex * 100) / maxLines + "%-"
							+Icd9ToSnomedCTMap.size());
		    }
		});
	}

	public void serializeIcd9CodeToNameMap() throws IOException {

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream(binFileName);

			oos = new ObjectOutputStream(fos);
			oos.writeObject(Icd9ToSnomedCTMap);

		} catch (Exception e) {
			throw new RuntimeException(e);
			//logger.error(e.getMessage(), e);
		} finally {
			fos.close();
			oos.close();
		}
	}

	public static HashMap<String, String> deSerializeIcd9CodeToNameMap()
			throws IOException {
		HashMap<String, String> map = null;
		ObjectInputStream ois = null;
		InputStream fis = null;

		try {
			fis = BinResourceFromIcd9ToSnomedCTMap.class.getResourceAsStream(binFileName);
			//if(fis==null) logger.error("mapping file not found:"+binFileName);
			ois = new ObjectInputStream(fis);
			map = (HashMap<String, String>) ois.readObject();

		} catch (Exception e) {

			throw new RuntimeException(e);
		} finally {
			ois.close();
			fis.close();
		}

		return map;
	}

	public static void main(String[] args) {

		try {
			new BinResourceFromIcd9ToSnomedCTMap(
					"/Users/kbw19/Downloads/ICD9CM_SNOMEDCT_map_201512/");
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}