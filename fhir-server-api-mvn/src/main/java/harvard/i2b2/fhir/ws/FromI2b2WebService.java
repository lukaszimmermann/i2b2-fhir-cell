package harvard.i2b2.fhir.ws;

import harvard.i2b2.fhir.ejb.OldMetaResourceDb;
import harvard.i2b2.fhir.ejb.MetaResourceDbWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Medication;
import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.Narrative;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.hl7.fhir.ResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3._1999.xhtml.Div;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.impl.scd.Iterators.Map;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.I2b2ToFhirTransform;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.core.MetaData;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

@Path("i2b2")
public class FromI2b2WebService {
	static Logger logger = LoggerFactory.getLogger(FromI2b2WebService.class);
	String i2b2SessionId;

	// @EJB
	// MetaResourceDbWrapper metaResourceDb;

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
	@Path("step1")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStep1(@Context HttpServletRequest request)
			throws DatatypeConfigurationException {
		HttpSession session = request.getSession();// new session
		String authId = Integer.toString(new Random().nextInt()).toString();

		session.setAttribute("testAttr1", authId);
		MetaResourceDb md = new MetaResourceDb();
		MetaResourceSet MRS = getEGPatient();
		System.out.println(((MetaResource) MRS.getMetaResource().get(0))
				.getResource().getId());

		md.addMetaResourceSet(MRS);
		Resource r = md.getParticularResource(Patient.class, "1000000005");
		System.out.println("res:" + FhirUtil.resourceToXml(r));
		session.setAttribute("md", md);

		return Response.ok().entity(authId

		).type(MediaType.TEXT_PLAIN).build();
	}

	@GET
	@Path("step2")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getStep2(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String sa = (String) session.getAttribute("testAttr1");
		System.out.println("gettestAttr1:" + sa);

		MetaResourceDb md = (MetaResourceDb) session.getAttribute("md");
		Resource r = md.getParticularResource(Patient.class, "1000000005");
		System.out.println("Got res:" + FhirUtil.resourceToXml(r));
		return Response.ok().entity(sa).type(MediaType.TEXT_PLAIN).build();
	}

	@GET
	@Path("login")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String doLogin() {
		String query = Utils
				.getFile("transform/i2b2/getSessionKeyFromGetServices.xquery");
		Client client = ClientBuilder.newClient();
		WebTarget myResource = client
				.target("http://services.i2b2.org/i2b2/services/PMService/getServices");
		String str = Utils.getFile("i2b2query/getServices.xml");
		String oStr = myResource.request(MediaType.APPLICATION_XML).post(
				Entity.entity(str, MediaType.APPLICATION_XML), String.class);
		System.out.println("got:"
				+ oStr.substring(0, (oStr.length() > 200) ? 200 : 0));
		return processXquery(query, oStr.toString());

	}

	@GET
	@Path("auth")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response doAuth(@Context HttpServletRequest request)
			throws DatatypeConfigurationException {

		HttpSession session = request.getSession();// new session
		String authId = Integer.toString(new Random().nextInt()).toString();

		String userName = request.getHeader("username");
		String password = request.getHeader("password");
		//if (userName != null && password != null) {
		if(1==1){	session.setAttribute("testAttr1", authId);
			MetaResourceDb md = new MetaResourceDb();
			MetaResourceSet MRS = getEGPatient();
			System.out.println(((MetaResource) MRS.getMetaResource().get(0))
					.getResource().getId());

			md.addMetaResourceSet(MRS);
			Resource r = md.getParticularResource(Patient.class, "1000000005");
			System.out.println("res:" + FhirUtil.resourceToXml(r));
			session.setAttribute("md", md);

			return Response.ok().entity(authId

			).type(MediaType.TEXT_PLAIN).build();
		}
		return Response.ok().entity("Auth failure")// .cookie(authIdCookie)
				.build();
	}

