package edu.harvard.i2b2;

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

public class PrepareBinaryResourceFilesFromRXNormData {
	static Logger logger = LoggerFactory
			.getLogger(PrepareBinaryResourceFilesFromRXNormData.class);

	HashMap<String, Integer> Ndc2CuiMap;
	HashMap<Integer, String> rxCuiMap;
	String rxNormFileFolder;

	public PrepareBinaryResourceFilesFromRXNormData(String rxNormFileFolder)
			throws IOException {

		this.rxNormFileFolder = rxNormFileFolder;
		initRxCuiMap();
		serializerxCuiMap();

	}




	public void initRxCuiMap() throws IOException {
		InputStream fileIS = null;
		BufferedReader reader = null;
		String nonEscapedPrimTT = "(BN#BPCK#DF#DFG#GPCK#IN#MIN#PIN#SBD#SBDC#SBDF#SBDG#SCD#SCDC#SCDF#SCDG)";
		String nonEscapedLine = "([0-9]*)|ENG||||||.*|.*|.*||(.*)|(.*)|.*|(.*)||(.*)|.*|";
		nonEscapedLine=nonEscapedLine.replace("||", "|.*|");
		nonEscapedLine=nonEscapedLine.replace("||", "|.*|");
		nonEscapedLine=nonEscapedLine.replace("||", "|.*|");
		
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
							"/Users/***REMOVED***/Downloads/RxNorm_full_06012015/rrf/RXNCONSO.RRF"));
			// logger.trace("read line:"+reader.readLine());

			String line = reader.readLine();
			int counter = 0;
			int matchedC = 0;
			boolean normFlag = false;
			Integer lastCui = 0;
			String prefName = null;
			String altName = null;
			while (line != null ) {
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
					
					Matcher matchTermTy=termPat.matcher(termType);
				

					if (normOrOb.equals("N") & matchTermTy.matches()	) {
						prefName = name;
					} else {
						//save longer name
						if(altName==null){altName = name;}
						else{ if(name.length()>altName.length()) altName=name;}
						
					}

					 //System.out.println(counter + "_" + matchedC + " matched:"
					 //+ cuiCode+"->"+prefName+"-"+altName+"-"+m.group(3)+"-"+matchTermTy.matches());

					if (lastCui != cuiCode) {
						// System.out.println("-->"
						// + cuiCode+"->"+prefName);

						if(prefName!=null) {rxCuiMap.put(cuiCode, prefName);}
						else{rxCuiMap.put(cuiCode, altName);}
						prefName = null;
						altName = null;
					}
					lastCui = cuiCode;

				}else{
					System.out.println("NOT matched:"+line);
				}
				line = reader.readLine();
				if(counter%10000==0)System.out.println((counter*100)/ 1100504 + "%-"+rxCuiMap.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
			// fileIS.close();
		}
	}
	

	public void serializerxCuiMap() throws IOException {

		FileOutputStream fos = null;
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream("rxCuiMap.bin");

			oos = new ObjectOutputStream(fos);
			oos.writeObject(rxCuiMap);

			fis = new FileInputStream("rxCuiMap.bin");
			ois = new ObjectInputStream(fis);
			HashMap<Integer, String> anotherList = (HashMap<Integer, String>) ois
					.readObject();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			fos.close();
			oos.close();
			ois.close();
			fis.close();
		}
	}
	
	public static HashMap<Integer, String> readCuiMap() throws IOException {

		HashMap<Integer, String> rxCuiMap2 = null;
		ObjectInputStream ois = null;
		FileInputStream fis = null;
	
		try {
				fis = new FileInputStream("rxCuiMap.bin");
			ois = new ObjectInputStream(fis);
			rxCuiMap2 = (HashMap<Integer, String>) ois
					.readObject();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			ois.close();
			fis.close();
		}
		
		return rxCuiMap2;
	}

	public static void main(String[] args) {

		try {
			new PrepareBinaryResourceFilesFromRXNormData(
					"/Users/***REMOVED***/Downloads/RxNorm_full_06012015/rrf");
			//System.out.println(PrepareBinaryResourceFilesFromRXNormData.readCuiMap().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}