package edu.harvard.i2b2.fhir.query;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryToken extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryToken.class);
	String code;
	String namespace;

	public QueryToken(Class resourceClass, String parameter, String value)
			throws QueryParameterException, QueryValueException {
		super(resourceClass, parameter, value);
	}

	@Override
	protected void init() throws QueryValueException, QueryParameterException {
		this.type = QueryType.DATE;

		// [parameter]=[namespace]|[code]
		// [parameter]=|[code]
		Pattern p = Pattern.compile("^(.*)\\|(.*)$");
		Matcher m = p.matcher(value);
		if (m.matches()) {
			this.namespace = m.group(1);
			this.code = m.group(2);
		} else {
			// [parameter]=[code]
			p = Pattern.compile("^(.*)$");
			m = p.matcher(value);
			if (m.matches()) {
				this.namespace = "";
				this.code = m.group(1);
			}
		}
		
		//if()
	}

	@Override
	public boolean match(Resource r) {
		ArrayList<String> list = getValuesAtParameterPath(r, this.getParameterPath());
		for (String v : list) {
			logger.info("matching on:" + v);
			// XX
		}

		return false;
	}

	@Override
	public void validateParameter() throws QueryParameterException {
		// TODO Auto-generated method stub
	}

	@Override
	public void validateValue() throws QueryValueException {

	}

	public String toString() {
		return super.toString() + "\nmodifier=" + this.getModifier() + "\ncode="
				+ this.code + "\nnamespace=" + this.namespace;

	}
}
