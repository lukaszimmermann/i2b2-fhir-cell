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

package edu.harvard.i2b2.fhir;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.core.Project;

public class I2b2Util {
	static Logger logger = LoggerFactory.getLogger(I2b2Util.class);

	public static String insertI2b2ParametersInXml(String xml, String username,
			String password, String i2b2Url, String I2b2Domain,
			String project) throws XQueryUtilException {

		xml = insertI2b2ParametersInXml(xml, username, password, i2b2Url,
				I2b2Domain);
		xml = replaceXMLString(xml, "//project_id", project);
		return xml;
	}

	public static String insertI2b2ParametersInXml(String xml, String username,
			String authToken, String i2b2Url, String I2b2Domain)
			throws XQueryUtilException {
		xml = replaceXMLString(xml, "//security/username", username);
		xml = replaceXMLString(xml, "//security/password", authToken);
		xml = replaceXMLString(xml, "//security/domain", I2b2Domain);
		xml = replaceXMLString(xml, "//proxy/redirect_url", i2b2Url
				+ "/services/QueryToolService/pdorequest");
		// logger.info("returning xml:"+xml);

		/*
		 * is_token=\"false attribute of password does not make a difference if
		 * set to false. Both token and password can be used.if session key: is
		 * appended to token
		 */
		// if password is token then indicate so
		/*
		 * if (authToken.contains("SessionKey:")) { } else { xml =
		 * xml.replaceAll("<password is_token=\"false\">",
		 * "<password is_token=\"true\">"); }
		 */
		return xml;
	}

	private static String replaceXMLString(String xmlInput, String path,
			String value) throws XQueryUtilException {
		String query = "copy $c := root()\n"
				+ "modify ( replace value of node $c" + path + " with \""
				+ value + "\")\n" + " return $c";
		logger.trace("query:" + query);
		return XQueryUtil.processXQuery(query, xmlInput);
	}

	public static String getToken(String pmResponseXml)
			throws XQueryUtilException {
		return XQueryUtil
				.processXQuery("//user/password/text()", pmResponseXml);

	}

	public static String getPmResponseXml(String username, String password,
			String i2b2Url, String I2b2Domain)
			throws XQueryUtilException, IOException {
		logger.trace("got request");
		String requestXml = IOUtils.toString(I2b2Util.class
				.getResourceAsStream("/i2b2query/getServices.xml"));
		requestXml = insertI2b2ParametersInXml(requestXml, username, password,
				i2b2Url, I2b2Domain);
		if(requestXml==null) {logger.error("requestXml is null");}
		logger.trace("requestXml:" + requestXml);
		String response = WebServiceCall.run(i2b2Url
				+ "/services/PMService/getServices", requestXml);
		return response;
	}

	public static String getPmResponseXmlWithAuthToken(String username,
			String authToken, String I2b2Url, String I2b2Domain)
			throws XQueryUtilException, IOException {
		String requestStr = IOUtils.toString(I2b2Util.class
				.getResourceAsStream("/i2b2query/getServices.xml"));
		requestStr = insertI2b2ParametersInXml(requestStr, username, authToken,
				I2b2Domain, I2b2Url);
		logger.trace("requestStr:" + requestStr);
		String oStr = WebServiceCall.run(I2b2Url
				+ "/services/PMService/getServices", requestStr);
		return oStr;
	}

	public static boolean authenticateUser(String pmResponseXml)
			throws XQueryUtilException {
		String loginStatusquery = "//response_header/result_status/status/@type/string()";
		String loginError = XQueryUtil.processXQuery(loginStatusquery,
				pmResponseXml);
		logger.trace("ERROR?:<" + loginError + ">");

		return (loginError.equals("ERROR")) ? false : true;

	}

	/*
	 * project id->project Name
	 */
	public static List<Project> getUserProjectMap(String pmResponseXml)
			throws XQueryUtilException {
		logger.trace("got xml" + pmResponseXml);

		List<Project> list = new ArrayList<Project>();
		ArrayList<String> projectsXml = XQueryUtil.getStringSequence(

		"//user/project", pmResponseXml);
		for (String xml : projectsXml) {
			logger.debug("xml:" + xml);
			String projId = XQueryUtil.processXQuery(".//project/@id/string()",
					xml);
			String projName = XQueryUtil.processXQuery(
					".//project/name/text()", xml);
			Project p = new Project();
			p.setId(projId);
			p.setName(projName);
			list.add(p);

			logger.trace("added proj:" + p);

		}

		logger.trace("returning projects:" + list.toString());
		return list;

	}

