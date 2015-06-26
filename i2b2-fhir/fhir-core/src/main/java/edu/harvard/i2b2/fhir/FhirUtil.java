package edu.harvard.i2b2.fhir;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
import org.apache.abdera.ext.opensearch.model.Url;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.parser.Parser;
import org.apache.abdera.writer.Writer;
import org.hl7.fhir.Bundle;
import org.hl7.fhir.BundleEntry;
import org.hl7.fhir.Id;
import org.hl7.fhir.Medication;
import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Reference;
import org.hl7.fhir.Resource;
import org.hl7.fhir.Reference;
import org.hl7.fhir.ResourceContainer;
import org.hl7.fhir.instance.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.core.MetaData;

public class FhirUtil {
	// public static boolean validateFhirResourceBeforeAddingFlag=false;

	static Logger logger = LoggerFactory.getLogger(FhirUtil.class);

	// final public static String RESOURCE_LIST_REGEX =
	// "("+FhirUtil.getResourceList().toString().replace(",", "|")
	// .replaceAll("[\\s\\[\\]]+", "")+")";

	final public static String RESOURCE_LIST_REGEX = "(Medication|MedicationStatement|Observation|Patient)";
	public static ArrayList<Class> resourceClassList = null;

	private static Validator v;

	public final static String namespaceDeclaration = "declare default element namespace \"http://hl7.org/fhir\";";
		
	static {
		initResourceClassList();
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
		//logger.trace("Class Not Found for FHIR resource:" + resourceName);
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

	

	static public boolean isContained(List<Resource> outList, String idStr) {
		for (Resource r : outList) {
			if (r.getId().getValue().equals(idStr))
				return true;
		}
		return false;

	}
	
	public static List<Object> getChildrenThruChain(Resource r, String pathStr,
			List<Resource> s)
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
				if (Reference.class.isInstance(child)) {
				Reference rr = Reference.class.cast(child);
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
			List<Resource> s)
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
			if (Reference.class.isInstance(o)) {
				Reference rr = Reference.class.cast(o);
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

	public static Resource findResourceById(String idString, List<Resource> s) {
		for (Resource r : s) {
			if (r.getId().getValue().equals(idString))
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
			if (Reference.class.isInstance(o)) {
				return o;
			} else {
				return o;
			}
		} else {
			return getChild(r, suffix);
		}

	}

	

	//TO rename it
	public static List<String> getResourceList() {
		return Arrays.asList(Utils.getFile("resourceList.txt").split("\\n"));
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
	
	public static Resource setId(Resource r, String idStr){
		Id id =new Id();
		id.setValue(idStr);;
		r.setId(id);
		return r;
	}

	public static Reference getReference(Resource r) {
		Reference pRef = new Reference();
		org.hl7.fhir.String str1=new org.hl7.fhir.String();
		str1.setValue(r.getId().getValue());
		pRef.setReference(str1);
		return pRef;
	}

	
	
	static public org.hl7.fhir.String getFhirString(String s1){
		org.hl7.fhir.String s2=new org.hl7.fhir.String();
		s2.setValue(s1);
		return s2;
	}

	public static Bundle getResourceBundle(List<Resource> s, String basePath,
			String url) {
		Bundle b= new Bundle();
		for(Resource r:s){
			BundleEntry be=new BundleEntry();
			ResourceContainer rc=FhirUtil.getResourceContainer(r);
			
			be.setResource(rc);
			b.getEntry().add(be);
		}
		return b;
	}

	private static ResourceContainer getResourceContainer(Resource r) {
		ResourceContainer rc=new ResourceContainer();
		String rClass=FhirUtil.getResourceClass(r).getSimpleName();
		switch(rClass){
			case "Patient": rc.setPatient((Patient)r);break;
			case "Medication": rc.setMedication((Medication)r);break;
			case "MedicationStatement": rc.setMedicationStatement((MedicationStatement)r);break;
		}
		return rc;
	}

	public static List<Resource> getResourceListFromBundle(Bundle b) {
		List<Resource> list=new ArrayList<Resource>();
		for(BundleEntry e:b.getEntry()){
			e.getResource();
			list.add(FhirUtil.getResourceFromContainer(e.getResource()));
		}
		return list;
	}

	private static Resource getResourceFromContainer(ResourceContainer rc) {
		Resource r=null;
		if(rc.getPatient()!=null) return rc.getPatient();
		if(rc.getMedication()!=null) return rc.getMedication();
		if(rc.getMedicationStatement()!=null) return rc.getMedicationStatement();
		if(rc.getSearchParameter()!=null) return rc.getSearchParameter();
		
		throw new RuntimeException("Not implemented all resource types");
		}

	
}
