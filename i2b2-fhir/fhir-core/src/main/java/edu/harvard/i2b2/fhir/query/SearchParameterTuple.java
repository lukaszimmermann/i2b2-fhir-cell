package edu.harvard.i2b2.fhir.query;

public class SearchParameterTuple {
	String parameter;
	String type;
	String description;
	String path;
	
	public SearchParameterTuple(String parameter, String type,
			String description, String path) {
		this.parameter = parameter;
		this.type = type;
		this.description = description;
		this.path = path;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

}
