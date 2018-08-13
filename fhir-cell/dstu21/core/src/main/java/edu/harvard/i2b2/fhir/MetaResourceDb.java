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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.BundleEntry;
import org.hl7.fhir.Id;
import org.hl7.fhir.Resource;
import org.hl7.fhir.Reference;
import org.hl7.fhir.ResourceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.query.SearchParameterMap;

public class MetaResourceDb {
	static Logger logger = LoggerFactory.getLogger(MetaResourceDb.class);

	HashMap<Class, List<Resource>> resourceMap;
	// List<Resource> resourceList;

	public MetaResourceDb() throws IOException {
		init();
	}

	public int getSize() {
		int count = 0;
		for (Class c : resourceMap.keySet()) {
			count += resourceMap.get(c).size();
		}
		return count;
	}

	public void init() throws IOException {
		resourceMap = new HashMap<>();
		// resourceList = new ArrayList<Resource>();
		// metaResources = new MetaResourcePrimaryDb();
		logger.trace("created resourcedb");
		logger.trace("resources size:" + getSize());
	}

	public String addResource(Resource r) throws JAXBException {
		Class c = FhirUtil.getResourceClass(r);
		List<Resource> resourceList = getResourceListCreateIfAbsent(c);

		//logger.trace("EJB Putting resource:" + c.getSimpleName());
		try {
			//logger.trace("EJB Put Resource:" + c.cast(r).getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (r.getId() == null) {
			Id id = new Id();
			id.setValue(Integer.toString(getResourceTypeCount(c)));
			r.setId(id);
		}

		Resource presentRes = searchById(r.getId().getValue(), c);
		if (presentRes != null) {
			// throw new
			// RuntimeException("resource with id:"+p.getId()+" already
			// exists");
			// resourceList.remove(presentRes);
			logger.debug("replacing resource with id:" + r.getId());
		}

		resourceList.add(r);

		logger.debug(
				"MrDB resources (after adding resource with id:" + r.getId().getValue() + ") size:" + this.getSize());
		return r.getId().getValue().toString();
	}

	public void addBundle(Bundle b) throws JAXBException {
		for (BundleEntry e : b.getEntry()) {
			ResourceContainer rc = e.getResource();
			Resource r = FhirUtil.getResourceFromContainer(rc);
			addResource(r);
		}

	}

	public int getResourceTypeCount(Class c) {
		logger.trace("EJB searching for resource type:" + c.getSimpleName());
		logger.trace("resources size:" + this.getSize());
		int count = 0;
		List<Resource> resourceList = this.resourceMap.get(c);
		for (Resource r : resourceList) {
			if (!c.isInstance(r))
				continue;
			count++;
		}
		return count;
	}

	public void removeResource(Resource r1) {
		Class c = FhirUtil.getResourceClass(r1);
		List<Resource> resourceList = getResourceListCreateIfAbsent(c);
		resourceList.remove(r1);
	}

	private List<Resource> getResourceListCreateIfAbsent(Class c) {
		List<Resource> resourceList = this.resourceMap.get(c);
		if ((resourceList) == null)
			this.resourceMap.put(c, new ArrayList<Resource>());
		return this.resourceMap.get(c);
	}

	public static String getValueOfFirstLevelChild(Resource r, Class c, String k) throws NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object returnValue = null;
		String returnStr = null;

