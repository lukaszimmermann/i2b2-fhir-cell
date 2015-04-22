package edu.harvard.i2b2.fhir.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name= "MetaData")
public class MetaData {
	String id; 
	String lastUpdated;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}
