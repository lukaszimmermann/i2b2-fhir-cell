package edu.harvard.i2b2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.WebServiceCall;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.XQueryUtilInstance;

public class XQueryUtilInstanceTest {
	static Logger logger = LoggerFactory.getLogger(XQueryUtilInstanceTest.class);

	@Test
	public void test() throws IOException, XQueryUtilException {
		
		XQueryUtilInstance xqinst=new XQueryUtilInstance();
		String xml=IOUtils.toString(I2b2Util.class
				.getResourceAsStream("/example/i2b2/labsAndMedicationsForAPatient.xml"));
		String query = IOUtils.toString(I2b2Util.class
				.getResourceAsStream("/transform/I2b2ToFhir/i2b2MedsToFHIRMedPrescription.xquery"));
		logger.info("xml:"+xml);
		logger.info("query:"+query);
		Date d1=new Date();
		for(int x=0;x<1000;x++){
			String xQueryResultString = XQueryUtil.processXQuery(query, xml);
		
			//logger.info("xQueryResultString:"+xQueryResultString);
		}
		Date d2=new Date();
		long sec=(d2.getTime()-d1.getTime())/1000;
		logger.info("timeElapsed:"+sec);
		
		d1=new Date();
		//for(int x=0;x<10;x++){
			String xQueryResultString = xqinst.processXQuery(query, xml);
			logger.info("xQueryResultString:"+xQueryResultString);
		//}
		d2=new Date();
		sec=(d2.getTime()-d1.getTime())/1000;
		logger.info("timeElapsed:"+sec);
	
	}

}
