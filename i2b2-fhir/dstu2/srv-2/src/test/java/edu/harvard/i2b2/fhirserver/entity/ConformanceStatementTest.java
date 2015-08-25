package edu.harvard.i2b2.fhirserver.entity;

import static org.junit.Assert.*;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Address;
import org.hl7.fhir.Conformance;
import org.hl7.fhir.ConformanceRest;
import org.hl7.fhir.ConformanceSecurity;
import org.hl7.fhir.Extension;
import org.hl7.fhir.Uri;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;

public class ConformanceStatementTest {
	static Logger logger = LoggerFactory.getLogger(  ConformanceStatementTest.class);

	@Test
	public void test() throws JAXBException {
		String fhirBase="https://localhost:8080/srv-dstu2-0.2";
		Conformance c= new Conformance();
		 ConformanceRest rest= new ConformanceRest();
		 ConformanceSecurity security = new ConformanceSecurity();
		 Extension OAuthext = new Extension();
		 security.getExtension().add(OAuthext);
		 OAuthext.setUrl("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");
		 
		 Extension authExt = new Extension();
		 authExt.setUrl("authorize");
		 Uri uri= new Uri();
		 uri.setValue(fhirBase+"/api/authz/authorize");
		 authExt.setValueUri(uri);
		 OAuthext.getExtension().add(authExt);
		 
		 Extension tokenExt = new Extension();
		 tokenExt.setUrl("token");
		 uri= new Uri();
		 uri.setValue(fhirBase+"/api/token");
		 tokenExt.setValueUri(uri);
		 OAuthext.getExtension().add(tokenExt);
		 
		 
		 rest.setSecurity(security);
		 c.getRest().add(rest);
		 logger.trace("conf:"+JAXBUtil.toXml(c));
		
	}

}
