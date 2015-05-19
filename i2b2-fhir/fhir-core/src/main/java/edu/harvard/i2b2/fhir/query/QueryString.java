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

import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class QueryString extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryString.class);
	String searchText;

	public QueryString(Class resourceClass, String parameter, String value)
			throws QueryParameterException, QueryValueException,
			FhirCoreException {
		super(resourceClass, parameter, value);
	}

	@Override
	protected void init() throws QueryValueException, QueryParameterException {
		this.type = QueryType.TOKEN;
		logger.trace(">>>MOD:" + this.getModifier() + "--rawVal:"
				+ this.getRawValue());
		if (this.getModifier().equals("exact"))
			searchText = this.getRawValue();
		else {
			searchText = this.getRawValue().toLowerCase()
					.replaceAll("\\s+", " ").replaceAll("^\\s", "")
					.replaceAll("\\s$", "");

		}
	}

	@Override
	public boolean match(Resource r) {

		List<String> xmlList = getXmlListFromParameterPath(r, this
				.getParameterPath().replace(".", "/"));
		for (String xml : xmlList) {
			logger.trace("xml:" + xml);
			if (xml.equals(""))
				continue;

			if (textSearch(xml))
				return true;
		}
		return false;
	}

	// CodeableConcept.text, Coding.display, or Identifier.label
	private boolean textSearch(String xml) {

		ArrayList<String> list = new ArrayList<String>();
		list.addAll(getListFromParameterPath(xml, "//@value/string()"));

		logger.trace("list:" + list.toString());
		for (String v : list) {
			if (this.getModifier().equals("exact")) {
				if (v.equals(this.searchText)) {
					logger.info("matched:"+ this.getRawParameter()+"="+this.getRawValue());
					return true;
				}
			} else {
				v = v.toLowerCase().replaceAll("\\s+", " ")
						.replaceAll("^\\s", "").replaceAll("\\s$", "");
				if (v.contains(this.searchText)) {
					logger.info("matched:"+ this.getRawParameter()+"="+this.getRawValue());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void validateParameter() throws QueryParameterException {
		if ((this.getModifier().length() > 0)
				& (!(this.getModifier().equals("exact")))) {
			throw new QueryParameterException("undefined modifier <"
					+ this.getModifier() + "> for Query of type token");
		}
	}

	@Override
	public void validateValue() throws QueryValueException {

	}

	@Override
	public String toString() {
		return "QueryString "+super.toString()+ "searchText=" + searchText + "]\n";
	}
}
