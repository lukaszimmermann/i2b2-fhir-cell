package edu.harvard.i2b2.fhirserver.ws;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.harvard.i2b2.fhirserver.ejb.AuthenticationService;

@Provider
@PreMatching
public class AuthenticationFilter implements ContainerRequestFilter {
	static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
	// public class RestAuthenticationFilter implements javax.servlet.Filter {
	public static final String AUTHENTICATION_HEADER = "Authorization";
	public static final String HARD_CODED_DAFEFAULT_TOKEN = "1f4ffead29414d1977fba44e2bf4d8b7";

	@EJB
	AuthenticationService authService;

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		// public void doFilter(ServletRequest request, ServletResponse
		// response,
		// FilterChain filter) throws IOException, ServletException {
		String accessToken = "-";
		String authCredentials = context.getHeaderString(AUTHENTICATION_HEADER);
		logger.trace("Authorization header:" + authCredentials + "\n for"
				+ context.getUriInfo().getPath().toString());
		// skip urls for authentication
		if (context.getUriInfo().getPath().toString().startsWith("/authz")
				|| context.getUriInfo().getPath().toString()
						.startsWith("/token")
				|| context.getUriInfo().getPath().toString()
						.startsWith("/open")	  //bypass Authentication	
				)
			return;

		if (authCredentials != null) {
			accessToken = authCredentials.replaceAll("Bearer\\s*", "");

		
			logger.info("searching for AccessToken:" + accessToken);
			if (accessToken.equals(HARD_CODED_DAFEFAULT_TOKEN)) {
				return;
			} else {
				boolean authenticationStatus = authService
						.authenticate(accessToken);

				if (authenticationStatus) {
					logger.trace("authentication Successful");
					return;
				} else {
					context.abortWith(denyRequest());
				}
			}
		}
		context.abortWith(denyRequest());
	}

	Response denyRequest() {
		Response r = Response.ok().entity("Authentication Failure")
		// .cookie(authIdCookie)
				.type(MediaType.TEXT_PLAIN)
				// .header("session_id", session.getId())
				.build();
		return r;
	}
}
