package harvard.i2b2.fhir.ejb;



import harvard.i2b2.fhir.ws.FromI2b2WebService;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Startup
@Singleton
public class ResourceDb {
	static Logger logger = LoggerFactory.getLogger(ResourceDb.class);
	
	List<Resource> resources;
	
	@PostConstruct
	void init(){
		logger.info("log4j works!");
		resources = new ArrayList<Resource>();
		System.out.println("created resourcedb");
		System.out.println("resources size:"+resources.size());
	}
	
	@Lock(LockType.WRITE)
	public String addResource(Resource p, Class c){
		System.out.println("EJB Putting resource:"+c.getSimpleName());
		try{
			System.out.println("EJB Put resource:"+c.cast(p).getClass().getSimpleName());
			System.out.println("EJB resources size:"+resources.size());
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		if (p.getId()==null) {
			p.setId(Integer.toString(getResourceTypeCount(c)));
		}
		
		Resource presentRes=getResource(p.getId(),c);
		if (presentRes!=null) {
				//throw new RuntimeException("resource with id:"+p.getId()+" already exists");
				System.out.println("replacing resource with id:"+p.getId());
				resources.remove(presentRes);	
		}
		resources.add(p);
		
		System.out.println("EJB resources (after adding) size:"+resources.size());
		return p.getId();
		}
	
	@Lock(LockType.READ)
	public Resource getResource(String id, Class c){
		System.out.println("EJB searching for resource with id:"+id);
		System.out.println("resources size:"+resources.size());
		for(Resource p:resources){
			if(!c.isInstance(p)) continue;
			System.out.println("examining resource with id:<"+p.getId() +"> for match to qid:<"+id+">");
			if (p.getId().equals(id)) {
				System.out.println("matched resource with id:"+p.getId());
				return p;
			}
		}
		return null;
	}
	
	@Lock(LockType.READ)
	public int getResourceTypeCount( Class c){
		System.out.println("EJB searching for resource type:"+c.getSimpleName());
		System.out.println("resources size:"+resources.size());
		int count=0;
		
		for(Resource p:resources){
			if(!c.isInstance(p)) continue;
			count++;
		}
		return count;
	}
	
	public void removeResource(Resource p1){
			resources.remove(p1);
	}
	
	@Lock(LockType.READ)
	public List<Resource> getQueried(Class c,
			MultivaluedMap<String,String> qp//Query Parameters
			)  {
		List<Resource> list=new ArrayList<Resource>();
		for(Resource p:getAll(c)){
			for(String k:qp.keySet()){
				Object returnValue=null;
				String returnStr=null;
				try {
					String methodName=k.substring(0,1).toUpperCase()+k.subSequence(1, k.length());
					System.out.println("searching for parameter:"+k+" with value "+qp.getFirst(k));
					Method method =
						    c.getMethod("get"+methodName, null);
					returnValue = method.invoke(p);
					Class returnType= method.getReturnType();
					if(returnType==org.hl7.fhir.String.class){
						org.hl7.fhir.String s1=(org.hl7.fhir.String)returnValue;
						returnStr=s1.getValue();
					}
					
				} catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				System.out.println("returnStr:"+returnStr);
				System.out.println("qp.getFirst(k):"+qp.getFirst(k));
				
				if (returnStr.toString().contains(qp.getFirst(k))){
					list.add(p);
			}
		  }
		}
		return list;
	}
	
	@Lock(LockType.READ)
	public List<Resource> getAll(Class c)  {
		List<Resource> list=new ArrayList<Resource>();
		for(Resource p:resources){
		  if(c.isInstance(p)){
					list.add(p);
			}
		}
		return list;
	}

	public Resource getParticularResource(Class c, String id) {
		for(Resource r:resources){
			  if(c.isInstance(r) && r.getId().equals(id)){
				  return r;			  }
		  }
		return null;
	}
}
