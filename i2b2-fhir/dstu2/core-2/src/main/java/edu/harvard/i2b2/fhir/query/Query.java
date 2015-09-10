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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

//parent to types of query, which implement the different search types
public abstract class Query {
	static Logger logger = LoggerFactory.getLogger(Query.class);

	private String rawParameter;
	private String rawValue;
	protected String parameterPath;// this will be resource specific defined at
									// runTime
	private Class resourceClass;
	private String modifier;
	private String parameter;

	QueryType type;

	/*
	 * The raw Parameter and raw Value have protected Function access by
	 * children Child will be init first and then validation will first be at
	 * parent then child level
	 */

	abstract protected void init() throws QueryValueException,
			QueryParameterException, QueryException;

	public Query(Class resourceClass, String rawParameter, String rawValue)
			throws QueryParameterException, QueryValueException, QueryException {
		this.resourceClass = resourceClass;
		this.rawValue = rawValue;
		this.rawParameter = rawParameter;

		Pattern p = Pattern.compile("^([^:]*)[:]*(.*)$");
		Matcher m = p.matcher(rawParameter);
		if (m.matches()) {
			this.parameter = m.group(1);
			this.modifier = m.group(2);
		} else {
			throw new QueryParameterException(
					"Parameter does not match template" + rawParameter);
		}
		initalize(true);
	}

	// to skip construction of queryParam for CUSTOM query
	public Query(Class resourceClass, String rawParameter, String rawValue,Object object) throws QueryParameterException,
			QueryValueException, QueryException {
		rawParameter=rawParameter.substring(1);//to skip #
		this.resourceClass = resourceClass;
		this.rawValue = rawValue;
		this.rawParameter = rawParameter;

		Pattern p = Pattern.compile("^([^:]*)[:]*(.*)$");
		Matcher m = p.matcher(rawParameter);
		if (m.matches()) {
			this.parameter = m.group(1);
			this.modifier = m.group(2);
		} else {
			throw new QueryParameterException(
					"Parameter does not match template" + rawParameter);
		}
		initalize(false);
	}

	// flag indicates if Path will be initialized
	private void initalize(boolean flag) throws QueryParameterException,
			QueryValueException, QueryException {

		try {
			if (flag){
				this.parameterPath = new SearchParameterMap().getParameterPath(
						resourceClass, this.parameter);
			}else{
				this.parameterPath=this.parameter.replace(".", "/");
			}
		} catch (FhirCoreException e) {
			//throw new QueryParameterException("no ParamPath found", e);
		}

		init();
		validate();
		validateParameter();
		validateValue();
	}

	private void validate() throws QueryValueException, QueryParameterException {
		if (this.rawValue == null)
			throw new QueryValueException("rawValue is null");
		if (this.rawParameter == null)
			throw new QueryParameterException("rawParameter is null");
		if (this.parameter == null)
			throw new QueryParameterException("Parameter is null");

		//if (this.parameterPath == null)
			//throw new QueryParameterException("Path not found for parameter "
				//	+ this.parameter + " for " + this.resourceClass);
	}

	/*
	 * provide either resourceXml or resource and MetaResourceSet is optional
	 */
	abstract public boolean match(String resourceXml, Resource r, List<Resource> s) throws  XQueryUtilException, QueryException;

	abstract public void validateParameter() throws QueryParameterException;

	abstract public void validateValue() throws QueryValueException;

	public ArrayList<String> getValuesBelowParameterPath(String xmlResource,
			String parPath) throws XQueryUtilException {
		return getValuesFromParameterPath(xmlResource, parPath, true);
	}

	public ArrayList<String> getValuesAtParameterPath(String xmlResource,
			String parPath) throws XQueryUtilException {
		return getValuesFromParameterPath(xmlResource, parPath, false);
	}

	// perhaps xquery might be faster than Java reflection, as the latter may
	// involve making call for
	// elements that may not be initialized

