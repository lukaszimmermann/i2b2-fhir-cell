package edu.harvard.i2b2.fhir;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hl7.fhir.Resource;
import org.hl7.fhir.ResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class MetaResourceDb {
	Logger logger = LoggerFactory.getLogger(MetaResourceDb.class); 
	
	List<MetaResource> metaResources;
	//MetaResourcePrimaryDb metaResources;
	
	public MetaResourceDb() {
		init();
	}
	
	public int getSize(){
		return this.metaResources.size();
	}

	public void init() {
		metaResources = new ArrayList<MetaResource>();
		//metaResources = new MetaResourcePrimaryDb();
		logger.trace("created resourcedb");
		logger.trace("resources size:" + metaResources.size());
	}

	public String addMetaResource(MetaResource p, Class c) {
		logger.trace("EJB Putting resource:" + c.getSimpleName());
		try {
			logger.trace("EJB Put MetaResource:"
					+ c.cast(p.getResource()).getClass().getSimpleName());
			} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Resource r=p.getResource();
		
		if (r.getId() == null) {
			r.setId(Integer.toString(getMetaResourceTypeCount(c)));
			
		}

		MetaResource presentRes = getMetaResource(r.getId(), c);
		if (presentRes != null) {
			// throw new
			// RuntimeException("resource with id:"+p.getId()+" already exists");
			logger.trace("replacing resource with id:"
					+ r.getId());
			metaResources.remove(presentRes);
		}
		metaResources.add(p);

		logger.trace("EJB resources (after adding) size:"
				+ metaResources.size());
		return p.getResource().getId();
	}

	public MetaResource getMetaResource(String id, Class c) {
		logger.trace("EJB searching for resource with id:" + id);
		logger.trace("metaResources size:" + metaResources.size());
		for (MetaResource p : metaResources) {
			Resource r = p.getResource();
			if (!c.isInstance(r))
				continue;
			//logger.trace("examining resource with id:<" + r.getId()+ "> for match to qid:<" + id + ">");
			if (r.getId().equals(id)) {
				//logger.trace("matched resource with id:" + r.getId());
				return p;
			}
		}
		return null;
	}

	public int getMetaResourceTypeCount(Class c) {
		logger.trace("EJB searching for resource type:"
				+ c.getSimpleName());
		logger.trace("resources size:" + metaResources.size());
		int count = 0;

		for (MetaResource p : metaResources) {
			if (!c.isInstance(p.getResource()))
				continue;
			count++;
		}
		return count;
	}

	public void removeMetaResource(MetaResource p1) {
		metaResources.remove(p1);
	}

	public List<MetaResource> getQueried(Class c,
			HashMap<String, String> qp// Query Parameters
	) {
		List<MetaResource> list = new ArrayList<MetaResource>();
		for (MetaResource p : getAll(c).getMetaResource()) {
			for (String k : qp.keySet()) {
				Object returnValue = null;
				String returnStr = null;
				try {
					logger.trace("searching for parameter:" + k
							+ " with value " + qp.get(k));
					getValueOfFirstLevelChild(p, c, k);

				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException e) {
					e.printStackTrace();
				}
				logger.trace("returnStr:" + returnStr);
				logger.trace("qp.getFirst(k):" + qp.get(k));

				if (returnStr.toString().contains(qp.get(k))) {
					list.add(p);
				}
			}
		}
		return list;
	}

	public static String getValueOfFirstLevelChild(MetaResource p, Class c,
			String k) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Object returnValue = null;
		String returnStr = null;

		returnValue = getFirstLevelChild(p, c, k);
		if (org.hl7.fhir.String.class.isInstance(returnValue)) {
			org.hl7.fhir.String s1 = (org.hl7.fhir.String) returnValue;
			returnStr = s1.getValue();
		}
		return null;
	}

	public static Object getFirstLevelChild(MetaResource p, Class c, String k)
			// k is name of child
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		String returnStr = null;

		String methodName = k.substring(0, 1).toUpperCase()
				+ k.subSequence(1, k.length());
		Method method = c.getMethod("get" + methodName, null);
		return method.invoke(c.cast(p.getResource()));

	}

	public MetaResourceSet getAll(Class c) {
		MetaResourceSet  s= new MetaResourceSet();
		List<MetaResource> list = s.getMetaResource();
		for (MetaResource p : metaResources) {
			if (c.isInstance(p.getResource())) {
				list.add(p);
			}
		}
		return s;
	}

	public Resource getParticularResource(Class c, String id) {
		for (MetaResource mr : metaResources) {
			Resource r = mr.getResource();
			logger.trace(r.getId());
			if (c.isInstance(r)
					&& r.getId().equals(c.getSimpleName() + "/" + id)) {
				return r;
			}
		}
		return null;
	}

	public void addMetaResourceSet(MetaResourceSet s) {
		// MetaResource mr=s.getMetaResource().get(0);
		// this.addMetaResource(mr,
		// FhirUtil.getResourceClass(mr.getResource()));
		for (MetaResource mr : s.getMetaResource()) {
			this.addMetaResource(mr,
					FhirUtil.getResourceClass(mr.getResource()));
		}
	}

	public MetaResource searchById(String id) {
		//logger.trace("searching id:" + id);
		for (MetaResource mr : metaResources) {
			if (mr.getResource().getId().equals(id))
				return mr;
		}
		logger.trace("id NOT found:" + id);
		return null;
	}

	public Resource searchResourceById(String id) {
		MetaResource mr = searchById(id);
		if (mr != null)
			return mr.getResource();
		return null;
	}

	//child can be a element in the resource or element of a reference element
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

			if (ResourceReference.class.isInstance(o)) {
				ResourceReference rr = (ResourceReference) o;
				logger.trace("returning from reference:");
				//String idn = rr.getId();// rr.getReference().getValue();
				//logger.trace(":" + rr.getId());
				
				String idn = rr.getReference().getValue();
				logger.trace(":" + idn);

				return searchResourceById(idn);
			} else {
				logger.trace("returning NOT from reference");
				return o;
			}
		} else {

			logger.trace("Suffix is not null");

			return getChildOfResource(r, suffix);
			// return getChildOfResource(r, suffix);
		}

	}

	public MetaResourceSet getIncludedMetaResources(Class c,
			MetaResourceSet inputSet, List<String> includeResources) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		//MetaResourceSet s = new MetaResourceSet();
		List<MetaResource> list = inputSet.getMetaResource();
		// list.addAll(metaResourceDb.getAll(Medication.class));
		//list.addAll(this.getAll(c));
		// XXX include dependent resources based on include portion of query
		// XXX to make the resource id accessible via cRud(readOnly) webservice

		// iterate through resources and add included dependencies
		MetaResourceSet s2 = new MetaResourceSet();
		ArrayList<MetaResource> sInclusions = (ArrayList<MetaResource>) s2
				.getMetaResource();
		for (String ir : includeResources) {
			String methodName = ir.split("\\.")[1];
			logger.trace("MethodName:" + methodName);
			for (MetaResource mr : inputSet.getMetaResource()) {
				String id = ((ResourceReference) this.getFirstLevelChild(mr, c,methodName)).getReference().getValue();

				logger.trace("Found dep:" + id);
				MetaResource depMr = searchById(id);
				if (depMr != null) {

					if (!FhirUtil.isContained(s2, id)) {
						sInclusions.add(depMr);
					}
					// logger.trace("added dep:"+depMr.getResource().getId());
				}
			}
		}
		list.addAll(sInclusions);

		return inputSet;
	}

	public MetaResourceSet filterMetaResources(Class c,
			HashMap<String, String> filter) throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		MetaResourceSet s = new MetaResourceSet();
		List<MetaResource> list = s.getMetaResource();

		logger.trace("filtering by:");
		for (MetaResource mr : this.getAll(c).getMetaResource()) {
			Resource r = mr.getResource();
			logger.trace("filtering on:" + r.getId());

			boolean matchF=false;
			for (String k : filter.keySet()) {
				if(matchF==true) continue;
				String v = filter.get(k);
				logger.trace("filter:" + k + "=" + v);

				//child 
				Resource child = (Resource) getChildOfResource(r, k);
				if (child != null) {
					logger.trace("filterinput child:" + child.getId());
				} else {
					logger.trace("filterinput child:NULL");
				}
				if (child != null && child.getId().equals(v)) {
					logger.trace("adding:"+r.getId());
					matchF=true;
					continue;
				}
			}
			if(matchF==true) list.add(mr);
		}
		return s;
	}

}
