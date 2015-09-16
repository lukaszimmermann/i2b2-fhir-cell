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
 * 		July 4, 2015
 */
package edu.harvard.i2b2.fhir.query;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.hl7.fhir.Resource;
import org.hl7.fhir.ResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class QueryChained extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryChained.class);

	// resource?param:Resource.param:
	// resource?subResource.param

	String className;
	String path;
	String param;
	Class baseResourceClass;
	QueryEngine subQe;
	String subClassName;

	public QueryChained(Class resourceClass, String param, String value)
			throws QueryParameterException, QueryValueException,
			FhirCoreException, QueryException {
		super(resourceClass, param, value);
	}

	@Override
	protected void init() throws QueryValueException, QueryParameterException,
			QueryException {
		this.type = QueryType.CHAINED;
		Pattern p = Pattern
				.compile("^([^\\.]+)\\.*(.*)\\.([^\\.]+)\\.([^\\.]+)$");
		Matcher m = p.matcher(this.getRawParameter());
		if (m.matches()) {
			this.className = m.group(1);
			this.path = m.group(2) + "." + m.group(3);
			this.path = this.path.replaceAll("^\\.", "");
			this.subClassName = m.group(3);
			this.param = m.group(4);
			baseResourceClass = FhirUtil.getResourceClass(this.className);
		} else {
			throw new QueryParameterException(
					"Parameter does not have form ().().()+"
							+ this.getRawParameter());
		}
		try {
			String subUrl = this.subClassName + "?" + this.param + "="
					+ this.getRawValue();
			logger.trace("subUrl:" + subUrl);
			subQe = new QueryEngine(subUrl);
			logger.trace("subQuery:" + subQe);
		} catch (FhirCoreException e) {
			throw new QueryException(e);
		}
	}

	@Override
	public boolean match(String resourceXml, Resource r, MetaResourceSet s)
			throws XQueryUtilException, QueryException, JAXBException {
		if (!this.baseResourceClass.isInstance(r))
			return false;

		String actualValue;
		List<Object> o = null;
		try {
			o = FhirUtil.getChildrenThruChain(r, path, s);
		} catch (NoSuchMethodException e) {
			logger.error("", e);
			throw new QueryException("", e);
		} catch (SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean matchF = false;
		for (Object child : o) {
			
			ResourceReference rr = ResourceReference.class.cast(child);
			Resource r1 = FhirUtil.findResourceById(rr.getReference()
					.getValue(), s);
			try {
				logger.trace(">>>>>>>>>runing:"+r1.getId());
				if (matchF == false
						&& (subQe.search(r1).getMetaResource().size() > 0))
					matchF = true;
			} catch (FhirCoreException | JAXBException e) {
				throw new QueryException(e);
			}
		}
		return matchF;
		/*
		 * if(Resource.class.isInstance(o)){ Resource r1=Resource.class.cast(o);
		 * actualValue=r1.getId(); }else if(String.class.isInstance(o)){
		 * actualValue=String.class.cast(o); }else{
		 * logger.trace("class:"+o.getClass()); throw new
		 * QueryException("Chained query path may be invalid:+"+this.path); }
		 * 
		 * logger.trace("actualValue:"+actualValue); if
		 * (actualValue.equals(this.getRawValue())) {
		 * logger.info("actualValue:"+actualValue+" matched:" +
		 * this.getRawParameter() + "=" + this.getRawValue()); return true; }
		 * 
		 * return false;
		 */
	}

	@Override
	public void validateParameter() throws QueryParameterException {

	}

	@Override
	public void validateValue() throws QueryValueException {

	}

	@Override
	public String toString() {
		return "QueryChained " + super.toString() + ", className=" + className
				+ ", subClassName=" + this.subClassName + ", path=" + path
				+ "]";
	}

}
