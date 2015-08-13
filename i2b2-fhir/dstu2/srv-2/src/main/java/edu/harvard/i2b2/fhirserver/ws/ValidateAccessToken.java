package edu.harvard.i2b2.fhirserver.ws;

import javax.ws.rs.Path;


@Path("/resource")
public class ValidateAccessToken{
    /* @GET
    @Produces("text/html")
    public Response get(@Context HttpServletRequest request)
        throws OAuthSystemException {
        try {
            // Make the OAuth Request out of this request
            OAuthAccessResourceRequest oauthRequest =
                new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
            // Get the access token
            String accessToken = oauthRequest.getAccessToken();

            // Validate the access token
            if (!database.isValidToken(accessToken)) {
                // Return the OAuth error message
                OAuthResponse oauthResponse = OAuthRSResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm(Common.RESOURCE_SERVER_NAME)
                        .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                        .buildHeaderMessage();

                //return Response.status(Response.Status.UNAUTHORIZED).build();
                return Response.status(Response.Status.UNAUTHORIZED)
                        .header(OAuth.HeaderType.WWW_AUTHENTICATE,
                        oauthResponse
                            .getHeader(OAuth.HeaderType.WWW_AUTHENTICATE))
                        .build();

            }
            // [1]
            return Response.status(Response.Status.OK)
                .entity(accessToken).build();
        } catch (OAuthProblemException e) {
            // Check if the error code has been set
            // Build error response....
        }
    }*/
}