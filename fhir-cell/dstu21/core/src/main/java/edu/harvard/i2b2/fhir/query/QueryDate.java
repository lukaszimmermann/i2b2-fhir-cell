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

import java.text.SimpleDateFormat;
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
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import javassist.bytecode.analysis.Util;

public class QueryDate extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryDate.class);
	String operator;
	String dateValue;
	GregorianCalendar dateValueExpected;
	String reEncodedValue;
	// the considerX flags are to zoom in/out the granularity of matching in
	// accordance
	// with the granularity of the provided date argument
	boolean considerMonth;
	boolean considerDay;

	public QueryDate(Class resourceClass, String parameter, String value)
			throws QueryParameterException, QueryValueException, FhirCoreException, QueryException {
		super(resourceClass, parameter, value);

	}

	protected void init() throws QueryValueException, QueryParameterException {
		this.type = QueryType.DATE;
		reEncodedValue = this.getRawValue().replace("%3E", ">").replace("%3C", "<").replace("%3D", "=")
				.replace("gt", ">").replace("lt", "<");
		Pattern p = Pattern.compile("^([<=>]*)[^\\s<=>]+");
		Matcher m = p.matcher(reEncodedValue);
		this.operator = m.matches() ? m.group(1) : "";
		logger.info("operator:" + this.operator);
		this.dateValue = (this.operator.length() > 0) ? reEncodedValue.substring(this.operator.length())
				: reEncodedValue;

		try {
			validateDate();
			this.dateValueExpected = DatatypeFactory.newInstance().newXMLGregorianCalendar(this.dateValue)
					.toGregorianCalendar();

			considerDay = false;// default
			considerMonth = false;// default
			if (this.dateValue.matches("\\d{4}-\\d{2}-\\d{2}")) {
				considerDay = true;
				considerMonth = true;
			}
			if (this.dateValue.matches("\\d{4}-\\d{2}.*")) {
				considerMonth = true;
			}

		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean match(String resourceXml, Resource r, List<Resource> s, MetaResourceDb db) throws XQueryUtilException {
		List<String> pathArr = Arrays.asList(this.getParameterPath().split("\\|"));
		for (String path : pathArr) {
			ArrayList<String> list;
			list = getValuesAtParameterPath(resourceXml, path);

			for (String v : list) {
				GregorianCalendar dateValueFound;
				logger.info("matching:" + this.getRawParameter() + "=" + this.getRawValue());
				if(v.contains("T")) v=v.substring(0,v.indexOf('T'))+"T00:00:00";//ignore Time and timeZone 
				
				try {
					dateValueFound = DatatypeFactory.newInstance().newXMLGregorianCalendar(v).toGregorianCalendar();
				} catch (DatatypeConfigurationException e) {
					logger.error("v:"+v+"\n"+e.getMessage(),e);
					throw new XQueryUtilException(e);
				}
				if (considerDay == false) {
					dateValueFound.set(GregorianCalendar.DAY_OF_MONTH, 1);
				}
				if (considerMonth == false) {
					dateValueFound.set(GregorianCalendar.DAY_OF_MONTH, 1);
					dateValueFound.set(GregorianCalendar.MONTH, 0);
				}

				logger.trace("expected:" + ((new SimpleDateFormat("yyyy-MM-dd")).format(dateValueExpected.getTime()))
						+ " found:" + ((new SimpleDateFormat("yyyy-MM-dd")).format(dateValueFound.getTime()))
						+ " monthflag:" + considerMonth + " dayFlag:" + considerDay);

				if (operator.contains("=") || operator.equals("")) {
					if (dateValueFound.equals(this.dateValueExpected)) {
						logger.info("matched:" + this.getRawParameter() + "=" + this.getRawValue());
						return true;
					}
				}
				if (operator.contains("<")) {
					if (dateValueFound.before(this.dateValueExpected)) {
						logger.info("matched:" + this.getRawParameter() + "=" + this.getRawValue());
						return true;
					}
				}
				if (operator.contains(">")) {
					if (dateValueFound.after(this.dateValueExpected)) {
						logger.info("matched:" + this.getRawParameter() + "=" + this.getRawValue());
						return true;
					}
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
		if (this.getRawValue() == null)
			throw new QueryValueException("rawValue is null");
	}

	public void validateDate() throws QueryValueException {
		try {
			DatatypeFactory.newInstance().newXMLGregorianCalendar(this.dateValue);
		} catch (IllegalArgumentException e) {
			throw new QueryValueException("value is not in XML date format:" + this.dateValue, e);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public String toString() {
		return "QueryDate " + super.toString() + ", operator=" + operator + ", dateValue=" + this.dateValue
				+ "reEncodedValue=" + this.reEncodedValue + "]\n";

	}
}
