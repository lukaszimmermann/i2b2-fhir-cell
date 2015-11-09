package edu.harvard.i2b2.oauth2.register.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/*
 * fhir cell config from database
 */

@Entity 
public class ConfigDb {
	@Id
	String parameter;
	
	@Column(columnDefinition = "TEXT")
	String value;
	
	
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "ConfigDb [parameter=" + parameter + ", value=" + value + "]";
	}
	
	
	
}
