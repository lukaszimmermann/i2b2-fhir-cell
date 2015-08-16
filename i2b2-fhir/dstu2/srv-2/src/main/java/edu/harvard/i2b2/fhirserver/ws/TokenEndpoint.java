package edu.harvard.i2b2.fhirserver.ws;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("aaaatoken")
public class TokenEndpoint {
	Logger logger = LoggerFactory.getLogger(TokenEndpoint.class);
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, OAuthSystemException {
			 
			OAuthTokenRequest oauthRequest = null;
			 
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
			 
			    try {
			           oauthRequest = new OAuthTokenRequest(request);
			        
			           //validateClient(oauthRequest);
			 
			           String authzCode = oauthRequest.getCode();
			 
			           // some code
			           String accessToken = oauthIssuerImpl.accessToken();
			           String refreshToken = oauthIssuerImpl.refreshToken();
			 
			           // some code
			            OAuthResponse r = OAuthASResponse
			                .tokenResponse(HttpServletResponse.SC_OK)
			                .setAccessToken(accessToken)
			                .setExpiresIn("3600")
			                .setRefreshToken(refreshToken)
			                .buildJSONMessage();
			 
			        response.setStatus(r.getResponseStatus());
			        PrintWriter pw = response.getWriter();
			        pw.print(r.getBody());
			        logger.info("body:"+r.getBody());
			        pw.flush();
			        pw.close();
			         //if something goes wrong
			    } catch(OAuthProblemException ex) {
			 
			    	logger.error(ex.getMessage(),ex);
			        OAuthResponse r = OAuthResponse
			            .errorResponse(401)
			            .error(ex)
			            .buildJSONMessage();
			 
			        response.setStatus(r.getResponseStatus());
			 
			        PrintWriter pw = response.getWriter();
			        pw.print(r.getBody());
			        pw.flush();
			        pw.close();
			 
			        response.sendError(401);
			    }
			 
			}
}
