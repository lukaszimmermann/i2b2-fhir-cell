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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryString extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryString.class);
	String searchText;

	public QueryString(Class resourceClass, String parameter, String value)
			throws QueryParameterException, QueryValueException,
			FhirCoreException, QueryException {
		super(resourceClass, parameter, value);
	}

	@Override
	protected void init() throws QueryValueException, QueryParameterException {
		this.type = QueryType.STRING;
		logger.trace(">>>MOD:" + this.getModifier() + "--rawVal:"
				+ this.getRawValue());
		if (this.getModifier().equals("exact"))
			searchText = this.getRawValue();
		else {
			searchText = this.getRawValue().toLowerCase()
					.replaceAll("\\s+", " ").replaceAll("^\\s", "")
					.replaceAll("\\s$", "");

		}
	}

	@Override
	public boolean match(String resourceXml,Resource r, List<Resource> s, MetaResourceDb db) throws XQueryUtilException  {
			logger.trace("resourceXml:"+resourceXml);
		List<String> xmlList;
		
			xmlList = getXmlListFromParameterPath(resourceXml, this.getParameterPath());
		
		for (String xml : xmlList) {
			logger.trace("xml:" + xml);
			if (xml.equals(""))
				continue;

			if (textSearch(xml,this.searchText))
				return true;
		}
		return false;
		
	}

	

	@Override
	public void validateParameter() throws QueryParameterException {
		if ((this.getModifier().length() > 0)
				& (!(this.getModifier().equals("exact")))) {
			throw new QueryParameterException("undefined modifier <"
					+ this.getModifier() + "> for Query of type token");
		}
	}

	@Override
	public void validateValue() throws QueryValueException {

	}

	@Override
	public String toString() {
		return "QueryString "+super.toString()+ "searchText=" + searchText + "]\n";
	}
}
