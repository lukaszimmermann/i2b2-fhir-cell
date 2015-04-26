package harvard.i2b2.fhir.ws;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Resource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.I2b2ToFhirTransform;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

@Path("i2b2")
public class FromI2b2WebService {
	// Logger logger= LoggerFactory.getLogger(ResourceFromI2b2WebService.class);
	String i2b2SessionId;

	@javax.ws.rs.core.Context
	ServletContext context;

	@PostConstruct
	private void init() {
		try {
			// doLogin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("login")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String doLogin() {
		String query = getFile("transform/i2b2/getSessionKeyFromGetServices.xquery");
		Client client = ClientBuilder.newClient();
		WebTarget myResource = client
				.target("http://services.i2b2.org/i2b2/services/PMService/getServices");
		String str = getFile("i2b2query/getServices.xml");
		String oStr = myResource.request(MediaType.APPLICATION_XML).post(
				Entity.entity(str, MediaType.APPLICATION_XML), String.class);
		System.out.println("got::"
				+ oStr.substring(0, (oStr.length() > 200) ? 200 : 0));
		return processXquery(query, oStr.toString());

	}

	@GET
	@Path("medication")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String getMedsForPatientSet(
	 @QueryParam("patientId") String patientId,
			@HeaderParam("accept") String acceptHeader) throws IOException, ParserConfigurationException, SAXException {
		// logger.log(Level.WARNING, "customer is null.");
		System.out.println("PatientId:"+patientId);
	
		String request = Utils.getFile("i2b2query/i2b2RequestMedsForAPatient.xml");
		String query = Utils
		.getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMedStatement.xquery");
		if(patientId!=null)request=request.replaceAll("PATIENTID", patientId);

				Client client = ClientBuilder.newClient();
				WebTarget webTarget = client
						.target("http://services.i2b2.org:9090/i2b2/services/QueryToolService/pdorequest");
				String oStr = webTarget.request().accept("Context-Type","application/xml").post(
						Entity.entity(request, MediaType.APPLICATION_XML), String.class);
				String xQueryResultString = XQueryUtil.processXQuery(query,  oStr);
				System.out.println(xQueryResultString);
		
		try {
			MetaResourceSet s = I2b2ToFhirTransform
					.MetaResourceSetFromI2b2Xml(xQueryResultString);
			List<Resource> rl = FhirUtil.getResourcesFromMetaResourceSet(s);

			// return rl.toString();
			return FhirUtil.getResourceBundle((List<org.hl7.fhir.Resource>) rl,
					"work in progress");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ERROR";

	}

	@GET
	@Path("patient")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String getAllPatients(@HeaderParam("accept") String acceptHeader)
			throws IOException {

		String query = getFile("transform/I2b2ToFhir/i2b2PatientToFhirPatient.xquery");
		Client client = ClientBuilder.newClient();
		WebTarget myResource = client
				.target("http://services.i2b2.org:9090/i2b2/services/QueryToolService/pdorequest");
		String str = getFile("i2b2query/getAllPatients.xml");
		String oStr = myResource.request(MediaType.APPLICATION_XML).post(
				Entity.entity(str, MediaType.APPLICATION_XML), String.class);
		System.out.println("got::"
				+ oStr.substring(0, (oStr.length() > 200) ? 200 : 0));
		// return "<html>"+processXquery(query,oStr.toString())+"</html>";
		str = processXquery(query, oStr.toString());
		List<org.hl7.fhir.Resource> listRes = (List<org.hl7.fhir.Resource>) FhirUtil
				.xmlToResource(str);
		return ResourceWebService.getResourceBundle(
				(List<org.hl7.fhir.Resource>) listRes, "uriinfo_string");
		// return oStr.toString();
	}

	private String getFile(String fileName) {

		String result = "";

		ClassLoader classLoader = getClass().getClassLoader();
		try {
			result = IOUtils
					.toString(classLoader.getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	private String processXquery(String query, String input) {

		return XQueryUtil.processXQuery(query, input);
	}

	public void stringToFile(String FileName, String str) throws IOException {
		Files.write(
				Paths.get(context.getInitParameter("storedFilePath")
						+ "/tempinfo.xml"), str.getBytes());

	}
	
	public String convert(String input) throws ParserConfigurationException, SAXException, IOException{
		// Create the encoder and decoder for ISO-8859-1
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		CharsetEncoder encoder = charset.newEncoder();

		    // Convert a string to ISO-LATIN-1 bytes in a ByteBuffer
		    // The new ByteBuffer is ready to be read.
		    ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(input));

		    // Convert ISO-LATIN-1 bytes in a ByteBuffer to a character ByteBuffer and then to a string.
		    // The new ByteBuffer is ready to be read.
		    CharBuffer cbuf = decoder.decode(bbuf);
		    //if(1==1)return Utils.getFile("example/i2b2/medicationsForAPatient2.xml");
		    if(1==1)return FromI2b2WebService.getStringFromDocument( FromI2b2WebService.xmltoDOM(cbuf.toString()));
		    return cbuf.toString();
		
	}
	
	public static Document xmltoDOM(String xmlStr) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlStr));

		return db.parse(is);
	}
	
	public static String getStringFromDocument(Document doc)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	} 
		
	
}
