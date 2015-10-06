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

package edu.harvard.i2b2.fhir.server.ws;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
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
import edu.harvard.i2b2.fhir.oauth2.ws.AuthenticationFilter;
import edu.harvard.i2b2.fhir.oauth2.ws.HttpHelper;
import edu.harvard.i2b2.fhir.query.QueryEngine;
import edu.harvard.i2b2.oauth2.core.ejb.AccessTokenService;
import edu.harvard.i2b2.oauth2.core.ejb.AuthenticationService;
import edu.harvard.i2b2.oauth2.core.ejb.PatientBundleManager;
import edu.harvard.i2b2.oauth2.core.ejb.QueryService;
import edu.harvard.i2b2.oauth2.core.entity.AccessToken;

/*
 * to use accessToken for authentication
 */
@Path("")
public class I2b2FhirWS {
	static Logger logger = LoggerFactory.getLogger(I2b2FhirWS.class);

	@EJB
	AuthenticationService authService;

	String i2b2SessionId;
	// contains ids of patients already called.

	@EJB
	PatientBundleManager service;
	
	@EJB
	QueryService queryManager;

	@javax.ws.rs.core.Context
	ServletContext context;

	@PostConstruct
	private void init() {

		try {
			// to remove prop and use server config
			// Properties props = new Properties();
			// props.load(getClass().getResourceAsStream("/log4j.properties"));
			// PropertyConfigurator.configure(props);

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
			@Context ServletContext servletContext) throws IOException {

		HttpSession session = null;
		String msg = null;
		String mediaType = null;
		MetaResourceDb md = new MetaResourceDb();

		logger.debug("got request " + request.getPathInfo() + "?"
				+ request.getQueryString());

		try {
			logger.info("Query param:"
					+ request.getParameterMap().keySet().toString());

			String head="";
			for (String h : headers.getRequestHeaders().keySet()) {
				head+="Header->" + h + ":" + headers.getRequestHeader(h);

			}
			logger.info(head);

			// md = I2b2Helper.getMetaResourceDb(session, sbb);

			Class c = FhirUtil.getResourceClass(resourceName);
			Bundle s = null;
			session = request.getSession();
			String basePath = request.getRequestURL().toString()
					.split(resourceName)[0];

			

			//I2b2Helper.resetMetaResourceDb(session, sbb);
			logger.debug("session id:" + session.getId());
			
			authService.authenticateSession(headers.getRequestHeader(AuthenticationFilter.AUTHENTICATION_HEADER).get(0),session);
			
			s=I2b2Helper.parsePatientIdToFetchPDO(session,  request,c.getSimpleName(),
			service); 
					
			md.addBundle(s);
			
			logger.info("running filter...");
			s = FhirUtil.getResourceBundle(md.getAll(c), basePath, "url");

			logger.info("running sophisticated query for:"
					+ request.getQueryString());

			if (request.getQueryString() != null) {
				String fhirQuery=c.getSimpleName() + "?"
						+ request.getQueryString();
				s=queryManager.runQueryEngine(fhirQuery, s,basePath);
			}

			logger.info("including...._include:" + includeResources.toString());
			if (s.getEntry().size() > 0) {
				List<Resource> list = md
						.getIncludedResources(c,
								FhirUtil.getResourceListFromBundle(s),
								includeResources);
				logger.trace("includedListsize:" + list.size());
				s = FhirUtil.getResourceBundle(list, basePath, "url");
			}

			// logger.info("getting bundle string..."+JAXBUtil.toXml(s));
			// logger.info("size of db:" + md.getSize());
			logger.info("returning response..." + JAXBUtil.toXml(s));
			if (acceptHeader.contains("application/json")
					|| acceptHeader.contains("application/json+fhir")) {
				msg = FhirUtil.bundleToJsonString(s);
				mediaType = "application/json+fhir";
			} else {
				msg = JAXBUtil.toXml(s);
				mediaType = "application/xml+fhir";
			}
			msg = I2b2Helper.removeSpace(msg);
			logger.info("acceptHeader:" + acceptHeader);
			return Response.ok().type(mediaType)
					.header("session_id", session.getId()).entity(msg).build();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			return Response.status(Status.BAD_REQUEST)
					.header("xreason", e.getMessage()).build();
		}

	}

	// http://localhost:8080/fhir-server-api-mvn/resources/i2b2/MedicationStatement/1000000005-1

	@GET
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/{id:[0-9|-]+}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
			"application/xml+fhir", "application/json+fhir" })
	public Response getParticularResource(
			@PathParam("resourceName") String resourceName,
			@PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader,
			@Context HttpHeaders headers, @Context HttpServletRequest request,
			@HeaderParam(AuthenticationFilter.AUTHENTICATION_HEADER) String tokString)
			throws DatatypeConfigurationException,
			ParserConfigurationException, SAXException, IOException,
			JAXBException, JSONException, XQueryUtilException,
			InterruptedException {

		logger.debug("got request " + request.getPathInfo() + "?"
				+ request.getQueryString());
		HttpSession session = request.getSession();

		try {
			
			
			// MetaResourceDb md = I2b2Helper.getMetaResourceDb(session, sbb);

			MetaResourceDb md = new MetaResourceDb();
			String msg = null;
			Resource r = null;
			Bundle s=null;
			String mediaType = null;

			logger.debug("session id:" + session.getId());
			logger.info("searching particular resource:<" + resourceName
					+ "> with id:<" + id + ">");
			Class c = FhirUtil.getResourceClass(resourceName);
			if (c == null)
				throw new RuntimeException("class not found for resource:"
						+ resourceName);
			authService.authenticateSession(headers.getRequestHeader(AuthenticationFilter.AUTHENTICATION_HEADER).get(0),session);
			s=I2b2Helper.parsePatientIdToFetchPDO(session,request,
					resourceName, service);
			md.addBundle(s);;

			r = md.getParticularResource(c, id);

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
				return Response.ok(msg).header("session_id", session.getId())
						.type(mediaType).build();
			} else {
				return Response
						.noContent()
						.header("xreason",
								resourceName + " with id:" + id + " NOT found")
						.header("session_id", session.getId()).build();
			}

		} catch (Exception e) {
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
/*
	private boolean authenticateOpenSession(HttpSession session, HttpHeaders headers)
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

		AccessToken tok = authService.getHttpAccessTokenString(authHeaderContent);
		session.setAttribute("i2b2domain", tok.getI2b2Project());
		session.setAttribute("i2b2domainUrl", Config.i2b2Url);
		session.setAttribute("username", tok.getResourceUserId());
		session.setAttribute("password", tok.getI2b2Token());

		return true;
	}
*/
}