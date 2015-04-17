package harvard.i2b2.fhir.ws;



import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;

import edu.harvard.i2b2.FhirUtil;
import edu.harvard.i2b2.XQueryUtil;


@Path("i2b2")
public class FromI2b2WebService<Resource> {
	//Logger logger= LoggerFactory.getLogger(ResourceFromI2b2WebService.class);
	String i2b2SessionId;
	

	
	@javax.ws.rs.core.Context
	ServletContext context;

	@PostConstruct
	private void init() {
		try{
			//doLogin();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@GET
	@Path("login")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String doLogin() {
		String query = getFile("transform/i2b2/getSessionKeyFromGetServices.xquery");
		Client client = ClientBuilder.newClient();
		WebTarget myResource = client.target("http://services.i2b2.org/i2b2/services/PMService/getServices");
		 String str=getFile("i2b2query/getServices.xml");
		 String oStr= myResource.request(MediaType.APPLICATION_XML).post(Entity.entity(str, MediaType.APPLICATION_XML),String.class);
		 System.out.println("got::"+oStr.substring(0,(oStr.length()>200)?200:0));
		 	 return processXquery(query,oStr.toString());
		
	}

	@GET
	@Path("medication")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String getMedsForPatientSet(
			//@PathParam("patientId") String patientId,
			@HeaderParam("accept") String acceptHeader) throws IOException {
			//logger.log(Level.WARNING, "customer is null.");
		
			String query = getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMeds.xquery");
			Client client = ClientBuilder.newClient();
			WebTarget myResource = client.target("http://services.i2b2.org:9090/i2b2/services/QueryToolService/pdorequest");
			 String str=getFile("i2b2query/i2b2RequestMeds1.xml");
			 String oStr= myResource.request(MediaType.APPLICATION_XML).post(Entity.entity(str, MediaType.APPLICATION_XML),String.class);
			 System.out.println("got::"+oStr.substring(0,(oStr.length()>200)?200:0));
			 	 //String input=getFile("example/i2b2/i2b2medspod.txt");
				 //str=processXquery(input);
				 return processXquery(query,oStr.toString());
					
	}
	
	@GET
	@Path("patient")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String getAllPatients(
			@HeaderParam("accept") String acceptHeader) throws IOException {
			

		String query = getFile("transform/I2b2ToFhir/i2b2PatientToFhirPatient.xquery");
		Client client = ClientBuilder.newClient();
		WebTarget myResource = client.target("http://services.i2b2.org:9090/i2b2/services/QueryToolService/pdorequest");
		 String str=getFile("i2b2query/getAllPatients.xml");
		 String oStr= myResource.request(MediaType.APPLICATION_XML).post(Entity.entity(str, MediaType.APPLICATION_XML),String.class);
		 System.out.println("got::"+oStr.substring(0,(oStr.length()>200)?200:0));
			 //return "<html>"+processXquery(query,oStr.toString())+"</html>";
		 str=processXquery(query,oStr.toString());
		 List<org.hl7.fhir.Resource> listRes=(List<org.hl7.fhir.Resource>) FhirUtil.xmlToResource(str);
			 return ResourceWebService.getResourceBundle((List<org.hl7.fhir.Resource>) listRes,"uriinfo_string");
			 //return oStr.toString();
	}
	
	
	
	private String processXquery(String query, String input) {
		return XQueryUtil.processXQuery(query, input);
	}
	
	
	
	private  String getFile(String fileName){
		 
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