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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

/*
 * Creates QueryList from String and
 * executes the Query List on resourceList
 */
public class QueryEngine {
	static Logger logger = LoggerFactory.getLogger(QueryEngine.class);
	List<Query> queryList;
	private Class resourceClass;
	private String queryUrl;
	private String rawQuery;
	private MetaResourceDb db;

	// url format:[resource][;jession=123]?[par1=val1]&[par2=val2]
	public QueryEngine(String queryUrl) throws QueryParameterException,
			QueryValueException, FhirCoreException {
		try {
			queryUrl = URLDecoder.decode(queryUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new FhirCoreException("Error", e);
		}
		logger.debug("queryUrl:" + queryUrl);
		this.db = db;
		queryList = new ArrayList<Query>();
		this.queryUrl = queryUrl;
		String fhirClassExp = "("
				+ FhirUtil.getResourceList().toString().replace(",", "|")
						.replaceAll("[\\s\\[\\]]+", "") + ")";
		Pattern p = Pattern.compile(fhirClassExp
				+ ";*([^&\\?;]*)\\?([^&\\?]*)$", Pattern.CASE_INSENSITIVE);

		p = Pattern.compile(FhirUtil.RESOURCE_LIST_REGEX + "\\?*([^\\?]*)",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(queryUrl);
		if (m.matches()) {
			this.resourceClass = FhirUtil.getResourceClass(m.group(1));
			this.rawQuery = m.group(2);
		} else {
			throw new FhirCoreException(
					"query part of url is not is correct format:" + queryUrl);
		}
		String suffix = this.rawQuery;

		while (suffix != null && suffix.length() > 0) {
			p = Pattern.compile("([^\\?&]+)([^\\?]*)");
			logger.trace("processing:" + suffix);
			m = p.matcher(suffix);
			if (m.matches()) {
				String prefix = m.group(1);
				suffix = m.group(2);
				if(suffix.length()>0) suffix=suffix.substring(1);//to drop preceding&
				if (!prefix.matches("^_id.*") && prefix.matches("^_.*")) {
					logger.info("excluding paramerters begining with _: except _id:"
							+ prefix);
				} else {
					Query q=null ;
					try{
						logger.trace("creating:" + prefix);
					q = new QueryBuilder(this.resourceClass, prefix)
							.build();
					}catch(Exception e){
						logger.error("",e);
						throw new FhirCoreException("Error in search param :"+prefix,e);
					}
					
					queryList.add(q);
				}
			} else {
				suffix = null;
			}
		}
	}

	public QueryEngine(Class c, Map<String, String> queryParamMap,
			MetaResourceDb db) throws QueryParameterException,
			QueryValueException, FhirCoreException, QueryException {
		this.db = db;
		queryList = new ArrayList<Query>();

		for (String k1 : queryParamMap.keySet()) {
			if (k1.matches("^_")) {
				continue;
			}
			logger.info("queryParamMap:" + queryParamMap.toString());
			String k = (String) k1;
			if (k == null)
				continue;
			String v = (String) queryParamMap.get(k);
			Query q = new QueryBuilder(this.resourceClass, k, v).build();
			queryList.add(q);
		}
	}

	public List<Resource> search(List<Resource> s) throws FhirCoreException,
			JAXBException, XQueryUtilException, QueryException {
		List<Resource> resultS = new ArrayList<Resource>();
		logger.trace("running query");
		logger.debug("size before query:" + s.size());
		String inputMRSXml;
		/*try {
			inputMRSXml = FhirUtil.toXml(s);
			// logger.trace("inputMRSXml:"+inputMRSXml);
		} catch (JAXBException e) {
			throw new FhirCoreException("JAXB error ", e);
		}*/            
		if (this.queryList.size() == 0) {
			logger.trace("returning from sophisQuery as queryList is empty");
			return s;
		}
		for (Resource r : s){

				if (r == null)
					throw new FhirCoreException("Resource is Null:");
			
			if (r.getId() == null)
				throw new FhirCoreException("Id is not mentioned in resource:"
						+ JAXBUtil.toXml(r));

			String resourceXml =
					// FhirUtil.getResourceXml(r.getId(), inputMRSXml);
					JAXBUtil.toXml(r);
			boolean matchF = true;
			for (Query q : this.queryList) {
				
				//if match fails on a query skip other queries
				if (matchF == true && (q.match(resourceXml,r,s)==false)){
						matchF = false;
						
				}
			}
			if(this.queryList.size()==0) matchF=false;
			//if match is true on all queries include in result
			logger.info("match res:"+matchF);
			if (matchF == true){resultS.add(r);}
		}
		logger.debug("size after query:" + resultS.size());

		return resultS;
	}

	@Override
	public String toString() {
		return "QueryEngine [queryList=" + queryList + ", resourceClass="
				+ resourceClass + ", queryUrl=" + queryUrl + ", rawQuery="
				+ rawQuery + "]\n";
	}

	public List<Resource> search(Resource r1) throws FhirCoreException,
			JAXBException, XQueryUtilException, QueryException {
		List<Resource> s1 = new ArrayList<Resource>();
			logger.trace("running subquery");
			s1.add(r1);
			return search(s1);
	}

}
