/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package edu.harvard.i2b2.fhir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaFileManager.Location;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.hl7.fhir.BundleEntry;
import org.hl7.fhir.BundleLink;
import org.hl7.fhir.BundleType;
import org.hl7.fhir.BundleTypeList;
import org.hl7.fhir.Code;
import org.hl7.fhir.CodeableConcept;
import org.hl7.fhir.Coding;
import org.hl7.fhir.DiagnosticReport;
import org.hl7.fhir.Id;
import org.hl7.fhir.Instant;
import org.hl7.fhir.Medication;
import org.hl7.fhir.MedicationDispense;
import org.hl7.fhir.MedicationOrder;
import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.Meta;
import org.hl7.fhir.Observation;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Condition;
import org.hl7.fhir.Reference;
import org.hl7.fhir.Resource;
import org.hl7.fhir.ResourceContainer;
import org.hl7.fhir.UnsignedInt;
import org.hl7.fhir.Uri;
import org.hl7.fhir.dstu21.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import wrapperHapi.WrapperHapi;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.query.SearchParameterMap;

public class FhirUtil {
	// public static boolean validateFhirResourceBeforeAddingFlag=false;

	static Logger logger = LoggerFactory.getLogger(FhirUtil.class);

	// final public static String RESOURCE_LIST_REGEX =
	// "("+FhirUtil.getResourceList().toString().replace(",", "|")
	// .replaceAll("[\\s\\[\\]]+", "")+")";

	final public static String RESOURCE_LIST_REGEX = "(Bundle|Condition|Medication|MedicationStatement|MedicationDispense|MedicationOrder|Observation|Patient|DiagnosticReport|DiagnosticOrder|DecisionSupportServiceModule|Parameters|Order|OperationOutcome|GuidanceResponse)";
	private static ArrayList<Class> resourceClassList = null;

	private static Validator v;

	public final static String namespaceDeclaration = "declare default element namespace \"http://hl7.org/fhir\";";

