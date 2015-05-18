package edu.harvard.i2b2.fhir.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryBuilder {
	static Logger logger = LoggerFactory.getLogger(QueryBuilder.class);

	Class resourceClass;
	String rawParameter;
	String rawValue;
	String queryTypeStr;

	public QueryBuilder() {
	}

	// will aply rules to parameter name and value to identify type of query and
	// create it
	public Query build() throws QueryParameterException, QueryValueException, FhirCoreException {
		String parameter=this.rawParameter.split("\\:")[0];
		SearchParameterTuple t = SearchParameterTupleMap.getTuple(
				this.resourceClass,parameter );

		if (t != null)
			this.queryTypeStr = t.getType();
		Query q = null;

		switch (this.queryTypeStr.toLowerCase()) {
		case "date":
			q = new QueryDate(resourceClass, rawParameter, rawValue);
			logger.info("created query:"+(QueryDate)q);
			break;
		case "token":
			q = new QueryToken(resourceClass, rawParameter, rawValue);
			logger.info("created query:"+(QueryToken)q);
			
			break;
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

}
