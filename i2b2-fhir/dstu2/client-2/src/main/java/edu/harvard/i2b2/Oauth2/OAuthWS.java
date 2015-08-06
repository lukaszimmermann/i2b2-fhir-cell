package edu.harvard.i2b2.Oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("getAuthCode")
public class OAuthWS {
	Logger logger = LoggerFactory.getLogger(OAuthWS.class);

	public void getAuthorizationCode(@Context HttpServletRequest request) {
		OAuthAuthzResponse oar;
		try {
			oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
			String code = oar.getCode();
			logger.info("gotAuthCode:"+code);
		} catch (OAuthProblemException e) {
			e.printStackTrace();
			logger.error("", e);
		}

	}
}
