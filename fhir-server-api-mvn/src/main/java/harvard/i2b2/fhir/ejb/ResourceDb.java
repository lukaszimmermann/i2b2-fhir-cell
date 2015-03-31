package harvard.i2b2.fhir.ejb;


import java.io.IOException;
import java.io.StringWriter;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Entry;
import org.apache.abdera.writer.Writer;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;

@Startup
@Singleton
public class ResourceDb {
	List<Resource> resources;
	
	@PostConstruct
	void init(){
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
	
/*
	@Lock(LockType.READ)
	public String getall(Class c)  {
		Abdera abdera = new Abdera();
		Writer writer1 = abdera.getWriterFactory().getWriter();//.getWriter("prettyxml");
		Feed feed = abdera.newFeed();
		 
		StringWriter swriter=new StringWriter();
		  try {
			  feed.setId("123");
			  feed.setTitle(c.getSimpleName()+" search");
			  JAXBContext jaxbContext = JAXBContext.newInstance(c);
			  Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			  jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			  
			  StringWriter pwriter=new StringWriter();
			  for(Resource p:resources){
				  if(c.isInstance(p)){
					Entry entry = feed.addEntry();
					entry.setId(p.getId());
					jaxbMarshaller.marshal(p, pwriter);
					entry.setContent(pwriter.toString(),"application/xml");
					pwriter.getBuffer().setLength(0);//reset String writer
				  }
				}
			writer1.writeTo(feed,swriter);
		} catch (IOException|JAXBException e) {
			e.printStackTrace();
		}
		  return swriter.toString();
	}
*/	
	public Resource getParticularResource(Class c, String id) {
		for(Resource r:resources){
			  if(c.isInstance(r) && r.getId().equals(id)){
				  return r;			  }
		  }
		return null;
	}
}
