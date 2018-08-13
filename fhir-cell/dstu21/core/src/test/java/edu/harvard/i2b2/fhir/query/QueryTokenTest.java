package edu.harvard.i2b2.fhir.query;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.Resource;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryTokenTest {
	static Logger logger = LoggerFactory.getLogger(QueryTokenTest.class);

	QueryBuilder qb;
	Query q;
	MetaResourceDb db;
	QueryEngine qe;
	List<Resource> s;

	@Before
	public void setup() throws FhirCoreException, JAXBException, IOException {
		String xmlBundle = Utils.getFile("PDOBundle.xml");
		Bundle b = JAXBUtil.fromXml(xmlBundle, Bundle.class);
		db=new MetaResourceDb();
			db.addBundle(b);
		logger.trace("" + db.getSize());

		s = db.getAll();
	}

	@Test
	public void testToken() throws QueryParameterException, QueryValueException, FhirCoreException, JAXBException,
			XQueryUtilException, QueryException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		
		  String url ="Patient?gender=male";
		 	  
					  
		  qe = new QueryEngine(url); logger.trace("qe:" + qe); 
		  List<Resource> resSet = qe.search(s,db);
		 
	}

}
