package edu.harvard.i2b2.fhir.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.hl7.fhir.Resource;

@XmlRootElement(name= "ResourceWithMetaDataSet")
public class FhirResourceWithMetaDataSet {
	List<FhirResourceWithMetaData> lists;

	public List<FhirResourceWithMetaData> getLists() {
		return lists;
	}

	public void setLists(List<FhirResourceWithMetaData> lists) {
		this.lists = lists;
	}
	
	
	
}
