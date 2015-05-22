package edu.harvard.i2b2.fhirserver.ws;


import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhirserver.ejb.ResourceDb;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.writer.Writer;
import org.hl7.fhir.Resource;
import org.json.JSONException;
import org.json.XML;

@Path("res")
public class ResourceWebService {
	
	@EJB
	ResourceDb resourcedb;
	
	@javax.ws.rs.core.Context
	ServletContext context;

	@GET
	// @Path("{resourceName:[a-z]+}/{id:[0-9]+}")
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX + "}/{id:[0-9]+}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getParticularResource(
			@PathParam("resourceName") String resourceName,
			@PathParam("id") String id,
			@HeaderParam("accept") String acceptHeader) throws JSONException, JAXBException {
		String msg = null;
		Resource r = null;
		System.out.println("searhcing particular resource2:<" + resourceName
				+ "> with id:<" + id + ">");
		Class c = FhirUtil.getResourceClass(resourceName);
		if (c == null)
			throw new RuntimeException("class not found for resource:"+ resourceName);

		r = resourcedb.getParticularResource(c, id);
		msg = FhirUtil.resourceToFhirXml(r, c);
		if (acceptHeader.equals("application/json")) {
			msg = Utils.xmlToJson(FhirUtil.resourceToFhirXml(r, c));
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

	

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX  + "}")
	public Response putParticularResource(
			@PathParam("resourceName") String resourceName,
			String xml
			//,Object r
			) throws JAXBException {
		
		Resource r= FhirUtil.fromXml(xml);
			
			System.out.println("putting  particular resource2:<" + resourceName
					+ ">");
			//System.out.println("object:" + r.toString());
			System.out
					.println("putting  particular resource2:<" + (Resource) r);
			try {
				Class c = FhirUtil.getResourceClass(resourceName);
				if (c == null)
				throw new RuntimeException("class not found for resource:"+ resourceName);
			
				if(!FhirUtil.isValid(xml)){
					String errorMsg=FhirUtil.getValidatorErrorMessage(xml);
					return Response.status(Status.BAD_REQUEST).header("xreason",
							errorMsg
							//v.getOutcome().toString()//@TODO avoid double validation_process
							).build();
				}	
				String id = resourcedb.addResource((Resource) r, c);
				if (id != null) {
						ResponseBuilder respbuild= Response.created(URI.create("res/"+resourceName + "/"+id));
						if (r.getId()!=null) respbuild.header("xreason", "updated existing resource").build();
						return respbuild.build();
				} else {
					throw new RuntimeException("resource not stored");
				}
			
			} catch (Exception e) {
				e.printStackTrace();
				//return Response.serverError().header("xreason", e.getMessage()).build();
				return Response.status(Status.BAD_REQUEST).header("xreason", e.getMessage()).build();
			}
	}

	//XXX to delegate to FhirHelper
	@GET
	@Path("{resourceName:" + FhirUtil.RESOURCE_LIST_REGEX  + "(//_search)[0,1]}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String getResourceBundle(
			@PathParam("resourceName") String resourceName,
			@HeaderParam("accept") String acceptHeader,
			@Context UriInfo uriInfo) {
		
			MultivaluedMap<String,String> q= uriInfo.getQueryParameters();
			
		
			Abdera abdera = new Abdera();
			Writer writer1 = abdera.getWriterFactory().getWriter();//.getWriter("prettyxml");
			Feed feed = abdera.newFeed();
			 
			StringWriter swriter=new StringWriter();
			  try {
				  Class c = FhirUtil.getResourceClass(resourceName);
				  feed.setId(uriInfo.getRequestUri().toString());
				  feed.setTitle(c.getSimpleName()+" bundle");
				  feed.setUpdated(new Date());
				  JAXBContext jaxbContext = JAXBContext.newInstance(c);
				  Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				  jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				  
				  StringWriter rwriter=new StringWriter();
				  //for(Resource r:resourcedb.getAll(c)){
				  for(Resource r:resourcedb.getQueried(c,q)){
					  if(c.isInstance(r)){
						Entry entry = feed.addEntry();
						entry.setId(r.getId());
						jaxbMarshaller.marshal(r, rwriter);
						entry.setContent(rwriter.toString(),"application/xml");
						rwriter.getBuffer().setLength(0);//reset String writer
					  }
					}
				writer1.writeTo(feed,swriter);
			} catch (IOException|JAXBException e) {
				e.printStackTrace();
			}
			return swriter.toString();
	}
			
	

	@PostConstruct
	private void init() {
		FhirUtil.initResourceClassList();
	}

	

	
	 public static String getResourceBundle(List<Resource> resList,String uriInfoString) {
			
		return "XX delegate to FhirHelper";
	}
 
	
 
	
}
