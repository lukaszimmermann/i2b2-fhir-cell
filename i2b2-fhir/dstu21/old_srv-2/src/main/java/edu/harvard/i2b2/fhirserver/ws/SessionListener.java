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

import javax.ejb.EJB;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhirserver.ejb.ConfigBean;

public class SessionListener implements HttpSessionListener {
	static Logger logger = LoggerFactory.getLogger(SessionListener.class);

  private static int totalActiveSessions;
  public static int getTotalActiveSession(){
	return totalActiveSessions;
  }
	
  @Override
  public void sessionCreated(HttpSessionEvent arg0) {
	totalActiveSessions++;
	logger.info("sessionCreated - add one session into counter:"+arg0.getSession().getId());
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent arg0) {
	totalActiveSessions--;
	logger.info("sessionDestroyed - deduct one session from counter:"+arg0.getSession().getId());
	//sbb.removeSessionBundle(arg0.getSession().getId());
  }	
}