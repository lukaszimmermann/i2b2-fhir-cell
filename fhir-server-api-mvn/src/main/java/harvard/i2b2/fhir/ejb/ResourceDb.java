package harvard.i2b2.fhir.ejb;


import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Entry;
import org.apache.abdera.writer.Writer;
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
		if (p.getId()==null) {
			p.setId(Integer.toString(resources.size()));
		}
		
		if (getResource(p.getId(),c)!=null) {
				throw new RuntimeException("resource with id:"+p.getId()+" already exists");
		}
		resources.add(p);
		System.out.println("Put resource:"+p.getClass().getSimpleName()+"/"+p.getId());
		System.out.println("resources size:"+resources.size());
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
	
	public void removeResource(Resource p1){
			resources.remove(p1);
	}

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
	
	
}
