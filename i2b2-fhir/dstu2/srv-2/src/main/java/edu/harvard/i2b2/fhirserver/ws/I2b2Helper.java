package edu.harvard.i2b2.fhirserver.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.WebServiceCall;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;

public class I2b2Helper {

	static Logger logger = LoggerFactory.getLogger(I2b2Helper.class);

	static boolean validateI2b2UserNamePasswordPair(String userName, String password,
			String domain, String i2b2Url) throws XQueryUtilException {
		logger.trace("validating username and password");
		String requestStr = Utils.getFile("i2b2query/getServices.xml");
		requestStr = insertSessionParametersInXml(requestStr, userName,
				password, domain, i2b2Url);
		logger.debug("Webservice Request:" + requestStr);

		String oStr = WebServiceCall.run(i2b2Url
				+ "/services/PMService/getServices", requestStr);

		logger.debug("got Response:" + oStr);

		String loginStatusQuery = "//response_header/result_status/status/@type/string()";
		String loginError = processXquery(loginStatusQuery, oStr);
		logger.trace("ERROR?:<" + loginError + ">");

		if (loginError.equals("ERROR")) {
			return false;
		} else {
			return true;
		}

	}

	static Bundle initAllPatients(HttpSession session)
			throws AuthenticationFailure, FhirServerException,
			XQueryUtilException, JAXBException {
		if (session == null) {
			return new Bundle();
		}
		MetaResourceDb md = (MetaResourceDb) session.getAttribute("md");
		if (session == null)
			throw new RuntimeException("session is null");

		String sa = (String) session.getAttribute("testAttr1");
		logger.info("gettestAttr1:" + sa);
		if (md == null)
			throw new RuntimeException("md is null");
		String i2b2Url = (String) session.getAttribute("i2b2domainUrl");
		String query = Utils
				.getFile("transform/I2b2ToFhir/i2b2PatientToFhirPatient.xquery");

		String requestStr = Utils.getFile("i2b2query/getAllPatients.xml");
		requestStr = insertSessionParametersInXml(requestStr, session);
		logger.debug(requestStr);
		// if(1==1) return new MetaResourceSet();

		String oStr = WebServiceCall.run(i2b2Url
				+ "/services/QueryToolService/pdorequest", requestStr);

		// logger.debug("got::"
		// + oStr.substring(0, (oStr.length() > 200) ? 200 : 0));
		logger.debug("got Response::" + oStr);

		String loginStatusquery = "//response_header/result_status/status/@type/string()";
		String loginError = processXquery(loginStatusquery, oStr);
		logger.trace("ERROR?:<" + loginError + ">");

		if (loginError.equals("ERROR"))
			throw new AuthenticationFailure();

		String xQueryResultString = processXquery(query, oStr);

		logger.debug("Got xQueryResultString :" + xQueryResultString);
		// System.out.println("Got xQueryResultString :" + xQueryResultString);

		Bundle b = null;
		try {
			b = JAXBUtil.fromXml(xQueryResultString, Bundle.class);

		} catch (JAXBException e) {
			e.printStackTrace();
			throw new FhirServerException("JAXBException", e);
		}
		logger.info("Got ResourceSet of size::" + b.getEntry().size());
		md.addBundle(b);
		return b;
	}

