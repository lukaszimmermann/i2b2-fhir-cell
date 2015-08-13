package edu.harvard.i2b2.fhir;

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
	public void exchangeOAuthCodeForAccessToken() {
		String code="123";
		try {
			OAuthClientRequest request = OAuthClientRequest
					.tokenLocation(
							"http://localhost:8080/srv-dstu2-0.2/api/token")
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setClientId("client-id")
					.setClientSecret("client-secret")
					.setCode(code)
					.setRedirectURI("uri")
					.setParameter("access_token", "accessToken")
					.buildQueryMessage();
			
			// create OAuth client that uses custom http client under the hood
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

			OAuthJSONAccessTokenResponse oAuthResponse;
			logger.trace("To get exchange authToken for access code at:"+request.getLocationUri());
			
			oAuthResponse = oAuthClient.accessToken(request, "POST");
			String accessToken = oAuthResponse.getAccessToken();
			Long expiresIn = oAuthResponse.getExpiresIn();
			logger.info("got Token:"+accessToken +" expires in "+expiresIn);
			
		
		} catch (OAuthSystemException | OAuthProblemException e) {

			e.printStackTrace();
			logger.error("", e);
		}
		

	}
	
}
