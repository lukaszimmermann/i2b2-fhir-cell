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
 */
package edu.harvard.i2b2.fhirserver.ws;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.PropertyConfigurator;
import org.hl7.fhir.Instant;
import org.hl7.fhir.Resource;
import org.hl7.fhir.Uri;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import edu.harvard.i2b2.fhir.*;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;
import edu.harvard.i2b2.fhir.query.QueryEngine;
import edu.harvard.i2b2.fhir.query.QueryParameterException;
import edu.harvard.i2b2.fhir.query.QueryValueException;

@Path("")
public class I2b2FhirWS {
	static Logger logger = LoggerFactory.getLogger(I2b2FhirWS.class);
	String i2b2SessionId;
	// contains ids of patients already called.

	// @EJB
	// MetaResourceDbWrapper metaResourceDb;

	@javax.ws.rs.core.Context
	ServletContext context;

	@PostConstruct
	private void init() {

		try {

			logger.info("Got init request");
			logger.info(" Print Got init request");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@POST
	@Path("auth")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response doAuthentication(@Context HttpServletRequest request,
			@FormParam("username") String usernameForm,
			@FormParam("password") String passwordForm,
			@FormParam("i2b2domain") String i2b2domainForm,
			@FormParam("i2b2url") String i2b2urlForm)
			throws XQueryUtilException, IOException, JAXBException {
		// Exception e1=new RuntimeException("test error");
		// logger.error("test error1:",e1);
		// if(1==1) throw (RuntimeException)e1;
		Properties props = new Properties();
		props.load(getClass().getResourceAsStream("/log4j.properties"));
		PropertyConfigurator.configure(props);
		logger.info("Got Auth request");

		HttpSession session = request.getSession(false);
		if (session != null) {
			// logger.info("invalidated session");
			// session.invalidate();
		}

		String username = request.getHeader("username");
		String password = request.getHeader("password");
		String i2b2domain = request.getHeader("i2b2domain");
		String i2b2url = request.getHeader("i2b2url");
		String basePath = request
				.getRequestURL()
				.toString()
				.replaceAll(
						I2b2FhirWS.class.getAnnotation(Path.class).value()
								.toString()
								+ "$", "");

		// after headers check for form
		if (username == null)
			username = usernameForm;
		if (password == null)
			password = passwordForm;
		if (i2b2domain == null)
			i2b2domain = i2b2domainForm;
		if (i2b2url == null)
			i2b2url = i2b2urlForm;
		session = request.getSession();// new session

		try {
			if (username != null && password != null) {
				// ArrayList<String> PDOcallHistory= new ArrayList<String> ();

				session.setAttribute("i2b2domain", i2b2domain);
				session.setAttribute("i2b2domainUrl", i2b2url);
				session.setAttribute("username", username);
				session.setAttribute("password", password);
				// session.setAttribute("PDOcallHistory", PDOcallHistory);
				MetaResourceDb md = new MetaResourceDb();
				session.setAttribute("md", md);

				initAllPatients(session);
				return Response.ok().entity("Auth successful.")
						.type(MediaType.TEXT_PLAIN)
						.header("session_id", session.getId()).build();
			}
		} catch (AuthenticationFailure e) {

			return Response.ok().entity("Authentication Failure")
					// .cookie(authIdCookie)
					.type(MediaType.TEXT_PLAIN)
					.header("session_id", session.getId()).build();

		} catch (FhirServerException e) {
			Response.ok().entity(e.getMessage())// .cookie(authIdCookie)
					.header("session_id", session.getId()).build();
		}
		return Response.ok().entity("Unknown ERROR")// .cookie(authIdCookie)
				.header("session_id", session.getId()).build();
	}

	@GET
	// @Path("MedicationStatement")
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}")
	// @Path("MedicationPrescription(//_search)*")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getQueryResult(
			@PathParam("resourceName") String resourceName,
			@QueryParam("_include") List<String> includeResources,
			@QueryParam("filterf") String filterf,
			@HeaderParam("accept") String acceptHeader,
			@Context HttpServletRequest request,
			@Context ServletContext servletContext) {
		try {
			// String resourceName="MedicationPrescription";
			logger.info("Query param:"
					+ request.getParameterMap().keySet().toString());

			Class c = FhirUtil.getResourceClass(resourceName);
			MetaResourceDb md = null;
			MetaResourceSet s = new MetaResourceSet();
			HttpSession session = request.getSession(false);
			String basePath = request.getRequestURL().toString()
					.split(resourceName)[0];

			if (session == null) {
				byPassAuthentication(request);
				session = request.getSession(false);
			}

			md = (MetaResourceDb) session.getAttribute("md");

			// filter if patientId is mentioned in query string
			HashMap<String, String> filter = new HashMap<String, String>();

			String patientId = extractPatientId(request.getQueryString());
			logger.info("PatientId:" + patientId);
			if (patientId != null) {
				// filter.put("Patient", "Patient/" + patientId);
				scanQueryParametersToGetPdo(session, request.getQueryString());
			}

			Map<String, String[]> q = request.getParameterMap();
			for (String k : q.keySet()) {
				if (k.equals("_include"))// || k.equals("patient"))
					continue;

				// filter.put(k, new String(request.getParameter(k)));
			}
			// XXX filter has to be translated to correct "Patient" path based
			// on class
			logger.info("running filter..." + filter.toString());
			s = md.getAll(c);
			/*
			 * s = md.filterMetaResources(c, filter); if (filter.size() == 0) {
			 * s = md.getAll(c); } else { s = md.filterMetaResources(c, filter);
			 * 
			 * }
			 */

			logger.info("running sophisticated query for:"
					+ request.getQueryString());
			// q.remove("_id");q.remove("_date");

			if (request.getQueryString() != null) {
				QueryEngine qe = new QueryEngine(c.getSimpleName() + "?"
						+ request.getQueryString());
				logger.info("created QE:" + qe);
				s = qe.search(s);
			}

			logger.info("including...._include:" + includeResources.toString());
			if (s.getMetaResource().size() > 0) {
				s = md.getIncludedMetaResources(c, s, includeResources);
			}

			logger.info("getting bundle string...");

			String url = request.getRequestURL().toString();
			if (url.contains(";"))
				url = url.substring(0, url.indexOf(";"));
			if (request.getQueryString() != null)
				url += "?" + request.getQueryString();

			// String returnString = FhirUtil.getResourceBundle(s, basePath,
			// url);
			logger.info("size of db:" + md.getSize());
			logger.info("returning response...");
			String msg = null;
			String mediaType=null;
			if (acceptHeader.contains("application/json")) {
				msg = FhirUtil.hapiBundleToJsonString(FhirUtil
						.getResourceHapiBundle(s, basePath, url));
				mediaType=MediaType.APPLICATION_JSON;
			} else {
				msg = FhirUtil.getResourceBundle(s, basePath, url);
				mediaType=MediaType.APPLICATION_XML;
			}
			msg=removeSpace(msg);
			logger.info("acceptHeader:"+acceptHeader);
			return Response.ok().type(mediaType)
					.header("session_id", session.getId()).entity(msg).build();

			// return Response.ok().type(MediaType.APPLICATION_XML)
			// .header("session_id", session.getId())
			// .entity(msg).build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST)
					.header("xreason", e.getMessage()).build();
		}

	}

