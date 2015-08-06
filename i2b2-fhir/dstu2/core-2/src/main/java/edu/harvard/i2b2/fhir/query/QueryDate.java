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
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryDate extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryDate.class);
	String operator;
	String dateValue;
	GregorianCalendar dateValueExpected;
	String reEncodedValue;

	public QueryDate(Class resourceClass, String parameter, String value)
			throws QueryParameterException, QueryValueException, FhirCoreException, QueryException {
		super(resourceClass, parameter, value);
		
	}
	
	protected void init() throws QueryValueException, QueryParameterException{
		this.type = QueryType.DATE;
		reEncodedValue=this.getRawValue().replace("%3E", ">").replace("%3C", "<").replace("%3D", "=");
		Pattern p = Pattern.compile("^([<=>]*)[^\\s<=>]+");
		Matcher m = p.matcher(reEncodedValue);
		this.operator = m.matches() ? m.group(1) : "";
		logger.info("operator:" + this.operator);
		this.dateValue = (this.operator.length() > 0) ? reEncodedValue
				.substring(this.operator.length()) : reEncodedValue;
				
		try {
			validateDate();
			this.dateValueExpected = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(this.dateValue)
					.toGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override	
	public boolean match(String resourceXml,Resource r, List<Resource>s) throws XQueryUtilException  {
		ArrayList<String> list;
			list = getValuesAtParameterPath(resourceXml,
					this.getParameterPath());
		
		for (String v : list) {
			GregorianCalendar dateValueFound;
			logger.info("matching:"+ this.getRawParameter()+"="+this.getRawValue());
			
			try {
				dateValueFound = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(v).toGregorianCalendar();
			} catch (DatatypeConfigurationException e) {
				throw new RuntimeException(e);
			}
			
			if (operator.contains("=")||operator.equals("")) {
				if (dateValueFound.equals(this.dateValueExpected)){
					logger.info("matched:"+ this.getRawParameter()+"="+this.getRawValue());
					return true;
				}
			}
			if (operator.contains("<")) {
				if (dateValueFound.before(this.dateValueExpected)){
					logger.info("matched:"+ this.getRawParameter()+"="+this.getRawValue());
					return true;
				}
			}
			if (operator.contains(">")) {
				if (dateValueFound.after(this.dateValueExpected)){
					logger.info("matched:"+ this.getRawParameter()+"="+this.getRawValue());
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void validateParameter() throws QueryParameterException {
		// TODO Auto-generated method stub
	}

	
	@Override
	public void validateValue() throws QueryValueException {
		if(this.getRawValue()==null) throw new QueryValueException("rawValue is null");
	}

	
	public void validateDate() throws QueryValueException {
		try {
			DatatypeFactory.newInstance().newXMLGregorianCalendar(
					this.dateValue);
		} catch (IllegalArgumentException e) {
			throw new QueryValueException("value is not in XML date format:"
					+ this.dateValue, e);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public String toString() {
		return "QueryDate "+super.toString() + ", operator=" + operator + ", dateValue="+ this.dateValue+ "reEncodedValue="
				+ this.reEncodedValue+ "]\n";

	}
}