	@GET
	//@Path("MedicationStatement")
		@Path("{resourceName:" + FhirUtil.RESOURCE_LIST + "}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getMedsForPatientSet(
			@PathParam("resourceName") String resourceName,
			@QueryParam("patientId") String patientId,
			@QueryParam("_include") List<String> includeResources,
			@QueryParam("filterf") String filterf,
			// @HeaderParam("accept") String acceptHeader,
			@Context HttpServletRequest request) throws IOException,
			ParserConfigurationException, SAXException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException,
			DatatypeConfigurationException {
		Class c = FhirUtil.getResourceClass(resourceName);
		MetaResourceDb md = null;
		
		HttpSession session = request.getSession(false);
		
		if(session==null){ return Response.status(Status.BAD_REQUEST)
		 .type(MediaType.APPLICATION_XML) .entity("login first ").build(); }
			String sa = (String) session.getAttribute("testAttr1");
			System.out.println("gettestAttr1:" + sa);

			md = (MetaResourceDb) session.getAttribute("md");
			Resource r = md.getParticularResource(Patient.class, "1000000005");
			System.out.println("Got res:" + FhirUtil.resourceToXml(r));
			// return
			// Response.ok().entity(sa).type(MediaType.TEXT_PLAIN).build();
		
		// System.out.println("session attributes:"+session.getAttributeNames()
		// .toString());

		System.out.println("PatientId:" + patientId);
		System.out.println("_include:" + includeResources.toArray());

		String requestStr = Utils
				.getFile("i2b2query/i2b2RequestMedsForAPatient.xml");
		String query = Utils
				.getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMedStatement.xquery");
		if (patientId != null)
			requestStr = requestStr.replaceAll("PATIENTID", patientId);

		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client
				.target("http://services.i2b2.org:9090/i2b2/services/QueryToolService/pdorequest");
		String oStr = webTarget
				.request()
				.accept("Context-Type", "application/xml")
				.post(Entity.entity(requestStr, MediaType.APPLICATION_XML),
						String.class);
		String xQueryResultString = XQueryUtil.processXQuery(query, oStr);
		// System.out.println(xQueryResultString);

		md.addMetaResourceSet(getEGPatient());

		try {
			MetaResourceSet s = I2b2ToFhirTransform
					.MetaResourceSetFromI2b2Xml(xQueryResultString);

			md.addMetaResourceSet(s);

			
			s = md.getIncludedMetaResources(c,
					includeResources);
	
			HashMap<String,String> filter=new HashMap<String,String>();
		filter.put("Patient", "Patient/1000000005");
		MetaResourceSet s1=new MetaResourceSet();
		if(filterf!=null){
			s=md.filterMetaResources(MedicationStatement.class,filter);
		}
			
			String returnString = FhirUtil
					.getResourceBundleFromMetaResourceSet(s,
							"http://localhost:8080/fhir-server-api-mvn/resources/i2b2/");

			return Response.ok().type(MediaType.APPLICATION_XML)
					.entity(returnString).build();

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return Response.status(Status.BAD_REQUEST)
				.header("xreason", "some ERROR").build();
	}

	@GET
	@Path("patient")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllPatients(@HeaderParam("accept") String acceptHeader,
			@Context HttpServletRequest request) throws IOException {

		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("authId") == null) {
			return Response.status(Status.BAD_REQUEST)
					.type(MediaType.APPLICATION_XML).entity("login first")
					.build();
		}

		MetaResourceDbWrapper metaResourceDb = (MetaResourceDbWrapper) session
				.getAttribute("MetaResourceDbWrapper");

		String query = Utils
				.getFile("transform/I2b2ToFhir/i2b2PatientToFhirPatient.xquery");
		Client client = ClientBuilder.newClient();
		WebTarget myResource = client
				.target("http://services.i2b2.org:9090/i2b2/services/QueryToolService/pdorequest");
		String str = Utils.getFile("i2b2query/getAllPatients.xml");
		String oStr = myResource.request(MediaType.APPLICATION_XML).post(
				Entity.entity(str, MediaType.APPLICATION_XML), String.class);
		System.out.println("got::"
				+ oStr.substring(0, (oStr.length() > 200) ? 200 : 0));
		// return "<html>"+processXquery(query,oStr.toString())+"</html>";
		str = processXquery(query, oStr.toString());
		List<org.hl7.fhir.Resource> listRes = (List<org.hl7.fhir.Resource>) FhirUtil
				.xmlToResource(str);
		String xmlStr = (String) ResourceWebService.getResourceBundle(
				(List<org.hl7.fhir.Resource>) listRes, "uriinfo_string");

		return Response.status(200).type(MediaType.APPLICATION_XML)
				.entity(xmlStr).build();

		// return oStr.toString();
	}

