package edu.harvard.i2b2.fhir.query;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.hl7.fhir.Resource;
import org.hl7.fhir.ResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class QueryChained extends Query {
	static Logger logger = LoggerFactory.getLogger(QueryChained.class);
	
	String className;
	String path;
	Class baseResourceClass;

	public QueryChained(Class resourceClass, String param, String value)
			throws QueryParameterException, QueryValueException,
			FhirCoreException {
		super(resourceClass, param, value);
	}

	@Override
	protected void init() throws QueryValueException, QueryParameterException {
		this.type = QueryType.CHAINED;
		Pattern p=Pattern.compile("([^\\s]+)\\.(.*)");
		Matcher m=p.matcher(this.getRawParameter());
		if(m.matches()){
			this.className=m.group(1);
			this.path = m.group(2);
			baseResourceClass=FhirUtil.getResourceClass(this.className);
		}else throw new QueryParameterException("Parameter does not have form ().()+"+this.getRawParameter());;
	}

	@Override
	public boolean match(String resourceXml,Resource r, MetaResourceSet s) throws XQueryUtilException, QueryException {
		if(!this.baseResourceClass.isInstance(r)) return false;
		
		String actualValue;
		Object o=null;
		try{
		 o=FhirUtil.getChildThruChain(r, path,s);
		}catch(NoSuchMethodException e){
			logger.error("",e);
			throw new QueryException("",e);
		} catch (SecurityException |IllegalAccessException |IllegalArgumentException |InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(Resource.class.isInstance(o)){
			Resource r1=Resource.class.cast(o);
			actualValue=r1.getId();
		}else if(String.class.isInstance(o)){
			actualValue=String.class.cast(o);
		}else{
			logger.trace("class:"+o.getClass());
			throw new QueryException("Chained query path may be invalid:+"+this.path);
		}
		
		if (actualValue.equals(this.getRawValue())) {
			logger.info("actualValue:"+actualValue+" matched:" + this.getRawParameter() + "="
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
		return "QueryChained " + super.toString() + ", className"+className+", path=" + path +"]";
	}

}
