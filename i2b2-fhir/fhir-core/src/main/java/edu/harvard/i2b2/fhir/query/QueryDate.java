package edu.harvard.i2b2.fhir.query;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.hl7.fhir.Resource;

public class QueryDate extends Query {

	public QueryDate(Class resourceClass,String parameter, String value)
			throws QueryParameterException, QueryValueException {
		super(resourceClass,parameter, value);
		this.type = QueryType.DATE;
	}

	
	
	
	@Override
	public boolean match(Resource r) {
		return false;
	}

	
	@Override
	public void validateParameter() throws QueryParameterException {
		// TODO Auto-generated method stub
	}

	@Override
	public void validateValue() throws QueryValueException {
		try {
			DatatypeFactory.newInstance().newXMLGregorianCalendar(this.value);
		} catch (IllegalArgumentException e) {
			throw new QueryValueException("value is not in XML date format:" + this.value, e);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

}
