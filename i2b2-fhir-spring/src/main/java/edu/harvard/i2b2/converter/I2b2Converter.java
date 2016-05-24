package edu.harvard.i2b2.converter;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.I2b2UtilByCategory;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;

@Repository
public class I2b2Converter implements Converter {
	static Logger logger = LoggerFactory.getLogger(Converter.class);
	
	String i2b2UserId="demo";
	String i2b2UserPassword="demouser";
	String i2b2Token;
	String i2b2Url="http://services.i2b2.org:9090/i2b2";
	String i2b2Domain="i2b2demo";
	String i2b2Project="Demo";
	String i2b2OntologyType="default";
	
	@Override
	public String getFhirXmlBundle(String pid) throws ConverterException {
		String fhirXmlBundle="empty";
		try {
			System.out.println("fetching PDO for pid:" + pid + " and tok:"+ i2b2Token);
			String pmResponseXml=I2b2Util.getPmResponseXml(i2b2UserId,i2b2UserPassword, i2b2Url, i2b2Domain);
			 i2b2Token=I2b2Util.getToken(pmResponseXml);
			
			 System.out.println("pmResponseXml:"+pmResponseXml);

			HashMap<String, String> map = new HashMap<String, String>();

			map.put("labs", "\\\\i2b2_LABS\\i2b2\\Labtests\\");
			Bundle b = I2b2UtilByCategory.getAllDataForAPatientAsFhirBundle(i2b2UserId, i2b2Token,
					i2b2Url, i2b2Domain, i2b2Project, pid, map, i2b2OntologyType);
			//fhirXmlBundle=JAXBUtil.toXml(b);
			
		} catch (XQueryUtilException |IOException | FhirCoreException  e) {
			throw new ConverterException(e);
		}
		return fhirXmlBundle;
		
	}

}
