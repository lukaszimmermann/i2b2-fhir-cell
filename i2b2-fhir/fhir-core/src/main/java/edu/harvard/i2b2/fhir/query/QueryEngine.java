package edu.harvard.i2b2.fhir.query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
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

	// url format:[resource][;jession=123]?[par1=val1]&[par2=val2]
	QueryEngine(String queryUrl) throws QueryParameterException, QueryValueException, FhirCoreException {
		queryList=new ArrayList<Query>();
		this.queryUrl = queryUrl;
		String fhirClassExp = "("
				+ FhirUtil.getResourceList().toString().replace(",", "|")
						.replaceAll("[\\s\\[\\]]+", "") + ")";
		Pattern p = Pattern.compile(fhirClassExp
				+ ";*([^&\\?;]*)\\?([^&\\?]*)$", Pattern.CASE_INSENSITIVE);

		p = Pattern.compile("(patient)\\?*([^?]*)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(queryUrl);
		if (m.matches()) {
			this.resourceClass = FhirUtil.getResourceClass(m.group(1));
			this.rawQuery = m.group(2);
		}
		String suffix = m.group(2);

		while(suffix.length()>0) {
			p = Pattern.compile("([^?&]*)&*([^?&]*)");
			m = p.matcher(suffix);
			if (m.matches()) {
				suffix = m.group(2);
			}
			logger.trace("prefix:"+m.group(1)+"\nsuffix:"+m.group(2));
			
			Query q=new QueryBuilder(this.resourceClass,m.group(1)).build();
			queryList.add(q);
		}
	}

	public List<Resource> executeQuery(List<Resource> resList) {
		List<Resource> resultList = new ArrayList<Resource>();

		return resultList;
	}

	@Override
	public String toString() {
		return "QueryEngine [queryList=" + queryList + ", resourceClass="
				+ resourceClass + ", queryUrl=" + queryUrl + ", rawQuery="
				+ rawQuery + "]\n";
	}

}
