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
package edu.harvard.i2b2.fhir.query;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryBuilder {
	static Logger logger = LoggerFactory.getLogger(QueryBuilder.class);

	Class resourceClass;
	String rawParameter;
	String rawValue;
	String queryTypeStr;
	String resourceClassRestriction;
	String rawType;
	
	public QueryBuilder() {
	}

	public QueryBuilder(String url) throws UnsupportedEncodingException {
		url = URLEncoder.encode(url, "UTF-8");
		String fhirClassExp = "("
				+ FhirUtil.getResourceList().toString().replace(",", "|")
						.replaceAll("[\\s\\[\\]]+", "") + ")";
		Pattern p = Pattern.compile(fhirClassExp
				+ ";*([^&\\?;]*)\\?([^=&\\?]*)=([^=&\\?]*)",
				Pattern.CASE_INSENSITIVE);
		// String.join(",", FhirUtil.getResourceList());//java 8
		Matcher m = p.matcher(url);
		if (m.matches()) {
			this.resourceClass = FhirUtil.getResourceClass(m.group(1));
			this.rawParameter = m.group(3);
			this.rawValue = m.group(4);
		}

	}

	public QueryBuilder(Class resourceClass, String url)
			throws FhirCoreException {
		this.resourceClass = resourceClass;
		Pattern p = Pattern.compile("([^=&\\?]*)=([^=&\\?]*)");
		Matcher m = p.matcher(url);
		if (m.matches()) {
			this.rawParameter = m.group(1);
			this.rawValue = m.group(2);
		}
		logger.trace("" + this.toString());
	}

	public QueryBuilder(Class resourceClass, String rawParam, String rawValue) {
		this.resourceClass = resourceClass;
		this.rawParameter = rawParam;
		this.rawValue = rawValue;
	}

	// will aply rules to parameter name and value to identify type of query and
	// create it
	public Query build() throws QueryParameterException, QueryValueException,
			FhirCoreException, QueryException {
		if (resourceClass == null)
			throw new FhirCoreException("resource class is null");

		// logger.trace("rawParameter:"+this.rawParameter);
		if (this.rawParameter.equals("patient:Patient")) {this.rawParameter="patient";}
		if (this.rawParameter.equals("subject:Patient")) {this.rawParameter="subject";}
		String parameter = this.rawParameter;
		
		
		
		if (parameter.contains(":"))
			parameter = parameter.split(":")[0];
		
		//pick out custom query type
		if (parameter.matches("^@.+")){
			parameter = parameter.substring(1);
			this.queryTypeStr = "custom";
		}
		
		
		
		Query q = null;

		try {
			if (this.queryTypeStr==null) {
				this.queryTypeStr = new SearchParameterMap().getType(
						this.getResourceClass(), parameter);
			}
		} catch (FhirCoreException e) {
			
			//throw new QueryParameterException(
				//	"no ParamPath found.Query could not be built for "
					//		+ this.toString(), e);
		}

		if(this.queryTypeStr==null){this.queryTypeStr="";}
		switch (this.queryTypeStr.toLowerCase()) {
		case "date":
			q = new QueryDate(resourceClass, rawParameter, rawValue);
			logger.info("created query:" + (QueryDate) q);
			break;
		case "token":
			q = new QueryToken(resourceClass, rawParameter, rawValue);
			logger.info("created query:" + (QueryToken) q);

			break;
		case "reference":
			q = new QueryReference(resourceClass, rawParameter, rawValue);
			logger.info("created query:" + (QueryReference) q);

			break;

		case "string":
			q = new QueryString(resourceClass, rawParameter, rawValue);
			logger.info("created query:" + (QueryString) q);

			break;
		case "custom":
			q = new QueryCustom(resourceClass, rawParameter, rawValue,false);
			logger.info("created query:" + (QueryCustom) q);
			break;
		default:
			q = new QueryChained(resourceClass, rawParameter, rawValue);
			logger.info("created query:" + (QueryChained) q);
			break;
			//throw new FhirCoreException("Query could not be built for:"
				//	+ this.toString());
		}

		return q;
	}

	public Class getResourceClass() {
		return resourceClass;
	}

	public QueryBuilder setResourceClass(Class resourceClass) {
		this.resourceClass = resourceClass;
		return this;
	}

	public String getRawParameter() {
		return rawParameter;
	}

	public QueryBuilder setRawParameter(String parameter) {
		this.rawParameter = parameter;
		return this;
	}

	public String getRawValue() {
		return rawValue;
	}

	public QueryBuilder setRawValue(String value) {
		this.rawValue = value;
		return this;
	}

	@Override
	public String toString() {
		return "QueryBuilder [resourceClass=" + resourceClass
				+ ", rawParameter=" + rawParameter + ", rawValue=" + rawValue
				+ ", queryTypeStr=" + queryTypeStr + "]";
	}

}
