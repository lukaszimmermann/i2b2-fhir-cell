package harvard.i2b2.fhir;

import harvard.i2b2.fhir.ejb.ResourceDb;
import harvard.i2b2.fhir.ejb.XQueryProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
import javax.ws.rs.client.WebTarget;
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
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import javax.xml.xquery.XQSequence;

import net.sf.saxon.Configuration;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.xqj.SaxonXQDataSource;

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
import org.xml.sax.InputSource;

@Path("i2b2")
public class FromI2b2WebService {
	//Logger logger= LoggerFactory.getLogger(ResourceFromI2b2WebService.class);
	
	@EJB
	XQueryProcessor xqp;
	
	@javax.ws.rs.core.Context
	ServletContext context;

	@PostConstruct
	private void init() {
		System.out.println("will run xquery");
		try{
			String input=getFile("example/i2b2/i2b2medspod.txt");
			//System.out.println(processXquery(input));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@GET
	@Path("medication")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String getMedsForPatientSet(
			//@PathParam("patientId") String patientId,
			@HeaderParam("accept") String acceptHeader) throws IOException {
			//logger.log(Level.WARNING, "customer is null.");
			Client client = ClientBuilder.newClient();
			WebTarget myResource = client.target("http://services.i2b2.org:9090/i2b2/services/QueryToolService/pdorequest");
			 String str=getFile("i2b2RequestMeds1.xml");
			 String oStr= myResource.request(MediaType.APPLICATION_XML).post(Entity.entity(str, MediaType.APPLICATION_XML),String.class);
			 System.out.println("got:"+oStr.substring(0,(oStr.length()>200)?200:0));
			 try{
				 //String input=getFile("example/i2b2/i2b2medspod.txt");
				 //str=processXquery(input);
				 return processXquery(oStr.toString());
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			 //System.out.println(str);
			return null; 		
	}
	
	private String processXquery(String input) throws InstantiationException, IllegalAccessException, ClassNotFoundException, XQException, XMLStreamException{
		String query = getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMeds.xquery");
	    return xqp.processXquery(query, input);
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
