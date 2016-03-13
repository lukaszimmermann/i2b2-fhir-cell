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

public class QueryId extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryId.class);
	String code;
	String namespace;

	public QueryId(Class resourceClass, String parameter, String value)
			throws QueryParameterException, QueryValueException, FhirCoreException, QueryException {
		super(resourceClass, parameter, value);
	}

	@Override
	protected void init() throws QueryValueException, QueryParameterException {
		this.type = QueryType.ID;

		// [parameter]=[namespace]|[code]
		// [parameter]=|[code]
		this.parameterPath=getResourceClass().getSimpleName()+"/id";
		Pattern p = Pattern.compile("^(.*)\\|(.*)$");
		Matcher m = p.matcher(this.getRawValue());
		if (m.matches()) {
			this.namespace = m.group(1);
			this.code = m.group(2);
		} else {
			// [parameter]=[code]
			p = Pattern.compile("^(.*)$");

			m = p.matcher(this.getRawValue());
			if (m.matches()) {
				this.namespace = "";
				this.code = m.group(1);
			} else {
				throw new QueryValueException(
						"query rawValue does not match Id template:"
								+ this.getRawValue());
			}
		}
		
	}

	@Override
	public boolean match(String resourceXml,Resource r, List<Resource> s, MetaResourceDb db) throws  XQueryUtilException {
			ArrayList<String> typeList;
			//logger.trace("xml:"+resourceXml);
			
		typeList = new ArrayList<String>(Arrays.asList("/id"));//id
		for (String type : typeList) {
			List<String> xmlList;
			
				xmlList = getXmlListFromParameterPath(resourceXml, "//"+r.getClass().getSimpleName()
						+ type);
			
			for (String xml : xmlList) {
				logger.trace("xml:" + xml);
				if (xml.equals(""))
					continue;
				String resultSystem = getXmlFromParameterPath(xml,
						"//@value/string()");
				logger.trace("resultSystem:" + resultSystem);

				// consider namespace
				if (this.namespace.length() > 0 ) {
					if (!(resultSystem.equals(namespace)))
						return false;
				}

				if (textSearch(xml))
					return true;
			}
		}

		return false;
		
	}

	// CodeableConcept.text, Coding.display, or Identifier.label
	private boolean textSearch(String xml) throws XQueryUtilException {
		ArrayList<String> pathExtList;
		// consider "text" modifier
			pathExtList = new ArrayList<String>(Arrays.asList(
					"@value/string()"));
		
		ArrayList<String> list = new ArrayList<String>();
		for (String ext : pathExtList) {
			list.addAll(getListFromParameterPath(xml, "/"+getLastElementOfParameterPath()+"/"+ext));
		}

		logger.trace("list:" + list.toString());
		for (String v : list) {
			if (v.equals(code)) {
				logger.info("matched:"+ this.getRawParameter()+"="+this.getRawValue());
				return true;
			}
		}
		return false;
	}

	

	@Override
	public void validateParameter() throws QueryParameterException {
		if ((this.getModifier().length() > 0)
				& (!(this.getModifier().equals("text")))) {
			throw new QueryParameterException("undefined modifier <"
					+ this.getModifier() + "> for Query of type token");
		}
	}

	@Override
	public void validateValue() throws QueryValueException {

	}

	@Override
	public String toString() {
		return "QueryId "+super.toString()+"code=" + code + ", namespace=" + namespace + "]\n";
	}
}
