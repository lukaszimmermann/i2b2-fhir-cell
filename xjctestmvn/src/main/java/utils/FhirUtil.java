package utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import org.hl7.fhir.Resource;
import org.hl7.fhir.instance.validation.Validator;

public class FhirUtil {
	
	private static Validator v;
	
	private static final String RESOURCE_LIST = "(patient)|(medication)|(observation)";
	private static List<Class> resourceClassList;

	public static String resourceToXml(Resource r, Class c) {
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

	public static String resourceToXml(Resource r) {

		StringWriter strw = new StringWriter();
		JAXBElement jbe = null;
		boolean classFound=false;
		for (Class c : getResourceClasses()) {
			if (c.isInstance(r)) {
				try {
					jbe = new JAXBElement(new QName("http://hl7.org/fhir",
							c.getSimpleName()), c, c.cast(r));
					JAXBContext jc = JAXBContext.newInstance(c);
					Marshaller marshaller = jc.createMarshaller();
					marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
							Boolean.TRUE);
					marshaller.marshal(jbe, strw);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
				classFound=true;
			}
		}
		if (!classFound) throw new RuntimeException("could not find class of the resource");
		return strw.toString();
	}

	public static Resource xmlToResource(String xml) {
		Resource r = null;
		
		try {
			JAXBContext jc = JAXBContext.newInstance(Resource.class);
			Unmarshaller unMarshaller = jc.createUnmarshaller();

			r = (Resource) unMarshaller.unmarshal(new StringReader(xml));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return r;
	}

	public static List<Resource> xmlToResource(List<String> xmlList) {
		List<Resource> resList= new ArrayList<Resource>();
		for(String xmlStr:xmlList) resList.add(xmlToResource(xmlStr));
		return resList;
	}
	public static String getResourceBundle(List<Resource> resList, String uriInfoString) {
		String fhirBase="http://localhost:8080/fhir-server-api/resources/res/";
		
		Abdera abdera = new Abdera();
		Writer writer1 = abdera.getWriterFactory().getWriter("prettyxml");
		Feed feed = abdera.newFeed();

		StringWriter swriter = new StringWriter();
		try {

			feed.setId(uriInfoString);
			feed.setTitle("all class" + " bundle");
			feed.setUpdated(new Date());
			feed.addExtension("http://www.w3.org/2005/Atom","published",null).setText(new Date().toGMTString());
			feed.addLink(fhirBase+uriInfoString).setAttributeValue("rel", "self");
			feed.addLink(fhirBase).setAttributeValue("rel", "fhir-base");

			
			feed.addExtension("http://a9.com/-/spec/opensearch/1.1/", "result", "os").setText("#count");
			StringWriter rwriter = new StringWriter();
			// for(Resource r:resourcedb.getAll(c)){
			for (Resource r : resList) {
				for (Class c : getResourceClasses()) {
					JAXBContext jaxbContext = JAXBContext.newInstance(c);
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					jaxbMarshaller.setProperty(
							Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

					if (c.isInstance(r)) {
						Entry entry = feed.addEntry();
						entry.setId(fhirBase+r.getId());
						entry.setUpdated(new Date());
						entry.addExtension("http://www.w3.org/2005/Atom","published",null).setText(new Date().toGMTString());
						entry.addLink(fhirBase+r.getId()).setAttributeValue("rel", "self");
						
						jaxbMarshaller.marshal(r, rwriter);
						entry.setContent(rwriter.toString(), "application/xml");
						rwriter.getBuffer().setLength(0);// reset String writer
					}
				}
			}
			writer1.writeTo(feed, swriter);
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		return swriter.toString();
	}

	
	public static List<Class> getResourceClasses() {
		if (resourceClassList == null) {
			resourceClassList = new ArrayList<Class>();
			for (String x : RESOURCE_LIST.split("\\|")) {
				x = x.replace("(", "").replace(")", "");
				Class y = getResourceClass(x);
				if (y != null)
					resourceClassList.add(y);
			}
		}
		return resourceClassList;

	}

	public static Class getResourceClass(String resourceName) {
		ClassLoader loader = FhirUtil.class.getClassLoader();
		System.out.println("processing resourceName:" + resourceName);
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
	public static String getValidatorErrorMessage(String input) {
		 
		String msg="";
		System.out.println("running validator for input:" + input);
		try {
			if (v==null) init();
			v.setSource(input);
			v.process();
			
		} catch (Exception e) {
			msg= e.getMessage();
		}
		return msg;
		
	}
	
	public static boolean isValid(String xml) {
		try {
			if (v==null) init();
			//System.out.println("setting source:"+xml);
			v.setSource(xml);
			v.process();
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static void init() {
		v = new Validator();
		//System.out.println("P:"+context.getRealPath("/validation.zip"));
		//v.setDefinitions(context.getRealPath("/validation.zip"));
		//System.out.println("url:");
		String path=Utils.getFilePath("validation.zip");//this.getClass().getResource("validation.zip").getPath();
		//System.out.println("url:"+path);
	
		v.setDefinitions(path);
		System.out.println(v.getDefinitions());
		System.out.println("ready");
	}
	


}
