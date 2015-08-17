package edu.harvard.i2b2.fhir;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class OAuthClientTest {
	
	Logger logger = LoggerFactory.getLogger(OAuthClientTest.class);

	@Test
	public void BuildEndUserAuthorizationRequest() throws OAuthSystemException {

		
		OAuthClientRequest request = OAuthClientRequest
				.authorizationLocation(
						"http://localhost:8080/srv-dstu2-0.2/api/authz")
				.setClientId("my-client-id")
				.setResponseType("code")
				.setScope("launch:1000000005+Patient/*")
				.setRedirectURI(
						"http://localhost:8080/client-dstu2-0.2/oauth2/getAuthCode")
				.buildQueryMessage();
		logger.trace(request.getLocationUri());
	}
	@Test
	public void  accessResource() {
			Client c = Client.create();
			WebResource r = c.resource("http://localhost:8080/srv-dstu2-0.2/api/Patient")
					;
		    String response = //r.accept("Context-Type", "application/xml")
		            //header(“X-FOO”, “BAR”).
		    		r.get(String.class);
		   // System.out.println(response);
		    
			logger.info("get response:" + response);
			
		
	}

	//@Test
	public String exchangeOAuthCodeForAccessToken(String authCode) {
		String accessToken="-";
		try {
			OAuthClientRequest request = OAuthClientRequest
					.tokenLocation(
							"http://localhost:8080/srv-dstu2-0.2/api/token")
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setClientId("client-id")
					.setClientSecret("client-secret")
					.setCode(authCode)
					.setRedirectURI("uri")
					.setParameter("access_token", "accessToken")
					.buildQueryMessage();
			
			// create OAuth client that uses custom http client under the hood
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

			OAuthJSONAccessTokenResponse oAuthResponse;
			logger.trace("To get exchange authToken for access code at:"+request.getLocationUri());
			
			oAuthResponse = oAuthClient.accessToken(request, "POST");
			accessToken = oAuthResponse.getAccessToken();
			Long expiresIn = oAuthResponse.getExpiresIn();
			logger.info("got Token:"+accessToken +" expires in "+expiresIn);
			return accessToken;
		
		} catch (OAuthSystemException | OAuthProblemException e) {

			e.printStackTrace();
			logger.error("", e);
		}
		return accessToken;
		
		
	}
	
	
	
}
