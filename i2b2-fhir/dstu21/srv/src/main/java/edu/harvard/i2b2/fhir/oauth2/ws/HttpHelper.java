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

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.server.ConfigParameter;
import edu.harvard.i2b2.fhir.server.ServerConfigs;

public class HttpHelper {
	static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

	static public URI getBasePath(HttpServletRequest request)
			throws URISyntaxException {
		String uri = request.getScheme()
				+ "://"
				+ request.getServerName()
				+ ("http".equals(request.getScheme())
						&& request.getServerPort() == 80
						|| "https".equals(request.getScheme())
						&& request.getServerPort() == 443 ? "" : ":"
						+ request.getServerPort())
				+ request.getRequestURI()
				+ (request.getQueryString() != null ? "?"
						+ request.getQueryString() : "");
		if(uri.contains("?")) uri=uri.split("\\?")[0];
		
		logger.trace("full uri:" + uri);
		uri = uri.substring(0, uri.lastIndexOf('/')) + "/";
		logger.info("base uri:" + uri);
		/*
		 String scheme = request.getScheme();
	        String serverName = request.getServerName();
	        int portNumber = request.getServerPort();
	        String contextPath = request.getContextPath();
	        String servletPath = request.getServletPath();
	        String pathInfo = request.getPathInfo();
	        String query = request.getQueryString();
	       
		logger.info("request.getContextPath():" + request.getContextPath());
		logger.info("request.getServletPath():" + request.getServletPath());
		logger.info("request.getPathInfo():" + request.getPathInfo());
		 */
		//if(serverConfigs.GetString(ConfigParameter.fhirbaseSSL).equals("true")){
		//	uri=uri.toString().replaceAll("^http:", "https:");
		//}
		return new URI(uri);
	}
	
	static public URI getBasePath(HttpServletRequest request,ServerConfigs serverConfigs )
			throws URISyntaxException {
		String uri = request.getScheme()
				+ "://"
				+ request.getServerName()
				+ ("http".equals(request.getScheme())
						&& request.getServerPort() == 80
						|| "https".equals(request.getScheme())
						&& request.getServerPort() == 443 ? "" : ":"
						+ request.getServerPort())
				+ request.getRequestURI()
				+ (request.getQueryString() != null ? "?"
						+ request.getQueryString() : "");
		if(uri.contains("?")) uri=uri.split("\\?")[0];
		
		logger.trace("full uri:" + uri);
		uri = uri.substring(0, uri.lastIndexOf('/')) + "/";
		logger.info("base uri:" + uri);
		/*
		 String scheme = request.getScheme();
	        String serverName = request.getServerName();
	        int portNumber = request.getServerPort();
	        String contextPath = request.getContextPath();
	        String servletPath = request.getServletPath();
	        String pathInfo = request.getPathInfo();
	        String query = request.getQueryString();
	       
		logger.info("request.getContextPath():" + request.getContextPath());
		logger.info("request.getServletPath():" + request.getServletPath());
		logger.info("request.getPathInfo():" + request.getPathInfo());
		 */
		if(serverConfigs.GetString(ConfigParameter.fhirbaseSSL).equals("true")){
			uri=uri.toString().replaceAll("^http:", "https:");
		}
		return new URI(uri);
	}
	
	static public URI getServletUri(HttpServletRequest request)
			throws URISyntaxException {
		String uri = request.getScheme()
				+ "://"
				+ request.getServerName()
				+ ("http".equals(request.getScheme())
						&& request.getServerPort() == 80
						|| "https".equals(request.getScheme())
						&& request.getServerPort() == 443 ? "" : ":"
						+ request.getServerPort())
				+ request.getContextPath();
		return new URI(uri);
	}
}
