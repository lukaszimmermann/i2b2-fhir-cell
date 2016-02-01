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

public class QueryReference extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryReference.class);
	String param;
	String url;

	public QueryReference(Class resourceClass, String param, String value)
			throws QueryParameterException, QueryValueException,
			FhirCoreException, QueryException {
		super(resourceClass, param, value);
	}

	@Override
	protected void init() throws QueryValueException, QueryParameterException {
		this.type = QueryType.REFERENCE;
		// [param]=[url]
		// [param]=[id]
		if (this.getRawValue().contains("/")) {
			this.url = this.getRawValue();
		} else {
			this.param = this.getRawValue();
		}
	}

	@Override
	public boolean match(String resourceXml,Resource r,List<Resource> s) throws XQueryUtilException {
		//logger.trace("xml:"+resourceXml);
		String rawId;
			rawId = getXmlFromParameterPath(resourceXml,  getParameterPath()+"/reference/@value/string()");
		
		// id=this.getLastElementOfparamPath()+"/"+id;
			rawId=rawId.split("/")[1];

		 logger.info("matching "+rawId+" to value:"+this.getRawValue());
		if (rawId.equals(this.getRawValue())) {
			logger.info("param:"+param+" matched:" + this.getRawParameter() + "="
					+ this.getRawValue());
			return true;
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
		return "QueryReference " + super.toString() + ", param=" + param + ", url="
				+ url + "]";
	}

}
