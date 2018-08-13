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
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.oauth2.core.ejb.I2b2AuthenticationManager;

//@Provider
//@PreMatching
//@Priority(1)//Priorities.HEADER_DECORATOR)
public class OAuthNavigationFilter implements ContainerRequestFilter {
	static Logger logger = LoggerFactory.getLogger(OAuthNavigationFilter.class);
	// public class RestAuthenticationFilter implements javax.servlet.Filter {
	public static final String AUTHENTICATION_HEADER = "Authorization";

	@EJB
	I2b2AuthenticationManager authManager;

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		UriInfo uriInfo=context.getUriInfo();
		String pathStr=uriInfo.getAbsolutePath().toString();
		logger.info("got uri::"+pathStr);
		/*
		pathStr=pathStr.replace("open/", "");
		URI uri=null;
		try {
			uri = new URI(pathStr);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.equals(e);
			e.printStackTrace();
			
		}
		logger.info("rewriting open url to:"+uri.toString());
		*/
		pathStr=authManager.navigate();
		URI uri=null;
		try {
			uri = new URI(pathStr);
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(),e);
		}
		logger.trace("navigating to:"+uri.toString());
		context.setRequestUri(uri);
	}

	
}
