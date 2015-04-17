package edu.harvard.i2b2.fhir.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.hl7.fhir.Resource;

@XmlRootElement(name= "FhirResourceSet")
public class FhirResourceSet {
	Resource r;
	FhirResourceMetaData rmd;
	
}
