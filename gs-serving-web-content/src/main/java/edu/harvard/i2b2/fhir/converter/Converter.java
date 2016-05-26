package edu.harvard.i2b2.fhir.converter;


public interface Converter {

	public String getFhirXmlBundle(String pid) throws ConverterException;
}
