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

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.Conformance;
import org.hl7.fhir.ConformanceRest;
import org.hl7.fhir.ConformanceSecurity;
import org.hl7.fhir.Extension;
import org.hl7.fhir.Resource;
import org.hl7.fhir.Uri;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
	
import edu.harvard.i2b2.fhir.*;
import edu.harvard.i2b2.fhir.query.QueryEngine;
import edu.harvard.i2b2.fhirserver.ejb.AccessTokenBean;
import edu.harvard.i2b2.fhirserver.ejb.AuthenticationService;
import edu.harvard.i2b2.fhirserver.ejb.SessionBundleBean;
import edu.harvard.i2b2.fhirserver.entity.AccessToken;

/*
 * to use accessToken for authentication
 */
@Path("")
public class I2b2FhirWS {
	static Logger logger = LoggerFactory.getLogger(I2b2FhirWS.class);

	@EJB
	AuthenticationService authService;

	@EJB
	AccessTokenBean accessTokenBean;

	String i2b2SessionId;
	// contains ids of patients already called.

	@EJB
	SessionBundleBean sbb;

	@javax.ws.rs.core.Context
	ServletContext context;

	@PostConstruct
	private void init() {

		try {
			// to remove prop and use server config
			//Properties props = new Properties();
			//props.load(getClass().getResourceAsStream("/log4j.properties"));
			//PropertyConfigurator.configure(props);

			logger.info("Got init request");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			"application/xml+fhir", "application/json+fhir" })
	public Response getQueryResult(
			@PathParam("resourceName") String resourceName,
			@QueryParam("_include") List<String> includeResources,
			@QueryParam("filterf") String filterf,
			@HeaderParam("accept") String acceptHeader,
			@Context HttpHeaders headers, @Context HttpServletRequest request,
			@Context ServletContext servletContext) {
		HttpSession session = null;
		logger.debug("got request " + request.getPathInfo()+"?"+request.getQueryString());
		
		try {
			logger.info("Query param:"
					+ request.getParameterMap().keySet().toString());

			for (String h : headers.getRequestHeaders().keySet()) {
				logger.info("Header->" + h + ":" + headers.getRequestHeader(h));

			}

			Class c = FhirUtil.getResourceClass(resourceName);
			Bundle s = new Bundle();
			session = request.getSession();
			String basePath = request.getRequestURL().toString()
					.split(resourceName)[0];

			if (authenticateSession(session, headers) == false) {
				return Response.status(Status.BAD_REQUEST)
						.type(MediaType.APPLICATION_XML).entity("login first")
						.build();
			}

			logger.debug("session id:" + session.getId());
			MetaResourceDb md = I2b2Helper.getMetaResourceDb(session, sbb);

			// filter if patientId is mentioned in query string
			HashMap<String, String> filter = new HashMap<String, String>();

			I2b2Helper.parsePatientIdToFetchPDO( request, session, sbb);
			md = I2b2Helper.getMetaResourceDb(session, sbb);

			Map<String, String[]> q = request.getParameterMap();
			for (String k : q.keySet()) {
				if (k.equals("_include"))// || k.equals("patient"))
					continue;

				// filter.put(k, new String(request.getParameter(k)));
			}
			// XXX filter has to be translated to correct "Patient" path based
			// on class
			logger.info("running filter..." + filter.toString());
			s = FhirUtil.getResourceBundle(md.getAll(c), basePath, "url");

			logger.info("running sophisticated query for:"
					+ request.getQueryString());
			// q.remove("_id");q.remove("_date");

			if (request.getQueryString() != null) {
				QueryEngine qe = new QueryEngine(c.getSimpleName() + "?"
						+ request.getQueryString());
				logger.info("created QE:" + qe);
				logger.trace("will search on bundle:" + JAXBUtil.toXml(s));
				List<Resource> list = FhirUtil.getResourceListFromBundle(s);
				logger.trace("list size:" + list.size());
				s = FhirUtil
						.getResourceBundle(qe.search(list), basePath, "url");
			}

			logger.info("including...._include:" + includeResources.toString());
			if (s.getEntry().size() > 0) {
				List<Resource> list=md.getIncludedResources(c,
						FhirUtil.getResourceListFromBundle(s),
						includeResources);
				logger.trace("includedListsize:"+list.size());
				s = FhirUtil.getResourceBundle(
						list, basePath, "url");
			}

			//logger.info("getting bundle string..."+JAXBUtil.toXml(s));
			//logger.info("size of db:" + md.getSize());
			logger.info("returning response..."+JAXBUtil.toXml(s));
			String msg = null;
			String mediaType = null;
			if (acceptHeader.contains("application/json")
					|| acceptHeader.contains("application/json+fhir")) {
				msg = FhirUtil.bundleToJsonString(s);
				mediaType="application/json+fhir";
			} else {
				msg = JAXBUtil.toXml(s);
				mediaType="application/xml+fhir";
			}
			msg = I2b2Helper.removeSpace(msg);
			logger.info("acceptHeader:" + acceptHeader);
			// I2b2Helper.releaseSessionLock(session);
			return Response.ok().type(mediaType)
					.header("session_id", session.getId()).entity(msg).build();

		
		} catch (Exception e) {
			// I2b2Helper.releaseSessionLock(session);
			e.printStackTrace();
			logger.error("", e);
			return Response.status(Status.BAD_REQUEST)
					.header("xreason", e.getMessage()).build();
		}

	}

	// http://localhost:8080/fhir-server-api-mvn/resources/i2b2/MedicationStatement/1000000005-1

