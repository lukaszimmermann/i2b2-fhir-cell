package edu.harvard.i2b2.fhir.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.XQueryUtil;

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
		Matcher m = p.matcher(this.getRawValue());
		if (m.matches()) {
			this.namespace = m.group(1);
			this.code = m.group(2);
		} else {
			// [parameter]=[code]
			p = Pattern.compile("^(.*)$");

			m = p.matcher(this.getRawValue());
			if (m.matches()) {
				this.namespace = "";
				this.code = m.group(1);
			} else {
				throw new QueryValueException(
						"query rawValue does not match TOKEN template:"
								+ this.getRawValue());
			}
		}

	}

	@Override
	public boolean match(Resource r) {
		ArrayList<String> typeList;
		typeList = new ArrayList<String>(Arrays.asList("/coding",
				"/CodeableConcept", "/Identifier"));
		for (String type : typeList) {
			String xml = getXmlFromParameterPath(r, this.getParameterPath()
					.replace(".", "/") + type);
			logger.trace("xml:" + xml);
			if (xml.equals(""))	continue;
			String resultSystem = getXmlFromParameterPath(xml,
					"//system/@value/string()");
			logger.trace("resultSystem:" + resultSystem);

			// consider namespace
			if (this.namespace.length() > 0
					&& (!resultSystem.equals(namespace)))
				return false;

			if (textSearch(xml))
				return true;
		}

		return false;
	}

	// CodeableConcept.text, Coding.display, or Identifier.label
	private boolean textSearch(String xml) {
		ArrayList<String> pathExtList;
		// consider "text" modifier
		if (this.getModifier().equals("text")) {
			pathExtList = new ArrayList<String>(Arrays.asList(
					"/coding/(code|display)/@value/string()",
					"/CodeableConcept/(code|text)/@value/string()",
					"/Identifier/(value|label)/@value/string()"));
		} else {
			pathExtList = new ArrayList<String>(Arrays.asList(
					"/coding/code/@value/string()",
					"/CodeableConcept/code/@value/string()",
					"/Identifier/value/@value/string()"));
		}
		ArrayList<String> list = new ArrayList<String>();
		for (String ext : pathExtList) {
			list.addAll(getListFromParameterPath(xml, ext));
		}

		logger.trace("list:" + list.toString());
		for (String v : list) {
			if (v.equals(code)) {
				logger.info("matched");
				return true;
			}
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

	public String toString() {
		return super.toString() + "\ncode=" + this.code + "\nnamespace="
				+ this.namespace;

	}
}
