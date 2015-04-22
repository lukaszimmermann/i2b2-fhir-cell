package example;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.hl7.fhir.Medication;
import org.junit.Test;

import edu.harvar.i2b2.fhir.core.MetaData;
import edu.harvar.i2b2.fhir.core.MetaResource;
import edu.harvar.i2b2.fhir.core.MetaResourceSet;




public class jaxbtutorial1 {

	
	@Test
	public void test2() throws JAXBException, IOException, DatatypeConfigurationException{
		 final String BOOKSTORE_XML = "./tmp.xml";

		   
		 MetaResourceSet s1= new MetaResourceSet ();
		    ArrayList<MetaResource> list = new ArrayList<MetaResource>();

		    // create books
		    
		    MetaResource r1= new MetaResource();
		    MetaData md1= new MetaData();
		    md1.setId("medication/1");
		    GregorianCalendar gc = new GregorianCalendar();
	       

		    md1.setLastUpdated( DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
		    
		    Medication m1= new Medication();
		    org.hl7.fhir.String str1=new org.hl7.fhir.String();
		    str1.setValue("med1");
		    m1.setName(str1);
		   
		    r1.setMetaData(md1);
		    r1.setResource(m1);
		    list.add(r1);
		    
		    
		 
		    // create JAXB context and instantiate marshaller
		    JAXBContext context = JAXBContext.newInstance( MetaResource.class);
		    Marshaller m = context.createMarshaller();
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		    // Write to System.out
		    m.marshal(r1, System.out);
/*
		    // Write to File
		    m.marshal(r1, new File(BOOKSTORE_XML));

		    
		    // get variables from our xml file, created before
		    System.out.println();
		    System.out.println("Output from our XML File: ");
		    Unmarshaller um = context.createUnmarshaller();
		   FhirResourceWithMD r2= (FhirResourceWithMD) um.unmarshal(new FileReader(BOOKSTORE_XML));
		      System.out.println("FhirResourceWithMD: " +((MetaData)r2.getMetaData()).getId()+"\n"+
		    		  ((Medication)r2.getResource()).getName().getValue());
	*/	    }	
	
	
}
