package edu.harvard.i2b2.fhir.query;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hl7.fhir.Patient;
import org.hl7.fhir.Profile;
import org.hl7.fhir.ProfileSearchParam;
import org.hl7.fhir.ProfileStructure;
import org.hl7.fhir.SearchParamType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class SearchParameterMap {
	static Logger logger = LoggerFactory.getLogger(SearchParameterMap.class);

	static HashMap<Class, Profile> hm = new HashMap<Class, Profile>();

	public SearchParameterMap() throws FhirCoreException {
		try {
			init();
		} catch (JAXBException e) {
			logger.error("init exception", e);
			throw new FhirCoreException("init JAXB exception", e);
		}
	}

	public void init() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Profile.class);
		Unmarshaller um = context.createUnmarshaller();
		Profile p = null;
		for (Class c : FhirUtil.resourceClassList) {
			logger.trace(">" + c.getSimpleName());
			String xml = Utils.getFile("profiles/"
					+ c.getSimpleName().toLowerCase() + ".profile.xml");

			p = (Profile) um.unmarshal(new ByteArrayInputStream(xml
					.getBytes(StandardCharsets.UTF_8)));
			hm.put(c, p);

		}

	}

	public String getParameterPath(Class c, String parName)
			throws FhirCoreException {
		return getProfileSearchParam(c,parName).getXpath().getValue().toString().replace("f:", "");
	}

	public String getType(Class c, String parName)
			throws FhirCoreException {
		
		return getProfileSearchParam(c,parName).getType().getValue().toString();
	}

	
	public ProfileSearchParam getProfileSearchParam(Class c, String parName) throws FhirCoreException {
			
		Profile p = hm.get(c);
		if (p==null) throw new FhirCoreException("No profile found for class:"+c.getCanonicalName());
		
		for (ProfileSearchParam s : p.getStructure().get(0).getSearchParam()) {
			if(s.getName().getValue().equals(parName)){
				logger.trace("FOUND profile" + s.getName().getValue());
				return  s;
			}
		
		}
		
		 throw new FhirCoreException("No profileSearchParam found for class:"+c.getCanonicalName()+" for param:"+parName) ;
	}
}
