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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.hl7.fhir.ConformanceStatementKind;
import org.hl7.fhir.ConformanceStatementKindList;
import org.hl7.fhir.Extension;
import org.hl7.fhir.Id;
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
import org.hl7.fhir.UnknownContentCode;
import org.hl7.fhir.UnknownContentCodeList;
import org.hl7.fhir.Uri;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3._1999.xhtml.Div;
import org.xml.sax.SAXException;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import edu.harvard.i2b2.Icd9.Icd9Mapper;
import edu.harvard.i2b2.fhir.*;
import edu.harvard.i2b2.fhir.oauth2.ws.AuthenticationFilter;
import edu.harvard.i2b2.fhir.oauth2.ws.HttpHelper;
import edu.harvard.i2b2.fhir.server.ServerConfigs;
import edu.harvard.i2b2.fhir.server.ws.operation.CdsHook;
import edu.harvard.i2b2.fhir.server.ws.operation.DSSEvaluate;
import edu.harvard.i2b2.fhir.server.ws.operation.Validate;
import edu.harvard.i2b2.loinc.LoincMapper;
import edu.harvard.i2b2.oauth2.core.ejb.AuthenticationService;
import edu.harvard.i2b2.oauth2.core.ejb.PatientBundleManager;
import edu.harvard.i2b2.oauth2.core.ejb.ProjectPatientMapManager;
import edu.harvard.i2b2.oauth2.core.ejb.QueryService;
import edu.harvard.i2b2.rxnorm.NdcToRxNormMapper;

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

	// everything
	@GET
	@Path("Patient/{resourceId:[0-9a-zA-Z|-]+}/$everything")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response everythingWrapper(@PathParam("compartmentName") String compartmentName,
			@PathParam("resourceId") String resourceId, @QueryParam("_include") List<String> includeResources,
			@QueryParam("filterf") String filterf, @HeaderParam("accept") String acceptHeader,
			@Context HttpHeaders headers, @Context HttpServletRequest request, @Context ServletContext servletContext)
					throws IOException, URISyntaxException {

		HttpSession session = request.getSession();
		URI fhirBase = HttpHelper.getBasePath(request, serverConfigs);
		String basePath = fhirBase.toString().split("patient")[0];
		String serverUriPath = HttpHelper.getServerUri(request, serverConfigs).toString();
		// basePath=(new URI(basePath)).toString();
		logger.trace("basePath:" + basePath);
		String queryString = "patient=" + resourceId;
		String requestUri = compartmentName;
		String rawRequest = basePath + request.getRequestURI();
		return getQueryResultCore("everything", basePath, requestUri, queryString, request.getRequestURL().toString(),
				includeResources, filterf, acceptHeader, headers, session, serverUriPath);

	}

	// compartment
	@GET
	@Path("Patient/{resourceId:[0-9a-zA-Z|-]+}/{compartmentName:" + FhirUtil.RESOURCE_LIST_REGEX + "}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response compartmentWrapper(@PathParam("compartmentName") String compartmentName,
			@PathParam("resourceId") String resourceId, @QueryParam("_include") List<String> includeResources,
			@QueryParam("filterf") String filterf, @HeaderParam("accept") String acceptHeader,
			@Context HttpHeaders headers, @Context HttpServletRequest request, @Context ServletContext servletContext)
					throws IOException, URISyntaxException {

		HttpSession session = request.getSession();
		URI fhirBase = HttpHelper.getBasePath(request, serverConfigs);
		String basePath = fhirBase.toString().split("patient")[0];
		String serverUriPath = HttpHelper.getServerUri(request, serverConfigs).toString();
		// basePath=(new URI(basePath)).toString();
		logger.trace("basePath:" + basePath);
		String queryString = "patient=" + resourceId;
		String requestUri = compartmentName;
		String rawRequest = basePath + request.getRequestURI();
		return getQueryResultCore(compartmentName, basePath, requestUri, queryString,
				request.getRequestURL().toString(), includeResources, filterf, acceptHeader, headers, session,
				serverUriPath);

	}

	@GET
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response getQueryResult(@PathParam("resourceName") String resourceName,
			@QueryParam("_include") List<String> includeResources, @QueryParam("filterf") String filterf,
			@QueryParam("_include") List<String> pageNum, @HeaderParam("accept") String acceptHeader,
			@Context HttpHeaders headers, @Context HttpServletRequest request, @Context ServletContext servletContext)
					throws IOException, URISyntaxException {

		HttpSession session = request.getSession();
		URI fhirBase = HttpHelper.getBasePath(request, serverConfigs);
		String basePath = fhirBase.toString();
		String serverUriPath = HttpHelper.getServerUri(request, serverConfigs).toString();
		logger.trace("basePath:" + basePath);

		return getQueryResultCore(resourceName, basePath, request.getRequestURI(), request.getQueryString(),
				request.getRequestURI() + "?" + request.getQueryString(), includeResources, filterf, acceptHeader,
				headers, session, serverUriPath);

	}

	public Response getQueryResultCore(String resourceName, String basePath, String requestUri, String queryString,
			String rawRequestUrl, List<String> includeResources, String filterf, String acceptHeader,
			HttpHeaders headers, HttpSession session, String serverUriPath) throws IOException {

		String msg = null;
		String mediaType = null;
		Bundle s = null;

		MetaResourceDb md = new MetaResourceDb();

		logger.debug("got request parts: " + requestUri + "?" + queryString);
		logger.trace("basePath:" + basePath);

		try {
			logger.info("Query string:" + queryString);

			String head = "";
			for (String h : headers.getRequestHeaders().keySet()) {
				head += "Header->" + h + ":" + headers.getRequestHeader(h);

			}
			logger.info(head);

			// md = I2b2Helper.getMetaResourceDb(session, sbb);

			Class c = FhirUtil.getResourceClass(resourceName);

			logger.trace("rawRequestUrl:" + rawRequestUrl);
			// String basePath =rawRequestUrl.split(resourceName)[0];
			logger.debug("session id:" + session.getId());

			authService.authenticateSession(headers.getRequestHeader(AuthenticationFilter.AUTHENTICATION_HEADER).get(0),
					session);

			s = I2b2Helper.parsePatientIdToFetchPDO(session, requestUri, queryString, c.getSimpleName(), service,
					ppmMgr, null);

			if (!(c.equals("$everything"))) {

				if (FhirHelper.isPatientDependentResource(c)) {
					md.addBundle(s);
				} else {
					FhirHelper.loadTestResources(md);
				}
				logger.info("running filter...");
				s = FhirUtil.getResourceBundle(md.getAll(c), basePath, "url");
			}
			logger.info("running sophisticated query for:" + queryString);

			if (queryString != null) {
				String fhirQuery = c.getSimpleName() + "?" + queryString;

				// optimization to avoid query search on patient id
				if (false// XXX
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
				logger.trace("basePath:" + basePath);
				s = FhirUtil.getResourceBundle(list, basePath, "url");
			}

			s.getLink().add(FhirUtil.createBundleLink("self", serverUriPath + requestUri + "?"
					+ ((queryString != null) ? queryString.replaceAll("&page=\\d+", "") : "")));

			// logger.info("getting bundle string..."+JAXBUtil.toXml(s));
			// logger.info("size of db:" + md.getSize());

			int pageNum = 1;
			if (queryString != null) {
				Pattern p = Pattern.compile(".*page=(\\d+).*");
				Matcher m = p.matcher(queryString);
				if (m.matches()) {
					String pageNumStr = m.group(1);
					logger.info("pageNum=" + pageNumStr);
					pageNum = Integer.parseInt(pageNumStr);
				}
			}
			s = FhirUtil.pageBundle(s, 20, pageNum);

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
			logger.error(e.getMessage(), e);
			return Response
					.ok(FhirHelper.generateOperationOutcome(e.getMessage(), IssueTypeList.EXCEPTION,
							IssueSeverityList.FATAL))
					.header("xreason", e.getMessage()).header("session_id", session.getId()).build();

		}

	}

	// POST version of getQueryResult using _search
	@POST
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/_search")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response getQueryResult_search(@PathParam("resourceName") String resourceName,
			@QueryParam("_include") List<String> includeResources, @QueryParam("filterf") String filterf,
			@HeaderParam("accept") String acceptHeader, @Context HttpHeaders headers,
			@Context HttpServletRequest request, @Context ServletContext servletContext)
					throws IOException, URISyntaxException {

		URI fhirBase = HttpHelper.getBasePath(request, serverConfigs);
		String serverUriPath = HttpHelper.getServerUri(request, serverConfigs).toString();
		String basePath = fhirBase.toString();
		HttpSession session = request.getSession();
		return getQueryResultCore(resourceName, basePath, request.getRequestURI().replace("/_search", ""),
				request.getQueryString(), request.getRequestURI() + "?" + request.getQueryString(), includeResources,
				filterf, acceptHeader, headers, session, serverUriPath);
	}

	// http://localhost:8080/fhir-server-api-mvn/resources/i2b2/MedicationStatement/1000000005-1

	@GET
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/{id:[0-9|-]+}")
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
				return Response
						.ok(FhirHelper.generateOperationOutcome(msg, IssueTypeList.EXCEPTION, IssueSeverityList.ERROR))
						.header("session_id", session.getId()).build();
			}

		} catch (Exception e) {
			logger.error("", e);
			return Response
					.ok(FhirHelper.generateOperationOutcome(e.getMessage(), IssueTypeList.EXCEPTION,
							IssueSeverityList.FATAL))
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

		if (FhirHelper.isPatientDependentResource(c)) {

			String patientId = id;
			if (patientId.contains("-"))
				patientId = patientId.split("-")[0];

			s = I2b2Helper.parsePatientIdToFetchPDO(session, request.getRequestURI(), request.getQueryString(),
					resourceName, service, ppmMgr, patientId);
			md.addBundle(s);
		} else {
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
	@Path("reset_cache")
	public Response resetCache() throws IOException {
		service.resetCache();
		ppmMgr.resetCache();
		return Response.ok().entity("Cache has been reset").build();
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

		Code statusCode = new Code();
		statusCode.setValue("active");
		c.setStatus(statusCode);

		c.setId(FhirUtil.generateId(Integer.toString(request.getRequestURI().hashCode())));
		ConformanceStatementKind kindValue = new ConformanceStatementKind();
		kindValue.setValue(ConformanceStatementKindList.INSTANCE);
		c.setKind(kindValue);
		c.setFhirVersion(FhirUtil.generateId("1.2.0"));

		UnknownContentCode uccValue = new UnknownContentCode();
		uccValue.setValue(UnknownContentCodeList.NO);
		c.setAcceptUnknown(uccValue);

		Code fc1 = new Code();
		fc1.setValue("xml");
		c.getFormat().add(fc1);

		Code fc2 = new Code();
		fc2.setValue("json");
		c.getFormat().add(fc2);

		request.getRequestURI().hashCode();

		return generateResponse(acceptHeader, request, c);

	}

	// URL: [base]/Resource/$Validate
	// URL: [base]/Resource/[id]/$Validate

	@GET
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/{id:[0-9a-zA-Z|-]+}/$validate")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response validateWrapperGet(@PathParam("resourceName") String resourceName, @PathParam("id") String id,
			@QueryParam("profile") String profile, @HeaderParam("accept") String acceptHeader,
			@Context HttpHeaders headers, @Context HttpServletRequest request, String inTxt)
					throws IOException, JAXBException, URISyntaxException, XQueryUtilException, AuthenticationFailure,
					FhirServerException, InterruptedException, ParserConfigurationException, SAXException {
		Resource r = getParticularResource(request, resourceName, id, headers);
		inTxt = JAXBUtil.toXml(r);
		return validate(resourceName, acceptHeader, request, inTxt, profile);
	}

	@POST
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/{id:[0-9a-zA-Z|-]+}/$validate")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response validateWrapperPost(@PathParam("resourceName") String resourceName, @PathParam("id") String id,
			@QueryParam("profile") String profile, @HeaderParam("accept") String acceptHeader,
			@Context HttpHeaders headers, @Context HttpServletRequest request, String inTxt)
					throws IOException, JAXBException, URISyntaxException, XQueryUtilException, AuthenticationFailure,
					FhirServerException, InterruptedException, ParserConfigurationException, SAXException {
		Resource r = getParticularResource(request, resourceName, id, headers);
		inTxt = JAXBUtil.toXml(r);
		return validate(resourceName, acceptHeader, request, inTxt, profile);
	}

	@POST
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/$validate")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response validate(@PathParam("resourceName") String resourceName, @HeaderParam("accept") String acceptHeader,
			@Context HttpServletRequest request, String inTxt, String profile) throws IOException, URISyntaxException,
					ParserConfigurationException, SAXException, FhirServerException, JAXBException {
		HttpSession session = request.getSession();
		String mediaType;
		Parameters ps = null;
		String resourceTxt = null;
		Resource r = null;
		String outTxt = "-";
		logger.trace("will run validator");

		try {
			Class resourceClass = FhirUtil.getResourceClass(resourceName);

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
				resourceTxt = inTxt;
				try {
					r = JAXBUtil.fromXml(resourceTxt, resourceClass);
				} catch (JAXBException e) {

					Throwable e2 = e.getLinkedException();
					throw new FhirServerException(e2.getMessage(), e2);
				}
				logger.trace(
						"could transform to" + resourceClass.getSimpleName() + "\n" + r.getClass().getSimpleName());

			} else {
				for (ParametersParameter p : ps.getParameter()) {
					logger.trace("getting pname:" + p.getName().getValue());
					if (p.getName().getValue().equals("resource")) {
						r = FhirUtil.getResourceFromContainer(p.getResource());
						resourceTxt = JAXBUtil.toXml(r);
					}

				}
				if (r == null) {
					String msg = "Resource was not specified correctly in the Parameters";
					logger.warn(msg);
					throw new FhirServerException(msg);
				}
			}

			if (!r.getClass().getSimpleName().equals(resourceClass.getSimpleName())) {
				String msg = "The input is not an instance of class:" + resourceClass;
				logger.warn(msg);
				throw new FhirServerException(msg);
			}

			outTxt = Validate.runValidate(resourceTxt, profile);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return generateResponse(acceptHeader, request, FhirHelper.generateOperationOutcome(e.toString(),
					IssueTypeList.EXCEPTION, IssueSeverityList.FATAL));
		}

		Resource rOut = JAXBUtil.fromXml(outTxt, OperationOutcome.class);
		return generateResponse(acceptHeader, request, rOut);
	}

	@POST
	@Path("{resourceName:(DecisionSupportServiceModule|DecisionSupportRule|CQIF-Questionnaire|OrderSet)}/{id:[0-9a-zA-Z|-]+}/$evaluate")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response evaluate(@PathParam("resourceName") String resourceName, @PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader, @Context HttpHeaders headers,
			@Context HttpServletRequest request, String inTxt)
					throws IOException, JAXBException, URISyntaxException, ParserConfigurationException, SAXException {
		Resource outR = null;
		if (resourceName.equals("DecisionSupportServiceModule")) {
			logger.trace("called DSS");
			outR = DSSEvaluate.evaluate(id, inTxt);
		} else {
			logger.trace("called evaluate for:" + resourceName);
			outR = FhirHelper.generateOperationOutcome("evaluate not (yet) implemented for " + resourceName,
					IssueTypeList.EXCEPTION, IssueSeverityList.ERROR);
		}
		return generateResponse(acceptHeader, request, outR);

	}

	@POST
	@Path("$cds-hook")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response cdsHook(@HeaderParam("accept") String acceptHeader, @Context HttpHeaders headers,
			@Context HttpServletRequest request, String inTxt) {
		try {
			Resource outR = null;
			logger.trace("called cds-hook");
			Parameters parameters = JAXBUtil.fromXml(inTxt, Parameters.class);
			if (parameters != null) {
				outR = (new CdsHook(parameters)).execute();
			} else {
				logger.trace("called cds-hook with null parameters");
				outR = FhirHelper.generateOperationOutcome("cds not (yet) implemented for ", IssueTypeList.EXCEPTION,
						IssueSeverityList.ERROR);
			}
			return generateResponse(acceptHeader, request, outR);
		} catch (Exception e) {
			return generateFatalResponse(e, request);
		}

	}

	// GET [base]/ValueSet/$lookup?system=http://loinc.org&code=1963-8
	// GET ValueSet/$lookup?system=http://hl7.org/fhir/sid/icd-9-cm&code=174.9
	// GET
	// ValueSet/$lookup?system=http://www.nlm.nih.gov/research/umls/rxnorm&code=1191

	@GET
	@Path("ValueSet/$lookup")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response valueSetLookup(@QueryParam("code") String code, @QueryParam("system") String system,
			@HeaderParam("accept") String acceptHeader, @Context HttpHeaders headers,
			@Context HttpServletRequest request)
					throws IOException, JAXBException, URISyntaxException, ParserConfigurationException, SAXException {
		Resource r = null;
		if (system.equals("http://loinc.org")) {
			LoincMapper loincMapper = new LoincMapper();
			String display = loincMapper.getLoincName(code);
			if (display != null)
				r = FhirHelper.generateConceptLookUpOutput(display, null, display, false, null, code);
		} else if (system.equals("http://hl7.org/fhir/sid/icd-9-cm")) {
			Icd9Mapper icd9Mapper = new Icd9Mapper();
			String display = icd9Mapper.getIcd9Name(code);
			if (display != null)
				r = FhirHelper.generateConceptLookUpOutput(display, null, display, false, null, code);
		} else if (system.equals("http://www.nlm.nih.gov/research/umls/rxnorm")) {
			NdcToRxNormMapper rxMapper = new NdcToRxNormMapper();
			String display = rxMapper.getRxCuiName(code);
			if (display != null)
				r = FhirHelper.generateConceptLookUpOutput(display, null, display, false, null, code);
		} else {
			r = FhirHelper.generateOperationOutcome("lookup not implemented for code system:" + system,
					IssueTypeList.EXCEPTION, IssueSeverityList.ERROR);
		}
		if (r == null)
			r = FhirHelper.generateOperationOutcome("code:" + code + " is invalid for system:" + system,
					IssueTypeList.NOT_FOUND, IssueSeverityList.ERROR);
		return generateResponse(acceptHeader, request, r);

	}

	public Response generateResponse(@HeaderParam("accept") String acceptHeader, @Context HttpServletRequest request,
			Resource r) throws JAXBException, IOException, ParserConfigurationException, SAXException {
		if (r == null)
			throw new IllegalArgumentException("input resource is null");
		String mediaType;
		String outTxt = "-";
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

	public Response generateFatalResponse(Exception e, HttpServletRequest request) {
		HttpSession session = request.getSession();
		return Response
				.ok(FhirHelper.generateOperationOutcome(e.getMessage(), IssueTypeList.EXCEPTION,
						IssueSeverityList.FATAL))
				.header("xreason", e.getMessage()).header("session_id", session.getId()).build();

	}
	// [base]/$meta
	// GET /fhir/Patient/$meta
	// GET /fhir/Patient/id/$meta
	// GET /fhir/Patient/id/$meta-add
	// GET /fhir/Patient/id/$meta-del

}