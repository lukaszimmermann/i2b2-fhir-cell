package harvard.i2b2.fhir;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.Collection;

import harvard.i2b2.fhir.ejb.PatientDb;
import harvard.i2b2.fhir.ejb.ResourceDb;
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
import org.hl7.fhir.Observation;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Path("res")

public class ResourceWebservice {
	
	@EJB
	ResourceDb resourcedb;
	
	@GET
	@Path("patient/{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Patient getPatient(@PathParam("id") String id){
		return (Patient) getResource(id,Patient.class);
	}
	
	@GET
	@Path("observation/{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Observation getObservation(@PathParam("id") String id){
		return (Observation) getResource(id,Observation.class);
	}
	
	
	private Resource getResource(String id, Class c){
		System.out.println("getting "+c.getSimpleName() +"/"+id);
		return resourcedb.getResource(id,c);
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("patient")
	public Response putPatient( Patient p){
		return Response.created(putResource(p, Patient.class)).build();
	}
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Path("observation")
	public Response putObservation( Observation p){
		return Response.created(putResource(p, Observation.class)).build();
	}
	
	
	private URI putResource(Resource p, Class c){
		System.out.println("resources:"+resourcedb.toString());
		System.out.println("Putting resource:"+p.getClass().getSimpleName());
		String id=resourcedb.addResource(p,Patient.class);
		return URI.create(c.getSimpleName().toLowerCase()+"/"+id);
	}
	
	
	@GET
	@Produces({MediaType.APPLICATION_ATOM_XML})
	@Path("patient")
	public String allPatients() {
		return prettyxml(resourcedb.getall(Patient.class));
    }
	
	@GET
	@Produces({MediaType.APPLICATION_ATOM_XML})
	@Path("observation")
	public String allObservation() {
		return prettyxml(resourcedb.getall(Observation.class));
    }
	
	private String prettyxml(String sourceString){
		String xmlString =null;
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult result = new StreamResult(new StringWriter());
		DocumentBuilder parser = 
			      DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc=parser.parse( new InputSource(new StringReader(sourceString)) );;
		DOMSource source = new DOMSource(doc);
		
			transformer.transform(source, result);
			xmlString = result.getWriter().toString();
		} catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return xmlString;
	}
	
}