	// http://jboss-javassist.github.io/javassist/tutorial/tutorial.html#load
	public static void addJAXBAnnotationsToClasses() {
		try {
			String className = "org.hl7.fhir.Observation";// Observation.class.getName();
			ClassPool pool = ClassPool.getDefault();
			// extracting the class
			CtClass cc = pool.get(className);
			ClassFile cf = cc.getClassFile();
			ConstPool cp = cf.getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);

			Annotation a = new Annotation("javax.xml.bind.annotation.XmlRootElement", cp);
			a.addMemberValue("name", new StringMemberValue(className.substring(className.lastIndexOf(".") + 1), cp));
			attr.setAnnotation(a);
			cf.addAttribute(attr);
			// cf.setVersionToJava5();

			// myClassReloadingFactory.newInstance("com.jenkov.MyObject");
			// print classAnnotations
			Class aClass = cc.toClass();
			/*
			 * java.lang.annotation.Annotation[] annotations =
			 * aClass.getAnnotations();
			 * 
			 * for(java.lang.annotation.Annotation annotation : annotations){
			 * //if(annotation instanceof MyAnnotation){ logger.trace("annot: "
			 * + annotation); //} }
			 */

		} catch (CannotCompileException |
				// | IOException | IllegalConnectorArgumentsException|
				NotFoundException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static ArrayList<Class> getResourceClassList() {
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
		return resourceClassList;
	}

	public static String getValidatorErrorMessageForProfile(String input, String profile) {
		String msg = "";
		logger.trace("running validator for input:" + input);
		try {
			if (v == null)
				init();

			File temp = File.createTempFile("tempFileForValidator", ".tmp");
			FileOutputStream outTemp = new FileOutputStream(temp);
			IOUtils.write(input, outTemp);
			outTemp.close();
			logger.trace("tmp file:" + new Scanner(temp).useDelimiter("\\Z").next());// temp.getAbsolutePath());

			v.setSource(temp.getPath());
			v.setProfile(profile);
			logger.trace("source" + v.getSource() + " \n profile:" + profile);
			v.process();

			temp.delete();
			return v.getOutcome().toString();

		} catch (Exception e) {
			msg = e.getMessage();
			logger.error(e.getMessage(), e);
		}
		return msg;

	}

	public static String getValidatorErrorMessage(String input) {
		return getValidatorErrorMessageForProfile(input, null);
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
			try {
				v = new Validator();
				// String path = Utils.getFilePath("validation-min.xml.zip");

				File temp = File.createTempFile("tempFileForDefinitions", ".tmp");
				FileOutputStream outTemp = new FileOutputStream(temp);

				IOUtils.copy(FhirUtil.class.getResourceAsStream("/validation-min.xml.zip"), outTemp);

				v.setDefinitions(temp.getAbsolutePath());

				logger.trace(v.getDefinitions());
				logger.trace("ready");
				temp.deleteOnExit();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

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

	public static List<Class> getAllFhirResourceClasses(String packageName) throws IOException {

		// logger.trace("Running getAllFhirResourceClasses for:" +
		// packageName);
		List<Class> commands = new ArrayList<Class>();

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

		Location location = StandardLocation.CLASS_PATH;

		Set<JavaFileObject.Kind> kinds = new HashSet<JavaFileObject.Kind>();
		kinds.add(JavaFileObject.Kind.CLASS);
		boolean recurse = false;

		Iterable<JavaFileObject> list = fileManager.list(location, packageName, kinds, recurse);

		for (JavaFileObject javaFileObject : list) {

			// commands.add(javaFileObject.getClass());
		}

		Class c = null;

		for (String x : Arrays.asList(RESOURCE_LIST_REGEX.replace("(", "").replace(")", "").split("\\|")))

		{
			x = "org.hl7.fhir." + x;
			// logger.trace("X:"+x);
			c = Utils.getClassFromClassPath(x);
			if (c != null)
				commands.add(c);
		}
		// logger.trace(commands.toString());
		return commands;
	}

	public static Class getResourceClass(String resourceName) {
		for (Class c : getResourceClassList()) {
			if (c.getSimpleName().toLowerCase().equals(resourceName.toLowerCase()))
				return c;
		}
		// logger.trace("Class Not Found for FHIR resource:" + resourceName);
		return null;

	}

	public static Class getResourceClass(Resource resource) {
		for (Class c : getResourceClassList()) {
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

	public static List<Object> getChildrenThruChain(Resource r, String pathStr, List<Resource> s)
			// is dot separated path
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		List<Object> children = new ArrayList<Object>();
		List<Object> resolvedChildren = new ArrayList<Object>();
		logger.trace("got obj:" + r);
		Class c = FhirUtil.getResourceClass(r);

		String suffix = null;
		String prefix = pathStr;
		logger.trace("pathStr:" + pathStr);

		if (pathStr.indexOf('.') > -1) {
			suffix = pathStr.substring(pathStr.indexOf('.') + 1);
			prefix = pathStr.substring(0, pathStr.indexOf('.'));
		}

		String methodName = prefix.substring(0, 1).toUpperCase() + prefix.subSequence(1, prefix.length());
		Method method = c.getMethod("get" + methodName, null);
		Object o = method.invoke(c.cast(r));

		if (List.class.isInstance(o)) {
			children.addAll((List<Object>) o);
		} else {
			children.add(o);
		}

		if (suffix == null) {
			return children;

		} else {

			for (Object child : children) {
				if (Reference.class.isInstance(child)) {
					Reference rr = Reference.class.cast(child);
					logger.trace("gotc:" + child.getClass());

					/*
					 * try { logger.trace("seek:" + JAXBUtil.toXml(rr));
					 * logger.trace("seek:" + rr.getReference().getValue()); }
					 * catch (JAXBException e) { // TODO Auto-generated catch
					 * block e.printStackTrace(); }
					 */

					Resource r1 = FhirUtil.findResourceById(rr.getReference().getValue(), s);
					resolvedChildren.add(getChildrenThruChain(r1, suffix, s));

				} else {
					resolvedChildren.add(getChildrenThruChain(r, suffix, s));
				}

			}
		}
		return resolvedChildren;
	}

	public static Object getChildThruChain(Resource r, String pathStr, List<Resource> s)
			// is dot separated path
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Class c = FhirUtil.getResourceClass(r);

		String suffix = null;
		String prefix = pathStr;
		logger.trace("pathStr:" + pathStr);

		if (pathStr.indexOf('.') > -1) {
			suffix = pathStr.substring(pathStr.indexOf('.') + 1);
			prefix = pathStr.substring(0, pathStr.indexOf('.'));
		}

		String methodName = prefix.substring(0, 1).toUpperCase() + prefix.subSequence(1, prefix.length());
		Method method = c.getMethod("get" + methodName, null);
		Object o = method.invoke(c.cast(r));

		if (suffix == null) {
			return o;

		} else {
			if (Reference.class.isInstance(o)) {
				Reference rr = Reference.class.cast(o);
				logger.trace("gotc:" + o.getClass());

				/*
				 * try { logger.trace("seek:" + JAXBUtil.toXml(rr));
				 * logger.trace("seek:" + rr.getReference().getValue()); } catch
				 * (JAXBException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */

				Resource r1 = FhirUtil.findResourceById(rr.getReference().getValue(), s);
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
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
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
		String methodName = prefix.substring(0, 1).toUpperCase() + prefix.subSequence(1, prefix.length());
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

	// TO rename it
	public static List<String> getResourceList() {
		return Arrays.asList(Utils.getFile("resourceList.txt").split("\\n"));
	}

	public static String getResourceXml(String id, String metaResourceSetXml) throws FhirCoreException {
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

	public static Resource setId(Resource r, String idStr) {
		Id id = new Id();
		id.setValue(idStr);

		r.setId(id);
		return r;
	}

	public static Reference getReference(Resource r) {
		Reference pRef = new Reference();
		Class resourceClass = FhirUtil.getResourceClass(r);
		org.hl7.fhir.String str1 = new org.hl7.fhir.String();
		// str1.setValue(r.getId().getValue());
		str1.setValue(resourceClass.getSimpleName() + "/" + r.getId().getValue());
		pRef.setReference(str1);
		return pRef;
	}

	static public org.hl7.fhir.String getFhirString(String s1) {
		org.hl7.fhir.String s2 = new org.hl7.fhir.String();
		s2.setValue(s1);
		return s2;
	}

	public static Bundle getResourceBundle(List<Resource> s, String basePath, String url) {
		Bundle b = new Bundle();
		for (Resource r : s) {
			if (r.getMeta() == null) {
				r.setMeta(FhirUtil.createMeta());
			}
			BundleEntry be = FhirUtil.newBundleEntryForResource(r);
			b.getEntry().add(be);

		}

		BundleType value = new BundleType();
		value.setValue(BundleTypeList.SEARCHSET);
		b.setType(value);
		UnsignedInt total = new UnsignedInt();
		total.setValue(BigInteger.valueOf(s.size()));
		b.setTotal(total);

		Uri u = new Uri();
		u.setValue(basePath);

		FhirUtil.setId(b, Long.toHexString(new Random().nextLong()));

		b.setMeta(FhirUtil.createMeta());

		// b.setBase(u);
		return b;
	}

	private static Meta createMeta() {
		Meta meta = new Meta();
		Id vId = new Id();
		vId.setValue("1");
		meta.setVersionId(vId);
		Instant instantVal = new Instant();
		XMLGregorianCalendar xmlGregvalue;
		try {
			xmlGregvalue = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
		} catch (DatatypeConfigurationException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		instantVal.setValue(xmlGregvalue);
		meta.setLastUpdated(instantVal);
		return meta;
	}

	static BundleEntry newBundleEntryForResource(Resource r) {
		BundleEntry be = new BundleEntry();
		ResourceContainer rc = FhirUtil.getResourceContainer(r);
		be.setResource(rc);
		return be;
	}

	public static ResourceContainer getResourceContainer(Resource r) {
		ResourceContainer rc = new ResourceContainer();
		String rClass = FhirUtil.getResourceClass(r).getSimpleName();
		switch (rClass) {
		case "Patient":
			rc.setPatient((Patient) r);
			break;
		case "Medication":
			rc.setMedication((Medication) r);
			break;
		case "MedicationStatement":
			rc.setMedicationStatement((MedicationStatement) r);
			break;
		case "MedicationOrder":
			rc.setMedicationOrder((MedicationOrder) r);
			break;
		case "MedicationDispense":
			rc.setMedicationDispense((MedicationDispense) r);
			break;
		case "Condition":
			rc.setCondition((Condition) r);
			break;
		case "Observation":
			rc.setObservation((Observation) r);
			break;
		case "Bundle":
			rc.setBundle((Bundle) r);
			break;
		case "DiagnosticReport":
			rc.setDiagnosticReport((DiagnosticReport) r);
			break;
		default:
			throw new RuntimeException("ResourceType not found:" + rClass);
		}
		return rc;
	}

	public static List<Resource> getResourceListFromBundle(Bundle b) {
		List<Resource> list = new ArrayList<Resource>();
		for (BundleEntry e : b.getEntry()) {
			e.getResource();
			list.add(FhirUtil.getResourceFromContainer(e.getResource()));
		}
		return list;
	}

	public static Resource getResourceFromContainer(ResourceContainer rc) {
		Resource r = null;
		if (rc.getPatient() != null)
			return rc.getPatient();
		if (rc.getMedication() != null)
			return rc.getMedication();
		if (rc.getMedicationStatement() != null)
			return rc.getMedicationStatement();
		if (rc.getMedicationDispense() != null)
			return rc.getMedicationDispense();
		if (rc.getMedicationOrder() != null)
			return rc.getMedicationOrder();
		if (rc.getCondition() != null)
			return rc.getCondition();
		if (rc.getObservation() != null)
			return rc.getObservation();
		if (rc.getBundle() != null)
			return rc.getBundle();
		if (rc.getSearchParameter() != null)
			return rc.getSearchParameter();
		if (rc.getDiagnosticReport() != null)
			return rc.getDiagnosticReport();
		if (rc.getDiagnosticOrder() != null)
			return rc.getDiagnosticOrder();
		if (rc.getOrder() != null)
			return rc.getOrder();
		String xml = null;
		try {
			xml = JAXBUtil.toXml(rc);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Not implemented all resource types:" + xml);
	}

	public static String resourceToJsonString(Resource r) throws JAXBException, IOException {
		return WrapperHapi.resourceXmlToJson(JAXBUtil.toXml(r));

	}

	public static String hapiBundleToJsonString(ca.uhn.fhir.model.api.Bundle b) throws Exception {
		return WrapperHapi.bundleToJson(b);
	}

	public static ca.uhn.fhir.model.api.Bundle fhirBundleToHapiBundle(Bundle b) throws Exception {
		ca.uhn.fhir.model.api.Bundle hb = new ca.uhn.fhir.model.api.Bundle();

		// IdDt idb = new IdDt();
		// idb.setValue(b.getId().getValue());
		// id.setValue(fhirBase + UUID.randomUUID().toString());

		// hb.setId(idb);

		String fhirBase = "b.getBase().toString()";

		for (BundleEntry be : b.getEntry()) {
			ResourceContainer rc = be.getResource();

			Resource r = getResourceFromContainer(rc);
			for (Class c : getResourceClassList()) {

				if (c.isInstance(r)) {

					ca.uhn.fhir.model.api.BundleEntry entry = new ca.uhn.fhir.model.api.BundleEntry();
					IdDt ide = new IdDt();
					ide.setValue(r.getId().getValue());
					entry.setId(ide);
					XMLGregorianCalendar lastUpdated = null;
					try {
						lastUpdated = null;
					} catch (Exception ex) {
					}
					if (lastUpdated != null)

					{
						Date d = lastUpdated.toGregorianCalendar().getTime();
						InstantDt dt = new InstantDt();
						dt.setValue(d);
						// entry.setUpdated(dt);
					}
					// entry.addExtension("http://www.w3.org/2005/Atom","published",null).setText(new
					// Date().toGMTString());
					// theLinkSelf
					StringDt theLinkSelf = new StringDt();
					theLinkSelf.setValue(fhirBase + r.getId());
					entry.setLinkSelf(theLinkSelf);
					IResource theResource = WrapperHapi.resourceXmlToIResource(JAXBUtil.toXml(r));
					entry.setResource(theResource);
					hb.addEntry(entry);
				}
			}
		}
		return hb;

	}

	@Deprecated // TODO
	public static Resource containResourceBySearchParameterName(Resource p, String searchParamName)
			throws JAXBException {

		Class parentClass = FhirUtil.getResourceClass(p);
		/*
		 * <code value="result"/> <base value="DiagnosticReport"/> <type
		 * value="reference"/> <description value=
		 * "Link to an atomic result (observation resource)"/> <xpath
		 * value="f:DiagnosticReport/f:result"/>
		 */
		String paramterPath = null;
		try {
			paramterPath = new SearchParameterMap().getParameterPath(parentClass, searchParamName);
		} catch (FhirCoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.trace("paramterPath:" + paramterPath);

		String[] set = paramterPath.split("/");
		String methodName = set[set.length - 1];
		logger.trace("MethodName:" + methodName);
		// XXXnot working
		return p;

	}

	// makes copy of child and puts in a container in p
	public static Resource containResource(Resource p, Resource c, String searchParamName) throws JAXBException {
		try {

			logger.trace("containing :" + c.getId().getValue() + " in " + p.getId().getValue());
			logger.trace("parent before :" + JAXBUtil.toXml(p));

			Class parentClass = FhirUtil.getResourceClass(p);
			Class childClass = FhirUtil.getResourceClass(c);

			if (childClass == null || parentClass == null)
				throw new RuntimeException("unknown resource class for child or parent");

			String xml = JAXBUtil.toXml(p);
			// add # prefix to reference of contained resource?
			String childReference = c.getId().getValue();
			// xml = xml.replace(childReference, "#-" + childReference);
			// p = JAXBUtil.fromXml(xml, parentClass);
			logger.trace(
					"getting path to parent:" + parentClass + "\nchild:" + childClass.getSimpleName().toLowerCase());

			// get path from profile

			String path = new SearchParameterMap().getParameterPath(parentClass, searchParamName.toLowerCase());

			logger.trace("SEARCH PATH:" + path);
			String childPath = path.replaceAll("^" + parentClass.getSimpleName() + "/", "");
			logger.trace("MchildPath:" + childPath);

			// Reference childRef = (Reference) FhirUtil.getChild(p, childPath);
			Object obj = FhirUtil.getChild(p, childPath);
			List<Reference> refList = new ArrayList();
			if (java.util.ArrayList.class.isInstance(obj)) {
				ArrayList<Object> childList = (ArrayList<Object>) obj;
				for (Object objA : childList)
					refList.add((Reference) objA);
			} else {
				Reference ref = (Reference) obj;
				refList.add(ref);
			}
			for (Reference childRef : refList) {
				String childId = childRef.getReference().getValue();

				String expectedId = childClass.getSimpleName() + "/" + c.getId().getValue();
				logger.trace("childRef.getReference().getValue():" + childRef.getReference().getValue()
						+ "\n c.getId().getValue(): <" + expectedId + ">");
				if (!childId.equals(expectedId))
					continue;

				childRef.getReference().setValue("#" // +
														// childClass.getSimpleName()
														// + "-"
						+ childId);

				Id cId = c.getId();
				c = FhirUtil.clone(c, childClass.getSimpleName() + "/" + cId.getValue());

				// Id cId = c.getId();
				// cId.setValue(childClass.getSimpleName() + "-" +
				// cId.getValue());
				// c.setId(cId);
				ResourceContainer childRc = FhirUtil.getResourceContainer(c);

				Method method;
				Object o;

				method = parentClass.getMethod("getContained", null);
				o = method.invoke(parentClass.cast(p));
				List<ResourceContainer> listRC = (List<ResourceContainer>) o;

				listRC.add(childRc);
				logger.debug("added " + c.getId() + " into " + p.getId());
				logger.trace("p during:" + JAXBUtil.toXml(p));
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | FhirCoreException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}

		logger.trace("p after:" + JAXBUtil.toXml(p));
		return p;

	}

	private static Resource clone(Resource i, String newId) throws JAXBException {
		Class rClass = FhirUtil.getResourceClass(i);
		Resource c = JAXBUtil.fromXml(JAXBUtil.toXml(i), rClass);
		FhirUtil.setId(c, newId);
		return c;
	}

	public static String bundleToJsonString(Bundle s) throws IOException, JAXBException {
		return WrapperHapi.resourceXmlToJson(JAXBUtil.toXml(s));
	}

	public static Patient getPatientResource(String inputXml) throws XQueryUtilException, JAXBException, IOException {
		String query = IOUtils
				.toString(FhirUtil.class.getResourceAsStream("/transform/I2b2ToFhir/i2b2PatientToFhirPatient.xquery"));
		String bundleXml = XQueryUtil.processXQuery(query, inputXml).toString();
		Bundle b = JAXBUtil.fromXml(bundleXml, Bundle.class);
		return (Patient) FhirUtil.getResourceFromContainer(b.getEntry().get(0).getResource());
	}

	@Deprecated
	public static Bundle convertI2b2ToFhirForAParticularPatient(String i2b2Xml)
			throws IOException, XQueryUtilException, JAXBException {

		String query = IOUtils.toString(
				FhirUtil.class.getResourceAsStream("/transform/I2b2ToFhir/i2b2MedsToFHIRMedPrescription.xquery"));

		String xQueryResultString = XQueryUtil.processXQuery(query, i2b2Xml).toString();
		Bundle b = JAXBUtil.fromXml(xQueryResultString, Bundle.class);
		Patient p = getPatientResource(i2b2Xml);
		b.getEntry().add(newBundleEntryForResource(p));
		return b;
	}

	static public String extractPatientId(String input) {
		if (input == null)
			return null;
		String id = null;
		Pattern p = Pattern.compile("(Subject:subject|subject|Patient|patient|_id)+=([^?&]+)");
		Matcher m = p.matcher(input);

		if (m.find()) {
			id = m.group(2);
			logger.trace(id);
		}
		return id;
	}

	static public String extractPatientIdFromRequestById(String string, String resourceName) {
		logger.debug("requestUrl is:" + string);
		if (string == null)
			return null;
		String id = null;

		Pattern p = Pattern.compile(".*/([^?&]+)$");
		Matcher m = p.matcher(string);

		if (m.find()) {
			id = m.group(1);
			logger.trace("id:" + id);
		}
		if (id.equals(resourceName))
			id = null;

		return id;
	}

	static public org.hl7.fhir.String generateFhirString(String val) {
		org.hl7.fhir.String fstr = new org.hl7.fhir.String();
		fstr.setValue(val);
		return fstr;
	}

	public static String generateRandomId() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);

	}

	public static Id generateId(String idString) {
		Id id = new Id();
		id.setValue(idString);
		return id;
	}

	public static Bundle createBundle(List<Object> list) {
		Bundle b = new Bundle();

		for (Object o : list) {
			Resource r = (Resource) o;
			b.getEntry().add(FhirUtil.newBundleEntryForResource(r));
		}

		UnsignedInt ustotal = new UnsignedInt();
		BigInteger bigInt = new BigInteger(Integer.toString(list.size()));
		ustotal.setValue(bigInt);
		b.setTotal(ustotal);
		return b;

	}

	static public Bundle addResourceToBundle(Bundle b, Resource r) {
		BundleEntry be = FhirUtil.newBundleEntryForResource(r);
		b.getEntry().add(be);
		setBundleTotal(b);
		return b;
	}

	static public Bundle setBundleTotal(Bundle b) {
		int tot = 0;
		if (b.getEntry() != null) {
			tot = b.getEntry().size();
		}
		UnsignedInt ustotal = new UnsignedInt();
		BigInteger bigInt = new BigInteger(Integer.toString(tot));
		ustotal.setValue(bigInt);
		b.setTotal(ustotal);
		return b;
	}

	public static Bundle addBundles(Bundle b1, Bundle b2) {
		Bundle b = new Bundle();
		List<Resource> list1 = FhirUtil.getResourceListFromBundle(b1);
		List<Resource> list2 = FhirUtil.getResourceListFromBundle(b2);
		for (Resource r : list1)
			FhirUtil.addResourceToBundle(b, r);
		for (Resource r : list2)
			FhirUtil.addResourceToBundle(b, r);
		return b;
	}

	public static CodeableConcept generateCodeableConcept(String code, String codeSystem, String display) {

		
		Coding coding = createCoding(code,codeSystem,display);
		CodeableConcept codeableConcept = new CodeableConcept();
		codeableConcept.getCoding().add(coding);
		return codeableConcept;
	}
	
	public static Coding createCoding(String code, String codeSystem, String display) {

		Code ccode = new Code();
		ccode.setValue(code);

		Coding coding = new Coding();
		coding.setCode(ccode);

		Uri uri = new Uri();
		uri.setValue(codeSystem);
		coding.setSystem(uri);

		if (display != null) {
			coding.setDisplay(FhirUtil.generateFhirString(display));
		}
		return coding;
	}

	public static List<Object> getChildrenThruParPath(Resource r, String pathStr, MetaResourceDb db)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		List<Object> children = new ArrayList<Object>();
		List<Object> resolvedChildren = new ArrayList<Object>();
		List<Resource> s = db.getAll();

		logger.trace("got obj:" + r);
		Class c = FhirUtil.getResourceClass(r);

		String suffix = null;
		String prefix = pathStr;
		logger.trace("pathStr:" + pathStr);

		if (pathStr.indexOf('.') > -1) {
			suffix = pathStr.substring(pathStr.indexOf('.') + 1);
			prefix = pathStr.substring(0, pathStr.indexOf('.'));
		}

		String methodName = prefix.substring(0, 1).toUpperCase() + prefix.subSequence(1, prefix.length());
		Method method = c.getMethod("get" + methodName, null);
		Object o = method.invoke(c.cast(r));

		if (List.class.isInstance(o)) {
			children.addAll((List<Object>) o);
		} else {
			children.add(o);
		}

		if (suffix == null) {
			return children;

		} else {

			for (Object child : children) {

				// if it is a reference resolve the reference
				if (Reference.class.isInstance(child)) {
					Reference rr = Reference.class.cast(child);
					logger.trace("gotc:" + child.getClass());
					Resource r1 = FhirUtil.findResourceById(rr.getReference().getValue(), s);
					resolvedChildren.add(getChildrenThruChain(r1, suffix, s));

				} else {
					resolvedChildren.add(getChildrenThruChain(r, suffix, s));
				}

			}
		}
		return resolvedChildren;
	}

	// Takes a bundle, and given the maxEntries and page Number,
	// generates the page for the bundle which includes links for related pages
	public static Bundle pageBundle(Bundle b, int maxEntries, int pageNum) {
		Bundle pagedB = new Bundle();
		FhirUtil.setId(pagedB, Long.toHexString(new Random().nextLong()));
		pagedB.setMeta(FhirUtil.createMeta());
		
		if (maxEntries <= 0)
			throw new IllegalArgumentException("max Entries should be >0 ");
		if (pageNum < 1)
			throw new IllegalArgumentException("page NUmber should be >=1 ");

		int sob = b.getEntry().size();// size of Bundle
		int lowIdx = maxEntries * (pageNum - 1);// index of lowest entry in
												// selected page
		int highIdx = (maxEntries * pageNum) ;// index of highest entry in
													// selected page

		int lastPageNum = (int) Math.ceil(sob / maxEntries);

		// if low and high index of
		if (lowIdx > sob) {
			return pagedB;
		}
		List<Object> listBE = new ArrayList<>();
		for (int i = lowIdx; i < highIdx; i++) {
			if (i < sob) {
				Resource r=FhirUtil.getResourceFromContainer(b.getEntry().get(i).getResource());
				listBE.add(r);
			}
		}
		pagedB = createBundle(listBE);

		// create links
		// get link for self from inputBundle if it exits

		String baseUrlValue = null;
		for (BundleLink link : b.getLink()) {
			if (link.getRelation() != null & link.getRelation().getValue() != null
					& link.getRelation().getValue().equals("self")
					& link.getUrl() != null) {
				baseUrlValue = link.getUrl().getValue().replaceAll("&{0,1}page=\\d+", "").replaceAll("\\?$","");
			}
		}

		if (baseUrlValue == null) {
			throw new RuntimeException("the self link is null ");
		}

		// self
		pagedB.getLink().add(createBundleLink("self", baseUrlValue + (baseUrlValue.contains("?")?"&":"?")+"page=" + pageNum));
		// first page
		pagedB.getLink().add(createBundleLink("first", baseUrlValue + (baseUrlValue.contains("?")?"&":"?")+ "page=" + 1));

		// previous
		if(pageNum>1){
			pagedB.getLink().add(createBundleLink("previous", baseUrlValue + (baseUrlValue.contains("?")?"&":"?")+ "page=" + (pageNum-1)));
		}
		
		// next
		if(pageNum<lastPageNum){
			pagedB.getLink().add(createBundleLink("next", baseUrlValue + (baseUrlValue.contains("?")?"&":"?")+ "page=" + (pageNum+1)));
		}
		// last page
		pagedB.getLink().add(createBundleLink("last", baseUrlValue + (baseUrlValue.contains("?")?"&":"?")+ "page=" + lastPageNum));
		

		return pagedB;
	}

	public static BundleLink createBundleLink(String relation, String url) {
		BundleLink link = new BundleLink();
		org.hl7.fhir.String relString = new org.hl7.fhir.String();
		relString.setValue(relation);
		link.setRelation(relString);
		Uri uri = new Uri();
		uri.setValue(url);
		link.setUrl(uri);
		return link;
	}

}