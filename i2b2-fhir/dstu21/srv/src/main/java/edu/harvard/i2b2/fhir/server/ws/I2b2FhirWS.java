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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
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

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.hl7.fhir.BundleLink;
import org.hl7.fhir.Code;
import org.hl7.fhir.Conformance;
import org.hl7.fhir.ConformanceInteraction;
import org.hl7.fhir.ConformanceResource;
import org.hl7.fhir.ConformanceRest;
import org.hl7.fhir.ConformanceSearchParam;
import org.hl7.fhir.ConformanceSecurity;
import org.hl7.fhir.Extension;
import org.hl7.fhir.IssueSeverity;
import org.hl7.fhir.IssueSeverityList;
import org.hl7.fhir.IssueType;
import org.hl7.fhir.IssueTypeList;
import org.hl7.fhir.Narrative;
import org.hl7.fhir.NarrativeStatusList;
import org.hl7.fhir.OperationOutcome;
import org.hl7.fhir.OperationOutcomeIssue;
import org.hl7.fhir.Parameters;
import org.hl7.fhir.ParametersParameter;
import org.hl7.fhir.Resource;
import org.hl7.fhir.TypeRestfulInteraction;
import org.hl7.fhir.TypeRestfulInteractionList;
import org.hl7.fhir.Uri;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3._1999.xhtml.Div;
import org.xml.sax.SAXException;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import edu.harvard.i2b2.fhir.*;
import edu.harvard.i2b2.fhir.oauth2.ws.AuthenticationFilter;
import edu.harvard.i2b2.fhir.oauth2.ws.HttpHelper;
import edu.harvard.i2b2.fhir.server.ServerConfigs;
import edu.harvard.i2b2.fhir.server.ws.operation.DSSEvaluate;
import edu.harvard.i2b2.fhir.server.ws.operation.Validate;
import edu.harvard.i2b2.oauth2.core.ejb.AuthenticationService;
import edu.harvard.i2b2.oauth2.core.ejb.PatientBundleManager;
import edu.harvard.i2b2.oauth2.core.ejb.ProjectPatientMapManager;
import edu.harvard.i2b2.oauth2.core.ejb.QueryService;

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
	ProjectPatientMapManager ppmMgr;

	@EJB
	QueryService queryManager;

	@javax.ws.rs.core.Context
	ServletContext context;

	@EJB
	ServerConfigs serverConfigs;

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
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response getQueryResult(@PathParam("resourceName") String resourceName,
			@QueryParam("_include") List<String> includeResources, @QueryParam("filterf") String filterf,
			@HeaderParam("accept") String acceptHeader, @Context HttpHeaders headers,
			@Context HttpServletRequest request, @Context ServletContext servletContext) throws IOException, URISyntaxException {

		
		HttpSession session = request.getSession();
		URI fhirBase = HttpHelper.getBasePath(request, serverConfigs);
		String basePath = fhirBase.toString();
		
		return getQueryResultCore( resourceName, basePath, request.getRequestURI(), request.getQueryString(),
				 includeResources, filterf,
				 acceptHeader,  headers,
				session) ;
		
	}
	
	
	public Response getQueryResultCore( String resourceName, String basePath,String requestUri,String queryString,
			List<String> includeResources, String filterf,
			 String acceptHeader,  HttpHeaders headers,
			HttpSession session) throws IOException {

		String msg = null;
		String mediaType = null;
		MetaResourceDb md = new MetaResourceDb();

		logger.debug("got request parts: " + requestUri + "?" + queryString);

		try {
			logger.info("Query string:" + queryString);

			String head = "";
			for (String h : headers.getRequestHeaders().keySet()) {
				head += "Header->" + h + ":" + headers.getRequestHeader(h);

			}
			logger.info(head);

			// md = I2b2Helper.getMetaResourceDb(session, sbb);

			Class c = FhirUtil.getResourceClass(resourceName);
			Bundle s = null;
			// String basePath =
			// request.getRequestURL().toString().split(resourceName)[0];
			logger.debug("session id:" + session.getId());

			authService.authenticateSession(headers.getRequestHeader(AuthenticationFilter.AUTHENTICATION_HEADER).get(0),
					session);

			s = I2b2Helper.parsePatientIdToFetchPDO(session, requestUri,queryString, c.getSimpleName(), service, ppmMgr, null);

			if(FhirHelper.isPatientDependentResource(c)){
				md.addBundle(s);
			}else{
				FhirHelper.loadTestResources(md);
			}
			logger.info("running filter...");
			s = FhirUtil.getResourceBundle(md.getAll(c), basePath, "url");

			logger.info("running sophisticated query for:" +queryString);

			if (queryString != null) {
				String fhirQuery = c.getSimpleName() + "?" + queryString;

				// optimization to avoid query search on patient id
				if (false//XXX
						&& !c.equals(Patient.class)) {
					fhirQuery = fhirQuery.replaceAll("\\?patient=(\\d+)", "");
					fhirQuery = fhirQuery.replaceAll("\\?subject=(\\d+)", "");
					logger.trace("bypassing sophisticated query");
				} else {
					s = queryManager.runQueryEngine(fhirQuery, s, basePath);
				}
			}

			logger.info("including...._include:" + includeResources.toString());
			if (s.getEntry().size() > 0) {
				List<Resource> list = md.getIncludedResources(c, FhirUtil.getResourceListFromBundle(s),
						includeResources);
				logger.trace("includedListsize:" + list.size());
				s = FhirUtil.getResourceBundle(list, basePath, "url");
			}
			
			BundleLink bl= new BundleLink();
			Uri blUri=new Uri();
			blUri.setValue(basePath+requestUri.substring(1)+"?"+queryString);
			bl.setUrl(blUri);
			org.hl7.fhir.String rs= new org.hl7.fhir.String();
			rs.setValue("self");
			bl.setRelation(rs);
			s.getLink().add(bl);

			// logger.info("getting bundle string..."+JAXBUtil.toXml(s));
			// logger.info("size of db:" + md.getSize());
			logger.info("returning response..." + JAXBUtil.toXml(s));
			if (acceptHeader.contains("application/json") || acceptHeader.contains("application/json+fhir")) {
				msg = FhirUtil.bundleToJsonString(s);
				mediaType = "application/json+fhir";
			} else {
				msg = JAXBUtil.toXml(s);
				mediaType = "application/xml+fhir";
			}
			msg = I2b2Helper.removeSpace(msg);
			logger.info("acceptHeader:" + acceptHeader);
			return Response.ok().type(mediaType).header("session_id", session.getId()).entity(msg).build();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			return Response.ok(FhirHelper.generateOperationOutcome(e.getMessage(), IssueTypeList.EXCEPTION, IssueSeverityList.FATAL))
					.header("xreason", e.getMessage()).header("session_id", session.getId()).build();

		}

	}


	//POST version of getQueryResult using _search
	@POST
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/_search")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response getQueryResult_search(@PathParam("resourceName") String resourceName,
			@QueryParam("_include") List<String> includeResources, @QueryParam("filterf") String filterf,
			@HeaderParam("accept") String acceptHeader, @Context HttpHeaders headers,
			@Context HttpServletRequest request, @Context ServletContext servletContext) throws IOException, URISyntaxException {
			
		URI fhirBase = HttpHelper.getBasePath(request, serverConfigs);
		String basePath = fhirBase.toString();
		HttpSession session = request.getSession();
		return getQueryResultCore( resourceName, basePath, request.getRequestURI().replace("/_search",""), request.getQueryString(),
				 includeResources, filterf,
				 acceptHeader,  headers,
				session) ;
	}
	
	
	

	// http://localhost:8080/fhir-server-api-mvn/resources/i2b2/MedicationStatement/1000000005-1

	@GET
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/{id:[0-9a-zA-Z|-]+}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response getParticularResourceWrapper(@PathParam("resourceName") String resourceName,
			@PathParam("id") String id, @HeaderParam("accept") String acceptHeader, @Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@HeaderParam(AuthenticationFilter.AUTHENTICATION_HEADER) String tokString)
					throws DatatypeConfigurationException, ParserConfigurationException, SAXException, IOException,
					JAXBException, JSONException, XQueryUtilException, InterruptedException {

		logger.debug("got request " + request.getPathInfo() + "?" + request.getQueryString());
		HttpSession session = request.getSession();

		try {
			String msg = null;
			String mediaType = "";
			authService.authenticateSession(headers.getRequestHeader(AuthenticationFilter.AUTHENTICATION_HEADER).get(0),
					session);
			Resource r = getParticularResource(request, resourceName, id, headers);

			
			if (r != null) {
				return generateResponse(acceptHeader, request, r);
			} else {
				msg = "xreason:" + resourceName + " with id:" + id + " NOT found";
				return Response.ok(FhirHelper.generateOperationOutcome(msg, IssueTypeList.EXCEPTION, IssueSeverityList.ERROR))
						.header("session_id", session.getId()).build();
			}

		} catch (Exception e) {
			logger.error("", e);
			return Response.ok(FhirHelper.generateOperationOutcome(e.getMessage(), IssueTypeList.EXCEPTION, IssueSeverityList.FATAL))
					.header("xreason", e.getMessage()).header("session_id", session.getId()).build();
		}

	}

	private Resource getParticularResource(HttpServletRequest request, String resourceName, String id,
			HttpHeaders headers) throws IOException, XQueryUtilException, JAXBException, AuthenticationFailure,
					FhirServerException, InterruptedException {
		MetaResourceDb md = new MetaResourceDb();
		String msg = null;
		Resource r = null;
		Bundle s = null;
		String mediaType = null;
		HttpSession session = request.getSession();
		authService.authenticateSession(headers.getRequestHeader(AuthenticationFilter.AUTHENTICATION_HEADER).get(0),
				session);

		logger.debug("session id:" + session.getId());
		logger.info("searching particular resource:<" + resourceName + "> with id:<" + id + ">");
		Class c = FhirUtil.getResourceClass(resourceName);
		if (c == null)
			throw new RuntimeException("class not found for resource:" + resourceName);
		
		if(FhirHelper.isPatientDependentResource(c)){
			s = I2b2Helper.parsePatientIdToFetchPDO(session, request.getRequestURI(),request.getQueryString(), resourceName, service, ppmMgr, id);
			md.addBundle(s);
		}else{
			FhirHelper.loadTestResources(md);
		}
		r = md.getParticularResource(c, id);
		return r;
	}

	

	@GET
	@Path("")
	public Response info2() {
		return dummyToByPassAuthentication();
	}

	/*
	 * 
	 * 
	 * @GET
	 * 
	 * @Path("")
	 * 
	 * @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,
	 * "application/xml+fhir", "application/json+fhir" }) public Response
	 * info(){ return info2(); }
	 */

	@GET
	@Path("open")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir", "application/json+fhir",
			MediaType.TEXT_HTML })
	public Response dummyToByPassAuthentication() {
		// return Response.ok().entity("<html><header><META
		// http-equiv=\"refresh\" content=\"0;URL=../demo/\"></header><body>This
		// is the FHIR endpoint. Append a resource e.g. /Patient to this URL to
		// get data</body></html>").build();
		return Response.ok()
				.entity("This is the FHIR endpoint. Append a resource e.g. /Patient to this URL to get data").build();
	}

	@OPTIONS
	@Path("")
	public Response conformanceStatement2(@HeaderParam("accept") String acceptHeader,
			@Context HttpServletRequest request) throws JAXBException, JSONException, IOException,
					ParserConfigurationException, SAXException, URISyntaxException {
		return conformanceStatement(acceptHeader, request);

	}

	@GET
	@Path("smartstyleuri")
	public Response smartStyleUri() throws IOException {
		return Response.ok().entity(Utils.fileToString("/smartStyleUri.json")).build();
	}

	@GET
	@Path("metadata")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response conformanceStatement(@HeaderParam("accept") String acceptHeader,
			@Context HttpServletRequest request) throws JAXBException, JSONException, IOException,
					ParserConfigurationException, SAXException, URISyntaxException {

		URI fhirBase = HttpHelper.getBasePath(request, serverConfigs);

		Conformance c = ConformanceStatement.getStatement(fhirBase);
		logger.trace("conf:" + JAXBUtil.toXml(c));

		
		return generateResponse(acceptHeader, request, c);
		
		

	}

	

	// URL: [base]/Resource/$validate
	// URL: [base]/Resource/[id]/$validate

	@POST
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/{id:[0-9a-zA-Z|-]+}/$validate")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response validate1(@PathParam("resourceName") String resourceName, @PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader, @Context HttpHeaders headers,
			@Context HttpServletRequest request, String inTxt) throws IOException, JAXBException, URISyntaxException,
					XQueryUtilException, AuthenticationFailure, FhirServerException, InterruptedException, ParserConfigurationException, SAXException {
		Resource r = getParticularResource(request, resourceName, id, headers);
		inTxt = JAXBUtil.toXml(r);

		return validate2(resourceName, acceptHeader, request, inTxt);

	}

	@POST
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/$validate")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response validate2(@PathParam("resourceName") String resourceName,
			@HeaderParam("accept") String acceptHeader, @Context HttpServletRequest request, String inTxt)
					throws IOException, JAXBException, URISyntaxException, ParserConfigurationException, SAXException {
		HttpSession session = request.getSession();
		String mediaType;
		Parameters ps = null;
		Resource r = null;
		String outTxt = "-";

		try {

			r = JAXBUtil.fromXml(inTxt, Resource.class);
			Class resourceClass = FhirUtil.getResourceClass(resourceName);
			if (!resourceClass.isInstance(r))
				throw new FhirServerException("The input is not an instance of class:" + resourceClass);

			try {
				ps = JAXBUtil.fromXml(inTxt, Parameters.class);
				if (ps != null) {
					for (ParametersParameter p : ps.getParameter()) {
						logger.trace("pname:" + p.getName().getValue());
					}
				} else {
					logger.trace("ps is null");
				}
			} catch (ClassCastException e) {

			}
			if (ps == null) {
				try {
					//TODO move most of the code here(Param check into Validate class)
					outTxt = Validate.runValidate(inTxt);
				} catch (JAXBException e) {
				}
			}
		} catch (Exception e) {
			outTxt = e.getMessage();
		}

		Resource rOut=JAXBUtil.fromXml(outTxt, OperationOutcome.class);
		return generateResponse(acceptHeader, request, rOut);
	}

	

	@POST
	@Path("{resourceName:(DecisionSupportServiceModule|DecisionSupportRule|CQIF-Questionnaire|OrderSet)}/{id:[0-9a-zA-Z|-]+}/$evaluate")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response evaluate( @PathParam("resourceName") String resourceName, @PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader, @Context HttpHeaders headers,
			@Context HttpServletRequest request, String inTxt) throws IOException, JAXBException, URISyntaxException, ParserConfigurationException, SAXException{
		Resource outR=null;
		if(resourceName.equals("DecisionSupportServiceModule")){
			logger.trace("called DSS");
			outR=DSSEvaluate.evaluate(id,inTxt);
		}else{
			logger.trace("called evaluate for:"+resourceName);
			outR=FhirHelper.generateOperationOutcome("evaluate not (yet) implemented for "+resourceName, IssueTypeList.EXCEPTION, IssueSeverityList.ERROR);
		}	
		return generateResponse(acceptHeader, request, outR);
		
	}

	public Response generateResponse(@HeaderParam("accept") String acceptHeader, @Context HttpServletRequest request,
			Resource r) throws JAXBException, IOException, ParserConfigurationException, SAXException {
		if(r==null) throw new IllegalArgumentException("input resource is null");
		String mediaType;
		String outTxt="-";
		HttpSession session = request.getSession();
		if (acceptHeader.contains("application/json") || acceptHeader.contains("application/json+fhir")) {
			outTxt = FhirUtil.resourceToJsonString(r);
			mediaType = "application/json";
		} else {
			outTxt = JAXBUtil.toXml(r);
			mediaType = "application/xml+fhir";
		}
		outTxt = I2b2Helper.removeSpace(outTxt);
		logger.info("acceptHeader:" + acceptHeader);

		return Response.ok().type(mediaType).header("session_id", session.getId()).entity(outTxt).build();

	}

	// [base]/$meta
	// GET /fhir/Patient/$meta
	// GET /fhir/Patient/id/$meta
	// GET /fhir/Patient/id/$meta-add
	// GET /fhir/Patient/id/$meta-del

}