package edu.harvard.i2b2.fhirserver.entity;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BasePathTest {
	static Logger logger = LoggerFactory.getLogger( BasePathTest.class);

	@Test
	public void test() {
		
		String url="http://localhost:8080/srv-dstu2-0.2/api/authz/i2b2login";
		//url=url.replaceAll("/(^//.+)$", "A");
		//String path = uri.getPath();
		logger.trace(url);
		url = url.substring(0,url.lastIndexOf('/') )+"";
		logger.trace(url);
	}

}
