package edu.harvard.i2b2.fhirserver.ws;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
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
	
	@EJB
	AuthenticationService authService;

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		// public void doFilter(ServletRequest request, ServletResponse
		// response,
		// FilterChain filter) throws IOException, ServletException {
		String authHeaderContent = context.getHeaderString(AUTHENTICATION_HEADER);
		logger.trace("Authorization header:" + authHeaderContent + "\n for"
				+ context.getUriInfo().getPath().toString());
		// skip urls for authentication
		if (context.getUriInfo().getPath().toString().startsWith("/authz")
				|| context.getUriInfo().getPath().toString()
						.startsWith("/token")
				|| context.getUriInfo().getPath().toString()
						.startsWith("/open")	  //bypass Authentication	
				)
			return;

		boolean authenticationStatus = authService
				.authenticate(authHeaderContent);
		if (authenticationStatus) {
			logger.trace("authentication Successful");
			return;
		} else {
			context.abortWith(denyRequest());
		}
		
		
		context.abortWith(denyRequest());
	}

	Response denyRequest() {
		Response r = Response.status(Status.UNAUTHORIZED).entity("Authentication Failure")
		// .cookie(authIdCookie)
				.type(MediaType.TEXT_PLAIN)
				// .header("session_id", session.getId())
				.build();
		return r;
	}
}
