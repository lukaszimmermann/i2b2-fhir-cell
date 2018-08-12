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

package edu.harvard.i2b2.fhir.server.ws;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;

@Provider
@PreMatching
@Priority( 3)
public class SearchUrlFilter implements ContainerRequestFilter {
	static Logger logger = LoggerFactory.getLogger(SearchUrlFilter .class);
	
 		@Override
	public void filter(ContainerRequestContext context) throws IOException {
 			
 			UriInfo uriInfo=context.getUriInfo();
		String pathStr=uriInfo.getAbsolutePath().toString();
		logger.info("got uri::"+pathStr);
		//pathStr=pathStr.replace("/_search", "");
	//	pathStr="http://localhost:8080/srv-dstu1-0.2/api/Patient";
		URI uri=null;
		try {
			uri = new URI(pathStr);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.equals(e);
			e.printStackTrace();
			
		}
		logger.info("rewriting search url to:"+uri.toString());
		context.setRequestUri(uri);
		
	}

}