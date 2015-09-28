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
package edu.harvard.i2b2.fhir.oauth2.ws;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpRequest;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.Config;
import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.Project;
import edu.harvard.i2b2.oauth2.core.ejb.AuthTokenService;
import edu.harvard.i2b2.oauth2.core.ejb.AuthenticationService;
import edu.harvard.i2b2.oauth2.core.entity.AuthToken;

//import edu.harvard.i2b2.fhirserver.ejb.AuthTokenManager;

/*
 * http://blogs.steeplesoft.com/posts/2013/a-simple-oauth2-client-and-server-example-part-i.html
 */

/*
 * The authorization server validates the request to ensure that all
 required parameters are present and valid.  The authorization server
 MUST verify that the redirection URI to which it will redirect the
 access token matches a redirection URI registered by the client as
 described in Section 3.1.2.

 If the request is valid, the authorization server authenticates the
 resource owner and obtains an authorization decision (by asking the
 resource owner or by establishing approval via other means).

 When a decision is established, the authorization server directs the
 user-agent to the provided client redirection URI using an HTTP
 redirection response, or by other means available to it via the
 user-agent.
 */
@Path("authz")
public class OAuth2AuthzEndpoint {
	static Logger logger = LoggerFactory.getLogger(OAuth2AuthzEndpoint.class);


	@EJB
	AuthTokenService authTokenBean;

	@EJB 
	AuthenticationService service;
	
	// http://localhost:8080/srv-dstu2-0.2/api/authz/authorize?scope=launch%3A1000000005%2BPatient%2F*&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fclient-dstu2-0.2%2Foauth2%2FgetAuthCode&client_id=fcclient1
	@GET
	@Path("authorize")
	public Response authorize(@Context HttpServletRequest request,
			@Context HttpRequest httprequest) throws URISyntaxException,
			OAuthSystemException {
		String authorizationCode = null;
		try {
			logger.trace("got request to authorize for OAuth2");
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
			OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(
					new MD5Generator());

			// build response according to response_type
			String responseType = oauthRequest
					.getParam(OAuth.OAUTH_RESPONSE_TYPE);

			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
					.authorizationResponse(request,
							HttpServletResponse.SC_FOUND);

			String redirectURI = oauthRequest.getRedirectURI();
			String state = oauthRequest.getState();
			final OAuthResponse Oresponse = builder.location(redirectURI)
					.buildQueryMessage();
			URI url = new URI(Oresponse.getLocationUri());
			Response response = Response.status(Oresponse.getResponseStatus())
					.location(url).build();
			if (url == null)
				throw new OAuthSystemException("redirectURI is missing");

			HttpSession session = request.getSession();

			String finalUri = successfulResponse(request);
			// finalUri+="&state="+state;
			logger.info("generated finalUri:" + finalUri);
			session.setAttribute("redirectUri", oauthRequest.getRedirectURI());
			session.setAttribute("clientId", oauthRequest.getClientId());
			session.setAttribute("state", oauthRequest.getState());
			session.setAttribute("scope", oauthRequest.getScopes());

			session.setAttribute("finalUri", finalUri);

			String clientId = (String) oauthRequest.getClientId();
			if (isClientIdValid(clientId) == true) {
				
				String uri = HttpHelper.getBasePath(request).toString() + "../../i2b2/login.xhtml";
				logger.trace("redirecting to:" + uri);
				return Response.status(Status.MOVED_PERMANENTLY)
						.location(new URI(uri))
						.header("session_id", request.getSession().getId()).build();
				
			} else
				return Response.status(Status.UNAUTHORIZED)
						.entity("clientId IsNotValid").build();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.header("xreason", e.getMessage()).build();
		}
	}

	
	// Authenticate resource owner
	// is there an i2b2 AuthorizationCode record associated with the submitted
	// AuthorizationCode
	boolean isClientIdValid(String clientId) {
		//if (clientId.equals("fcclient1"))
			return true;
		/*
		 * AuthToken authTok = authTokenBean.findByClientId(clientId);
		 * 
		 * if (authTok == null || authTok.getAuthorizationCode() == null ||
		 * authTok.getClientId() == null || authTok.getClientRedirectUri() ==
		 * null || authTok.getI2b2Token() == null || authTok.getResourceUserId()
		 * == null || authTok.getExpiryDate() == null) return false; Date
		 * currentDate = new Date(); if
		 * (currentDate.after(authTok.getExpiryDate())) return false; if
		 * (authTok.getClientId().equals(clientId)) return true; else return
		 * false;
		 */
		//return false;
	}

	@Path("i2b2login")
	@GET
	public Response srvResourceOwnerLoginPage(@Context HttpServletRequest request)
			throws URISyntaxException {
		HttpSession session=request.getSession();
		String msg=(String) session.getAttribute("msg"); if (msg==null) msg="";
		
		String loginPage = "<div align=\"center\">"
				+"<br><p style=\"color:red\">"+msg+"</p><br>"
				+ "The application located at the following URL is requesting read access to i2b2 data accessible to your account:<br><bold>"
				+session.getAttribute("redirectUri")
				+"</bold></div><br><br>"
				+"<form align=\"center\" action=\"processi2b2login\" method=\"post\">"
				+ " UserName:<br> <input type=\"text\" name=\"username\" value=\"demo\">"
				+ "<br> Password:<br><input type=\"text\" name=\"password\" value=\"demouser\">"
				
				+ "<br><br><input type=\"submit\" value=\"Submit\">"
				+ "</form>";
		return Response.ok().entity(loginPage).header("session_id", session.getId())
				.build();
	}