	private boolean authenticateSession(HttpSession session, HttpHeaders headers)
			throws XQueryUtilException, IOException, JAXBException,
			InterruptedException {
		List<String> authHeaderContentList = headers
				.getRequestHeader(AuthenticationFilter.AUTHENTICATION_HEADER);
		if (authHeaderContentList.size() == 0) {
			logger.warn("No Authentication header present");
			return false;
		}
		String authHeaderContent = authHeaderContentList.get(0);
		boolean authenticationStatus = authService
				.authenticate(authHeaderContent);
		if (authenticationStatus == false) {
			return false;
		}

		AccessToken tok = authService.getAccessTokenString(authHeaderContent);
		session.setAttribute("i2b2domain", tok.getI2b2Project());
		session.setAttribute("i2b2domainUrl", Config.i2b2Url);
		session.setAttribute("username", tok.getResourceUserId());
		session.setAttribute("password", tok.getI2b2Token());

		return true;
	}

	@GET
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/{id:[0-9|-]+}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			"application/xml+fhir", "application/json+fhir" })
	public Response getParticularResource(
			@PathParam("resourceName") String resourceName,
			@PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader,
			@Context HttpHeaders headers, @Context HttpServletRequest request)
			throws DatatypeConfigurationException,
			ParserConfigurationException, SAXException, IOException,
			JAXBException, JSONException, XQueryUtilException,
			InterruptedException {
		
		logger.debug("got request " + request.getPathInfo()+"?"+request.getQueryString());
		HttpSession session = request.getSession();

		try {
			if (authenticateSession(session, headers) == false) {
				return Response.status(Status.BAD_REQUEST)
						.type(MediaType.APPLICATION_XML).entity("login first")
						.build();
			}
			// I2b2Helper.getSessionLock(session);
			// MetaResourceDb md = (MetaResourceDb) session.getAttribute("md");
			MetaResourceDb md = I2b2Helper.getMetaResourceDb(session, sbb);
			logger.debug("session id:" + session.getId());

			String msg = null;
			Resource r = null;
			logger.info("searhcing particular resource2:<" + resourceName
					+ "> with id:<" + id + ">");
			Class c = FhirUtil.getResourceClass(resourceName);
			if (c == null)
				throw new RuntimeException("class not found for resource:"
						+ resourceName);

			I2b2Helper.parsePatientIdToFetchPDO( request, session, sbb);
			md = I2b2Helper.getMetaResourceDb(session, sbb);
			
			r = md.getParticularResource(c, id);
			String mediaType = null;
			if (acceptHeader.contains("application/json")
					|| acceptHeader.contains("application/json+fhir")) {
				msg = FhirUtil.resourceToJsonString(r);
				mediaType = "application/json";
			} else {
				msg = JAXBUtil.toXml(r);
				mediaType = "application/xml";
			}

			msg = I2b2Helper.removeSpace(msg);
			if (r != null) {
				I2b2Helper.releaseSessionLock(session);
				return Response.ok(msg).header("session_id", session.getId())
						.type(mediaType).build();
			} else {
				I2b2Helper.releaseSessionLock(session);
				return Response
						.noContent()
						.header("xreason",
								resourceName + " with id:" + id + " NOT found")
						.header("session_id", session.getId()).build();
			}
		} catch (Exception e) {
			I2b2Helper.releaseSessionLock(session);
			logger.error("", e);
			return Response.noContent().header("xreason", e.getMessage())
					.header("session_id", session.getId()).build();
		}

	}

	@GET
	@Path("open")
	public Response dummyToByPassAuthentication() {
		return Response.ok().entity("dummy").build();

	}

	@OPTIONS
	@Path("")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			"application/xml+fhir", "application/json+fhir" })
	public Response conformanceStatement2(
			@HeaderParam("accept") String acceptHeader,
			@Context HttpServletRequest request) throws JAXBException,
			JSONException, IOException, ParserConfigurationException,
			SAXException, URISyntaxException {
		return conformanceStatement(acceptHeader, request);

	}

	@GET
	@Path("metadata")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			"application/xml+fhir", "application/json+fhir" })
	public Response conformanceStatement(
			@HeaderParam("accept") String acceptHeader,
			@Context HttpServletRequest request) throws JAXBException,
			JSONException, IOException, ParserConfigurationException,
			SAXException, URISyntaxException {

		URI fhirBase = HttpHelper.getBasePath(request);
		Conformance c = new Conformance();
		ConformanceRest rest = new ConformanceRest();
		ConformanceSecurity security = new ConformanceSecurity();
		Extension OAuthext = new Extension();
		security.getExtension().add(OAuthext);
		OAuthext.setUrl("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");

		Extension authExt = new Extension();
		authExt.setUrl("authorize");
		Uri uri = new Uri();
		uri.setValue(fhirBase + "authz/authorize");
		authExt.setValueUri(uri);
		OAuthext.getExtension().add(authExt);

		Extension tokenExt = new Extension();
		tokenExt.setUrl("token");
		uri = new Uri();
		uri.setValue(fhirBase + "token");
		tokenExt.setValueUri(uri);
		OAuthext.getExtension().add(tokenExt);

		rest.setSecurity(security);
		c.getRest().add(rest);
		logger.trace("conf:" + JAXBUtil.toXml(c));

		String msg;
		String mediaType;
		if (acceptHeader.contains("application/json")
				|| acceptHeader.contains("application/json+fhir")) {
			msg = FhirUtil.resourceToJsonString(c);
			mediaType = "application/json";
		} else {
			msg = JAXBUtil.toXml(c);
			mediaType = "application/xml";
		}
		msg = I2b2Helper.removeSpace(msg);
		logger.info("acceptHeader:" + acceptHeader);

		return Response.ok().type(mediaType).entity(msg).build();

	}

}