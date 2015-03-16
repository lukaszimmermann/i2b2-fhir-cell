package harvard.i2b2.fhir;

import harvard.i2b2.fhir.ejb.ResourceDb;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sf.json.JSONSerializer;

import org.apache.commons.collections.Factory;
import org.hl7.fhir.Observation;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Path("res")
public class ResourceWebService2 {
	@EJB
	ResourceDb resourcedb;
	
	
	
	@GET
	@Path("{resourceName:[a-z]+}/{id:[0-9]+}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String getParticularResource(@PathParam("resourceName") String resourceName,
			@PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader){
		String msg="";
		StringWriter strw=new StringWriter();
		JAXBElement jbe=null;
		System.out.println("searhcing particular resource2:<"+resourceName+"> with id:<"+id+">");
		ClassLoader loader=this.getClass().getClassLoader();
		String targetClassName="org.hl7.fhir."+resourceName.substring(0,1).toUpperCase()+resourceName.substring(1,resourceName.length());
		try {
			
			if (Class.forName(targetClassName, false, loader) != null){
				Class c= Class.forName(targetClassName);
				//String msg=(String) resourcedb.getParticularResource(c,id);
				List<Resource> rs=resourcedb.getParticularResource(c,id);
				Resource r=rs.get(0);
				jbe=new JAXBElement( 
			          new QName("http://hl7.org/fhir","Patient"), c, c.cast(r));
				
				   JAXBContext jc = JAXBContext.newInstance(c);
			        Marshaller marshaller = jc.createMarshaller();
			        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			        marshaller.marshal(jbe,strw);
			        System.out.println("JSON:");
			       // msg= JSONSerializer.toJSON(c.cast(r)).toString(4);
			        
			        //ObjectMapper mapper = new ObjectMapper();
			  //      mapper.addMixInAnnotations(JAXBElement.class, JAXBElementMixin.class);
			    //    System.out.println(mapper.writerWithDefaultPrettyPrinter()
			      //          .writeValueAsString(orderJAXBElement));
			        
			}
		} catch (ClassNotFoundException | JAXBException e) {
			e.printStackTrace();
		}
		msg=strw.toString();
		msg=XML.toJSONObject(msg).toString(2);
		return msg;
	}

	@GET
	@Path("patient0")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Patient getPatient(){
		return (Patient) getResource("0",Patient.class);
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
	
	
	private URI putResource(Resource p, Class c){
		System.out.println("resources2:"+resourcedb.toString());
		System.out.println("Putting resource2:"+p.getClass().getSimpleName());
		String id=resourcedb.addResource(p,Patient.class);
		return URI.create(c.getSimpleName().toLowerCase()+"/"+id);
	}
	
	
	
}
