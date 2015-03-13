package harvard.i2b2.fhir.ejb;

import harvard.i2b2.fhir.entity.Patients;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Entry;
import org.apache.abdera.writer.Writer;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Startup
@Singleton
public class PatientDb {
	List<Patient> patients;
	
	@PostConstruct
	void init(){
		patients= new ArrayList<Patient>();
		System.out.println("created patientdb");
		System.out.println("patients size:"+patients.size());
	}
	
	@Lock(LockType.WRITE)
	public void addPatient(Patient p){
		if (p.getId()==null) {
			p.setId(Integer.toString(patients.size()));
		}
		
		if (getPatient(p.getId())!=null) {
				throw new RuntimeException("patient with id:"+p.getId()+" already exists");
		}
		System.out.println("adding patient with id:"+p.getId());
		patients.add(p);
		System.out.println("patients size:"+patients.size());
	}
	
	@Lock(LockType.READ)
	public Patient getPatient(String id){
		System.out.println("EJB searching for patient with id:"+id);
		System.out.println("patients size:"+patients.size());
		for(Patient p:patients){
			System.out.println("examining patient with id:<"+p.getId() +"> for match to qid:<"+id+">");
			if (p.getId().equals(id)) {
				System.out.println("matched patient with id:"+p.getId());
				return p;
			}
		}
		return null;
	}
	
	public void removePatient(Patient p1){
			patients.remove(p1);
	}

	/*
	@Lock(LockType.READ)
	public Patients getall() {
		Patients ps=new Patients();
		for(Patient p:patients){
			ps.patients.add(p);
		}
		return ps;
	}
	*/
	@Lock(LockType.READ)
	public String getall()  {
		Abdera abdera = new Abdera();
		Writer writer1 = abdera.getWriterFactory().getWriter();//.getWriter("prettyxml");
		Feed feed = abdera.newFeed();
		 
		StringWriter swriter=new StringWriter();
		  try {
			  feed.setId("123");
			  feed.setTitle("Patient search");
			  JAXBContext jaxbContext = JAXBContext.newInstance(Patient.class);
			  Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			  jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				
				
			  for(Patient p:patients){
				  StringWriter pwriter=new StringWriter();
					Entry entry = feed.addEntry();
					entry.setId(p.getId());
					jaxbMarshaller.marshal(p, pwriter);
					entry.setContent(pwriter.toString(),"application/xml");
				}
			writer1.writeTo(feed,swriter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*feed.setId("fhir-api on i2b2");
		feed.setTitle("Patient search");
		feed.setSubtitle("Feed subtitle");
		feed.setUpdated(new Date());
		feed.addAuthor("i2b2");
		feed.addLink("http://i2b2.harvard.edu/fhir-api");
		for(Patient p:patients){
			Entry entry = feed.addEntry();
			entry.setId(p.getId());
			entry.setContent(getPatient(p.getId()).toString());
		}*/
		//return "<xml><Patient><id>100</id></Patient></xml>";
		  return swriter.toString();
	}
	
	
}
