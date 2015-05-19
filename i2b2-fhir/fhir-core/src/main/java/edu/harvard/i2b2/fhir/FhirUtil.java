package edu.harvard.i2b2.fhir;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaFileManager.Location;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.writer.Writer;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.hl7.fhir.ResourceReference;
import org.hl7.fhir.instance.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.MetaData;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class FhirUtil {
	// public static boolean validateFhirResourceBeforeAddingFlag=false;

	static Logger logger = LoggerFactory.getLogger(FhirUtil.class);

	// final public static String RESOURCE_LIST_REGEX =
	// "("+FhirUtil.getResourceList().toString().replace(",", "|")
	// .replaceAll("[\\s\\[\\]]+", "")+")";

	final public static String RESOURCE_LIST_REGEX = "(Medication|MedicationStatement|Observation|Patient)";
	public static ArrayList<Class> resourceClassList = null;

	private static HashMap<Class, JAXBContext> hmJaxbc = null;

	private static Validator v;

	static {
		initResourceClassList();
	}

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
		init();

		StringWriter strw = new StringWriter();
		JAXBElement jbe = null;
		boolean classFound = false;
		for (Class c : resourceClassList) {
			// logger.trace("instanceOf:"+c.getSimpleName());
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
				classFound = true;
			}
		}
		if (!classFound)
			throw new RuntimeException("could not find class of the resource");
		return strw.toString();
	}

	public static Resource xmlToResource(String xml) {
		if (xml.equals("") || xml == null)
			return null;
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
		List<Resource> resList = new ArrayList<Resource>();
		for (String xmlStr : xmlList)
			resList.add(xmlToResource(xmlStr));
		return resList;
	}

	public static String getResourceBundle(MetaResourceSet s,
			String uriInfoString, String urlString) {
		String fhirBase = uriInfoString;

		Abdera abdera = new Abdera();
		Writer writer1 = abdera.getWriterFactory().getWriter("prettyxml");
		Feed feed = abdera.newFeed();

		StringWriter swriter = new StringWriter();
		try {

			feed.setId(uriInfoString);
			feed.setTitle("Query result");
			feed.setUpdated(new Date());
			feed.addExtension("http://www.w3.org/2005/Atom", "published", null)
					.setText(new Date().toGMTString());
			feed.addLink(urlString).setAttributeValue("rel", "query");
			feed.addLink(uriInfoString).setAttributeValue("rel", "self");
			feed.addLink(fhirBase).setAttributeValue("rel", "fhir-base");

			feed.addExtension("http://a9.com/-/spec/opensearch/1.1/", "result",
					"os").setText(Integer.toString(s.getMetaResource().size()));
			StringWriter rwriter = new StringWriter();

			if (hmJaxbc == null) {
				hmJaxbc = new HashMap<Class, JAXBContext>();

				for (Class c : getResourceClassList()) {
					JAXBContext jaxbContext = JAXBContext.newInstance(c);
					hmJaxbc.put(c, jaxbContext);
				}
			}
			for (MetaResource mr : s.getMetaResource()) {
				Resource r = mr.getResource();
				MetaData m = mr.getMetaData();
				for (Class c : getResourceClassList()) {

					if (c.isInstance(r)) {
						JAXBContext jaxbContext = hmJaxbc.get(c);// JAXBContext.newInstance(c);
						Marshaller jaxbMarshaller = jaxbContext
								.createMarshaller();
						jaxbMarshaller.setProperty(
								Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

						Entry entry = feed.addEntry();
						entry.setId(fhirBase + r.getId());
						String lastUpdated = null;
						try {
							lastUpdated = m.getLastUpdated().toString();
						} catch (Exception e) {
						}
						if (lastUpdated != null)
							entry.setUpdated(lastUpdated);
						// entry.addExtension("http://www.w3.org/2005/Atom","published",null).setText(new
						// Date().toGMTString());
						entry.addLink(fhirBase + r.getId()).setAttributeValue(
								"rel", "self");

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

	private static ArrayList<Class> getResourceClassList() {
		initResourceClassList();
		return resourceClassList;
	}

	public static String getValidatorErrorMessage(String input) {

		String msg = "";
		logger.trace("running validator for input:" + input);
		try {
			if (v == null)
				init();
			v.setSource(input);
			v.process();

		} catch (Exception e) {
			msg = e.getMessage();
		}
		return msg;

	}

	public static boolean isValid(String xml) {
		try {
			if (v == null)
				init();
			// logger.trace("setting source:"+xml);
			v.setSource(xml);
			v.process();
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
		return true;
	}

	private static void init() {
		if (v == null) {
			v = new Validator();
			String path = Utils.getFilePath("validation.zip");

			v.setDefinitions(path);
			// logger.trace(v.getDefinitions());
			// logger.trace("ready");
		}
		if (resourceClassList == null)
			initResourceClassList();
	}

	public List<Class> getResourceClasses() {
		List<Class> classList = new ArrayList<Class>();
		for (String x : RESOURCE_LIST_REGEX.split("|")) {
			x = x.replace("(", "").replace(")", "");
			Class y = getResourceClass(x);
			if (y != null)
				classList.add(y);
		}
		return classList;

	}

	public static void initResourceClassList() {
		if (resourceClassList == null) {
			resourceClassList = new ArrayList<Class>();
			try {
				for (Class c : getAllFhirResourceClasses("org.hl7.fhir")) {

					// logger.trace(c.getSimpleName());
					resourceClassList.add(c);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static List<Class> getAllFhirResourceClasses(String packageName)
			throws IOException {

		// logger.trace("Running getAllFhirResourceClasses for:" +
		// packageName);
		List<Class> commands = new ArrayList<Class>();

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(
				null, null, null);

		Location location = StandardLocation.CLASS_PATH;

		Set<JavaFileObject.Kind> kinds = new HashSet<JavaFileObject.Kind>();
		kinds.add(JavaFileObject.Kind.CLASS);
		boolean recurse = false;

		Iterable<JavaFileObject> list = fileManager.list(location, packageName,
				kinds, recurse);

		for (JavaFileObject javaFileObject : list) {

			// commands.add(javaFileObject.getClass());
		}

		Class c = null;
		for (String x : "org.hl7.fhir.Patient|org.hl7.fhir.Medication|org.hl7.fhir.Observation|org.hl7.fhir.MedicationStatement"
				.split("\\|")) {
			c = Utils.getClassFromClassPath(x);
			if (c != null)
				commands.add(c);
		}
		// logger.trace(commands.toString());
		return commands;
	}

	public static Class getResourceClass(String resourceName) {
		if (resourceClassList == null)
			initResourceClassList();
		for (Class c : resourceClassList) {
			if (c.getSimpleName().toLowerCase()
					.equals(resourceName.toLowerCase()))
				return c;
		}
		logger.trace("Class Not Found for FHIR resource:" + resourceName);
		return null;

	}

	public static Class getResourceClass(Resource resource) {
		if (resourceClassList == null)
			initResourceClassList();
		for (Class c : resourceClassList) {
			if (c.isInstance(resource))
				return c;
		}
		logger.trace("Class Not Found for FHIR resource:" + resource.getId());
		return null;

	}

	static public List<Resource> getResourcesFromMetaResourceSet(
			MetaResourceSet s) {
		List<Resource> list = new ArrayList<Resource>();
		for (MetaResource r : s.getMetaResource()) {
			list.add(r.getResource());
		}
		return list;

	}

	static public boolean isContained(MetaResourceSet s, String id) {
		for (MetaResource mr : s.getMetaResource()) {
			if (mr.getResource().getId().equals(id))
				return true;
		}
		return false;

	}

	static Object getChild(Resource r, String pathStr)
			// is dot separated path
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Class c = FhirUtil.getResourceClass(r);
		String returnStr = null;
		String suffix = null;
		String prefix = pathStr;
		logger.trace("pathStr:" + pathStr);
		if (pathStr.indexOf('.') > -1) {
			suffix = pathStr.substring(pathStr.indexOf('.') + 1);
			prefix = pathStr.substring(0, pathStr.indexOf('.'));
		}
		String methodName = prefix.substring(0, 1).toUpperCase()
				+ prefix.subSequence(1, prefix.length());
		Method method = c.getMethod("get" + methodName, null);
		if (suffix == null) {
			Object o = method.invoke(c.cast(r));
			if (ResourceReference.class.isInstance(o)) {
				return o;
			} else {
				return o;
			}
		} else {
			return getChild(r, suffix);
		}

	}

	static public String getPatientId(MetaResource mr) {
		String id = mr.getResource().getId();
		Resource r = mr.getResource();
		if (Patient.class.isInstance(r)) {
			if (id.contains("/")) {
				return id.split("/")[1];
			} else
				throw new RuntimeException("id is malformed:" + id);
		} else {
			if (id.contains("/") && id.contains("-")) {
				return id.split("/")[1].split("-")[0];
			} else
				throw new RuntimeException("id is malformed:" + id);

		}
	}

	static public ResourceReference getResourceReferenceForRessource(Resource r) {
		ResourceReference rRef = null;
		if (r.getId() != null) {
			rRef = new ResourceReference();
			org.hl7.fhir.String shl7 = new org.hl7.fhir.String();
			shl7.setValue(r.getId());
			rRef.setReference(shl7);
		}
		return rRef;
	}

	public static List<String> getResourceList() {
		return Arrays.asList(Utils.getFile("resourceList.txt").split("\\n"));
	}

	public static MetaResource getMetaResource(Resource r) throws FhirCoreException {

		MetaData md = new MetaData();
		GregorianCalendar gc = new GregorianCalendar();
		try {
			md.setLastUpdated(DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gc));
		} catch (DatatypeConfigurationException e) {
			throw new FhirCoreException("error",e);
		}

		MetaResource mr = new MetaResource();
		mr.setResource((Resource) r);
		mr.setMetaData(md);

		return mr;

	}
}