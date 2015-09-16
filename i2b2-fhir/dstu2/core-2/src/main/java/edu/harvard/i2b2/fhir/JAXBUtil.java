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
package edu.harvard.i2b2.fhir;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.hl7.fhir.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.query.QueryEngine;
import edu.harvard.i2b2.map.MapSystem;
import edu.harvard.i2b2.map.MapSystemSet;

public class JAXBUtil {
	private static HashMap<Class, JAXBContext> hmJaxbc = null;
	static Logger logger = LoggerFactory.getLogger(JAXBUtil.class);
	
	public static JAXBContext getJaxBContext(Class c) throws JAXBException {
		logger.trace("Call for class:"+c.getSimpleName());
		if (hmJaxbc == null) {
			hmJaxbc = new HashMap<Class, JAXBContext>();
		}
		if (hmJaxbc.get(c) == null) {
			JAXBContext jaxbContext = JAXBContext.newInstance(c);
			hmJaxbc.put(c, jaxbContext);
		}
		return hmJaxbc.get(c);

	}
	
	
	static public <C> C fromXml(String xml,Class c) throws JAXBException{
		if (xml.equals("") || xml == null)
			return null;
		C mr=null;
		try {
			JAXBContext jc = getJaxBContext(c);
			Unmarshaller unMarshaller = jc.createUnmarshaller();

			mr = (C) unMarshaller.unmarshal(new StringReader(xml));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return mr;
	}
	
	
	
	public static <C> String toXml(C c) throws JAXBException{
		if(c==null) throw new IllegalArgumentException("input object is null");
		StringWriter rwriter = new StringWriter();
		JAXBContext jaxbContext = getJaxBContext(c.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
				Boolean.TRUE);
		jaxbMarshaller.marshal(c, rwriter);
		return rwriter.toString();
	}
	
}
