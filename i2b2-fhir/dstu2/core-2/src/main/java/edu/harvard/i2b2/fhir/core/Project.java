package edu.harvard.i2b2.fhir.core;

public class Project {
	private String id;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + "]";
	}
	
}