	private static void getPdo(HttpSession session, String patientId)
			throws XQueryUtilException {
		ArrayList<String> PDOcallHistory = (ArrayList<String>) session
				.getAttribute("PDOcallHistory");
		if (PDOcallHistory == null) {
			PDOcallHistory = new ArrayList<String>();
			session.setAttribute("PDOcallHistory", PDOcallHistory);
		}
		if (PDOcallHistory.contains(patientId)) {
			logger.info("patient already present:" + patientId);
			return;// avoid recall on historical patient
		}
		PDOcallHistory.add(patientId);

		MetaResourceDb md = (MetaResourceDb) session.getAttribute("md");

		String requestStr = Utils
		// .getFile("i2b2query/i2b2RequestMedsForAPatient.xml");
				.getFile("i2b2query/i2b2RequestAllDataForAPatient.xml");
		requestStr = I2b2Helper.insertSessionParametersInXml(requestStr,
				session);
		if (patientId != null)
			requestStr = requestStr.replaceAll("PATIENTID", patientId);

		String query = Utils
				.getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMedPrescription.xquery");
				//.getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMedStatement.xquery");
		
		String i2b2Url = (String) session.getAttribute("i2b2domainUrl");

		logger.info("fetching from i2b2host...");
		String oStr = WebServiceCall.run(i2b2Url
				+ "/services/QueryToolService/pdorequest", requestStr);
		logger.trace("got i2b2 response:" + oStr);
		logger.info("running transformation...");
		String xQueryResultString = I2b2Helper.processXquery(query, oStr);
		logger.trace("xQueryResultString1:" + xQueryResultString);

		// System.out.println("xQueryResultString:"+xQueryResultString);
		// md.addMetaResourceSet(getEGPatient());

		try {
			Bundle b = JAXBUtil.fromXml(xQueryResultString,
					Bundle.class);
			logger.trace("bundle:" + JAXBUtil.toXml(b));
			logger.trace("list size:" + b.getEntry().size());
			logger.info("adding to memory...");
			md.addBundle(b);
		} catch (Exception e) {
			logger.trace("xQueryResultString1:" + xQueryResultString);

			logger.error("ERROR MSG:" + e.getMessage(), e);
			// e.printStackTrace();

		}
	}

	static void scanQueryParametersToGetPdo(HttpSession session,
			String queryString) throws XQueryUtilException {
		if (queryString != null) {
			String pid = extractPatientId(queryString);
			if (pid != null) {
				logger.info("will fetch Patient with id:" + pid);
				getPdo(session, pid);
			} else {
				logger.info("will not fetch Patient as there is no Patient id in query");
			}

		}
	}

	private static String extractPatientIdFromQuery(HashMap<String, String> hm) {
		String id = null;
		for (String k : hm.keySet()) {
			String v = hm.get(k);
			String kv = k + "=" + v;
			id = extractPatientId(kv);
			if (id != null)
				return id;
		}
		return id;
	}

	static String extractPatientId(String input) {
		if (input == null)
			return null;
		String id = null;
		Pattern p = Pattern.compile("[P|p]atient=([a-zA-Z0-9]+)");
		Matcher m = p.matcher(input);

		if (m.find()) {
			id = m.group(1);
			logger.trace(id);
		}
		return id;
	}

	static String insertSessionParametersInXml(String xml, HttpSession session)
			throws XQueryUtilException {
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		String i2b2domain = (String) session.getAttribute("i2b2domain");
		String i2b2domainUrl = (String) session.getAttribute("i2b2domainUrl");

		return insertSessionParametersInXml(xml, username, password,
				i2b2domain, i2b2domainUrl);
	}

	static String insertSessionParametersInXml(String xml, String username,
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

	private static String replaceXMLString(String xmlInput, String path,
			String value) throws XQueryUtilException {
		String query = "copy $c := root()\n"
				+ "modify ( replace value of node $c" + path + " with \""
				+ value + "\")\n" + " return $c";
		logger.trace("query:" + query);
		return processXquery(query, xmlInput);
	}

	static String processXquery(String query, String input)
			throws XQueryUtilException {
		logger.trace("will run query:" + query + "\n input" + input);
		return XQueryUtil.processXQuery(query, input);
	}

	static void getSessionLock(HttpSession session) throws InterruptedException {

		while (session.getAttribute("SESSION_LOCK") != null
				&& session.getAttribute("SESSION_LOCK").equals(true)) {
			Thread.sleep(100);
			logger.trace("Session locked. Hence sleeping.. for session id:"
					+ session.getId());
		}
		logger.trace("setting session Lock:" + session.getId());
		session.setAttribute("SESSION_LOCK", true);
		return;

	}

	static void releaseSessionLock(HttpSession session) {
		logger.trace("releasing session Lock:" + session.getId());
		session.setAttribute("SESSION_LOCK", false);
	}

	static String removeSpace(String input)
			throws ParserConfigurationException, SAXException, IOException {
		// return Utils.getStringFromDocument(Utils.xmltoDOM(input.replaceAll(
		// "(?m)^[ \t]*\r?\n", "")));
		return input.replaceAll("(?m)^[ \t]*\r?\n", "");
		// return input;
	}

}
