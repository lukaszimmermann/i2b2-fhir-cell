package edu.harvard.i2b2.fhir.query;

import static org.junit.Assert.assertFalse;

import org.hl7.fhir.Resource;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.hl7.fhir.Patient;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.SetupExamples;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class QueryEngineTest {
	static Logger logger = LoggerFactory.getLogger(QueryEngineTest.class);
	Patient p;
	Patient p2;

	QueryEngine qe;

	//@Before
	public void setup() {
		String xml = Utils.getFile("example/fhir/singlePatient.xml");
		p = (Patient) FhirUtil.xmlToResource(xml);
		xml = Utils
				.getFile("example/fhir/singlePatientWithoutCodeSystemForGender.xml");
		p2 = (Patient) FhirUtil.xmlToResource(xml);
	}

	@Test
	public void testQueryUrl() throws QueryParameterException,
			QueryValueException, FhirCoreException, DatatypeConfigurationException, JAXBException {
		qe = new QueryEngine("Patient?name=pieter&gender=M");
		logger.trace(qe.toString());
		
		MetaResourceSet s = new MetaResourceSet();
		MetaResource mr = FhirUtil.getMetaResource(p);
		s.getMetaResource().add(mr);
		MetaResource mr2 = FhirUtil.getMetaResource(p2);
		s.getMetaResource().add(mr2);
		s.getMetaResource().add(SetupExamples.getEGPatient().getMetaResource().get(0));


		MetaResourceSet s2=qe.search(s);
		logger.trace("Input:"+s.getMetaResource().size());
		logger.trace("Result:"+s2.getMetaResource().size());
		
	}
}
