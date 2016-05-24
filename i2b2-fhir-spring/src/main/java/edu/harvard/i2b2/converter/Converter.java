package edu.harvard.i2b2.converter;

public interface Converter {

	public String getFhirXmlBundle(String pid) throws ConverterException;
}
