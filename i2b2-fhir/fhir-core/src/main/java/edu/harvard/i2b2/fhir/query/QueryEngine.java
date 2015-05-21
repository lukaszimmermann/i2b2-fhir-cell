package edu.harvard.i2b2.fhir.query;

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
	public QueryEngine(String queryUrl)
			throws QueryParameterException, QueryValueException,
			FhirCoreException {
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
		String suffix = null;
		if (m.groupCount() > 1)
			suffix = m.group(2);

		while (suffix.length()>0) {
			p = Pattern.compile("([^?&]*)&*([^?&]*)");
			m = p.matcher(suffix);
			if (m.matches()) {
				String prefix = m.group(1);
				suffix = m.group(2);
				Query q = new QueryBuilder(this.resourceClass, prefix).build();
				queryList.add(q);
			}else{
				suffix=null;
			}
			logger.trace("suffix:" + m.group(2));
		}
	}

	public QueryEngine(Class c, Map<String, String> queryParamMap,
			MetaResourceDb db) throws QueryParameterException,
			QueryValueException, FhirCoreException {
		this.db = db;
		queryList = new ArrayList<Query>();

		for (String k1 : queryParamMap.keySet()) {
			if (k1.matches("^_"))
				continue;
			logger.info("queryParamMap:" + queryParamMap.toString());
			String k = (String) k1;
			if (k == null)
				continue;
			String v = (String) queryParamMap.get(k);
			Query q = new QueryBuilder(this.resourceClass, k, v).build();
			queryList.add(q);
		}
	}

	public MetaResourceSet search(MetaResourceSet s) throws FhirCoreException {
		MetaResourceSet resultS = new MetaResourceSet();
		logger.trace("running query");
		logger.debug("size before query:" + s.getMetaResource().size());
		String inputMRSXml;
		try {
			inputMRSXml = FhirUtil.getMetaResourceSetXml(s);
			//logger.trace("inputMRSXml:"+inputMRSXml);
		} catch (JAXBException e) {
			throw new FhirCoreException("JAXB error ", e);
		}
		if (this.queryList.size() == 0)
			return s;
		
		for (MetaResource mr : s.getMetaResource()) {
			Resource r = mr.getResource();
			
			
				try {
					if(r==null)
					throw new FhirCoreException("Resource is Null:"+FhirUtil.metaResourceToXml(mr));
				} catch (JAXBException e) {
					throw new FhirCoreException("JaxB Error:",e);
				}
			
			if(r.getId()==null)
				throw new FhirCoreException("Id is not given in resource:"+FhirUtil.resourceToXml(r));
			
			String resourceXml = FhirUtil
					.getResourceXml(r.getId(), inputMRSXml);
			for (Query q : this.queryList) {
				if (q.match(resourceXml))
					resultS.getMetaResource().add(mr);
			}
		}
		logger.debug("size after query:" + resultS.getMetaResource().size());

		return resultS;
	}

	@Override
	public String toString() {
		return "QueryEngine [queryList=" + queryList + ", resourceClass="
				+ resourceClass + ", queryUrl=" + queryUrl + ", rawQuery="
				+ rawQuery + "]\n";
	}

}
