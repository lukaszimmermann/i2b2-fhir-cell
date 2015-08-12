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

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("")
public class OAuthWS {
	Logger logger = LoggerFactory.getLogger(OAuthWS.class);

	/*/authorize?response_type=code&client_id=s6BhdRkqt3&state=xyz
	        &redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb
	        
	        https://client.example.com/cb?code=SplxlOBeZQQYbYS6WxSbIA
               &state=xyz
            http://localhost:8080/client-dstu2-0.2/oauth2/getAuthCode?code=SplxlOBeZQQYbYS6WxSbIA&state=xyz 
               
	        */
	@GET
    @Path("getAuthCode")
	public Response getAuthorizationCode(@Context HttpServletRequest request) {
		OAuthAuthzResponse oar;
		try {
			oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
			String code = oar.getCode();
			logger.info("gotAuthCode:"+code);
			return Response.ok().entity("Auth code:"+code)
	                .type(MediaType.TEXT_PLAIN)
	                .build();
		} catch (OAuthProblemException e) {
			e.printStackTrace();
			logger.error("", e);
		    return errorResponse(e);

		}
	
    
	}
	
	

	@GET
    @Path("getAuthUri")
	public Response getAuthorizationUri() throws OAuthSystemException, URISyntaxException {
		OAuthClientRequest request = OAuthClientRequest
				.authorizationLocation(
						"http://localhost:8080/srv-dstu2-0.2/api/authz")
				.setClientId("my-client-id1")
				.setRedirectURI(
						"http://localhost:8080/client-dstu2-0.2/oauth2/getAuthCode")
				.buildQueryMessage();
		logger.info(request.getLocationUri());
		URI url = new URI(request.getLocationUri());
		return Response.status(Status.MOVED_PERMANENTLY).location(url)
				 .build();
		
	}
	
	/*@GET
    @Path("login")
	public Response getloginPage() {
		
		return Response.ok().entity("login")
				 .build();
		
	}*/
	
	
	private Response errorResponse(Exception e) {
		return Response.status(Status.BAD_REQUEST)
        .header("xreason", e.getMessage()).build();
	}
}
