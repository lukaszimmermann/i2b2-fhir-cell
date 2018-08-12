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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.oauth2.core.ejb.AuthenticationService;
import edu.harvard.i2b2.oauth2.core.entity.AccessToken;
import edu.harvard.i2b2.oauth2.register.ejb.LogEntryService;
import edu.harvard.i2b2.oauth2.register.entity.LogEntry;
import edu.harvard.i2b2.oauth2.register.entity.User;

@WebFilter(urlPatterns = { "/*" }, description = "Website Filter")
public class WebsiteFilter implements Filter {
	static Logger logger = LoggerFactory.getLogger(WebsiteFilter.class);
	private FilterConfig config = null;

	@EJB
	LogEntryService logEntryService;
	
	
	@Override
	public void destroy() {

		logger.warn("Destroying SessionCheckerFilter");

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		String url = ((HttpServletRequest) request).getRequestURI().toString();
		String servletPath = ((HttpServletRequest) request).getServletPath();
		logger.info("url:" + url);
		
		
		String query=((HttpServletRequest) request).getQueryString();
		if(query==null) query="";
		String fullUrl=url+"?"+query;
		LogEntry le= new LogEntry();
		HttpSession session = ((HttpServletRequest) request).getSession();
		le.setUrl(fullUrl);
		le.setDateAccessed(new Date());
		le.setSessionId(session.getId());
		String accessToken="-";
		String clientId="-";
		if (session.getAttribute("accessToken")!=null) {
			accessToken=((AccessToken) session.getAttribute("accessToken")).getTokenString();
			clientId=((AccessToken) session.getAttribute("accessToken")).getClientId();
			
		}
		le.setAccessToken(accessToken);
		le.setClientId(clientId);
		le.setUserId(-1);
		
		if ((servletPath.startsWith("/user") 
				//|| servletPath.startsWith("/config")
				 || servletPath.startsWith("/client"))) {

			Object u = ((HttpServletRequest) request).getSession()
					.getAttribute("authenticatedUser");
			
			if (u == null) {
				logger.info("User is not authenticated. Hence redirecting to login page");
				HttpServletResponse httpServletResponse = (HttpServletResponse) response;
				httpServletResponse.sendRedirect("../login/signin.xhtml");	
			}
			
			if(u!=null){
				le.setUserId(User.class.cast(u).getId());
			}
			
		}
		
		logEntryService.create(le);
		
		chain.doFilter(new PublicClientWrapper((HttpServletRequest) request),
				response);

	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		logger.warn("Initializing WebsiteFilter");

	}

}