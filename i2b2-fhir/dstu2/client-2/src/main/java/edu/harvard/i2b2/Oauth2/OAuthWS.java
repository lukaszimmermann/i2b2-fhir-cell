package edu.harvard.i2b2.Oauth2;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("")
public class OAuthWS {
	Logger logger = LoggerFactory.getLogger(OAuthWS.class);

	/*
	 * /authorize?response_type=code&client_id=s6BhdRkqt3&state=xyz
	 * &redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb
	 * 
	 * https://client.example.com/cb?code=SplxlOBeZQQYbYS6WxSbIA &state=xyz
	 * http://localhost:8080/client-dstu2-0.2/oauth2/getAuthCode?code=
	 * SplxlOBeZQQYbYS6WxSbIA&state=xyz
	 */
	// http://localhost:8080/client-dstu2-0.2/oauth2/launch

	@GET
	@Path("getAuthCode")
	public Response getAuthorizationCode(@Context HttpServletRequest request) {
		OAuthAuthzResponse oar;
		try {
			oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
			String code = oar.getCode();
			logger.info("gotAuthCode:" + code);

			String accessToken = exchangeOAuthCodeForAccessToken(code);

			return Response.ok().entity("Access Token:" + accessToken)
					.type(MediaType.TEXT_PLAIN).build();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
			logger.error("", e);
			return errorResponse(e);

		}

	}

	public String exchangeOAuthCodeForAccessToken(String code) {
		try {
			OAuthClientRequest request = OAuthClientRequest
					.tokenLocation(
							"http://localhost:8080/srv-dstu2-0.2/api/token")
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setClientId("client-id")
					.setClientSecret("client-secret")
					.setCode(code)
					.setRedirectURI("uri")
					.buildQueryMessage();

			// create OAuth client that uses custom http client under the hood
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

			GitHubTokenResponse oAuthResponse;
			logger.trace("get exchange authToken for access code at:"+request.getLocationUri());
			
			oAuthResponse = oAuthClient.accessToken(request,
					GitHubTokenResponse.class);
			String accessToken = oAuthResponse.getAccessToken();
			Long expiresIn = oAuthResponse.getExpiresIn();
			logger.info("got Token:"+accessToken +" expires in "+expiresIn);
			
			return accessToken;
		} catch (OAuthSystemException | OAuthProblemException e) {

			e.printStackTrace();
			logger.error("", e);
		}
		return "error";

	}

	@GET
	@Path("launch2")
	public Response getAuthorizationUri() throws OAuthSystemException,
			URISyntaxException {
		OAuthClientRequest request = OAuthClientRequest
				.authorizationLocation(
						"http://localhost:8080/srv-dstu2-0.2/api/authz/authorize")
				.setClientId("my-client-id1")
				.setResponseType("code")
				.setScope("user/*.*")
				.setRedirectURI(
						"http://localhost:8080/client-dstu2-0.2/oauth2/getAuthCode")
				.buildQueryMessage();
		logger.info("seeking authorizationUri" + request.getLocationUri());
		URI url = new URI(request.getLocationUri());
		return Response.status(Status.MOVED_PERMANENTLY).location(url).build();

	}

	/*
	 * @GET
	 * 
	 * @Path("login") public Response getloginPage() {
	 * 
	 * return Response.ok().entity("login") .build();
	 * 
	 * }
	 */

	private Response errorResponse(Exception e) {
		return Response.status(Status.BAD_REQUEST)
				.header("xreason", e.getMessage()).build();
	}
}
