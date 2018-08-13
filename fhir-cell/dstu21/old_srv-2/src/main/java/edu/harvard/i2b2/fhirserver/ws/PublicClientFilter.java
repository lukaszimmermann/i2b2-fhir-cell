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

package edu.harvard.i2b2.fhirserver.ws;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(urlPatterns = {"/*"}, description = "Session Checker Filter")
public class PublicClientFilter implements Filter {
	static Logger logger = LoggerFactory.getLogger(PublicClientFilter.class);
	private FilterConfig config = null;

	/*
	 * public void filter(ContainerRequestContext context) throws IOException {
	 * 
	 * UriInfo uriInfo=context.getUriInfo(); String
	 * pathStr=uriInfo.getAbsolutePath().toString();
	 * logger.info("got uri::"+pathStr); //for(String
	 * h:context.getHeaders().keySet()){ //
	 * logger.info("got ..::"+h+"->"+context.getHeaders().get(h)); //}
	 * if(!context.getUriInfo().getPath().toString() .startsWith("/token"))
	 * return;
	 * 
	 * 
	 * String txt=IOUtils.toString(context.getEntityStream());
	 * logger.info("txt:"+txt); //context.setProperty("client_secret", "dummy");
	 * context.setEntityStream(new
	 * ByteArrayInputStream(txt.getBytes(StandardCharsets.UTF_8)));
	 * 
	 * }
	 */

	@Override
	public void destroy() {

		logger.warn("Destroying SessionCheckerFilter");

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		String url = ((HttpServletRequest) request).getRequestURL().toString();
		logger.info("url:" + url);
		 //if(!url.startsWith("/token")) return;
			
		String msg = "";
		Enumeration<String> kl = request.getParameterNames();
		while (kl.hasMoreElements()) {
			String k = kl.nextElement();
			msg += k + "->" + request.getParameter(k) + "\n";
		}
		logger.info(msg);

		chain.doFilter(new PublicClientWrapper((HttpServletRequest) request),
				response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		logger.warn("Initializing SessionCheckerFilter");

	}

}