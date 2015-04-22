package example;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.hl7.fhir.FhirResourceWithMD;
import org.hl7.fhir.Medication;
import org.hl7.fhir.MetaData;
import org.junit.Test;



public class jaxbtutorial1 {

	
	@Test
	public void test2() throws JAXBException, IOException{
		 final String BOOKSTORE_XML = "./tmp.xml";

		   

		    ArrayList<FhirResourceWithMD> list = new ArrayList<FhirResourceWithMD>();

		    // create books
		    
		    FhirResourceWithMD r1= new FhirResourceWithMD();
		    MetaData md1= new MetaData();
		    md1.setId("medication/1");
		    md1.setLastUpdated("2015-04-22");
		    
		    Medication m1= new Medication();
		    org.hl7.fhir.String str1=new org.hl7.fhir.String();
		    str1.setValue("med1");
		    m1.setName(str1);
		   
		    r1.setMetaData(md1);
		    r1.setResource(m1);
		    list.add(r1);
		    
		 
		    // create JAXB context and instantiate marshaller
		    JAXBContext context = JAXBContext.newInstance( FhirResourceWithMD.class);
		    Marshaller m = context.createMarshaller();
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		    // Write to System.out
		    m.marshal(r1, System.out);

		    // Write to File
		    m.marshal(r1, new File(BOOKSTORE_XML));

		    
		    // get variables from our xml file, created before
		    System.out.println();
		    System.out.println("Output from our XML File: ");
		    Unmarshaller um = context.createUnmarshaller();
		   FhirResourceWithMD r2= (FhirResourceWithMD) um.unmarshal(new FileReader(BOOKSTORE_XML));
		      System.out.println("FhirResourceWithMD: " +r2.getMetaData().getId()+"\n"+
		    		  ((Medication)r2.getResource()).getName().getValue());
		    }	
}
