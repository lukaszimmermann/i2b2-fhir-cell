package edu.harvard.i2b2.fhir.query;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hl7.fhir.ElementDefinition;
import org.hl7.fhir.Patient;
import org.hl7.fhir.StructureDefinition;
import org.hl7.fhir.StructureDefinition;
import org.hl7.fhir.SearchParamType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class SearchParameterMap {
	static Logger logger = LoggerFactory.getLogger(SearchParameterMap.class);

	static HashMap<Class, StructureDefinition> hm = new HashMap<Class, StructureDefinition>();

	public SearchParameterMap() throws FhirCoreException {
		try {
			init();
		} catch (JAXBException e) {
			logger.error("init exception", e);
			throw new FhirCoreException("init JAXB exception", e);
		}
	}

	public void init() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(StructureDefinition.class);
		Unmarshaller um = context.createUnmarshaller();
		StructureDefinition p = null;
		for (Class c : FhirUtil.resourceClassList) {
			//logger.trace(">" + c.getSimpleName());
			String xml = Utils.getFile("profiles/"
					+ c.getSimpleName().toLowerCase() + ".profile.xml");

			p = (StructureDefinition) um.unmarshal(new ByteArrayInputStream(xml
					.getBytes(StandardCharsets.UTF_8)));
			hm.put(c, p);

		}

			}

	public String getParameterPath(Class c, String parName)
			throws FhirCoreException {
		return getElementDefinition(c,parName).getPath().toString();
				}

	public String getType(Class c, String parName)
			throws FhirCoreException {
		
		return getElementDefinition(c,parName).getType().get(0).getCode().getValue().toString();
				
	}
/*
	<element>
    <path value="Patient.identifier"/>
    <short value="An identifier for this patient"/>
    <definition value="An identifier for this patient."/>
    <requirements value="Patients are almost always assigned specific numerical identifiers."/>
    <min value="0"/>
    <max value="*"/>
    <type>
      <code value="Identifier"/>
    </type>
    <isSummary value="true"/>
    <mapping>
      <identity value="v2"/>
      <map value="PID-3"/>
    </mapping>
    <mapping>
      <identity value="rim"/>
      <map value="id"/>
    </mapping>
  </element>
*/	
	public ElementDefinition getElementDefinition(Class c, String parName) throws FhirCoreException {
			
		StructureDefinition p = hm.get(c);
		if (p==null) throw new FhirCoreException("No StructureDefinition found for class:"+c.getCanonicalName());
		
		for (ElementDefinition e : p.getSnapshot().getElement()) {
			//if(e.getName().getValue().equals(parName)){
			logger.trace("searching path:"+e.getPath().getValue()+" for:"+parName);
			if(e.getPath().getValue().contains(parName)){
				return  e;
			}
		
		}
		
		
		 throw new FhirCoreException("No StructureDefinitionSearchParam found for class:"+c.getCanonicalName()+" for param:"+parName) ;
	}
}
