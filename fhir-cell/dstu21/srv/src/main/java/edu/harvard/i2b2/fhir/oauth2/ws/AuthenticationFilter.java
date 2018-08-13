/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

package edu.harvard.i2b2.fhir.oauth2.ws;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.server.ConfigParameter;
import edu.harvard.i2b2.fhir.server.ServerConfigs;
import edu.harvard.i2b2.oauth2.core.ejb.AuthenticationService;
import edu.harvard.i2b2.oauth2.register.ejb.ConfigDbService;


@Provider
@PreMatching
@Priority( 1)//Priorities.HEADER_DECORATOR)

public class AuthenticationFilter implements ContainerRequestFilter {
	static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
	// public class RestAuthenticationFilter implements javax.servlet.Filter {
	public static final String AUTHENTICATION_HEADER = "Authorization";

	@EJB
	AuthenticationService authService;
	
	@EJB
	ServerConfigs serverConfig;
	
	

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		String authHeaderContent = context
				.getHeaderString(AUTHENTICATION_HEADER);
		logger.trace("Authorization header:" + authHeaderContent + "\n for"
				+ context.getUriInfo().getPath().toString());

		
		if (serverConfig.GetString(ConfigParameter.openAccessToken) != null & serverConfig.isOpenAccess()
				& context.getUriInfo().getPath().toString().startsWith("/open")) {
			logger.debug("for open connection adding demo access token to header");
			context.getHeaders().add(
					AuthenticationFilter.AUTHENTICATION_HEADER,
					"Bearer " + serverConfig.GetString(ConfigParameter.openAccessToken));
			return;

		}
		
		if (context.getMethod().equals("OPTIONS")) return;
		
		// skip urls for authentication
		if (context.getUriInfo().getPath().toString().startsWith("/authz")
				|| context.getUriInfo().getPath().toString()
						.startsWith("/token")
				|| context.getUriInfo().getPath().toString()
						.startsWith("/meta")
						|| context.getUriInfo().getPath().toString()
						.startsWith("/i2b2")
						|| context.getUriInfo().getPath().toString()
						.startsWith("/client")
						|| context.getUriInfo().getPath().toString()
						.startsWith("/user")
						|| context.getUriInfo().getPath().toString()
						.startsWith("/smartstyleuri")
						
						
		// bypass Authentication
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
		Response r = Response.status(Status.UNAUTHORIZED)
				.entity("Authentication Failure")
				.type(MediaType.TEXT_PLAIN)
				.build();
		return r;
	}
}
