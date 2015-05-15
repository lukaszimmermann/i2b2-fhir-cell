package edu.harvard.i2b2.fhir.query;

public class QueryBuilder {
	Class resourceClass;
	String parameter;
	String value;

	
	public QueryBuilder(){
	}
	
	//will aply rules to parameter name and value to identify type of query and create it
	public Query build() throws QueryParameterException,QueryValueException{
		Query q=new QueryDate(resourceClass,parameter,value);
		return q;
	}
	
	public Class getResourceClass() {
		return resourceClass;
	}

	public QueryBuilder setResourceClass(Class resourceClass) {
		this.resourceClass = resourceClass;
		return this;
	}

	public String getParameter() {
		return parameter;
	}

	public QueryBuilder setParameter(String parameter) {
		this.parameter = parameter;
		return this;
	}

	public String getValue() {
		return value;
	}

	public QueryBuilder setValue(String value) {
		this.value = value;
		return this;
	}
	
	

	
}
