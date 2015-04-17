package harvard.i2b2.fhir.del.ws;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Collection;

import harvard.i2b2.fhir.del.ejb.PatientDb;
import harvard.i2b2.fhir.entity.Patients;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.abdera.model.Feed;
import org.hl7.fhir.Patient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Path("patient")
public class PatientWebservice {
	
	@EJB
	PatientDb patientdb;
	
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Patient getPatient(@PathParam("id") String id){
		System.out.println("searching for patient with id:"+id);
		return patientdb.getPatient(id);
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response putPatient( Patient p){
		System.out.println("patients:"+patientdb.toString());
		System.out.println("Got patient:"+p.toString());
		
		patientdb.addPatient(p);
		//return patientdb.getPatient(p.getId());
		final URI id = URI.create("patient/"+p.getId());
        return Response.created(id).build();
	}
	
	@GET
	@Produces({MediaType.APPLICATION_ATOM_XML})
    public String allPatients() {
		return prettyxml(patientdb.getall());
    }
	
	private String prettyxml(String sourceString){
		String xmlString =null;
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		//initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());
		DocumentBuilder parser = 
			      DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc=parser.parse( new InputSource(new StringReader(sourceString)) );;
		DOMSource source = new DOMSource(doc);
		
			transformer.transform(source, result);
			xmlString = result.getWriter().toString();
		} catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return xmlString;
	}
	
}
