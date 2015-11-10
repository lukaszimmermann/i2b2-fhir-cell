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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
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
@Priority(2)
// Priorities.HEADER_DECORATOR)
public class WebsiteAuthenticationFilter implements ContainerRequestFilter {
	static Logger logger = LoggerFactory
			.getLogger(WebsiteAuthenticationFilter.class);

	@Context
	private HttpServletRequest servletRequest;

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		logger.trace("website filtering:" + context.getUriInfo().getPath());
		// check urls for authentication
		if (context.getUriInfo().getPath().toString().startsWith("/i2b2")
				|| context.getUriInfo().getPath().toString()
						.startsWith("/client")
				|| context.getUriInfo().getPath().toString()
						.startsWith("/user")) {
			// CHECK if user is present in session
			// IF not redirect to login page
			HttpSession session = servletRequest.getSession();
			if (session.getAttribute("authenticatedUser") == null) {
				context.abortWith(loginRedirect());
			}

		}
	}

	Response loginRedirect() {
		Response r = Response.status(Status.UNAUTHORIZED).entity("Login first")
				.type(MediaType.TEXT_PLAIN).build();
		return r;
	}
}
