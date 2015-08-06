/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package edu.harvard.i2b2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.Resource;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.WebServiceCall;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;

public class WSi2b2 {

	// @Test
	public void test() throws XQueryUtilException {
		String request = Utils
				.getFile("i2b2query/i2b2RequestMedsForAPatient.xml");
		String query = Utils
				.getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMedStatement.xquery");

		String oStr=WebServiceCall.run("http://services.i2b2.org:9090/i2b2/services/QueryToolService/pdorequest",request);
				
		String xQueryResultString = XQueryUtil.processXQuery(query, oStr);
		System.out.println(xQueryResultString);
	}

	// @Test
	public void test2() throws XQueryUtilException {
		String request = Utils
				.getFile("i2b2query/i2b2RequestMedsForAPatient.xml");
		String query = "declare namespace ns3=\"http://www.i2b2.org/xsd/cell/crc/pdo/1.1/\";"
				+ "copy $c :=//fn:root() "
				+ "modify("
				+ "replace value of node $c//message_body/ns3:request/input_list/patient_list/patient_id with 'BaseX'"
				+ ")" + " return $c";
		// "replace value of node //message_body/ns3:request/input_list/patient_list/patient_id/text() with '222' "+
		// " //message_body/ns3:request/input_list/patient_list/patient_id ";
		// String query="replace value of node /n with 'newValue'";
		String xQueryResultString = XQueryUtil.processXQuery(query, request);
		System.out.println(xQueryResultString);
	}

	// @Test
	public void Test3() throws JAXBException, XQueryUtilException, IOException {
		MetaResourceDb md = new MetaResourceDb();
		String query = Utils
				.getFile("transform/I2b2ToFhir/i2b2PatientToFhirPatient.xquery");
		
		String requestStr = Utils.getFile("i2b2query/getAllPatients.xml");
		String oStr=WebServiceCall.run("http://services.i2b2.org:9090/i2b2/services/QueryToolService/pdorequest",requestStr);
		
		System.out.println("got::"
				+ oStr.substring(0, (oStr.length() > 200) ? 200 : 0));

		// if(1==1)return Response.ok().type(MediaType.APPLICATION_XML)
		// .entity(oStr).build();
		String xQueryResultString = XQueryUtil.processXQuery(query, oStr);
		// System.out.println(xQueryResultString);
		Bundle b = JAXBUtil.fromXml(xQueryResultString, Bundle.class);
		List<Resource> s = FhirUtil.getResourceListFromBundle(b);
		System.out.println(JAXBUtil.toXml(s.get(0)));
		md.addResourceList(s);
	}

	static void writeFileBytes(String filename, String content) {
		try {
			Files.write(FileSystems.getDefault().getPath(".", filename),
					content.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Test
	public void Test4() throws XQueryUtilException {
		String requestStr = Utils.getFile("i2b2query/getAllPatients.xml");
		String query = "replace node / with <a/>";
		String xQueryResultString = XQueryUtil.processXQuery(query, requestStr);
		System.out.println("RES:" + xQueryResultString);
	}

}
