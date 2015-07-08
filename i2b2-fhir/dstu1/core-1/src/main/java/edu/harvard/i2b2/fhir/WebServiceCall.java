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
package edu.harvard.i2b2.fhir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;


public class WebServiceCall {
	static Logger logger = LoggerFactory.getLogger(WebServiceCall.class);

	public static String run(String path,String requestXml){
		String response=null;
	
		/*
		Client client = ClientBuilder.newBuilder().build();
		WebTarget  webTarget = client.target(path);
		
		String oStr = webTarget
				.request()
				.accept("Context-Type", "application/xml")
				.post(Entity.entity(requestXml, MediaType.APPLICATION_XML),
						String.class);
		*/
		
		Client c = Client.create();
		WebResource r = c.resource(path);
	    response = r.accept("Context-Type", "application/xml")
	            //header(“X-FOO”, “BAR”).
	    		.entity(requestXml, "application/xml")
	            .post(String.class);
	   // System.out.println(response);
		return response;
	}

}
