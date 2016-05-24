package edu.harvard.i2b2.fhir.modules;

import edu.harvard.i2b2.converter.ConverterException;

public interface Converter {

	public String getFhirXmlBundle(String pid) throws ConverterException;
}
