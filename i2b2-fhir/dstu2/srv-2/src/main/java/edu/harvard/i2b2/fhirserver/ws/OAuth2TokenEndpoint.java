package edu.harvard.i2b2.fhirserver.ws;

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

@Path("token")
public class OAuth2TokenEndpoint {
	Logger logger = LoggerFactory.getLogger(OAuth2TokenEndpoint.class);

	@EJB
	AccessTokenBean accessTokenBean;

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json")
	public Response authorize(@Context HttpServletRequest request)
			throws OAuthSystemException {

		try {
			logger.info("got url:" + request.getRequestURL());

			OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(
					new MD5Generator());

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
				if (!checkAuthCode(oauthRequest.getParam(OAuth.OAUTH_CODE))) {
					return buildBadAuthCodeResponse();
				}
			} else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.PASSWORD.toString())) {
				if (!checkUserPass(oauthRequest.getUsername(),
						oauthRequest.getPassword())) {
					return buildInvalidUserPassResponse();
				}
			} else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.REFRESH_TOKEN.toString())) {
				// refresh token is not supported in this implementation
				// buildInvalidUserPassResponse();
			}

			final String accessToken = oauthIssuerImpl.accessToken();
			// database.addToken(accessToken);
			HttpSession session = request.getSession();
			accessTokenBean.createAccessToken(
					(String)session.getAttribute("resourceUserId"),
					(String)session.getAttribute("i2b2Token"),
					(String)session.getAttribute("authorizationCode"),
					(String)session.getAttribute("clientRedirectUri"),
					(String)session.getAttribute("clientId")
					);

			OAuthResponse response = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessToken).setExpiresIn("3600")
					.buildJSONMessage();
			logger.info("returning res:" + response.getBody());

			return Response.status(response.getResponseStatus())
					.entity(response.getBody()).build();

		} catch (OAuthProblemException e) {
			logger.error("", e);
			OAuthResponse res = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
					.buildJSONMessage();
			return Response.status(res.getResponseStatus())
					.entity(res.getBody()).build();
		}
	}

	private Response buildInvalidUserPassResponse() {
		// TODO Auto-generated method stub
		return Response.ok().entity("not supported").build();
	}

	private boolean checkUserPass(String username, String password) {
		// TODO Auto-generated method stub
		return true;
	}

	private Response buildBadAuthCodeResponse() {
		// TODO Auto-generated method stub
		return Response.ok().entity("bad auth code").build();
	}

	private boolean checkAuthCode(String param) {
		// TODO Auto-generated method stub
		return true;
	}

	private Response buildInvalidClientSecretResponse() {
		// TODO Auto-generated method stub
		return Response.ok().entity("invalid client").build();
	}

	private boolean checkClientSecret(String clientSecret) {
		// TODO Auto-generated method stub
		return true;
	}

	private Response buildInvalidClientIdResponse() {
		// TODO Auto-generated method stub
		return Response.ok().entity("invalid client").build();
	}

	private boolean checkClientId(String clientId) {
		// TODO Auto-generated method stub
		return true;
	}

	// ...
}