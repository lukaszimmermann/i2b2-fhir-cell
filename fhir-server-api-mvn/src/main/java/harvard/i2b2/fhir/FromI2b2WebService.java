package harvard.i2b2.fhir;

import harvard.i2b2.fhir.ejb.ResourceDb;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Init;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.writer.Writer;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.hl7.fhir.instance.validation.Validator;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("i2b2")
public class FromI2b2WebService {
	//Logger logger= LoggerFactory.getLogger(ResourceFromI2b2WebService.class);
	
	@javax.ws.rs.core.Context
	ServletContext context;

	@GET
	@Path("medication")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getParticularResource(
			//@PathParam("patientId") String patientId,
			@HeaderParam("accept") String acceptHeader) throws IOException {
			//logger.log(Level.WARNING, "customer is null.");
			Client client = ClientBuilder.newClient();
			 Builder builder=
			client.target("http://services.i2b2.org:9090/i2b2/services/QueryToolService/pdorequest")
			.request(MediaType.APPLICATION_XML);
			 String str=getFile("i2b2RequestMeds1.xml");
			 Response response= builder.post(Entity.entity(str, MediaType.APPLICATION_XML));
			System.out.println(response.toString());
			return response; 		
			
		//System.out.println(;
		//return Response.ok().build();
				
		}
	
	private String getFile(String fileName){
		 
		  String result = "";
	 
		  ClassLoader classLoader = getClass().getClassLoader();
		  try {
			result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
		  } catch (IOException e) {
			e.printStackTrace();
		  }
	 
		  return result;
	 
	  }

	
}
