/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package edu.harvard.i2b2.rxnorm;

import java.io.BufferedReader;
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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Code;
import org.hl7.fhir.Coding;
import org.hl7.fhir.Medication;
import org.hl7.fhir.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.query.QueryBuilder;

public class BinResourceFromRXNormData {
	static Logger logger = LoggerFactory
			.getLogger(BinResourceFromRXNormData.class);

	HashMap<String, Integer> Ndc2CuiMap;
	HashMap<Integer, String> rxCuiMap;
	String rxNormFileFolder;

	public BinResourceFromRXNormData(String rxNormFileFolder)
			throws IOException {

		this.rxNormFileFolder = rxNormFileFolder;
		initRxCuiMap();
		serializerxCuiMap();
		initNdc2CuiMap();
		serializeNdc2CuiMap();

	}

	public void initRxCuiMap() throws IOException {
		String inputFilePath = "/Users/kbw19/Downloads/RxNorm_full_06012015/rrf/RXNCONSO.RRF";
		int maxLines = Utils.countLines(inputFilePath);

		InputStream fileIS = null;
		BufferedReader reader = null;
		String nonEscapedPrimTT = "(BN#BPCK#DF#DFG#GPCK#IN#MIN#PIN#SBD#SBDC#SBDF#SBDG#SCD#SCDC#SCDF#SCDG)";
		String nonEscapedLine = "([0-9]*)|ENG||||||.*|.*|.*||(.*)|(.*)|.*|(.*)||(.*)|.*|";
		nonEscapedLine = nonEscapedLine.replace("||", "|.*|");
		nonEscapedLine = nonEscapedLine.replace("||", "|.*|");
		nonEscapedLine = nonEscapedLine.replace("||", "|.*|");

		// "2|ENG||||||3091081||N0000007747||NDFRT|SY|N0000007747|1,2-Dihexadecyl-sn-Glycerophosphocholine||N||";
		// "2|ENG||||||[0-9]*||N0000007747||NDFRT|SY|N0000007747|1,2-Dihexadecyl-sn-Glycerophosphocholine||N||";
		String escapedLine = nonEscapedLine.replace("|", "\\|").replace("#",
				"|");
		String escapedPrimTT = nonEscapedPrimTT.replace("|", "\\|").replace(
				"#", "|");
		Pattern linePat = Pattern.compile(escapedLine);
		Pattern termPat = Pattern.compile(escapedPrimTT);

		try {
			rxCuiMap = new HashMap<Integer, String>();
			String fileName = rxNormFileFolder + "/RXNCONSO.RRF";
			fileIS = Utils.getInputStream(fileName);
			// if (fileIS == null)
			// throw new IllegalArgumentException("file not found:" + fileName);

			reader = new BufferedReader(
					new FileReader(
							"/Users/kbw19/Downloads/RxNorm_full_06012015/rrf/RXNCONSO.RRF"));
			// logger.trace("read line:"+reader.readLine());

			String line = reader.readLine();
			int counter = 0;
			int matchedC = 0;
			boolean normFlag = false;
			Integer lastCui = 0;
			String prefName = null;
			String altName = null;
			while (line != null) {
				counter++;
				Matcher m = linePat.matcher(line);

				if (m.matches()) {
					matchedC++;
					// System.out.println(counter + "_" + matchedC + " matched:"
					// + line);

					Integer cuiCode = Integer.parseInt(m.group(1));
					String termType = m.group(3);
					String name = m.group(4);
					String normOrOb = m.group(5);

					Matcher matchTermTy = termPat.matcher(termType);

					if (normOrOb.equals("N") & matchTermTy.matches()) {
						prefName = name;
					} else {
						// save longer name
						if (altName == null) {
							altName = name;
						} else {
							if (name.length() > altName.length())
								altName = name;
						}

					}

					// System.out.println(counter + "_" + matchedC + " matched:"
					// +
					// cuiCode+"->"+prefName+"-"+altName+"-"+m.group(3)+"-"+matchTermTy.matches());

					if (lastCui != cuiCode) {
						// System.out.println("-->"
						// + cuiCode+"->"+prefName);

						if (prefName != null) {
							rxCuiMap.put(cuiCode, prefName);
						} else {
							rxCuiMap.put(cuiCode, altName);
						}
						prefName = null;
						altName = null;
					}
					lastCui = cuiCode;

				} else {
					System.out.println("NOT matched:" + line);
				}
				line = reader.readLine();
				if (counter % 10000 == 0)
					System.out.println((counter * 100) / maxLines + "%-"
							+ rxCuiMap.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
			// fileIS.close();
		}
	}

	public void initNdc2CuiMap() throws IOException {
		String inputFilePath = "/Users/kbw19/Downloads/RxNorm_full_06012015/rrf/RXNSAT.RRF";
		int maxLines = Utils.countLines(inputFilePath);
		Ndc2CuiMap = new HashMap<String, Integer>();
		InputStream fileIS = null;
		BufferedReader reader = null;
		String nonEscapedLine = "([0-9]*)||||||||NDC|(.*)|(.*)|||";
		nonEscapedLine = nonEscapedLine.replace("||", "|.*|");
		nonEscapedLine = nonEscapedLine.replace("||", "|.*|");
		nonEscapedLine = nonEscapedLine.replace("||", "|.*|");

		String escapedLine = nonEscapedLine.replace("|", "\\|").replace("#",
				"|");

		if (1 == 0) {
			System.out.println(escapedLine);
			return;
		}
		Pattern linePat = Pattern.compile(escapedLine);
		String line = null;
		try {
			rxCuiMap = new HashMap<Integer, String>();

			reader = new BufferedReader(new FileReader(inputFilePath));
			// logger.trace("read line:"+reader.readLine());

			line = reader.readLine();
			int counter = 0;
			int matchedC = 0;
			while (line != null) {
				counter++;
				Matcher m = linePat.matcher(line);

				if (m.matches()) {
					matchedC++;
					Integer cuiCode = Integer.parseInt(m.group(1));
					String ndc = m.group(3);
					// System.out.println("matched:" + ndc + "-" + cuiCode);
					Ndc2CuiMap.put(ndc, cuiCode);
				} else {
					// System.out.println("NOT matched:" + line);
				}
				line = reader.readLine();
				if (counter % 100000 == 0)
					System.out.println((counter * 100) / maxLines + "%-"
							+ Ndc2CuiMap.size());
			}
		} catch (Exception e) {
			System.out.println("" + line);
			e.printStackTrace();
		} finally {
			reader.close();
			// fileIS.close();
		}
	}

	public void serializeNdc2CuiMap() throws IOException {

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream("ndc2CuiMap.bin");

			oos = new ObjectOutputStream(fos);
			oos.writeObject(Ndc2CuiMap);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			fos.close();
			oos.close();
		}
	}

	public void serializerxCuiMap() throws IOException {

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream("rxCuiMap.bin");

			oos = new ObjectOutputStream(fos);
			oos.writeObject(rxCuiMap);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			fos.close();
			oos.close();
		}
	}

