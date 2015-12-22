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

package edu.harvard.i2b2.fhir.oauth2.ws;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.server.ConfigParameter;
import edu.harvard.i2b2.fhir.server.ServerConfigs;
import edu.harvard.i2b2.oauth2.core.ejb.AccessTokenService;
import edu.harvard.i2b2.oauth2.core.ejb.AuthTokenService;
import edu.harvard.i2b2.oauth2.core.entity.AccessToken;
import edu.harvard.i2b2.oauth2.core.entity.AuthToken;
import edu.harvard.i2b2.oauth2.register.ejb.ClientManager;
import edu.harvard.i2b2.oauth2.register.ejb.ClientService;
import edu.harvard.i2b2.oauth2.register.entity.Client;

@Path("token")
public class OAuth2TokenEndpoint {
	Logger logger = LoggerFactory.getLogger(OAuth2TokenEndpoint.class);
	private Client client;
	
	@EJB
	AuthTokenService authTokenBean;
	@EJB
	AccessTokenService accessTokenBean;
	
	@EJB
	ClientService clientService;
	
	@Inject
	ServerConfigs serverConfig;
	

	/*
	 * currently only checking for auth code and client id as stored in authcode
	 * during authorization client secret is ignored
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response authorize(@Context HttpServletRequest request)
			throws OAuthSystemException, IOException, URISyntaxException {

		try {
			logger.info("got url:" + request.getRequestURL());
			
			Enumeration<String> kl = request.getParameterNames();
			 while(kl.hasMoreElements()){
				 String k=kl.nextElement();
				 logger.debug(k+"->"+request.getParameter(k));
			 }
			
			OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(
					new MD5Generator());

			// find if AuthToken was issued, from db
			String authCode = oauthRequest.getCode();
			AuthToken authToken = authTokenBean.find(authCode);
			if (authToken == null){
				logger.debug("authToken is not found");
				
				return buildBadAuthCodeResponse();
			}
			logger.debug("authToken is "+authToken.toString());
			
			// check if clientid is valid
			if (!checkClientId(oauthRequest.getClientId(),authToken)) {
				return buildInvalidClientIdResponse();
			}

			// check if client_secret is valid
			if (!checkClientSecret(oauthRequest.getClientSecret())) {
				return buildInvalidClientSecretResponse();
			}

			// do checking for different grant types
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.AUTHORIZATION_CODE.toString())) {
				if (!checkAuthCode(authToken,
						oauthRequest.getParam(OAuth.OAUTH_CODE))) {
					return buildBadAuthCodeResponse();
				}
			} else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.PASSWORD.toString())) {
				if (!checkUserPass(oauthRequest.getUsername(),
						oauthRequest.getPassword(),authToken)) {
					return buildInvalidUserPassResponse();
				}
			} else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.REFRESH_TOKEN.toString())) {
				// refresh token is not supported in this implementation
				// buildInvalidUserPassResponse();
				//buildAccessTokenNotSupportedResponse();
			}
			
			logger.trace("authToken:"+authToken);
			String state=authToken.getState();
			String patientId=authToken.getPatient();

			AccessToken accessToken=accessTokenBean.createAccessTokenAndDeleteAuthToken(authToken);
			
			URI fhirBase = HttpHelper.getBasePath(request,serverConfig);
			OAuthResponse response = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessToken.getTokenString()).setExpiresIn("3600")
					.setScope(accessToken.getScope())
					.setParam("token_type", "Bearer")
					.setParam("state", state)
					.setParam("patient", patientId)//"1000000005")//authToken.getPatient())
					.setParam("need_patient_banner", "true")
					.setParam("smart_style_url", fhirBase.toString()+"smartstyleuri")
					.buildJSONMessage();
			logger.trace("pat:"+authToken.getPatient());
			logger.info("returning res:" + response.getBody());

			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();

		} catch (OAuthProblemException e) {
			logger.error(e.getMessage(), e);
			OAuthResponse res = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
					.buildJSONMessage();
			return Response.status(res.getResponseStatus())
					.entity(res.getBody()).build();
		}
	}

	private Response buildInvalidUserPassResponse() {
		// TODO Auto-generated method stub
		return Response.status(Status.BAD_REQUEST)
				.entity("Failure:Invalid User").build();
	}

	private Response buildInvalidClientSecretResponse() {
		// TODO Auto-generated method stub
		return Response.ok().entity("invalid client").build();
	}

	private Response buildBadAuthCodeResponse() {
		// TODO Auto-generated method stub
		return Response
				.status(Status.BAD_REQUEST).entity("{'x-reason':'bad auth code'}").header("x-reason","bad auth code")
				.build();
	}

	private Response buildInvalidClientIdResponse() {
		return Response.ok().entity("invalid client").build();
	}

	private boolean checkAuthCode(AuthToken t, String inputAuthCode) {
		return t.getAuthorizationCode().equals(inputAuthCode) ? true : false;
	}

	private boolean checkUserPass(String username, String password,
			AuthToken authTok) {
		return (authTok.getResourceUserId().equals(username) && authTok
				.getI2b2Token().equals(password)) ? true : false;

	}

	private boolean checkClientSecret(String clientSecret) {
		//for open open client
		if(serverConfig.GetString(ConfigParameter.openClientId)!=null && serverConfig.GetString(ConfigParameter.openClientId).equals(client.getClientId())) return true;
		
		boolean res= client.getClientSecret().equals(clientSecret)?true:false;
		if (res==false) logger.warn("client secret is invalid");
		return res;
	}

	private boolean checkClientId(String clientId, AuthToken authToken) {
		if(!authToken.getClientId().equals(clientId)) {
			logger.warn("clientId in request is different from that for which the auth code was issued:");
			return false;
		}
		
		client=clientService.find(clientId);
		if (client==null) logger.warn("client not found for id:"+clientId);
		
		
		return client!=null?true:false;
	}

}