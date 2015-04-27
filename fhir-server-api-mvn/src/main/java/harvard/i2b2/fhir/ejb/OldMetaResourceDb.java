package harvard.i2b2.fhir.ejb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.core.MultivaluedMap;

import org.hl7.fhir.Resource;
import org.hl7.fhir.instance.model.ResourceReference;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

@Startup
@Singleton
public class OldMetaResourceDb {
	List<MetaResource> metaResources;

	@PostConstruct
	void init() {
		metaResources = new ArrayList<MetaResource>();
		System.out.println("created resourcedb");
		System.out.println("resources size:" + metaResources.size());
	}

	@Lock(LockType.WRITE)
	public String addMetaResource(MetaResource p, Class c) {
		System.out.println("EJB Putting resource:" + c.getSimpleName());
		try {
			System.out.println("EJB Put MetaResource:"
					+ c.cast(p.getResource()).getClass().getSimpleName());
			System.out
					.println("EJB MetaResources size:" + metaResources.size());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (p.getMetaData().getId() == null) {
			p.getResource()
					.setId(Integer.toString(getMetaResourceTypeCount(c)));
			p.getMetaData().setId(p.getResource().getId());
		}

		MetaResource presentRes = getMetaResource(p.getMetaData().getId(), c);
		if (presentRes != null) {
			// throw new
			// RuntimeException("resource with id:"+p.getId()+" already exists");
			System.out.println("replacing resource with id:"
					+ p.getMetaData().getId());
			metaResources.remove(presentRes);
		}
		metaResources.add(p);

		System.out.println("EJB resources (after adding) size:"
				+ metaResources.size());
		return p.getMetaData().getId();
	}

	@Lock(LockType.READ)
	public MetaResource getMetaResource(String id, Class c) {
		System.out.println("EJB searching for resource with id:" + id);
		System.out.println("metaResources size:" + metaResources.size());
		for (MetaResource p : metaResources) {
			if (!c.isInstance(p))
				continue;
			System.out.println("examining resource with id:<"
					+ p.getMetaData().getId() + "> for match to qid:<" + id
					+ ">");
			if (p.getMetaData().getId().equals(id)) {
				System.out.println("matched resource with id:"
						+ p.getMetaData().getId());
				return p;
			}
		}
		return null;
	}

	@Lock(LockType.READ)
	public int getMetaResourceTypeCount(Class c) {
		System.out.println("EJB searching for resource type:"
				+ c.getSimpleName());
		System.out.println("resources size:" + metaResources.size());
		int count = 0;

		for (MetaResource p : metaResources) {
			if (!c.isInstance(p.getResource()))
				continue;
			count++;
		}
		return count;
	}

	public void removeResource(MetaResource p1) {
		metaResources.remove(p1);
	}

	@Lock(LockType.READ)
	public List<MetaResource> getQueried(Class c,
			MultivaluedMap<String, String> qp// Query Parameters
	) {
		List<MetaResource> list = new ArrayList<MetaResource>();
		for (MetaResource p : getAll(c)) {
			for (String k : qp.keySet()) {
				Object returnValue = null;
				String returnStr = null;
				try {
					System.out.println("searching for parameter:" + k
							+ " with value " + qp.getFirst(k));
					getValueOfFirstLevelChild(p, c, k);

				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException
						| SecurityException e) {
					e.printStackTrace();
				}
				System.out.println("returnStr:" + returnStr);
				System.out.println("qp.getFirst(k):" + qp.getFirst(k));

				if (returnStr.toString().contains(qp.getFirst(k))) {
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

	@Lock(LockType.READ)
	public List<MetaResource> getAll(Class c) {
		List<MetaResource> list = new ArrayList<MetaResource>();
		for (MetaResource p : metaResources) {
			if (c.isInstance(p.getResource())) {
				list.add(p);
			}
		}
		return list;
	}

	public MetaResource getParticularResource(Class c, String id) {
		for (MetaResource r : metaResources) {
			if (c.isInstance(r.getResource())
					&& r.getMetaData().getId().equals(id)) {
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
		for (MetaResource mr : metaResources) {
			if (mr.getResource().getId().equals(id))
				return mr;
		}
		System.out.println("id NOT found:" + id);
		return null;
	}

	public  Object getChild(Resource r, String pathStr)
			// is dot separated path
			throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		Class c = FhirUtil.getResourceClass(r);
		String returnStr = null;
		String suffix = null;
		String prefix = pathStr;
		System.out.println("pathStr:" + pathStr);
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
				ResourceReference rr= (ResourceReference) o;
				return searchById(rr.getReference().getXmlId());
			} else {
				return o;
			}
		} else {
			return getChild(r, suffix);
		}

	}
}