		returnValue = getFirstLevelChild(r, c, k);
		if (org.hl7.fhir.String.class.isInstance(returnValue)) {
			org.hl7.fhir.String s1 = (org.hl7.fhir.String) returnValue;
			returnStr = s1.getValue();
		}
		return null;
	}

	public static Object getFirstLevelChild(Resource r, Class c, String k)
			// k is name of child
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		String methodName = k.substring(0, 1).toUpperCase() + k.subSequence(1, k.length());
		Method method = c.getMethod("get" + methodName, null);
		return method.invoke(c.cast(r));

	}

	public List<Resource> getAll(Class c) {
		List<Resource> list = new ArrayList<Resource>();
		List<Resource> resourceList = getResourceListCreateIfAbsent(c);
		for (Resource r : resourceList) {
			if (c.isInstance(r)) {
				list.add(r);
			}
		}
		return list;
	}

	public List<Resource> getAll() {
		List<Resource> all = new ArrayList<Resource>();
		for (Class c : resourceMap.keySet()) {
			all.addAll(resourceMap.get(c));
		}
		return all;
	}

	public Resource getParticularResource(Class c, String id) {
		List<Resource> resourceList = getResourceListCreateIfAbsent(c);
		for (Resource r : resourceList) {
			logger.trace(r.getId().getValue().toString());
			if (c.isInstance(r) && r.getId().getValue().toString().equals(id)) {
				return r;
			}
		}
		return null;
	}

	public Resource searchById(String idStr, Class c) {
		// logger.trace("searching id:" + id);
		List<Resource> resourceList = getResourceListCreateIfAbsent(c);
		for (Resource r : resourceList) {
			// logger.trace("id:"+r.getId().getValue())
			if (c.isInstance(r) && r.getId().getValue().equals(idStr))
				return r;
		}
		logger.debug("id NOT found:" + idStr);
		return null;
	}

	// child can be a element in the resource or element of a reference element
	public Object getChildOfResource(Resource r, String pathStr)
			// is dot separated path
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Class c = FhirUtil.getResourceClass(r);
		String returnStr = null;
		String suffix = null;
		String prefix = pathStr;
		logger.trace("pathStr:" + pathStr + "	Resource:" + r.getId());
		if (pathStr.indexOf('.') > -1) {
			suffix = pathStr.substring(pathStr.indexOf('.') + 1);
			prefix = pathStr.substring(0, pathStr.indexOf('.'));
		}
		String methodName = prefix.substring(0, 1).toUpperCase() + prefix.subSequence(1, prefix.length());
		Method method = c.getMethod("get" + methodName, null);
		if (suffix == null) {
			Object o = method.invoke(c.cast(r));

			if (Reference.class.isInstance(o)) {
				Reference rr = (Reference) o;
				logger.trace("returning from reference:");
				// String idn = rr.getId();// rr.getReference().getValue();
				// logger.trace(":" + rr.getId());

				String idn = rr.getReference().getValue();
				logger.trace(":" + idn);

				return searchById(idn, c);
			} else {
				logger.trace("returning NOT from reference");
				return o;
			}
		} else {

			logger.trace("Suffix is not null");

			Object o = method.invoke(c.cast(r));
			Resource nextR = null;
			if (Reference.class.isInstance(o)) {
				Reference rr = (Reference) o;
				logger.trace("returning from reference:");
				// String idn = rr.getId();// rr.getReference().getValue();
				// logger.trace(":" + rr.getId());

				String idn = rr.getReference().getValue();
				logger.trace(":" + idn);

				nextR = searchById(idn, c);
			} else {
				logger.trace("returning NOT from reference");
				return o;
			}

			return getChildOfResource(nextR, suffix);
			// return getChildOfResource(r, suffix);
		}

	}

	// inputSet is set of resources to process for inclusion
	// c is class of the resources for processing
	// includesResources is search name of resources for inclusion
	public List<Resource> getIncludedResources(Class c, List<Resource> inputSet, List<String> includeResources)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, JAXBException, FhirCoreException {
		// MetaResourceSet s = new MetaResourceSet();
		List<Resource> outList = new ArrayList<Resource>();
		// iterate through resources and add included dependencies

		// logger.trace("inputSet size:"+inputSet.size());

		for (String ir : includeResources) {
			String paramterPath = new SearchParameterMap().getParameterPath(c, ir);
			//logger.trace("paramterPath:" + paramterPath);
			String[] set = paramterPath.split("/");
			String methodName = set[set.length - 1];
			//logger.trace("MethodName:" + methodName);

			outList = new ArrayList<Resource>();
			for (Resource r : inputSet) {
				// String id = ((Reference)this.getFirstLevelChild(r, c,
				// methodName)).getId();

				Object o = getFirstLevelChild(r, c, methodName);
				List<Reference> refList = new ArrayList();
				if (java.util.ArrayList.class.isInstance(o)) {
					ArrayList<Object> childList = (ArrayList<Object>) o;
					for (Object obj : childList)
						refList.add((Reference) obj);
				} else {
					Reference ref = (Reference) o;
					refList.add(ref);
				}
				for (Reference ref : refList) {

					String rawid = ref.getReference().getValue().toString();
					// String id=rawid;//
					String id = rawid.split("/")[1];
					Class depClass = FhirUtil.getResourceClass(rawid.split("/")[0]);

					logger.trace("Found dep:<" + id + ">");
					Resource depMr = searchById(id, depClass);
					if (depMr != null) {
						// logger.trace("dependent Res:"+JAXBUtil.toXml(depMr));
						outList.add(FhirUtil.containResource(r, depMr,ir));
					} else {
						outList.add(r);
					}
				}
			}
			inputSet = outList;
		}
		// outList.addAll(inputSet);
		;
		return inputSet;
	}

	public Resource getIncludedResource(Class c, Resource r, List<String> includeResources)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, JAXBException, FhirCoreException {

		for (String ir : includeResources) {
			String paramterPath = new SearchParameterMap().getParameterPath(c, ir);
			//logger.trace("paramterPath:" + paramterPath);
			String[] set = paramterPath.split("/");
			String methodName = set[set.length - 1];
			//logger.trace("MethodName:" + methodName);

			// String id = ((Reference)this.getFirstLevelChild(r, c,
			// methodName)).getId();
			Reference ref = (Reference) getFirstLevelChild(r, c, methodName);

			String rawid = ref.getReference().getValue().toString();
			// String id=rawid;//
			String id = rawid.split("/")[1];
			Class depClass = FhirUtil.getResourceClass(rawid.split("/")[0]);

			//logger.trace("Found dep:<" + id + ">");
			Resource depMr = searchById(id, depClass);
			if (depMr != null) {
				// logger.trace("dependent Res:"+JAXBUtil.toXml(depMr));
				FhirUtil.containResource(r, depMr,ir);
			}
		}

		return r;
	}

	public void addResourceList(List<Resource> s) throws JAXBException {
		for (Resource r : s)
			addResource(r);
	}

}