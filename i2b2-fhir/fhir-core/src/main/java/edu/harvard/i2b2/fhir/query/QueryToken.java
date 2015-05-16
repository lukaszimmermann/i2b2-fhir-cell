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
		String xml;
		if (this.namespace.equals("")) {
			xml = getXmlFromParameterPath(r, "/coding/code/@value/string()");
			logger.info("xml:" + xml);
			if (xml.equals(code)) {
				logger.info("matched");
				return true;
			}
			// if modifier is text
			if(textSearch(r)) return true;
		

		} else {
			xml = getXmlFromParameterPath(r, "/coding");
			logger.trace("xml:" + xml);
			String xqSystem = "declare default element namespace \"http://hl7.org/fhir\";"
					+ "/coding/system/@value/string()";
			String resultSystem = XQueryUtil.processXQuery(xqSystem, xml);
			String xqCode = "declare default element namespace \"http://hl7.org/fhir\";"
					+ "/coding/code/@value/string()";
			String resultCode = XQueryUtil.processXQuery(xqCode, xml);

			// logger.trace("resultCode:"+resultCode);
			// logger.trace("resultSystem:"+resultSystem);
			if ( resultSystem.equals(namespace)) {
				if(resultCode.equals(code)){
					logger.info("matched");
					return true;
				}
				// if modifier is text
				if(textSearch(r)) return true;
			}
		}


		return false;

	}

	// CodeableConcept.text, Coding.display, or Identifier.label
	private boolean textSearch(Resource r) {
		if (this.getModifier().equals("text")) {
			ArrayList<String> list = this.getListFromParameterPath(r, this.getParameterPath()+"/coding/(code|system|display)/@value/string()");
			list.addAll(getListFromParameterPath(r, this.getParameterPath()+"/CodeableConcept/text/@value/string()"));
			list.addAll(getListFromParameterPath(r, this.getParameterPath()+"/Indentifier/label/@value/string()"));
			
			logger.trace("list:"+list.toString());
			for(String v:list){
				if(v.equals(code)){
					logger.info("matched");
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void validateParameter() throws QueryParameterException {
		if((this.getModifier().length()>0) & 
				(!(this.getModifier().equals("text")))
				){
				throw new QueryParameterException("undefined modifier <"+this.getModifier()+"> for Query of type token");
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
