package edu.harvard.i2b2.Oauth2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;


@Path("")
public class OAuthWS {
	Logger logger = LoggerFactory.getLogger(OAuthWS.class);
	static String basePath;

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
		basePath = request
				.getRequestURL()
				.toString()
				.replaceAll(
						OAuthWS.class.getAnnotation(Path.class).value()
								.toString()
								+ "$", "").split("client")[0];

		try {
			oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
			String code = oar.getCode();
			String redirectUrl = oar.getParam("redirect_uri");
			logger.info("gotAuthCode:" + code);
			// return Response.ok().entity("AuthCode:" + code).build();
			String accessToken = exchangeOAuthCodeForAccessToken(code,
					"fcclient1", "csecret1", redirectUrl);

			String msg = accessResource(accessToken);
			return Response.ok()// .entity("Access Token:" + accessToken)
					.entity(msg).type(MediaType.TEXT_PLAIN).build();
		} catch (OAuthProblemException e) {
			logger.error(e.getMessage(), e);
			return errorResponse(e);

		}

	}

	public String exchangeOAuthCodeForAccessToken(String authCode,
			String clientId, String clientSecret, String redirectUrl) {
		try {
			logger.info(authCode + " " + clientId + " " + clientSecret + " "
					+ redirectUrl);
			OAuthClientRequest request = OAuthClientRequest
					.tokenLocation(
							basePath+"srv-dstu2-0.2/api/token")
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setClientId("clientId").setClientSecret("clientSecret")
					.setCode(authCode).setRedirectURI("redirectUrl")
					.setParameter("access_token", "accessToken")
					.buildQueryMessage();
			// create OAuth client that uses custom http client under the hood
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

			OAuthJSONAccessTokenResponse oAuthResponse;
			logger.trace("To get exchange authToken for access code at:"
					+ request.getLocationUri());

			oAuthResponse = oAuthClient.accessToken(request, "POST");
			String accessToken = oAuthResponse.getAccessToken();
			Long expiresIn = oAuthResponse.getExpiresIn();
			logger.info("got Token:" + accessToken + " expires in " + expiresIn);
			return accessToken;

		} catch (OAuthSystemException | OAuthProblemException e) {

			e.printStackTrace();
			logger.error("", e);
		}
		return "ERROR";

	}
	
	public String accessResource(String accessToken) {
		Client c = Client.create();
		WebResource r = c.resource(basePath+"srv-dstu2-0.2/api/Patient")
				;
	    String response = //r.accept("Context-Type", "application/xml")
	            r.header("Authorization", "Bearer " +accessToken)
	    		.get(String.class);
	   // System.out.println(response);
	    
		logger.info("get response:" + response);
		return response;
	}
	@GET
	@Path("launch")
	public Response getAuthorizationUri() throws OAuthSystemException,
			URISyntaxException {
		OAuthClientRequest request = OAuthClientRequest
				.authorizationLocation(
						"http://localhost:8080/srv-dstu2-0.2/api/authz/authorize")
				.setClientId("fcclient1")
				.setResponseType("code")
				.setScope("user/*.*")
				.setRedirectURI(
						"http://localhost:8080/client-dstu2-0.2/oauth2/getAuthCode")
				.buildQueryMessage();
		logger.info("seeking authorizationUri" + request.getLocationUri());
		URI url = new URI(request.getLocationUri());
		return Response.status(Status.MOVED_PERMANENTLY).location(url).build();

	}

	@GET
	@Path("test")
	public Response tesss() {

		logger.info("resr log");

		return Response.ok().entity("ok").build();

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
