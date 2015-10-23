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

package edu.harvard.i2b2.fhirserver.ws;

import java.io.IOException;
import java.util.Enumeration;

import javax.ejb.EJB;
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

import edu.harvard.i2b2.fhirserver.ejb.AccessTokenBean;
import edu.harvard.i2b2.fhirserver.ejb.AuthTokenBean;
import edu.harvard.i2b2.fhirserver.entity.AuthToken;

@Path("token")
public class OAuth2TokenEndpoint {
	Logger logger = LoggerFactory.getLogger(OAuth2TokenEndpoint.class);

	@EJB
	AuthTokenBean authTokenBean;
	@EJB
	AccessTokenBean accessTokenBean;

	/*
	 * currently only checking for auth code and client id as stored in authcode
	 * during authorization client secret is ignored
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response authorize(@Context HttpServletRequest request)
			throws OAuthSystemException, IOException {

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
			if (!checkClientId(oauthRequest.getClientId())) {
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

			final String accessTokenString = oauthIssuerImpl.accessToken();
			// database.addToken(accessToken);
			String resourceUserId=authToken.getResourceUserId();
			String i2b2Token=authToken.getI2b2Token();
			String i2b2Project=authToken.getI2b2Project();
			String clientId=authToken.getClientId();
			String scope=authToken.getScope();
			
			
			accessTokenBean.createAccessTokenAndDeleteAuthToken(authCode,accessTokenString, resourceUserId, i2b2Token, i2b2Project, clientId, scope);
			
			
			OAuthResponse response = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessTokenString).setExpiresIn("3600")
					.buildJSONMessage();
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
		// TODO Auto-generated method stub
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
		// TODO XXX
		return true;
	}

	private boolean checkClientId(String clientId) {
		// TODO XXX
		return true;
	}

	// ...
}