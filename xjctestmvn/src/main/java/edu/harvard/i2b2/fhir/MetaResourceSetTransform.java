package edu.harvard.i2b2.fhir;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class MetaResourceSetTransform {
	
	static public MetaResourceSet MetaResourceSetFromXml(String xml) throws JAXBException{
		JAXBContext context = JAXBContext.newInstance(MetaResourceSet.class);
		Unmarshaller um = context.createUnmarshaller();
		MetaResourceSet s = (MetaResourceSet) um
				.unmarshal(new ByteArrayInputStream(xml
						.getBytes(StandardCharsets.UTF_8)));
		
		return s;
	}
}


