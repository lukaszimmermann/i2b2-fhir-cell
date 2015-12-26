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
import org.hl7.fhir.OperationOutcome;
import org.hl7.fhir.OperationOutcomeIssue;
import org.hl7.fhir.Resource;
import org.hl7.fhir.TypeRestfulInteraction;
import org.hl7.fhir.TypeRestfulInteractionList;
import org.hl7.fhir.Uri;
import org.hl7.fhir.instance.validation.Validator;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import edu.harvard.i2b2.fhir.*;
import edu.harvard.i2b2.fhir.oauth2.ws.AuthenticationFilter;
import edu.harvard.i2b2.fhir.oauth2.ws.HttpHelper;
import edu.harvard.i2b2.fhir.query.QueryEngine;
import edu.harvard.i2b2.fhir.server.ConfigParameter;
import edu.harvard.i2b2.fhir.server.ServerConfigs;
import edu.harvard.i2b2.oauth2.core.ejb.AccessTokenService;
import edu.harvard.i2b2.oauth2.core.ejb.AuthenticationService;
import edu.harvard.i2b2.oauth2.core.ejb.PatientBundleManager;
import edu.harvard.i2b2.oauth2.core.ejb.ProjectPatientMapManager;
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
			@Context HttpServletRequest request, @Context ServletContext servletContext) throws IOException {

		HttpSession session = null;
		String msg = null;
		String mediaType = null;
		MetaResourceDb md = new MetaResourceDb();

		logger.debug("got request " + request.getPathInfo() + "?" + request.getQueryString());

		try {
			logger.info("Query param:" + request.getParameterMap().keySet().toString());

			String head = "";
			for (String h : headers.getRequestHeaders().keySet()) {
				head += "Header->" + h + ":" + headers.getRequestHeader(h);

			}
			logger.info(head);

			// md = I2b2Helper.getMetaResourceDb(session, sbb);

			Class c = FhirUtil.getResourceClass(resourceName);
			Bundle s = null;
			session = request.getSession();
			//String basePath = request.getRequestURL().toString().split(resourceName)[0];
			URI fhirBase = HttpHelper.getBasePath(request, serverConfigs);
			String basePath=fhirBase.toString();
			logger.debug("session id:" + session.getId());

			authService.authenticateSession(headers.getRequestHeader(AuthenticationFilter.AUTHENTICATION_HEADER).get(0),
					session);

			s = I2b2Helper.parsePatientIdToFetchPDO(session, request, c.getSimpleName(), service, ppmMgr,null);

			md.addBundle(s);

			logger.info("running filter...");
			s = FhirUtil.getResourceBundle(md.getAll(c), basePath, "url");

			logger.info("running sophisticated query for:" + request.getQueryString());

			if (request.getQueryString() != null) {
				String fhirQuery = c.getSimpleName() + "?" + request.getQueryString();

				// optimization to avoid query search on patient id
				if (!c.equals(Patient.class)) {
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
			return Response.ok(getOperationOutcome(e.getMessage(), IssueTypeList.EXCEPTION, IssueSeverityList.FATAL))
					.header("xreason", e.getMessage()).header("session_id", session.getId()).build();

		}

	}

	// http://localhost:8080/fhir-server-api-mvn/resources/i2b2/MedicationStatement/1000000005-1

	@GET
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/{id:[0-9|-]+}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response getParticularResourceWrapper(@PathParam("resourceName") String resourceName, @PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader, @Context HttpHeaders headers,
			@Context HttpServletRequest request,
			@HeaderParam(AuthenticationFilter.AUTHENTICATION_HEADER) String tokString)
					throws DatatypeConfigurationException, ParserConfigurationException, SAXException, IOException,
					JAXBException, JSONException, XQueryUtilException, InterruptedException {

		logger.debug("got request " + request.getPathInfo() + "?" + request.getQueryString());
		HttpSession session = request.getSession();

		try {

			// MetaResourceDb md = I2b2Helper.getMetaResourceDb(session, sbb);

			MetaResourceDb md = new MetaResourceDb();
			String msg = null;
			String mediaType="";
			authService.authenticateSession(headers.getRequestHeader(AuthenticationFilter.AUTHENTICATION_HEADER).get(0),
					session);
			Resource r =getParticularResource(request,resourceName,id,headers);
			
			

			if (acceptHeader.contains("application/json") || acceptHeader.contains("application/json+fhir")) {
				msg = FhirUtil.resourceToJsonString(r);
				mediaType = "application/json";
			} else {
				msg = JAXBUtil.toXml(r);
				mediaType = "application/xml";
			}

			msg = I2b2Helper.removeSpace(msg);
			if (r != null) {
				return Response.ok(msg).header("session_id", session.getId()).type(mediaType).build();
			} else {
				msg = "xreason:" + resourceName + " with id:" + id + " NOT found";
				return Response.ok(getOperationOutcome(msg, IssueTypeList.EXCEPTION, IssueSeverityList.ERROR))
						.header("session_id", session.getId()).build();
			}

		} catch (Exception e) {
			logger.error("", e);
			return Response.ok(getOperationOutcome(e.getMessage(), IssueTypeList.EXCEPTION, IssueSeverityList.FATAL))
					.header("xreason", e.getMessage()).header("session_id", session.getId()).build();
		}

	}

	private Resource getParticularResource(HttpServletRequest request, String resourceName, String id,HttpHeaders headers) throws IOException, XQueryUtilException, JAXBException, AuthenticationFailure, FhirServerException, InterruptedException {
		MetaResourceDb md = new MetaResourceDb();
		String msg = null;
		Resource r = null;
		Bundle s = null;
		String mediaType = null;
		HttpSession session=request.getSession();
		authService.authenticateSession(headers.getRequestHeader(AuthenticationFilter.AUTHENTICATION_HEADER).get(0),
				session);

		
		logger.debug("session id:" + session.getId());
		logger.info("searching particular resource:<" + resourceName + "> with id:<" + id + ">");
		Class c = FhirUtil.getResourceClass(resourceName);
		if (c == null)
			throw new RuntimeException("class not found for resource:" + resourceName);
		
		s = I2b2Helper.parsePatientIdToFetchPDO(session, request, resourceName, service, ppmMgr,id);
		md.addBundle(s);
		;

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

		// Patient

		rest.getResource().add(getReadOnlyConformanceResource("Patient", new HashMap<String, String>() {
			{
				put("_id", "token");
				put("gender", "token");
			}
		}));
		rest.getResource().add(getReadOnlyConformanceResource("MedicationOrder", new HashMap<String, String>() {
			{
				put("_id", "token");
				put("patient", "token");
				put("medication", "reference");
			}
		}));
		rest.getResource().add(getReadOnlyConformanceResource("Medication", new HashMap<String, String>() {
			{
				put("_id", "token");
				put("code", "token");
			}
		}));
		rest.getResource().add(getReadOnlyConformanceResource("Observation", new HashMap<String, String>() {
			{
				put("_id", "token");
				put("code", "token");
				put("value", "string");
			}
		}));
		rest.getResource().add(getReadOnlyConformanceResource("Condition", new HashMap<String, String>() {
			{
				put("_id", "token");
				put("code", "token");
			}
		}));

		c.getRest().add(rest);

		logger.trace("conf:" + JAXBUtil.toXml(c));

		String msg;
		String mediaType;
		if (acceptHeader.contains("application/json") || acceptHeader.contains("application/json+fhir")) {
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

	private OperationOutcome getOperationOutcome(String message, IssueTypeList issueType,
			IssueSeverityList issueSeverity) {
		OperationOutcome o = new OperationOutcome();
		OperationOutcomeIssue i = new OperationOutcomeIssue();
		IssueType iType = new IssueType();
		iType.setValue(issueType);
		i.setCode(iType);
		IssueSeverity severity = new IssueSeverity();
		severity.setValue(issueSeverity);
		i.setSeverity(severity);
		org.hl7.fhir.String s1 = new org.hl7.fhir.String();
		s1.setValue(message);
		i.setDiagnostics(s1);
		o.getIssue().add(i);

		return o;
	}

	private ConformanceResource getReadOnlyConformanceResource(String name, HashMap<String, String> hm) {
		ConformanceResource p = new ConformanceResource();
		Code value2 = new Code();
		value2.setValue(name);
		p.setType(value2);
		ConformanceInteraction ci = new ConformanceInteraction();
		TypeRestfulInteraction value = new TypeRestfulInteraction();
		value.setValue(TypeRestfulInteractionList.READ);
		ci.setCode(value);
		p.getInteraction().add(ci);
		for (String k : hm.keySet()) {
			addConformanceSearchParam(p, k, hm.get(k));
		}
		return p;
	}

	private void addConformanceSearchParam(ConformanceResource p, String name, String type) {
		ConformanceSearchParam sp = new ConformanceSearchParam();
		org.hl7.fhir.String s = new org.hl7.fhir.String();
		s.setValue(name);
		sp.setName(s);
		Code value3 = new Code();
		value3.setValue(type);
		sp.setType(value3);
		p.getSearchParam().add(sp);

	}

	// URL: [base]/Resource/$validate
	// URL: [base]/Resource/[id]/$validate

	@POST
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/{id:[0-9|-]+}/$validate")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
	"application/json+fhir" })
	public Response validate1(@PathParam("resourceName") String resourceName, @PathParam("id") String id,@HeaderParam("accept") String acceptHeader, @Context HttpHeaders headers,
			@Context HttpServletRequest request,  String inTxt) throws IOException, JAXBException, URISyntaxException, XQueryUtilException, AuthenticationFailure, FhirServerException, InterruptedException {
		Resource r=getParticularResource(request,resourceName,id,headers);
		inTxt=JAXBUtil.toXml(r);
		
		return validate2(acceptHeader,request,inTxt);

	}

	@POST
	@Path("validate")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, "application/xml+fhir",
			"application/json+fhir" })
	public Response validate2(
			
			@HeaderParam("accept") String acceptHeader, 
			@Context HttpServletRequest request,  String inTxt)
					throws IOException, JAXBException, URISyntaxException {
		HttpSession session = request.getSession();
		String mediaType;
		String outTxt = validate(inTxt);

		if (acceptHeader.contains("application/json") || acceptHeader.contains("application/json+fhir")) {
			// outTxt = FhirUtil.resourceToJsonString(oo);
			mediaType = "application/json+fhir";
		} else {
			// outTxt = JAXBUtil.toXml(oo);
			mediaType = "application/xml+fhir";
		}

		return Response.ok().type(mediaType).header("session_id", session.getId()).entity(outTxt).build();

	}

	private String validate(String inTxt) throws JAXBException {
		String outTxt = "-";
		/*
		Resource r = JAXBUtil.fromXml(inTxt, Resource.class);
		Class c = FhirUtil.getResourceClass(r);
		logger.debug("Resource is of type:" + c);
		// logger.debug(" got Resource:"+JAXBUtil.toXml(r));

		Bundle s = null;
		if (Bundle.class.isInstance(r)) {
			s = (Bundle) r;
		}
*/
		String ooTxt = FhirUtil.getValidatorErrorMessage(inTxt);
		outTxt = ooTxt;
		logger.trace("ooTxt:" + ooTxt);
		// OperationOutcome oo=JAXBUtil.fromXml(ooTxt, OperationOutcome.class);
		return outTxt;
	}

	/*
	 * static Validator v; static public String init1(String fhirBase) { String
	 * outText="-"; if (v == null) { try{ v = new Validator(); //String path =
	 * Utils.getFilePath("validation-min.xml.zip"); String
	 * fileName="/validation-min.xml.zip"; File file = new
	 * File(FhirUtil.class.getResource(fileName).getFile());
	 * 
	 * 
	 * //v.setDefinitions(file.getAbsolutePath());//"file:///"+FhirUtil.class.
	 * getResource(fileName).getPath()); //logger.trace(v.getDefinitions());
	 * logger.trace("ready");
	 * 
	 * 
	 * File temp = File.createTempFile("tempFileForValidator", ".tmp");
	 * FileOutputStream outTemp = new FileOutputStream(temp); String
	 * input=Utils.getFile("/example/fhir/DSTU2/singlePatient.xml");
	 * IOUtils.write(input, outTemp); outTemp.close(); logger.trace("tmp file:"
	 * +new Scanner(temp).useDelimiter("\\Z").next());//temp.getAbsolutePath());
	 * 
	 * v.setSource(fhirBase.split("srv*")[0]+"validation-min.xml.zip");//temp.
	 * getPath()); logger.trace("source"+v.getSource()); v.process();
	 * 
	 * temp.delete(); outText=v.getOutcome().toString(); logger.debug(outText);
	 * 
	 * }catch(Exception e){ logger.error(e.getMessage(),e); } } return outText;
	 * }
	 */
}