package edu.harvard.i2b2.fhirserver.ws;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import javax.xml.bind.JAXBException;

import org.apache.http.HttpRequest;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.WebServiceCall;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhirserver.ejb.AuthTokenBean;

@Provider
@PreMatching
public class AuthenticationFilter implements ContainerRequestFilter {
	static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
	// public class RestAuthenticationFilter implements javax.servlet.Filter {
	public static final String AUTHENTICATION_HEADER = "Authorization";

	
	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		// public void doFilter(ServletRequest request, ServletResponse
		// response,
		// FilterChain filter) throws IOException, ServletException {
		String authCredentials = context.getHeaderString(AUTHENTICATION_HEADER);
		logger.trace("Authorization header:"+authCredentials+"\n for"+context.getUriInfo());
		
		
		// better injected
		AuthenticationService authenticationService = new AuthenticationService();

		boolean authenticationStatus = authenticationService
				.authenticate(authCredentials);
		
		if (authenticationStatus) {
			logger.trace("authenticaltion Successful");
		} else {
			// proceedcontext.:redirect to auth info page
			Response r = Response.ok().entity("Authentication Failure")
			// .cookie(authIdCookie)
					.type(MediaType.TEXT_PLAIN)
					// .header("session_id", session.getId())
					.build();
			context.abortWith(r);

		}

	}

}