	public static HashMap<Integer, String> deSerializeRxCuiMap()
			throws IOException {

		HashMap<Integer, String> rxCuiMap2 = null;
		ObjectInputStream ois = null;
		InputStream fis = null;

		try {
			fis = Utils.getInputStream("rxnorm/rxCuiMap.bin");
			ois = new ObjectInputStream(fis);
			rxCuiMap2 = (HashMap<Integer, String>) ois.readObject();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			ois.close();
			fis.close();
		}

		return rxCuiMap2;
	}

	public static HashMap<String, Integer> deSerializeNdc2CuiMap()
			throws IOException {

		HashMap<String, Integer> Ndc2CuiMap2 = null;
		ObjectInputStream ois = null;
		InputStream fis = null;

		try {
			fis = Utils.getInputStream("rxnorm/ndc2CuiMap.bin");
			ois = new ObjectInputStream(fis);
			Ndc2CuiMap2 = (HashMap<String, Integer>) ois.readObject();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			ois.close();
			fis.close();
		}

		return Ndc2CuiMap2;
	}

	public static void main(String[] args) {

		try {
			new BinResourceFromRXNormData(
					"/Users/kbw19/Downloads/RxNorm_full_06012015/rrf");
			// System.out.println(PrepareBinaryResourceFilesFromRXNormData.readCuiMap().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}