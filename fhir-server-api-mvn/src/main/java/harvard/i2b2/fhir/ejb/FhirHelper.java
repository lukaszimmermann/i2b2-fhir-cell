package harvard.i2b2.fhir.ejb;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.writer.Writer;
import org.hl7.fhir.Resource;

/**
 * Session Bean implementation class FhirHelper
 */
@Stateless
@LocalBean
public class FhirHelper {

	private static final String RESOURCE_LIST = "(patient)|(medication)|(observation)";

    /**
     * Default constructor. 
     */
    public FhirHelper() {
        // TODO Auto-generated constructor stub
    }
    
	public String getResourceBundle(List<Resource> resList, String uriInfoString) {

		Abdera abdera = new Abdera();
		Writer writer1 = abdera.getWriterFactory().getWriter();// .getWriter("prettyxml");
		Feed feed = abdera.newFeed();

		StringWriter swriter = new StringWriter();
		try {

			feed.setId(uriInfoString);
			feed.setTitle("all class" + " bundle");
			feed.setUpdated(new Date());

			StringWriter rwriter = new StringWriter();
			// for(Resource r:resourcedb.getAll(c)){
			for (Resource r : resList) {
				for (Class c : getResourceClasses()) {
					JAXBContext jaxbContext = JAXBContext.newInstance(c);
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					jaxbMarshaller.setProperty(
							Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

					if (c.isInstance(r)) {
						Entry entry = feed.addEntry();
						entry.setId(r.getId());
						jaxbMarshaller.marshal(r, rwriter);
						entry.setContent(rwriter.toString(), "application/xml");
						rwriter.getBuffer().setLength(0);// reset String writer
					}
				}
			}
			writer1.writeTo(feed, swriter);
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		return swriter.toString();
	}

    public Class getResourceClass(String resourceName) {
		ClassLoader loader = this.getClass().getClassLoader();
		String targetClassName = "org.hl7.fhir."
				+ resourceName.substring(0, 1).toUpperCase()
				+ resourceName.substring(1, resourceName.length());
		try {
			return Class.forName(targetClassName, false, loader);
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Found for FHIR resource:"
					+ resourceName);
			return null;
		}
	}
    
	public List<Class> getResourceClasses(){
		List<Class> classList=new ArrayList<Class>();
		for(String x:RESOURCE_LIST.split("|")){
			x=x.replace("(", "").replace(")","");
			Class y=getResourceClass(x);
			if(y!=null)classList.add(y);
		}
		return classList;
		
	}
			

}
