package edu.harvard.i2b2.fhir.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hl7.fhir.Medication;
import org.hl7.fhir.Resource;

@XmlRootElement(name= "FhirResourceWithMetaData")
public class FhirResourceWithMetaData {
	MetaData rmd;
	
	
	Medication r;
	
	@XmlElement(name = "Medication")
	public Medication getR() {
		return r;
	}
	public void setR(Medication r) {
		this.r = r;
	}
	
	
	@XmlElement(name = "MetaData")
	public MetaData getRmd() {
		return rmd;
	}
	
	public void setRmd(MetaData rmd) {
		this.rmd = rmd;
	}
	
}
