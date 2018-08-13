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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/*
 * This Request wrapper modifies requests for public Clients that are not required
 *  to pass client secret for getting access tokens
 */
public class PublicClientWrapper extends HttpServletRequestWrapper {

	public PublicClientWrapper(HttpServletRequest request) {
		super(request);
	}

	public String getParameter(String name) {

		HttpServletRequest req = (HttpServletRequest) super.getRequest();
		if (name.equals("client_secret") && req.getParameter(name)==null) {
			return "dummy";
		} else {
			return req.getParameter(name);
		}
	}

	public Map<String, String[]> getParameterMap() {

		Map<String, String[]> map = super.getParameterMap();

		Iterator iterator = map.keySet().iterator();

		Map<String, String[]> newMap = new LinkedHashMap<String, String[]>();

		while (iterator.hasNext()) {

			String key = iterator.next().toString();

			String[] values = map.get(key);
			
			newMap.put(key, values);
		}
		if(!map.keySet().contains("client_secret")){
			String[] values={"dummy"};
			newMap.put("client_secret", values);
		}
		return newMap;
	}

	// --A.in.the.k 08:57, 19 March 2009 (UTC) for correct implementation see
	// http://www.owasp.org/index.php/How_to_perform_HTML_entity_encoding_in_Java
	// Return HTML Entity code equivalents for any special characters
	public static String HTMLEntityEncode(String input) {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < input.length(); ++i) {

			char ch = input.charAt(i);

			if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0'
					&& ch <= '9') {
				sb.append(ch);
			} else {
				sb.append("&#" + (int) ch + ";");
			}
		}
		return sb.toString();
	}

}