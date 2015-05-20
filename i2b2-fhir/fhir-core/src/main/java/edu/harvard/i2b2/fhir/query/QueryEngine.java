package edu.harvard.i2b2.fhir.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

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
	public QueryEngine(String queryUrl, MetaResourceDb db) throws QueryParameterException,
			QueryValueException, FhirCoreException {
		logger.debug("queryUrl:"+queryUrl);
		this.db=db;
		queryList = new ArrayList<Query>();
		this.queryUrl = queryUrl;
		String fhirClassExp = "("
				+ FhirUtil.getResourceList().toString().replace(",", "|")
						.replaceAll("[\\s\\[\\]]+", "") + ")";
		Pattern p = Pattern.compile(fhirClassExp
				+ ";*([^&\\?;]*)\\?([^&\\?]*)$", Pattern.CASE_INSENSITIVE);

		p = Pattern.compile( FhirUtil.RESOURCE_LIST_REGEX+"\\?*([^\\?]*)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(queryUrl);
		if (m.matches()) {
			this.resourceClass = FhirUtil.getResourceClass(m.group(1));
			this.rawQuery = m.group(2);
		}else{
			throw new FhirCoreException("query part of url is not is correct format:"+queryUrl);
		}
		String suffix =null;
		if(m.groupCount()>1) suffix=m.group(2);

		while (suffix.length() > 0) {
			p = Pattern.compile("([^?&]*)&*([^?&]*)");
			m = p.matcher(suffix);
			if (m.matches()) {
				suffix = m.group(1);
			}
			logger.trace("suffix:" + m.group(1));

			Query q = new QueryBuilder(this.resourceClass, suffix,db).build();
			queryList.add(q);
		}
	}

	public QueryEngine(Class c, Map<String, String> queryParamMap,MetaResourceDb db) throws QueryParameterException, QueryValueException, FhirCoreException {
		this.db=db;
		queryList = new ArrayList<Query>();
		
		for (String k1 : queryParamMap.keySet()) {
			if(k1.matches("^_")) continue;
			logger.info("queryParamMap:"+queryParamMap.toString());
			String k=(String) k1;
			if(k==null) continue;
			String v=(String)queryParamMap.get(k);
			Query q = new QueryBuilder(this.resourceClass, k,v,db).build();
			queryList.add(q);
		}
	}

	public List<Resource> search(List<Resource> resList) throws FhirCoreException {
		logger.trace("running query");
		List<Resource> resultList = resList;
		for (Query q : this.queryList) {
			resultList = q.search(resultList);
		}
		return resultList;
	}

	public MetaResourceSet search(MetaResourceSet s) throws FhirCoreException {
		logger.trace("running query");
		logger.debug("size before query:"+s.getMetaResource().size());
		if(this.queryList.size()==0) return s;
		MetaResourceSet resultS = new MetaResourceSet();
		for (Query q : this.queryList) {
			resultS = q.search(s);
		}
		logger.debug("size after query:"+resultS.getMetaResource().size());
		
		return resultS;
	}

	@Override
	public String toString() {
		return "QueryEngine [queryList=" + queryList + ", resourceClass="
				+ resourceClass + ", queryUrl=" + queryUrl + ", rawQuery="
				+ rawQuery + "]\n";
	}

}
