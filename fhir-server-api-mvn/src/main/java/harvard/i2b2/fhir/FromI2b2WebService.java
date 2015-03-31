package harvard.i2b2.fhir;

import harvard.i2b2.fhir.ejb.ResourceDb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
	
	@javax.ws.rs.core.Context
	ServletContext context;

	@PostConstruct
	private void init() {
		System.out.println("will run xquery");
		try{
			System.out.println(processXquery("hi"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@GET
	@Path("medication")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getMedsForPatientSet(
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
	}
	
	private String processXquery(String input) throws InstantiationException, IllegalAccessException, ClassNotFoundException, XQException{
		XQDataSource xqds = new SaxonXQDataSource();
		XQConnection conn = xqds.getConnection();
		// The XQuery expression to be executed
		String query = "var x=.;<hello-world>{x + 1}</hello-world>";
		 
		// Bind a value (21) to an external variable with the QName x
		 
		// Execute the XQuery expression
		//XQExpression expr = conn.createExpression();
		 
		XQPreparedExpression expr = conn.prepareExpression(query); 
		expr.bindInt(new QName("x"), 21, null);
		
		XQSequence result = expr.executeQuery();
        //System.out.println(result.getSequenceAsString(null));
        
		return result.getSequenceAsString(null);
	}
	
	
	/*
	protected String doGet(String input,String xquery) {
		try {
			StringWriter sw= new StringWriter();
			 Processor processor1 = new Processor(false);
			 //processor1.newSerializer(file)
			 //processor1.
			 Processor processor = new Processor(false);
	       
	        XQueryCompiler compiler = processor.newXQueryCompiler();
	        Serializer serializer = new Serializer();
	        serializer.setOutputWriter(sw);
	        
	        XQueryExecutable executable = compiler.compile(IOUtils.toInputStream(input));
	        XQueryEvaluator evaluator = executable.load();
	        evaluator.setDestination(serializer);
	        evaluator.run();
	        return sw.toString();
	    } catch (SaxonApiException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public void go(String xmldata, String queryfilename) throws Exception {
		//
		// Get the input query file
		FileInputStream fin = new FileInputStream(queryfilename);
		BufferedReader newquery
		= new BufferedReader(new InputStreamReader(fin));
		// We could make the query a string, for example
		String squery = "for $a in fn:distinct-values(//author)" +
		"return (xs:string($a)," +
		"for $b in //book[author = $a]" +
		"return $b/title)";
		
		// Now get the XML file wiht the and make it a SAX Source
		InputSource is = new InputSource(new File(xmlfilename).toURL().toString()); 
		SAXSource saxsource = new SAXSource( is);
		
		StringReader stringread = new StringReader( xmldata);
		StreamSource streamsource = new StreamSource( stringread);
		// if you want the string xmldata replace saxsource below with streamsource 
		Configuration config = new Configuration();
		config.setHostLanguage( config.XQUERY);
		
		StaticQueryContext staticQueryContext = new StaticQueryContext();
		QueryProcessor qp = new QueryProcessor(config, staticQueryContext);
		DocumentInfo docinfo = qp.buildDocument( saxsource);
		XQueryExpression exp = qp.compileQuery( newquery);
		DynamicQueryContext dynamicQueryContext = new DynamicQueryContext();
		dynamicQueryContext.setContextNode( docinfo);
		// evaluate the query and get a java.util.List
		List querylist = exp.evaluate(dynamicQueryContext);
		// now get a java.util iterator
		Iterator javautilIter = querylist.iterator();
		// now evaluate the query and get a net.sf.saxon.om.SequenceIterator
		SequenceIterator saxonIter = exp.iterator(dynamicQueryContext);
		// Output the results
		System.out.println("Number Nodes = " + querylist.size());
		printout( javautilIter, saxonIter);
		}
*/	
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
