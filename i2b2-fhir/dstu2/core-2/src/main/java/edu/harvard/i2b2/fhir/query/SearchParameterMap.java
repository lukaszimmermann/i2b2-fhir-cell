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
package edu.harvard.i2b2.fhir.query;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.hl7.fhir.BundleEntry;
import org.hl7.fhir.SearchParameter;
import org.hl7.fhir.Patient;
import org.hl7.fhir.SearchParameter;
import org.hl7.fhir.SearchParameter;
import org.hl7.fhir.SearchParamType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class SearchParameterMap {
	static Logger logger = LoggerFactory.getLogger(SearchParameterMap.class);

	static List<SearchParameter> list;

	public SearchParameterMap() throws FhirCoreException {
		try {
			init();
		} catch (JAXBException e) {
			logger.error("init exception", e);
			throw new FhirCoreException("init JAXB exception", e);
		}
	}

	public void init() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(SearchParameter.class);
		Unmarshaller um = context.createUnmarshaller();
		SearchParameter p = null;
		if (list == null) {
			list = new ArrayList<SearchParameter>();
			String bundleString=null;
			try {
				bundleString = IOUtils.toString(SearchParameter.class.getResourceAsStream(
						"/profiles/search-parameters.xml"));
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
			// logger.trace("bundleString:"+bundleString);
			Bundle b = JAXBUtil.fromXml(bundleString, Bundle.class);
			for (BundleEntry rc : b.getEntry()) {
				p = rc.getResource().getSearchParameter();
				list.add(p);
			}
		}
	}

	public String getParameterPath(Class c, String parName)
			throws FhirCoreException {
		
		return getSearchParameter(c, parName).getXpath().getValue().toString()
				.replace("f:", "");
	}

	public String getType(Class c, String parName) throws FhirCoreException {

		return getSearchParameter(c, parName).getType().getValue().toString();

	}

	public SearchParameter getSearchParameter(Class c, String parName)
			throws FhirCoreException {

		for (SearchParameter sp : list) {
			//logger.trace("sp:" + sp);
			String resourceClassString = sp.getBase().getValue();
			Class foundClass = FhirUtil.getResourceClass(resourceClassString);
			String foundName = sp.getName().getValue();

			if (c.equals(foundClass) && foundName.equals(parName)) {
				return sp;
			}
		}
		throw new FhirCoreException(
				"No SearchParameterSearchParam found for class:"
						+ c.getCanonicalName() + " for param:" + parName);
	}
}
