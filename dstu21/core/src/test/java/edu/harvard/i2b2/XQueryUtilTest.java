package edu.harvard.i2b2;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.WebServiceCall;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;

public class XQueryUtilTest {
	static Logger logger = LoggerFactory.getLogger(XQueryUtilTest.class);

	@Test
	public void test() throws IOException, XQueryUtilException {
		String xml=IOUtils.toString(I2b2Util.class
				.getResourceAsStream("/example/i2b2/labsAndMedicationsForAPatient.xml"));
		String query = IOUtils.toString(I2b2Util.class
				.getResourceAsStream("/transform/I2b2ToFhir/i2b2MedsToFHIRMedPrescription.xquery"));
		logger.info("xml:"+xml);
		logger.info("query:"+query);
		String xQueryResultString = XQueryUtil.processXQuery(query, xml);
		logger.info("xQueryResultString:"+xQueryResultString);
	}

}
