package edu.harvard.i2b2.util;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.Utils;

public class CompressTest {
	static Logger logger = LoggerFactory.getLogger(CompressTest.class);

	@Test
	public void test() throws IOException {
		// logger.trace("Compressed:"+compress("hiThere"));
		logger.trace("Compressed:" +Utils.unCompressString(Utils.compress("hiThere","UTF-8"),"UTF-8"));
	}

	
}