	// http://localhost:8080/fhir-server-api-mvn/resources/i2b2/MedicationStatement/1000000005-1

	private void byPassAuthentication(HttpServletRequest request)
			throws XQueryUtilException, IOException, JAXBException {
		/*
		 * return Response.status(Status.BAD_REQUEST)
		 * .type(MediaType.APPLICATION_XML).entity("login first ") .build();
		 */
		String username = request.getHeader("username");
		String password = request.getHeader("password");
		String i2b2domain = request.getHeader("i2b2domain");
		String i2b2url = request.getHeader("i2b2url");
		doAuthentication(request, username == null ? "demo" : username,
				password == null ? "demouser" : password,
				i2b2domain == null ? "i2b2demo" : i2b2domain,
				i2b2url == null ? "http://services.i2b2.org:9090/i2b2"
						: i2b2url);

	}

	@GET
	// @Path("{resourceName:[a-z]+}/{id:[0-9]+}")
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/{id:[0-9|-]+}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getParticularResource(
			@PathParam("resourceName") String resourceName,
			@PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader,
			@Context HttpServletRequest request)
			throws DatatypeConfigurationException,
			ParserConfigurationException, SAXException, IOException,
			JAXBException, JSONException, XQueryUtilException {

		HttpSession session = request.getSession(false);
		if (session == null) {
			byPassAuthentication(request);
			session = request.getSession(false);
		}
		if (session == null) {
			return Response.status(Status.BAD_REQUEST)
					.type(MediaType.APPLICATION_XML).entity("login first")
					.build();
		}

		MetaResourceDb md = (MetaResourceDb) session.getAttribute("md");

		String msg = null;
		Resource r = null;
		logger.info("searhcing particular resource2:<" + resourceName
				+ "> with id:<" + id + ">");
		Class c = FhirUtil.getResourceClass(resourceName);
		if (c == null)
			throw new RuntimeException("class not found for resource:"
					+ resourceName);

		r = md.getParticularResource(c, id);
		String mediaType=null;
		if (acceptHeader.contains("application/json")) {
			msg = FhirUtil.resourceToJsonString(r);
			mediaType=MediaType.APPLICATION_JSON;
		} else {
			msg = JAXBUtil.toXml(r);
			mediaType=MediaType.APPLICATION_XML;
		}

		msg=removeSpace(msg);
		if (r != null) {
			
			return Response.ok(msg).header("session_id", session.getId()).type(mediaType)
					.build();
		} else {
			return Response
					.noContent()
					.header("xreason",
							resourceName + " with id:" + id + " NOT found")
					.header("session_id", session.getId()).build();
		}
	}

