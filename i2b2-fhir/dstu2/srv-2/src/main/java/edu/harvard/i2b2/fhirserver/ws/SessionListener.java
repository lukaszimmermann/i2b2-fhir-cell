package edu.harvard.i2b2.fhirserver.ws;

import javax.ejb.EJB;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhirserver.ejb.ConfigBean;
import edu.harvard.i2b2.fhirserver.ejb.SessionBundleBean;

public class SessionListener implements HttpSessionListener {
	static Logger logger = LoggerFactory.getLogger(SessionListener.class);

  private static int totalActiveSessions;
  @EJB
  SessionBundleBean sbb;
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
	sbb.removeSessionBundle(arg0.getSession().getId());
  }	
}