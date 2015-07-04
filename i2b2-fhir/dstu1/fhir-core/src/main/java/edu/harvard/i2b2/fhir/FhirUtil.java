package edu.harvard.i2b2.fhir;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import javax.xml.bind.PropertyException;
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

	final public static String RESOURCE_LIST_REGEX = "(Medication|MedicationStatement|MedicationPresricption|Observation|Patient)";
	public static ArrayList<Class> resourceClassList = null;

	private static Validator v;

	public final static String namespaceDeclaration = "declare default element namespace \"http://hl7.org/fhir\";"
			+ "declare namespace i=\"http://i2b2.harvard.edu/fhir/core\";";

	static {
		initResourceClassList();
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

			for (MetaResource mr : s.getMetaResource()) {
				Resource r = mr.getResource();
				MetaData m = mr.getMetaData();
				for (Class c : getResourceClassList()) {

					if (c.isInstance(r)) {
						JAXBContext jaxbContext = JAXBUtil.getJaxBContext(c);// JAXBContext.newInstance(c);
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
			if (mr.getMetaData().getId().equals(id))
				return true;
		}
		return false;

	}
	
	public static List<Object> getChildrenThruChain(Resource r, String pathStr,
			MetaResourceSet s)
			// is dot separated path
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		
		List<Object> children= new ArrayList<Object>();
		List<Object> resolvedChildren= new ArrayList<Object>();
		logger.trace("got obj:"+r);
		Class c = FhirUtil.getResourceClass(r);
		
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
		Object o = method.invoke(c.cast(r));
		
		if (List.class.isInstance(o)){
			children.addAll((List<Object>) o);
		}else {children.add(o);}
		
		if (suffix == null) {
			return children;

		} else {
			
			
			for(Object child:children){
				if (ResourceReference.class.isInstance(child)) {
				ResourceReference rr = ResourceReference.class.cast(child);
				logger.trace("gotc:" + child.getClass());
			
				/*try {
					logger.trace("seek:" + JAXBUtil.toXml(rr));
					logger.trace("seek:" + rr.getReference().getValue());
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/

				Resource r1= FhirUtil.findResourceById(rr.getReference().getValue(),s);
				resolvedChildren.add( getChildrenThruChain(r1, suffix, s));
				
				} else {
					resolvedChildren.add(getChildrenThruChain(r, suffix, s));
				}
				
			}
		}
		return resolvedChildren;
	}

	public static Object getChildThruChain(Resource r, String pathStr,
			MetaResourceSet s)
			// is dot separated path
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Class c = FhirUtil.getResourceClass(r);
		
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
		Object o = method.invoke(c.cast(r));
		
		if (suffix == null) {
			return o;

		} else {
			if (ResourceReference.class.isInstance(o)) {
				ResourceReference rr = ResourceReference.class.cast(o);
				logger.trace("gotc:" + o.getClass());

				/*try {
					logger.trace("seek:" + JAXBUtil.toXml(rr));
					logger.trace("seek:" + rr.getReference().getValue());
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/

				Resource r1= FhirUtil.findResourceById(rr.getReference().getValue(),s);
				return getChildThruChain(r1, suffix, s);
			} else {
				return getChildThruChain(r, suffix, s);
			}
		}
	}

	public static Resource findResourceById(String id, MetaResourceSet s) {
		for (MetaResource mr : s.getMetaResource()) {
			Resource r = mr.getResource();
			if (r.getId().equals(id))
				return r;
		}
		return null;

	}

	public static Object getChild(Resource r, String pathStr)
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

	public static MetaResource getMetaResource(Resource r)
			throws FhirCoreException {

		MetaData md = new MetaData();
		GregorianCalendar gc = new GregorianCalendar();
		try {
			md.setLastUpdated(DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gc));
		} catch (DatatypeConfigurationException e) {
			throw new FhirCoreException("error", e);
		}

		MetaResource mr = new MetaResource();
		mr.setMetaData(md);
		mr.setResource(r);

		return mr;

	}

	public static String getResourceXml(String id, String metaResourceSetXml)
			throws FhirCoreException {
		String xml;
		String xQuery = namespaceDeclaration + "//i:Resource[@id='" + id + "']";

		// logger.trace("xml:" + metaResourceSetXml);
		String res;
		try {
			res = XQueryUtil.processXQuery(xQuery, metaResourceSetXml);
		} catch (XQueryUtilException e) {
			throw new FhirCoreException(e);
		}
		// logger.trace("res:" + res);
		return res;

	}

}
