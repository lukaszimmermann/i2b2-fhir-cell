/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package edu.harvard.i2b2.map;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.hl7.fhir.Patient;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.map.MapSystemEngine;

public class MapSystemEngineTest {

	static Logger logger = LoggerFactory.getLogger(MapSystemEngineTest.class); 
	
	@Before
	public void setup()  {
	}
	
	@Test
	public void test1() throws MalformedURLException, JAXBException, IOException, XQueryUtilException  {
		//MapSystemEngine me= new MapSystemEngine(Utils.getFile("example/map/MapSystem1.xml"));
		/*String xmlPatient = Utils.getFile("example/fhir/singlePatient.xml");
		Patient p = (Patient) JAXBUtil.fromXml(xmlPatient,Patient.class);
		logger.trace(JAXBUtil.toXml(p));
		*/
		MapSystemEngine me= new MapSystemEngine(Utils.getFile("example/map/MapSystem1.xml"));
		//logger.trace(me.runMapSystem(Utils.getFile("example/i2b2/AllPatients.xml")));
		//logger.trace(me.appendPath("<a></a>", "Patient/maritalStatus/Coding/Code", ""));
		logger.trace(me.appendPath("<a></a>", "Patient/maritalStatus/Coding/Code/@value", ""));
		//me.runMapSystem(Utils.getFile("example/i2b2/medicationsForAPatient.xml"));
		/*MapSystemSet mss=new MapSystemSet();
		MapSystem ms=new MapSystem();
		FromPath fp=new FromPath();fp.setName("f");
		ToPath tp=new ToPath();tp.setName("f");
		ms.setFromPath(fp);
		ms.setToPath(tp);
		List<MapSystem> list= new ArrayList<MapSystem>();
		mss.getMapSystem().add(ms);
		
		logger.trace(JAXBUtil.toXml(mss));
		*/
	}
	
}