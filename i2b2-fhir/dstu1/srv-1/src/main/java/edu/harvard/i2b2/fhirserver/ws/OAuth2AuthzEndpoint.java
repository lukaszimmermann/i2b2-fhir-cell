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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
	
    @GET
    public Response authorize(@Context HttpServletRequest request)
            throws URISyntaxException, OAuthSystemException {
        try {
        	logger.trace("got request to authorize for OAuth2");
            OAuthAuthzRequest oauthRequest =
                new OAuthAuthzRequest(request);
            OAuthIssuerImpl oauthIssuerImpl =
                new OAuthIssuerImpl(new MD5Generator());

            //build response according to response_type
            String responseType =
                oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);

            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(request,
                        HttpServletResponse.SC_FOUND);

            request.getAttribute("client_id");
            
            // 1
            if (responseType.equals(ResponseType.CODE.toString())) {
                final String authorizationCode =
                    oauthIssuerImpl.authorizationCode();
                //database.addAuthCode(authorizationCode);
                logger.info(" issuing authorizationCode:"+authorizationCode);
                builder.setCode(authorizationCode);
            }

            String redirectURI =
                oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            final OAuthResponse response = builder
                .location(redirectURI)
                .buildQueryMessage();
            URI url = new URI(response.getLocationUri());
            return Response.status(response.getResponseStatus())
                .location(url)
                .build();
        } catch (OAuthProblemException e) {
        	logger.error(e.getMessage());
        	return Response.status(Status.BAD_REQUEST)
					.header("xreason", e.getMessage()).build();
        }
    }
    
    private HttpSession getSessionWithAuthorizationCode(HttpServletRequest request,String authorizationCode){
    	HttpSession session = request.getSession(false);
    	session.setAttribute("AuthorizationCode", authorizationCode);
    	return session;
    }
}