	// TODO domain and URL
	/*
	@Path("processi2b2login")
	@POST
	public Response processResourceOwnerLogin(
			@FormParam("username") String username,
			@FormParam("password") String password,
			@Context HttpServletRequest request) {
		try{
		logger.trace("processing login: for username:" + username
				+ "\npassword:" + password + "\nclientId:" + request.getSession().getAttribute("clientId"));
		HttpSession session = request.getSession();
		logger.trace("sessionid:" + session.getId());

		String pmResponseXml = I2b2Util.getPmResponseXml(username, password,
				 Config.i2b2Url,Config.i2b2Domain);
		logger.trace("got pmResponseXml:" + pmResponseXml);
		if (I2b2Util.authenticateUser(pmResponseXml)) {
			// logger.trace("got pmResponseXml:" + pmResponseXml);
			// String uri = getBasePath(request).toString() + "scope";
			// logger.trace("redirecting to:" + uri);
			// return Response.status(Status.MOVED_PERMANENTLY)
			// .location(new URI(uri)).build();
			session.setAttribute("msg","Authentication Successful!");
			
			session.setAttribute("resourceUserId", username);
			session.setAttribute("pmResponseXml", pmResponseXml);
			session.setAttribute("i2b2Token", I2b2Util.getToken(pmResponseXml));

			return Response.ok()
					.entity(srvResourceOwnerScopeChoice(pmResponseXml))
					.header("session_id", request.getSession().getId()).build();

		} else {
			session.setAttribute("msg","username or password was invalid");
			String uri = HttpHelper.getBasePath(request).toString() + "i2b2login";
			logger.trace("redirecting to:" + uri);
			return Response.status(Status.MOVED_PERMANENTLY)
					.location(new URI(uri))
					.header("session_id", request.getSession().getId()).build();
		}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			return Response.serverError().header("xreason", e.getMessage()).build();
		}
	}
	*/

	// @Path("scope")
	// @GET
	public String srvResourceOwnerScopeChoice(String pmResponseXml)
			throws XQueryUtilException {
		String page = 	"<div align=\"center\"><br><p style=\"color:green\"> Authentication Successful!</p><br>"
				+"<br>Choose one of the following projects for the SMART session: <br></div><br>"
				+"<form align=\"center\" action=\"processScope\" method=\"post\">";
		List<Project> projList = I2b2Util.getUserProjectMap(pmResponseXml);
		List<String> projects = new ArrayList<String>();
		for(Project p:projList){
				page += "<input type=\"radio\" name=\"project\" value=\"" + p.getId()
					+ "\" checked>" + p.getName() + "<br>";
		}
		page += "<br><input type=\"submit\" value=\"Submit\"></form>";

		return page;
	}

	// if submit yes then redirect to client url, along with AuthToken
	@Path("processScope")
	@POST
	public Response processResourceOwnerScopeChoice(
			@FormParam("project") String i2b2Project,
			@Context HttpServletRequest request) {
		try {
			logger.trace("processing scope:" + i2b2Project + " sessionid:"
					+ request.getSession().getId());
			// save scope to session and
			// redirect to client uri
			HttpSession session = request.getSession();
			session.setAttribute("permittedScopes", "user/*.*");

			String finalUri = (String) session.getAttribute("finalUri");

			String msg = "";
			Enumeration x = session.getAttributeNames();
			while (x.hasMoreElements()) {
				String p = (String) x.nextElement();
				msg = msg + p + "=" + session.getAttribute(p).toString() + "\n";
			}
			logger.trace("sessionAttributes:" + msg);
			// create AuthToken in Database;

			String pmResponseXml = (String) session
					.getAttribute("pmResponseXml");
			if (pmResponseXml == null)
				throw new RuntimeException("PMRESPONSE NOT FOUND");

			String resourceUserId = (String) session
					.getAttribute("resourceUserId");
			String i2b2Token = (String) I2b2Util.getToken(pmResponseXml);
			String authorizationCode = (String) session
					.getAttribute("authorizationCode");
			String clientRedirectUri = (String) session
					.getAttribute("redirectUri");
			String clientId = (String) session.getAttribute("clientId");
			String state = (String) session.getAttribute("state");
			String scope = "user/*.*";// HashSet<String>
										// session.getAttribute("scope");
			AuthToken authToken =authTokenBean.find(authorizationCode);
			if(authToken==null) authToken=authTokenBean.createAuthToken(authorizationCode, resourceUserId, i2b2Token, clientRedirectUri, clientId, state, scope, i2b2Project);
					 
			session.setAttribute("msg", "");
			return Response.status(Status.MOVED_PERMANENTLY)
					.location(new URI(finalUri))
					.header("session_id", session.getId()).build();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(e.getMessage()).build();
		}
	}

	// obtains an authorization decision (by asking the resource owner or by
	// establishing approval via other means).
	

	String successfulResponse(HttpServletRequest request)
			throws OAuthSystemException, URISyntaxException,
			OAuthProblemException {
		OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
		OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(
				new MD5Generator());

		String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

		OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
				.authorizationResponse(request, HttpServletResponse.SC_FOUND);

		String redirectURI = oauthRequest.getRedirectURI();

		if (responseType.equals(ResponseType.CODE.toString())) {
			String authorizationCode = oauthIssuerImpl.authorizationCode();

			logger.info("generated authorizationCode:" + authorizationCode);
			builder.setCode(authorizationCode);
			builder.setParam("state", oauthRequest.getState());

			HttpSession session = request.getSession();
			session.setAttribute("authorizationCode", authorizationCode);
			logger.info("put generated authcode "
					+ session.getAttribute("authorizationCode")
					+ " in session " + session.getId());

		}
		final OAuthResponse Oresponse = builder.location(redirectURI)
				.buildQueryMessage();
		URI url = new URI(Oresponse.getLocationUri());

		return url.toString();
	}
	
	
}
