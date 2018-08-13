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
package edu.harvard.i2b2.lib.mapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public final class LoincMapper {

    // No instance
    private LoincMapper() {

        throw new AssertionError();
    }

	private static Map<String, String> loincCodeToNameMap;

	// Load LOINC Map
	static {

		try( final InputStream fis = LoincMapper.class.getResourceAsStream("/loinc/loincCodeToNameMap.bin");
		     final ObjectInputStream ois = new ObjectInputStream(fis)) {

			loincCodeToNameMap = (HashMap<String, String>) ois.readObject();

		} catch (ClassNotFoundException | IOException e) {

			throw new RuntimeException(e);
		}
	}



	public static String getLoincName(String loincNumber) {
	    return LoincMapper.loincCodeToNameMap.get(loincNumber);
	}




    //	public void initLoincCodeToNameMap() throws IOException {
//		String inputFilePath = inputDataFolder+"loinc.csv";
//		final int maxLines = Utils.countLines(inputFilePath);
//		CSV csv = CSV
//			    .separator(',')  // delimiter of fields
//			    .quote('"')      // quote character
//			    .create();       // new instance is immutable
//		//File csvData = new File(inputFilePath);
//		csv.read(inputFilePath, new CSVReadProc() {
//		    public void procRow(int rowIndex, String... values) {
//		     	List<String> arr=Arrays.asList(values);
//		        //logger.trace(rowIndex+""+Arrays.asList(values));
//		        if(rowIndex>0){
//		        	String num=arr.get(0);
//		        	String name=arr.get(29);
//		        	if(num.equals("789-8"))logger.trace(num+"->"+name);
//		        	loincCodeToNameMap.put(num,name);
//		        }
//		        if (rowIndex % 10000 == 0)
//					System.out.println((rowIndex * 100) / maxLines + "%-"
//							+loincCodeToNameMap.size());
//		    }
//		});
//	}
//	public void serializeLoincCodeToNameMap() throws IOException {
//
//		FileOutputStream fos = null;
//		ObjectOutputStream oos = null;
//
//		try {
//			fos = new FileOutputStream("loincCodeToNameMap.bin");
//
//			oos = new ObjectOutputStream(fos);
//			oos.writeObject(loincCodeToNameMap);
//
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		} finally {
//			fos.close();
//			oos.close();
//		}
//	}


//	static String readFile(String path, Charset encoding) throws IOException {
//		byte[] encoded = Files.readAllBytes(Paths.get(path));
//		return new String(encoded, encoding);
//	}
}