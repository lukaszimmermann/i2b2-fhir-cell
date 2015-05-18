package edu.harvard.i2b2.fhir.query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryBuilder {
	static Logger logger = LoggerFactory.getLogger(QueryBuilder.class);

	Class resourceClass;
	String rawParameter;
	String rawValue;
	String queryTypeStr;

	public QueryBuilder() {
	}
	
	public QueryBuilder(String url) {
		Pattern  p=Pattern.compile(  "(["+FhirUtil.RESOURCE_LIST+"]);*([^&\\?;]*)\\?([^=&\\?]*)=([^=&\\?]*)", Pattern.CASE_INSENSITIVE);
		Matcher m= p.matcher(url);
		if(m.matches()){
			this.resourceClass=FhirUtil.getResourceClass(m.group(1));
			this.rawParameter=m.group(3);
			this.rawValue=m.group(4);
		}
		
		logger.trace(""+this.toString()+FhirUtil.RESOURCE_LIST);
	}
	
	public QueryBuilder(Class resourceClass,String url) {
		Pattern  p=Pattern.compile( "([^=&\\?]*)=([^=&\\?]*)");
		Matcher m= p.matcher(url);
		if(m.matches()){
			this.rawParameter=m.group(2);
			this.rawValue=m.group(3);
		}
		
		logger.trace(""+this.toString());
	}

	// will aply rules to parameter name and value to identify type of query and
	// create it
	public Query build() throws QueryParameterException, QueryValueException,
			FhirCoreException {
		String parameter = this.rawParameter.split("\\:")[0];

		Query q = null;

		try {
			this.queryTypeStr = new SearchParameterMap().getType(
					this.getResourceClass(), this.rawParameter.split(":")[0]);
		} catch (FhirCoreException e) {
			throw new QueryParameterException("no ParamPath found.Query could not be built for "+
					this.toString(), e);
		}

		switch (this.queryTypeStr.toLowerCase()) {
		case "date":
			q = new QueryDate(resourceClass, rawParameter, rawValue);
			logger.info("created query:" + (QueryDate) q);
			break;
		case "token":
			q = new QueryToken(resourceClass, rawParameter, rawValue);
			logger.info("created query:" + (QueryToken) q);

			break;
		case "string":
			q = new QueryString(resourceClass, rawParameter, rawValue);
			logger.info("created query:" + (QueryString) q);

			break;
		default:
			throw new FhirCoreException(
					"Query could not be built for:"+ this.toString());
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

	@Override
	public String toString() {
		return "QueryBuilder [resourceClass=" + resourceClass
				+ ", rawParameter=" + rawParameter + ", rawValue=" + rawValue
				+ ", queryTypeStr=" + queryTypeStr + "]";
	}
	
	
}
