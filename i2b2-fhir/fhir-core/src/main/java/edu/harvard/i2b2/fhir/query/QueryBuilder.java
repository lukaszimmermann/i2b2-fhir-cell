package edu.harvard.i2b2.fhir.query;

public class QueryBuilder {
	Class resourceClass;
	String rawParameter;
	String rawValue;
	String queryTypeStr;

	public QueryBuilder() {
	}

	// will aply rules to parameter name and value to identify type of query and
	// create it
	public Query build() throws QueryParameterException, QueryValueException {
		SearchParameterTuple t = SearchParameterTupleMap.getTuple(
				this.resourceClass, this.rawParameter);

		if (t != null)
			this.queryTypeStr = t.getType();
		Query q = null;

		switch (this.queryTypeStr) {
		case "date":
			q = new QueryDate(resourceClass, rawParameter, rawValue);
			break;
		case "token":
			q = new QueryToken(resourceClass, rawParameter, rawValue);
			break;
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

}
