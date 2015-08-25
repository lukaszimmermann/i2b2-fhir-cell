package edu.harvard.i2b2.fhirserver.ws;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	static public String i2b2Url ;
	static public String demoAccessToken ;

	static Logger logger = LoggerFactory.getLogger(Config.class);

	static {
		try {
			CompositeConfiguration config = new CompositeConfiguration();
			config.addConfiguration(new SystemConfiguration());
			config.addConfiguration(new PropertiesConfiguration(
					"application.properties"));
			
			i2b2Url =config.getString("i2b2Url");
			demoAccessToken =config.getString("demoAccessToken");
			
			logger.info("initialized:"+"\ni2b2Url:"+i2b2Url
					+"\ndemoAccessToken:"+demoAccessToken);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	
	
	
}