	/*
	 * static public String insertSessionParametersInXml(String xml, HttpSession
	 * session) throws XQueryUtilException { String username = (String)
	 * session.getAttribute("username"); String password = (String)
	 * session.getAttribute("password"); String I2b2Domain = (String)
	 * session.getAttribute("I2b2Domain"); String I2b2Url = (String)
	 * session.getAttribute("I2b2Url");
	 * 
	 * return I2b2Util.insertI2b2ParametersInXml(xml, username, password,
	 * I2b2Url,I2b2Domain); }
	 * 
	 * public static Bundle getAllPatientsAsFhirBundle(HttpSession session)
	 * throws XQueryUtilException, JAXBException, IOException,
	 * AuthenticationFailure{ return
	 * getAllPatientsAsFhirBundle((String)session.getAttribute
	 * ("i2b2User"),(String
	 * )session.getAttribute("i2b2Token"),(String)session.getAttribute
	 * ("i2b2Url"),(String)session.getAttribute("I2b2Domain")); }
	 */

	public static Bundle getAllPatientsAsFhirBundle(String pdo)
			throws XQueryUtilException, JAXBException, IOException,
			AuthenticationFailure {

		String query = IOUtils
				.toString(FhirUtil.class
						.getResourceAsStream("/transform/I2b2ToFhir/i2b2PatientToFhirPatient.xquery"));
		String bundleXml = XQueryUtil.processXQuery(query, pdo).toString();
		return JAXBUtil.fromXml(bundleXml, Bundle.class);
	}

	public static String getAllPatients(String i2b2User, String i2b2Token,
			String i2b2Url, String I2b2Domain, String project)
			throws XQueryUtilException, IOException {

		String requestXml = IOUtils.toString(FhirUtil.class
				.getResourceAsStream("/i2b2query/getAllPatients.xml"));
		requestXml = I2b2Util.insertI2b2ParametersInXml(requestXml, i2b2User,
				i2b2Token, i2b2Url, I2b2Domain, project);

		String pdo = WebServiceCall.run(i2b2Url
				+ "/services/QueryToolService/pdorequest", requestXml);
		String loginStatusquery = "//response_header/result_status/status/@type/string()";
		String loginError = XQueryUtil.processXQuery(loginStatusquery, pdo);
		logger.trace("ERROR?:<" + loginError + ">");

		if (loginError.equals("ERROR"))
			throw new IllegalArgumentException();

		return pdo;
	}

	public static Bundle getAllDataForAPatientAsFhirBundle(String pdoXml)
			throws JAXBException, IOException, XQueryUtilException {
		Bundle b = FhirUtil.convertI2b2ToFhirForAParticularPatient(pdoXml);
		FhirEnrich.enrich(b);
		logger.trace("bundle:" + JAXBUtil.toXml(b));
		logger.trace("list size:" + b.getEntry().size());
		logger.info("adding to memory...");
		return b;
	}

	public static String getAllDataForAPatient(String i2b2User,
			String i2b2Token, String i2b2Url, String I2b2Domain,
			String project, String patientId) throws IOException,
			XQueryUtilException {
		String requestXml = IOUtils
				.toString(I2b2Util.class
						.getResourceAsStream("/i2b2query/i2b2RequestAllDataForAPatient.xml"));
		requestXml = I2b2Util.insertI2b2ParametersInXml(requestXml,i2b2User, i2b2Token,
				i2b2Url, I2b2Domain, project);

		if (patientId != null)
			requestXml = requestXml.replaceAll("PATIENTID", patientId);

		String responseXml=WebServiceCall.run(i2b2Url
				+ "/services/QueryToolService/pdorequest", requestXml);
		logger.trace("got response:"+responseXml);
		return responseXml;
	}

	static boolean validateI2b2UserNamePasswordPair(String pmResponse)
			throws XQueryUtilException, IOException {
		logger.trace("validating username and password");

		String loginStatusQuery = "//response_header/result_status/status/@type/string()";
		String loginError = XQueryUtil.processXQuery(loginStatusQuery,
				pmResponse);
		logger.trace("ERROR?:<" + loginError + ">");

		if (loginError.equals("ERROR")) {
			return false;
		} else {
			return true;
		}

	}

}
