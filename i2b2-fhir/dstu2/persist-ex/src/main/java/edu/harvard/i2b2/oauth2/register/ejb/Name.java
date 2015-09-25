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

package edu.harvard.i2b2.oauth2.register.ejb;

import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named 
@RequestScoped 
@Stateless
public class Name { 
	static Logger logger = LoggerFactory.getLogger(Name.class);
	
	public String getValue() {
		logger.info("called getValue()");
		return "hmm";
	}

		
}