	// http://localhost:8080/fhir-server-api-mvn/resources/i2b2/MedicationStatement/1000000005-1

	@GET
	// @Path("{resourceName:[a-z]+}/{id:[0-9]+}")
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST + "}/{id:[0-9|-]+}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getParticularResource(
			@PathParam("resourceName") String resourceName,
			@PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader,
			@Context HttpServletRequest request)
			throws DatatypeConfigurationException {

		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("authId") == null) {
			return Response.status(Status.BAD_REQUEST)
					.type(MediaType.APPLICATION_XML).entity("login first")
					.build();
		}

		MetaResourceDbWrapper metaResourceDb = (MetaResourceDbWrapper) session
				.getAttribute("MetaResourceDbWrapper");

		// request.getSession().setAttribute(CART_SESSION_KEY, cartBean);
		String msg = null;
		Resource r = null;
		System.out.println("searhcing particular resource2:<" + resourceName
				+ "> with id:<" + id + ">");
		Class c = FhirUtil.getResourceClass(resourceName);
		if (c == null)
			throw new RuntimeException("class not found for resource:"
					+ resourceName);

		// metaResourceDb.addMetaResourceSet(getPatientAndMedicationStatementEg());

		r = metaResourceDb.getParticularResource(c, id);
		msg = FhirUtil.resourceToXml(r, c);
		if (acceptHeader.equals("application/json")) {
			msg = Utils.xmlToJson(FhirUtil.resourceToXml(r, c));
		}
		if (// (acceptHeader.equals("application/xml")||acceptHeader.equals("application/json"))&&
		r != null) {
			return Response.ok(msg).build();
		} else {
			return Response
					.noContent()
					.header("xreason",
							resourceName + " with id:" + id + " NOT found")
					.build();
		}
	}

	private String processXquery(String query, String input) {

		return XQueryUtil.processXQuery(query, input);
	}

	public void stringToFile(String FileName, String str) throws IOException {
		Files.write(
				Paths.get(context.getInitParameter("storedFilePath ")
						+ "/tempinfo.xml"), str.getBytes());

	}

	private MetaResourceSet getEGPatient()
			throws DatatypeConfigurationException {
		Patient p = new Patient();
		p.setId("Patient/1000000005");
		
		MetaData md1 = new MetaData();
		GregorianCalendar gc = new GregorianCalendar();
		md1.setLastUpdated(DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(gc));

		md1.setId(p.getId());
		
		MetaResource mr1 = new MetaResource();
		mr1.setResource((Resource) p);
		mr1.setMetaData(md1);
		
		MetaResourceSet s1 = new MetaResourceSet();
		s1.getMetaResource().add(mr1);

		return s1;
	}

	private MetaResourceSet getPatientAndMedicationStatementEg()
			throws DatatypeConfigurationException {
		MetaResourceSet s1 = new MetaResourceSet();

		Patient p = new Patient();
		p.setId("Patient/1000000005");

		MedicationStatement ms = new MedicationStatement();
		ms.setId("MedicationStatement/1000000005-1");
		ResourceReference pRef = new ResourceReference();
		pRef.setId(p.getId());
		ms.setPatient(pRef);

		MetaResource mr1 = new MetaResource();
		MetaData md1 = new MetaData();
		md1.setId(p.getId());
		mr1.setResource(p);
		mr1.setMetaData(md1);
		GregorianCalendar gc = new GregorianCalendar();
		md1.setLastUpdated(DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(gc));
		s1.getMetaResource().add(mr1);

		MetaResource mr2 = new MetaResource();
		MetaData md2 = new MetaData();
		md2.setId(ms.getId());
		mr2.setResource(ms);
		mr2.setMetaData(md2);
		md2.setLastUpdated(DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(gc));

		s1.getMetaResource().add(mr2);
		return s1;
	}

}
