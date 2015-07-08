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
package edu.harvard.i2b2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;

public class PreProcessFhirSpec {
	static Logger logger = LoggerFactory.getLogger(PreProcessFhirSpec.class);

	public PreProcessFhirSpec() throws IOException, XQueryUtilException {
		logger.trace("hi");
		ArrayList<String> resourceList = new ArrayList<String>();
		resourceList.add("Patient".toLowerCase());
		resourceList.add("Medication".toLowerCase());
		resourceList.add("MedicationStatement".toLowerCase());
		resourceList.add("Observation".toLowerCase());
		ZipFile zipFile = new ZipFile(Utils.getFilePath("fhir-spec.zip"));// fhir-all-xsd.zip
		logger.trace("" + zipFile.getName());

		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		
		for(String rName:resourceList){
			String fName="site/"+rName+".profile.xml";
			logger.trace(""+zipFile.getEntry(fName));
			String fContent=IOUtils.toString(zipFile.getInputStream(zipFile.getEntry(fName)));
			logger.trace(""+XQueryUtil.processXQuery("declare default element namespace \"http://hl7.org/fhir\";//searchParam",fContent));
			
		}
		
		/*while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if(!entry.getName().matches(".*\\.html$")) continue;
			Pattern p = Pattern.compile("^site/([^\\.\\\\]*)\\..*$");
			Matcher m = p.matcher(entry.getName());
			String rname="";
			if (m.matches()) {
				//logger.trace("" + entry.getName() + "->" + m.group(1));
				rname=m.group(1).toLowerCase();
			}
			
			if (resourceList.contains(rname))
				logger.trace(entry.getName()+"->" + rname);
			// InputStream stream = zipFile.getInputStream(entry);
			
		}*/

	}

	public static void main(String[] args) throws IOException, XQueryUtilException {
		new PreProcessFhirSpec();

	}

}
