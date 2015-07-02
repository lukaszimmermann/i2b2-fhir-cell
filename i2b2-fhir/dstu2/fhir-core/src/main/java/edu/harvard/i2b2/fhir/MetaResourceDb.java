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
import java.io.StringWriter;
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
import org.hl7.fhir.Medication;
import org.hl7.fhir.ResourceContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.rxnorm.RxNormAdapter;

public class MetaResourceDb {
	Logger logger = LoggerFactory.getLogger(MetaResourceDb.class);

	List<Resource> resourceList;
	static RxNormAdapter rxNormAdapter = null;

	public MetaResourceDb() throws IOException {
		init();
	}

	public int getSize() {
		return this.resourceList.size();
	}

	public void init() throws IOException {
		if (rxNormAdapter == null)
			rxNormAdapter = new RxNormAdapter();
		resourceList=new ArrayList<Resource>();
		// metaResources = new MetaResourcePrimaryDb();
		logger.trace("created resourcedb");
		logger.trace("resources size:" + getSize());
	}

	public String addResource(Resource r) throws JAXBException {
		Class c=FhirUtil.getResourceClass(r);
		logger.trace("EJB Putting resource:" + c.getSimpleName());
		try {
			logger.trace("EJB Put Resource:"
					+ c.cast(r).getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (r.getId() == null) {
			Id id=new Id();
			id.setValue(Integer.toString(getResourceTypeCount(c)));
			r.setId(id);
		}

		Resource presentRes = getResource(r.getId().getValue(), c);
		if (presentRes != null) {
			// throw new
			// RuntimeException("resource with id:"+p.getId()+" already exists");
			logger.trace("replacing resource with id:" + r.getId());
			resourceList.remove(presentRes);
		}

		if (Medication.class.isInstance(r)) {
			Medication m = Medication.class.cast(r);
			rxNormAdapter.addRxCui(m);
		}
		resourceList.add(r);

		logger.trace("EJB resources (after adding) size:"
				+ this.getSize());
		return r.getId().getValue().toString();
	}

	public void addBundle(Bundle b) throws JAXBException {
		for (BundleEntry e : b.getEntry()) {
			ResourceContainer rc=e.getResource();
			Resource r=FhirUtil.getResourceFromContainer(rc);
			addResource(r);
		}
	}

	public Resource getResource(String id, Class c) {
		logger.trace("EJB searching for resource with id:" + id);
		logger.trace("mResources size:" + this.getSize());
		for (Resource r : resourceList) {
			if (!c.isInstance(r))
				continue;
			// logger.trace("examining resource with id:<" + r.getId()+
			// "> for match to qid:<" + id + ">");
			if (r.getId().equals(id)) {
				// logger.trace("matched resource with id:" + r.getId());
				return r;
			}
		}
		return null;
	}

	public int getResourceTypeCount(Class c) {
		logger.trace("EJB searching for resource type:" + c.getSimpleName());
		logger.trace("resources size:" + this.getSize());
		int count = 0;

		for (Resource r : resourceList) {
			if (!c.isInstance(r))
				continue;
			count++;
		}
		return count;
	}

	public void removeResource(Resource r1) {
		resourceList.remove(r1);
	}

	
	public static String getValueOfFirstLevelChild(Resource r, Class c,
			String k) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
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
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		String methodName = k.substring(0, 1).toUpperCase()
				+ k.subSequence(1, k.length());
		Method method = c.getMethod("get" + methodName, null);
		return method.invoke(c.cast(r));

	}

	public List<Resource> getAll(Class c) {
		List<Resource> list = new ArrayList<Resource>();
		for (Resource r : resourceList) {
			if (c.isInstance(r)) {
				list.add(r);
			}
		}
		return list;
	}

	public List<Resource> getAll() {
		return this.resourceList;
		
	}

	public Resource getParticularResource(Class c, String id) {
		for (Resource r : resourceList){
			logger.trace(r.getId().getValue().toString());
			if (c.isInstance(r)
					&& r.getId().getValue().toString().equals(c.getSimpleName() + "/" + id)) {
				return r;
			}
		}
		return null;
	}

	public Resource searchById(String idStr) {
		// logger.trace("searching id:" + id);
		for (Resource r : resourceList){
			if (r.getId().getValue().equals(idStr))
				return r;
		}
		logger.trace("id NOT found:" + idStr);
		return null;
	}

	public Resource searchResourceById(String id) {
		Resource r = searchById(id);
		return r;
	}

	// child can be a element in the resource or element of a reference element
	public Object getChildOfResource(Resource r, String pathStr)
			// is dot separated path
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
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
		String methodName = prefix.substring(0, 1).toUpperCase()
				+ prefix.subSequence(1, prefix.length());
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

				return searchResourceById(idn);
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

				nextR = searchResourceById(idn);
			} else {
				logger.trace("returning NOT from reference");
				return o;
			}

			return getChildOfResource(nextR, suffix);
			// return getChildOfResource(r, suffix);
		}

	}

	public List<Resource> getIncludedResources(Class c,
			List<Resource> inputSet, List<String> includeResources)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		// MetaResourceSet s = new MetaResourceSet();
		List<Resource> outList = new ArrayList<Resource>();
		// list.addAll(metaResourceDb.getAll(Medication.class));
		// list.addAll(this.getAll(c));
		// XXX include dependent resources based on include portion of query
		// XXX to make the resource id accessible via cRud(readOnly) webservice

		// iterate through resources and add included dependencies
		
		for (String ir : includeResources) {
			String methodName = ir.split("\\.")[1];
			logger.trace("MethodName:" + methodName);
			for (Resource  r: inputSet) {
				//String id =  ((Reference)this.getFirstLevelChild(r, c,
					//	methodName)).getId();
				Reference ref=(Reference) this.getFirstLevelChild(r, c,methodName);

				String id=ref.getReference().getValue().toString();
						logger.trace("Found dep:<" + id+">");
				Resource depMr = searchById(id);
				if (depMr != null) {

					if (!FhirUtil.isContained(outList, id)) {
						outList.add(depMr);
					}
					// logger.trace("added dep:"+depMr.getResource().getId());
				}
			}
		}
		outList.addAll(inputSet);
		return outList;
	}

	public List<Resource> filterResources(Class c,
			HashMap<String, String> filter) throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		List<Resource> list = new ArrayList<Resource>();

		logger.trace("filtering by:" + filter.toString());

		logger.trace("size:" + this.getSize());
		for (Resource r : resourceList) {
			logger.trace("filtering on:" + r.getId());

			boolean matchF = false;
			for (String k : filter.keySet()) {
				if (matchF == true)
					continue;
				String v = filter.get(k);
				logger.trace("filter:" + k + "=" + v);

				// child
				Resource child = null;
				Object obj = getChildOfResource(r, k);
				try {
					child = (Resource) obj;
				} catch (ClassCastException e) {

					logger.trace("Not a Resource:<" + obj + ">. Its a "
							+ e.getMessage());
					String gotVal = (String) obj;
					logger.trace("comparing <" + v + "> with <" + gotVal + ">");
					if (gotVal.equals(v))
						matchF = true;
				}
				if (child != null) {
					logger.trace("filterinput child:" + child.getId());
				} else {
					logger.trace("filterinput child:NULL");
				}
				if (child != null && child.getId().equals(v)) {
					matchF = true;
					continue;
				}
			}
			if (matchF == true) {
				logger.trace("adding:" + r.getId());
				list.add(r);
			}
		}
		return list;
	}

	public void addResourceList(List<Resource> s) throws JAXBException {
		for(Resource r:s) addResource(r);
	}

}