package edu.harvard.i2b2;

import static org.junit.Assert.*;

import org.junit.Test;

public class misc {

	@Test
	public void test() {
		System.out.println("http://fhir".replaceAll("^http:", "https:"));
		
	}

}
