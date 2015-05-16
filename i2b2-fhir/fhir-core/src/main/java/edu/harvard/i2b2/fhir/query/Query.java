package edu.harvard.i2b2.fhir.query;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

//parent to types of query, which implement the different search types
public abstract class Query {
	static Logger logger = LoggerFactory.getLogger(Query.class);

	private String rawParameter;
	private String rawValue;
	private String parameterPath;// this will be resource specific defined at runTime
	private Class resourceClass;
	private String modifier;
	private String parameter;
	
	String value;
	QueryType type;

	/*
	 * The raw Parameter and raw Value have protected Function access by children
	 * Child will be init first and then validation will first be at parent then
	 * child level
	 */
	private Query() {
	}

	abstract protected void init() throws QueryValueException,
			QueryParameterException;

	public Query(Class resourceClass, String rawParameter, String rawValue)
			throws QueryParameterException, QueryValueException {
		this.resourceClass = resourceClass;
		this.rawValue = rawValue;
		this.rawParameter = rawValue;

		Pattern p = Pattern.compile("^(.*)[:]*(.*)$");
		Matcher m = p.matcher(rawParameter);
		if (m.matches()) {
			this.parameter = m.group(1);
			this.modifier = m.group(2);
		}

		this.value = rawValue;

		SearchParameterTuple t = SearchParameterTupleMap.getTuple(
				this.resourceClass, this.parameter);
		if (t != null)
			this.parameterPath = t.getPath();
		init();
		validate();
		validateParameter();
		validateValue();
	}

	private void validate() throws QueryValueException, QueryParameterException {
		if (this.rawValue == null)
			throw new QueryValueException("rawValue is null");
		if (this.rawParameter == null)
			throw new QueryParameterException("rawParameter is null");
		if (this.value == null)
			throw new QueryValueException("Value is null");
		if (this.parameter == null)
			throw new QueryParameterException("Parameter is null");

		if (this.parameterPath == null)
			throw new QueryParameterException(
					"rawParameter Path not found for " + this.rawParameter
							+ " for " + this.resourceClass);
	}

	abstract public boolean match(Resource r);

	final public boolean match(MetaResource mr) {
		return match(mr.getResource());

	}

	final public void removeNonMatching(MetaResourceSet s) {
		MetaResourceSet s2 = new MetaResourceSet();
		ArrayList<MetaResource> list = (ArrayList<MetaResource>) s2
				.getMetaResource();
		ArrayList<MetaResource> slist = (ArrayList<MetaResource>) s
				.getMetaResource();
		for (MetaResource mr : slist) {
			if (match(mr))
				list.add(mr);
		}
		slist.clear();
		slist.addAll(list);
	}

	abstract public void validateParameter() throws QueryParameterException;

	abstract public void validateValue() throws QueryValueException;

	public String toString() {
		return "resourceClass=" + this.resourceClass.getCanonicalName()
				+ "\nrawparameter=" + this.rawParameter + "\nparameter="
				+ this.parameter + "\nrawValue=" + this.rawValue + "\nvalue="
				+ this.value + "\ntype=" + this.type + "\nparameterPath"
				+ this.parameterPath + "\nmodifier" + this.modifier;
	}

	public ArrayList<String> getValuesBelowParameterPath(Resource r,
			String parPath) {
		return getValuesFromParameterPath(r, parPath, true);
	}

	public ArrayList<String> getValuesAtParameterPath(Resource r, String parPath) {
		return getValuesFromParameterPath(r, parPath, false);
	}

	// perhaps xquery might be faster than Java reflection, as the latter may
	// involve making call for
	// elements that may not be initialized

	private ArrayList<String> getValuesFromParameterPath(Resource r,
			String parPath, boolean explodeF) {
		ArrayList<String> list = new ArrayList<String>();

		String xml = FhirUtil.resourceToXml(r);
		list = XQueryUtil.getStringSequence(xml,
				"declare default element namespace \"http://hl7.org/fhir\";"
						+ "/" + this.parameterPath.replace(".", "/")// "/Patient/birthDate+"
						+ (explodeF ? "/*" : "") + "/@value/string()");
		logger.trace("list:" + list.toString());
		return list;
	}

	protected String getRawValue() {
		return rawValue;
	}
	protected String getRawParameter() {
		return rawParameter;
	}

	protected String getParameterPath() {
		return parameterPath;
	}
	
	protected String getParameter() {
		return parameter;
	}

	protected Class getResourceClass() {
		return resourceClass;
	}

	protected String getModifier() {
		return modifier;
	}

}
