package edu.harvard.i2b2;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.query.QueryToken;

public class ResourceListTest {
	static Logger logger = LoggerFactory.getLogger(ResourceListTest.class);

	@Test
	public void test() {
		FhirUtil.initResourceClassList();
	}

}
