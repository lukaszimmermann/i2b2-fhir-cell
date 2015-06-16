package edu.harvard.i2b2.fhir.query;

import static org.junit.Assert.assertFalse;

import org.hl7.fhir.Medication;
import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.Resource;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.hl7.fhir.Patient;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.SetupExamples;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class QueryChainedTest {
	static Logger logger = LoggerFactory.getLogger(QueryChainedTest.class);
	Patient p;
	String xmlPatient;
	String xmlMedicationStatement;
	MedicationStatement ms;
	
	QueryBuilder qb;
	Query q;
	MetaResourceDb db;
	QueryEngine qe;
	MetaResourceSet s;
	@Before
	public void setup() throws FhirCoreException, JAXBException, IOException {
		xmlPatient = Utils.getFile("example/fhir/singlePatient.xml");
		p = (Patient) JAXBUtil.fromXml(xmlPatient,Patient.class);
		qb = new QueryBuilder();
		xmlMedicationStatement = Utils.getFile("example/fhir/MedicationStatement.xml");
		ms = (MedicationStatement) JAXBUtil.fromXml(xmlMedicationStatement,MedicationStatement.class);
		ms.setId("1-1");
		qb = new QueryBuilder();
		db=new MetaResourceDb();
		db.addMetaResource(FhirUtil.getMetaResource(p), Patient.class);
		db.addMetaResource(FhirUtil.getMetaResource(ms), MedicationStatement.class);
		s=db.getAll();
	}

	@Test
	public void testSingle() throws QueryParameterException, QueryValueException, FhirCoreException, JAXBException, XQueryUtilException, QueryException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//logger.trace("ms:"+JAXBUtil.toXml(ms));
		logger.trace("id:"+FhirUtil.getChildThruChain(ms,"Patient.id", s));
		String url="MedicationStatement?MedicationStatement.Patient.id=example";
		qe = new QueryEngine(url);
		logger.info("res:"+qe.search(s));	
	
		}
}
