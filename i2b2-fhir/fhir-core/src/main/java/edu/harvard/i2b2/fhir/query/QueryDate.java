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

public class QueryDate extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryDate.class);
	String operator;
	String dateValue;
	GregorianCalendar dateValueExpected;

	public QueryDate(Class resourceClass, String parameter, String value)
			throws QueryParameterException, QueryValueException {
		super(resourceClass, parameter, value);
		
	}
	
	protected void init() throws QueryValueException, QueryParameterException{
		this.type = QueryType.DATE;
		Pattern p = Pattern.compile("^([<=>]*)[^\\s]*");
		Matcher m = p.matcher(this.getRawValue());
		this.operator = m.matches() ? m.group(1) : "";
		logger.info("operator:" + this.operator);
		this.dateValue = (this.operator.length() > 0) ? this.getRawValue()
				.substring(this.operator.length()) : this.getRawValue();
				
		try {
			validateDate();
			this.dateValueExpected = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(this.dateValue)
					.toGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean match(Resource r) {
		ArrayList<String> list = getValuesAtParameterPath(r,
				this.getParameterPath());
		for (String v : list) {
			GregorianCalendar dateValueFound;
			logger.info("matching on:"+v);
			
			try {
				dateValueFound = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(v).toGregorianCalendar();
			} catch (DatatypeConfigurationException e) {
				throw new RuntimeException(e);
			}
			if (operator.contains("=")) {
				if (dateValueFound.equals(this.dateValueExpected)){
					logger.info("matched");
					return true;
				}
			}else if (operator.contains("<")) {
				if (dateValueFound.before(this.dateValueExpected)){
					logger.info("matched");
					return true;
				}
			}else if (operator.contains(">")) {
				if (dateValueFound.after(this.dateValueExpected)){
					logger.info("matched");
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void validateParameter() throws QueryParameterException {
		// TODO Auto-generated method stub
	}

	
	@Override
	public void validateValue() throws QueryValueException {
		if(this.getRawValue()==null) throw new QueryValueException("rawValue is null");
	}

	
	public void validateDate() throws QueryValueException {
		try {
			DatatypeFactory.newInstance().newXMLGregorianCalendar(
					this.dateValue);
		} catch (IllegalArgumentException e) {
			throw new QueryValueException("value is not in XML date format:"
					+ this.dateValue, e);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public String toString() {
		return super.toString() + "\noperator=" + operator + "\ndateValue="
				+ this.dateValue;

	}
}
