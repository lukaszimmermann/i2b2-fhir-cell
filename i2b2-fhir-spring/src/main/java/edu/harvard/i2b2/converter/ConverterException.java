package edu.harvard.i2b2.converter;

public class ConverterException extends Exception {

	public ConverterException(Exception e) {
		super(e.getMessage(),e);
	}

}
