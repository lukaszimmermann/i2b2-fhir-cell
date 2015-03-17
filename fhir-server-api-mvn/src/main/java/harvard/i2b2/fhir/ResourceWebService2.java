package harvard.i2b2.fhir;

import harvard.i2b2.fhir.ejb.ResourceDb;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.hl7.fhir.instance.validation.Validator;
import org.json.XML;

@Path("res")
public class ResourceWebService2 {
	private static final String RESOURCE_LIST = "(patient)|(medication)|(observation)";

	@EJB
	ResourceDb resourcedb;

	@javax.ws.rs.core.Context
	ServletContext context;

	Validator v;

	@GET
	// @Path("{resourceName:[a-z]+}/{id:[0-9]+}")
	@Path("{resourceName:" + RESOURCE_LIST + "}/{id:[0-9]+}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getParticularResource(
			@PathParam("resourceName") String resourceName,
			@PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader) {
		String msg = null;
		Resource r = null;
		System.out.println("searhcing particular resource2:<" + resourceName
				+ "> with id:<" + id + ">");
		Class c = getResourceClass(resourceName);
		if (c == null)
			throw new RuntimeException("class not found for resource:"+ resourceName);

		r = resourcedb.getParticularResource(c, id);
		msg = resourceToXml(r, c);
		if (acceptHeader.equals("application/json")) {
			msg = xmlToJson(resourceToXml(r, c));
		}
		if (// (acceptHeader.equals("application/xml")||acceptHeader.equals("application/json"))&&
				r != null) {
			return Response.ok(msg).build();
		} else {
			return Response
					.noContent()
					.header("xreason",resourceName + " with id:" + id + " NOT found")
					.build();
		}
	}

	/*@GET
	@Path("patient0")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Patient getPatient() {
		return (Patient) getResource("0", Patient.class);
	}*/

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{resourceName:" + RESOURCE_LIST + "}")
	public Response putParticularResource(
			@PathParam("resourceName") String resourceName,
			String xml
			//,Object r
			) {
		
		Resource r= xmlToResource(xml);
			
			System.out.println("putting  particular resource2:<" + resourceName
					+ ">");
			//System.out.println("object:" + r.toString());
			System.out
					.println("putting  particular resource2:<" + (Resource) r);
			try {
				Class c = getResourceClass(resourceName);
				if (c == null)
				throw new RuntimeException("class not found for resource:"+ resourceName);
			/*r
				if(!isValid(r)){
					return Response.status(Status.BAD_REQUEST).header("xreason",
							validate(r.toString())
							//v.getOutcome().toString()
							).build();
				}	
			*/
				String id = resourcedb.addResource((Resource) r, c);
				if (id != null) {
					return Response.created(URI.create(resourceName + "/"+id)).build();
				} else {
					throw new RuntimeException("resource not stored");
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				return Response.serverError().header("xreason", e.getMessage()).build();
			}
	}

	private Class getResourceClass(String resourceName) {
		ClassLoader loader = this.getClass().getClassLoader();
		String targetClassName = "org.hl7.fhir."
				+ resourceName.substring(0, 1).toUpperCase()
				+ resourceName.substring(1, resourceName.length());
		try {
			return Class.forName(targetClassName, false, loader);
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Found for FHIR resource:"
					+ resourceName);
			return null;
		}
	}

	@POST
	@Path("_validate")
	public String validate(String input) {
		System.out.println("running validator for input:" + input);
		try {
			v.setSource(input);
			v.process();
			return v.getOutcome().toString();
		} catch (Exception e) {
			// e.printStackTrace();
			return "error:" + e.getMessage();
		}

	}
	
	public boolean isValid(Object r) {
		try {
			v.setSource(r.toString());
			v.process();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@PostConstruct
	private void init() {
		v = new Validator();
		// System.out.println("P:"+context.getRealPath("/validation.zip"));
		v.setDefinitions(context.getRealPath("/validation.zip"));
		System.out.println(v.getDefinitions());
		System.out.println("ready");
	}

	private static String xmlToJson(String xmlStr) {
		return XML.toJSONObject(xmlStr).toString(2);
	}

	private static String resourceToXml(Resource r, Class c) {
		StringWriter strw = new StringWriter();
		JAXBElement jbe = null;
		jbe = new JAXBElement(new QName("http://hl7.org/fhir",
				c.getSimpleName()), c, c.cast(r));
		try {
			JAXBContext jc = JAXBContext.newInstance(c);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			marshaller.marshal(jbe, strw);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return strw.toString();
	}
	
	private static Resource xmlToResource(String xml) {
		Resource r=null;
		try {
			JAXBContext jc = JAXBContext.newInstance(Resource.class);
			Unmarshaller unMarshaller = jc.createUnmarshaller();
		
			r=(Resource)unMarshaller.unmarshal(new StringReader(xml));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return r;
	}

}
