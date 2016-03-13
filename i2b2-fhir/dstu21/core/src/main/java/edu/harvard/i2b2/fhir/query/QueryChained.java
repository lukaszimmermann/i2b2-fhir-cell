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
import org.hl7.fhir.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryChained extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryChained.class);

	// resource?param:Resource.param:
	// resource?subResource.param

	String className;
	String path;
	String param;
	Class baseResourceClass;
	QueryEngine subQe;
	String subUrl;
	List<Object> chainObjList;

	public QueryChained(Class resourceClass, String param, String value)
			throws QueryParameterException, QueryValueException, FhirCoreException, QueryException {
		super(resourceClass, param, value);
	}

	@Override
	protected void init() throws QueryValueException, QueryParameterException, QueryException {
		this.type = QueryType.CHAINED;

	}

	@Override
	public boolean match(String resourceXml, Resource r, List<Resource> s, MetaResourceDb db)
			throws XQueryUtilException, QueryException {
		if (!this.getResourceClass().isInstance(r))
			return false;
		logger.trace("id:" + r.getId().getValue());// +"\nthis.baseResourceClass:"+this.baseResourceClass.getSimpleName());
		List<Resource> allResInDb = db.getAll();

		Pattern p = Pattern
				// compile("^([^\\.]+)(:[^\\.]+)*\\.([^\\.]+)$");
				.compile("([^\\.]+)\\.([^\\.]+)$");
		// subject:Patient.birthDate
		Matcher m = p.matcher(this.getRawParameter());
		if (m.matches()) {
			String chainElemName = m.group(1);
			subUrl = m.group(2);

			try {
				logger.trace("chainElemName:" + chainElemName + ", subUrl:" + subUrl + ", rawParam:"
						+ this.getRawParameter());
				chainObjList = FhirUtil.getChildrenThruChain(r, chainElemName, allResInDb);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				logger.error(e.getMessage(), e);
				throw new XQueryUtilException(e);
			}

		}

		boolean matchF = false;
		for (Object child : chainObjList) {
			logger.trace("child:" + child.toString());
			Reference rr = Reference.class.cast(child);
			logger.trace(">>>>searching for :" + rr.getReference().getValue());
			String chainClassName = rr.getReference().getValue().split("/")[0];
			String chainClassId = rr.getReference().getValue().split("/")[1];
			Class chainClass = FhirUtil.getResourceClass(chainClassName);

			Resource r1 = db.getParticularResource(chainClass, chainClassId);
			logger.trace(">>>>got id :" + r1.getId().getValue());
			try {
				subQe = new QueryEngine(chainClassName + "?" + subUrl + "=" + this.getRawValue());
				if (matchF == false && (subQe.search(r1, db).size() > 0)) {
					matchF = true;
					continue;
				}
			} catch (QueryParameterException | QueryValueException | FhirCoreException | JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return matchF;

	}

	@Override
	public void validateParameter() throws QueryParameterException {

	}

	@Override
	public void validateValue() throws QueryValueException {

	}

	@Override
	public String toString() {
		return "QueryChained " + super.toString() + ", className=" + className + ", path=" + path + "]";
	}

}