	private MetaResourceSet initAllPatients(HttpSession session)
			throws AuthenticationFailure, FhirServerException,
			XQueryUtilException, JAXBException {
		if (session == null) {
			return new MetaResourceSet();
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
		logger.trace("ERROR:<" + loginError + ">");

		if (loginError.equals("ERROR"))
			throw new AuthenticationFailure();

		String xQueryResultString = processXquery(query, oStr);

		logger.debug("Got xQueryResultString :" + xQueryResultString);
		// System.out.println("Got xQueryResultString :" + xQueryResultString);

		MetaResourceSet b = null;
		try {
			b = JAXBUtil.fromXml(xQueryResultString, MetaResourceSet.class);

		} catch (JAXBException e) {
			e.printStackTrace();
			throw new FhirServerException("JAXBException", e);
		}
		logger.info("Got ResourceSet of size::" + b.getMetaResource().size());
		md.addMetaResourceSet(b);
		return b;
	}

	private void getPdo(HttpSession session, String patientId)
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
				.getFile("i2b2query/i2b2RequestMedsForAPatient.xml");
		requestStr = insertSessionParametersInXml(requestStr, session);
		if (patientId != null)
			requestStr = requestStr.replaceAll("PATIENTID", patientId);

		String query = Utils
				.getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMedPrescription.xquery");

		String i2b2Url = (String) session.getAttribute("i2b2domainUrl");

		logger.info("fetching from i2b2host...");
		String oStr = WebServiceCall.run(i2b2Url
				+ "/services/QueryToolService/pdorequest", requestStr);
		logger.info("running transformation...");
		String xQueryResultString = processXquery(query, oStr);
		logger.trace("xQueryResultString:" + xQueryResultString);
		// System.out.println("xQueryResultString:"+xQueryResultString);
		// md.addMetaResourceSet(getEGPatient());

		try {
			MetaResourceSet b = JAXBUtil.fromXml(xQueryResultString,
					MetaResourceSet.class);
			logger.trace("bundle:" + JAXBUtil.toXml(b));
			logger.trace("list size:" + b.getMetaResource().size());
			logger.info("adding to memory...");
			md.addMetaResourceSet(b);
		} catch (JAXBException e) {
			e.printStackTrace();

		}
	}

	private void scanQueryParametersToGetPdo(HttpSession session,
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

	private static String insertSessionParametersInXml(String xml,
			HttpSession session) throws XQueryUtilException {
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		String i2b2domain = (String) session.getAttribute("i2b2domain");
		String i2b2domainUrl = (String) session.getAttribute("i2b2domainUrl");

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

	private static String extractPatientId(String input) {
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

	private static String removeSpace(String input)
			throws ParserConfigurationException, SAXException, IOException {
		// return Utils.getStringFromDocument(Utils.xmltoDOM(input.replaceAll(
		// "(?m)^[ \t]*\r?\n", "")));
		return input.replaceAll("(?m)^[ \t]*\r?\n", "");
		// return input;
	}

	private static String processXquery(String query, String input)
			throws XQueryUtilException {
		logger.trace("will run query:" + query + "\n input" + input);
		return XQueryUtil.processXQuery(query, input);
	}

	@GET
	@Path("a/Patient/1137192")
	@Produces({ "application/json; charset=UTF-8" })
	public Response getSMARTexamplePatients(@Context HttpServletRequest request) {
		logUrlAccess(request);
		return Response.ok().type("application/json; charset=UTF-8")
				.entity(Utils.getFile("example/smart/ParticularPatient.json"))
				.build();
	}

	@GET
	@Path("a/Patient")
	@Produces({ "application/json; charset=UTF-8" })
	public Response getSMARTexampleParticularPatient(
			@Context HttpServletRequest request) {
		logUrlAccess(request);
		return Response.ok().type("application/json; charset=UTF-8")
				.entity(Utils.getFile("example/smart/AllPatients.json"))
				.build();
	}

	@GET
	@Path("a/MedicationPrescription/_search")
	@Produces({ "application/json; charset=UTF-8" })
	public Response getSMARTexampleParticularPatientPrescription(
			@Context HttpServletRequest request) {

		logUrlAccess(request);
		return Response
				.ok()
				.type("application/json; charset=UTF-8")
				.entity(Utils
						.getFile("example/smart/ParticularPatientPrescription.json"))
				.build();
	}

	private void logUrlAccess(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		String queryString = request.getQueryString();
		if (queryString != null) {
			url.append('?');
			url.append(queryString);
		}
		String requestURL = url.toString();
		logger.trace(request.getRemoteAddr() + "<-" + requestURL);
	}
}
