package harvard.i2b2.fhir.ejb;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaFileManager.Location;
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
import org.apache.xml.serializer.utils.Utils;
import org.hl7.fhir.Resource;


/**
 * Session Bean implementation class FhirHelper
 */
@Startup
@Singleton
public class FhirHelper {

	public static final String RESOURCE_LIST = "(Patient)|(Medication)|(Observation)|(MedicationStatement)";
	public static List<Class>resourceClassList =new ArrayList<Class>();
    /**
     * Default constructor. 
     */
   
    
    @PostConstruct
	void init(){
    	initResourceClassList();
    	System.out.println("resClassList:"+resourceClassList);
    }
    public static List<Class> getResourceClassList(){
    	return resourceClassList;
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
    	if(resourceClassList==null) initResourceClassList();
    	for(Class c:resourceClassList){ 
    		if (c.getSimpleName().toLowerCase().equals(resourceName.toLowerCase()))
    			return c;
    	}
			System.out.println("Class Not Found for FHIR resource:"
					+ resourceName);
			return null;
		
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
			
public static void initResourceClassList(){
	
		try {
			for(Class c:getAllFhirResourceClasses("org.hl7.fhir")){
			
				System.out.println(c.getSimpleName());
				resourceClassList.add(c);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
	}
		
	public static List<Class> getAllFhirResourceClasses(String packageName) throws IOException{
		
		System.out.println("Running getAllFhirResourceClasses for:"+packageName);
		List<Class> commands = new ArrayList<Class>();

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(
		        null, null, null);

		Location location = StandardLocation.CLASS_PATH;
		
		Set<JavaFileObject.Kind> kinds = new HashSet<JavaFileObject.Kind>();
		kinds.add(JavaFileObject.Kind.CLASS);
		boolean recurse = false;

		Iterable<JavaFileObject> list = fileManager.list(location, packageName,
		        kinds, recurse);

		for (JavaFileObject javaFileObject : list) {
		    commands.add(javaFileObject.getClass());
		}
		
		Class c=null;
		for (String x: 
			"org.hl7.fhir.Patient|org.hl7.fhir.Medication|org.hl7.fhir.Observation|org.hl7.fhir.MedicationStatement".split("\\|")){
		c=harvard.i2b2.fhir.utils.Utils.getClassFromClassPath(x);
		if(c!=null)commands.add(c);
		}
		System.out.println(commands.toString());
		return commands;
	}

}