	private ArrayList<String> getValuesFromParameterPath(String xmlResource,
			String parPath, boolean explodeF) throws XQueryUtilException {
		ArrayList<String> list = new ArrayList<String>();

		String xqueryStr = FhirUtil.namespaceDeclaration + "/"
				+ parPath.replace(".", "/")// "/Patient/birthDate+"
				+ (explodeF ? "/" : "") + "/@value/string()";
		logger.trace("xqueryStr:" + xqueryStr);
		list = XQueryUtil.getStringSequence(xqueryStr, xmlResource);

		logger.trace("list:" + list.toString());
		return list;
	}

	protected ArrayList<String> getListFromParameterPath(Resource r,
			String parPath) throws JAXBException, XQueryUtilException {
		return getListFromParameterPath(JAXBUtil.toXml(r), parPath);
	}

	protected ArrayList<String> getListFromParameterPath(String xml,
			String parPath) throws XQueryUtilException {
		ArrayList<String> list = new ArrayList<String>();
		String xqueryStr = FhirUtil.namespaceDeclaration
		// + getAugmentedParameterPath()
				+ parPath;

		logger.trace("xqueryStr:" + xqueryStr);
		list = XQueryUtil.getStringSequence(xqueryStr, xml);

		logger.trace("list:" + list.toString());
		return list;
	}

	protected String getXmlFromParameterPath(String xml, String parPath) throws XQueryUtilException {
		String xqueryStr = FhirUtil.namespaceDeclaration
		// + getAugmentedParameterPath()
				+ parPath;// "/Patient/gender;
		logger.trace(" XmlFromParamPath xquery:" + xqueryStr);

		String msg = XQueryUtil.processXQuery(xqueryStr, xml);

		logger.trace("msg:" + msg.toString());
		return msg;
	}

	protected List<String> getXmlListFromParameterPath(String xml,
			String parPath) throws XQueryUtilException {
		String xqueryStr = FhirUtil.namespaceDeclaration
				+ getParameterPath();// "/Patient/gender;
		logger.trace("xqueryStr:" + xqueryStr);

		ArrayList<String> msg = XQueryUtil.getStringSequence(xqueryStr, xml);

		logger.trace("msg:" + msg.toString());
		return msg;
	}

	protected String getRawValue() {
		return rawValue;
	}

	protected String getRawParameter() {
		return rawParameter;
	}

	protected String getParameterPath() {
		return parameterPath;
	}

	/*
	 * allows xml queries even on i2b2.resource
	 */
	

	protected String getParameter() {
		return parameter;
	}

	protected Class getResourceClass() {
		return resourceClass;
	}

	protected String getModifier() {
		return modifier;
	}

	@Override
	public String toString() {
		return " [rawParameter=" + rawParameter + ", rawValue=" + rawValue
				+ ", parameterPath=" + parameterPath + ", resourceClass="
				+ resourceClass + ", modifier=" + modifier + ", parameter="
				+ parameter + ", type=" + type + ",";
	}

	protected String getLastElementOfParameterPath() {
		String s = this.getParameterPath();
		Pattern p = Pattern.compile(".*//*([^//]+)$");
		Matcher m = p.matcher(s);
		if (m.matches())
			s = m.group(1);
		return s;
	}

	protected String getFirstElementOfParameterPath() {
		String s = this.getParameterPath();
		Pattern p = Pattern.compile("^([^//]+)//*.*");
		Matcher m = p.matcher(s);
		if (m.matches())
			s = m.group(1);
		return s;
	}

	// CodeableConcept.text, Coding.display, or Identifier.label
	//should be moved to QueryString, and inherited in QuesryCustom.
		protected boolean textSearch(String xml,String searchText) throws XQueryUtilException {

			ArrayList<String> list = new ArrayList<String>();
			list.addAll(getListFromParameterPath(xml, "//@value/string()"));

			logger.trace("list:" + list.toString());
			for (String v : list) {
				if (this.getModifier().equals("exact")) {
					if (v.equals(searchText)) {
						logger.info("matched:"+ this.getRawParameter()+"="+this.getRawValue());
						return true;
					}
				} else {
					v = v.toLowerCase().replaceAll("\\s+", " ")
							.replaceAll("^\\s", "").replaceAll("\\s$", "");
					if (v.contains(searchText)) {
						logger.info("matched:"+ this.getRawParameter()+"="+this.getRawValue());
						return true;
					}
				}
			}
			return false;
		}
}
