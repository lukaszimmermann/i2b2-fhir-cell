package edu.harvard.i2b2.oauth2.ejb;

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

