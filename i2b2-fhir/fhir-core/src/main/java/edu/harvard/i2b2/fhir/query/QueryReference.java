package edu.harvard.i2b2.fhir.query;

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
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class QueryReference extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryReference.class);
	String param;
	String url;

	public QueryReference(Class resourceClass, String param, String value)
			throws QueryParameterException, QueryValueException,
			FhirCoreException {
		super(resourceClass, param, value);
	}

	@Override
	protected void init() throws QueryValueException, QueryParameterException {
		this.type = QueryType.REFERENCE;
		// [param]=[url]
		// [param]=[id]
		if (this.getRawValue().contains("/")) {
			this.url = this.getRawValue();
		} else {
			this.param = this.getRawValue();
		}
	}

	@Override
	public boolean match(String resourceXml,Resource r, MetaResourceSet s) throws XQueryUtilException {
		String rawId;
			rawId = getXmlFromParameterPath(resourceXml,  getAugmentedParameterPath()+"/reference/@value/string()");
		
		// id=this.getLastElementOfparamPath()+"/"+id;
		Pattern p = Pattern.compile(".*/([^/]+)$");
		Matcher m = p.matcher(rawId);
		param="-";
		if (m.matches()) {
			param = m.group(1);
			logger.trace("id is:"+param);
		}
		// logger.info("matching "+id+" to value:"+this.getRawValue());
		if (param.equals(this.getRawValue())) {
			logger.info("param:"+param+" matched:" + this.getRawParameter() + "="
					+ this.getRawValue());
			return true;
		}

		return false;
	}

	

	@Override
	public void validateParameter() throws QueryParameterException {
		if ((this.getModifier().length() > 0)
				& (!(this.getModifier().equals("text")))) {
			throw new QueryParameterException("undefined modifier <"
					+ this.getModifier() + "> for Query of type token");
		}
	}

	@Override
	public void validateValue() throws QueryValueException {

	}

	@Override
	public String toString() {
		return "QueryReference " + super.toString() + ", param=" + param + ", url="
				+ url + "]";
	}

}
