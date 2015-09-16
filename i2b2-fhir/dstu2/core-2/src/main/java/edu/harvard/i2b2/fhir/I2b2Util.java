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

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class I2b2Util {
	static Logger logger = LoggerFactory.getLogger(I2b2Util.class);

	public static String insertI2b2ParametersInXml(String xml, String username,
			String password, String i2b2domain, String i2b2domainUrl)
			throws XQueryUtilException {

		xml = replaceXMLString(xml, "//security/username", username);
		xml = replaceXMLString(xml, "//security/password", password);
		xml = replaceXMLString(xml, "//security/domain", i2b2domain);
		xml = replaceXMLString(xml, "//proxy/redirect_url", i2b2domainUrl
				+ "/services/QueryToolService/pdorequest");
		// logger.info("returning xml:"+xml);
		return xml;
	}

	public static String insertI2b2ParametersAuthTokenInXml(String xml,
			String username, String authToken, String i2b2domain,
			String i2b2domainUrl) throws XQueryUtilException {
		xml = replaceXMLString(xml, "//security/username", username);
		xml = replaceXMLString(xml, "//security/password", authToken);
		xml = replaceXMLString(xml, "//security/domain", i2b2domain);
		xml = replaceXMLString(xml, "//proxy/redirect_url", i2b2domainUrl
				+ "/services/QueryToolService/pdorequest");
		// logger.info("returning xml:"+xml);

		// if password is token then indicate so
		if (authToken.contains("SessionKey:")) {
		} else {
			xml = xml.replace("<password>", "<password is_token=\"true\">");
		}
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
			String i2b2domain, String i2b2domainUrl)
			throws XQueryUtilException, IOException {
		String requestStr = IOUtils.toString(I2b2Util.class
				.getResourceAsStream("/i2b2query/getServices.xml"));
		requestStr = insertI2b2ParametersInXml(requestStr, username, password,
				i2b2domain, i2b2domainUrl);
		logger.trace("requestStr:" + requestStr);
		String oStr = WebServiceCall.run(i2b2domainUrl
				+ "/services/PMService/getServices", requestStr);
		return oStr;
	}

	public static String getPmResponseXmlWithAuthToken(String username,
			String authToken, String i2b2domain, String i2b2domainUrl)
			throws XQueryUtilException, IOException {
		String requestStr = IOUtils.toString(I2b2Util.class
				.getResourceAsStream("/i2b2query/getServices.xml"));
		requestStr = insertI2b2ParametersAuthTokenInXml(requestStr, username,
				authToken, i2b2domain, i2b2domainUrl);
		logger.trace("requestStr:" + requestStr);
		String oStr = WebServiceCall.run(i2b2domainUrl
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

	public static List<String> getUserProjects(String pmResponseXml)
			throws XQueryUtilException {
		logger.trace("got xml" + pmResponseXml);
		List<String> projects = XQueryUtil.getStringSequence(
				"//user/domain/text()", pmResponseXml);

		logger.trace("returning projects:" + projects.toString());
		return projects;

	}

	static public String insertSessionParametersInXml(String xml,
			HttpSession session) throws XQueryUtilException {
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		String i2b2domain = (String) session.getAttribute("i2b2domain");
		String i2b2domainUrl = (String) session.getAttribute("i2b2domainUrl");

		return I2b2Util.insertI2b2ParametersInXml(xml, username, password,
				i2b2domain, i2b2domainUrl);
	}

	public static Bundle getAllPatientsAsFhirBundle(HttpSession session)
			throws XQueryUtilException, JAXBException, IOException,
			AuthenticationFailure {

		String requestXml = IOUtils.toString(FhirUtil.class
				.getResourceAsStream("/i2b2query/getAllPatients.xml"));
		requestXml = I2b2Util.insertSessionParametersInXml(requestXml, session);
		String i2b2Url = (String) session.getAttribute("i2b2domainUrl");

		String i2b2Response = WebServiceCall.run(i2b2Url
				+ "/services/QueryToolService/pdorequest", requestXml);
		String loginStatusquery = "//response_header/result_status/status/@type/string()";
		String loginError = XQueryUtil.processXQuery(loginStatusquery,
				i2b2Response);
		logger.trace("ERROR?:<" + loginError + ">");

		if (loginError.equals("ERROR"))
			throw new AuthenticationFailure();

		String query = IOUtils
				.toString(FhirUtil.class
						.getResourceAsStream("/transform/I2b2ToFhir/i2b2PatientToFhirPatient.xquery"));
		String bundleXml = XQueryUtil.processXQuery(query, i2b2Response)
				.toString();
		return JAXBUtil.fromXml(bundleXml, Bundle.class);
	}

	static boolean validateI2b2UserNamePasswordPair(String userName,
			String password, String domain, String i2b2Url)
			throws XQueryUtilException, IOException {
		logger.trace("validating username and password");
		String requestXml = IOUtils.toString(I2b2Util.class
				.getResourceAsStream("i2b2query/getServices.xml"));
		requestXml = I2b2Util.insertI2b2ParametersInXml(requestXml, userName,
				password, domain, i2b2Url);
		logger.debug("Webservice Request:" + requestXml);

		String i2b2Response = WebServiceCall.run(i2b2Url
				+ "/services/PMService/getServices", requestXml);

		logger.debug("got Response:" + i2b2Response);

		String loginStatusQuery = "//response_header/result_status/status/@type/string()";
		String loginError = XQueryUtil.processXQuery(loginStatusQuery,
				i2b2Response);
		logger.trace("ERROR?:<" + loginError + ">");

		if (loginError.equals("ERROR")) {
			return false;
		} else {
			return true;
		}

	}

}
