package harvard.i2b2.fhir;

import harvard.i2b2.fhir.ejb.ResourceDb;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.json.XML;

@Path("res")
public class ResourceWebService2 {
	@EJB
	ResourceDb resourcedb;

	@GET
	@Path("{resourceName:[a-z]+}/{id:[0-9]+}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getParticularResource(@PathParam("resourceName") String resourceName,
			@PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader){
		String msg=null;
		Resource r=null;
		System.out.println("searhcing particular resource2:<"+resourceName+"> with id:<"+id+">");
			Class c=getResourceClass(resourceName);
			if (c!= null){
				r=resourcedb.getParticularResource(c,id);
				msg= resourceToXml(r,c);
				if(acceptHeader.equals("application/json")){
					 msg= xmlToJson(resourceToXml(r,c));
				}
			}
			if((acceptHeader.equals("application/xml")||acceptHeader.equals("application/json"))
					 &&c!=null && r!=null){
				return Response.ok(msg).build();
			 }else{
				 if(c!=null && r==null){
					 return Response.noContent().build(); 
				 }
				 return Response.status(Status.BAD_REQUEST).build(); 
				  
			 }
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
	@Path("{resourceName:[a-z]+}")
	public Response putParticularResource(@PathParam("resourceName") String resourceName, Object r){
		System.out.println("putting  particular resource2:<"+resourceName+">");
		Class c=getResourceClass(resourceName);
		if(c!=null){
			return Response.created(
					URI.create(resourceName+"/"+resourcedb.addResource((Resource)r,c))
					).build();
		}else{
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	
	
	private Class getResourceClass(String resourceName) {
		ClassLoader loader=this.getClass().getClassLoader();
		String targetClassName="org.hl7.fhir."+resourceName.substring(0,1).toUpperCase()+resourceName.substring(1,resourceName.length());
		try {
			return Class.forName(targetClassName, false, loader);
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Found for FHIR resource:"+resourceName);
			return null;
		}
	}
	
	private static String xmlToJson(String xmlStr) {
		return XML.toJSONObject(xmlStr).toString(2);
	}

	private static String resourceToXml(Resource r, Class c) {
		StringWriter strw=new StringWriter();
		JAXBElement jbe=null;
		jbe=new JAXBElement( 
		          new QName("http://hl7.org/fhir",c.getSimpleName()), c, c.cast(r));
			try {
				JAXBContext jc = JAXBContext.newInstance(c);
			    Marshaller marshaller = jc.createMarshaller();
		        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		        marshaller.marshal(jbe,strw);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		return strw.toString();
	}
	
	
}
