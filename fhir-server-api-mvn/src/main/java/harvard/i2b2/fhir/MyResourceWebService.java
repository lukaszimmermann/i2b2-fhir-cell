package harvard.i2b2.fhir;

import harvard.i2b2.fhir.entity.MyResource;
import harvard.i2b2.fhir.entity.Customer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBElement;

import org.hl7.fhir.Patient;


@Path("myresource")
public class MyResourceWebService {
	/*
	@GET
	public String get(){
		return "Hello2:";
	}
	*/
	@GET
	@Path("{name}")
	@Produces("text/plain")
	public String get(@PathParam("name") String name){
		
		return "Hello5:"+ name;
	}
	@GET
	@Path("byname/{name}")
	//@Produces("text/plain")
	public MyResource getResource(@PathParam("name") String name){
		MyResource r=new MyResource();
		r.setName(name);
		
		
		return r;
	}

	@GET
	@Path("patient-{name}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Patient getPatient(@PathParam("name") String name){
		Patient r=new Patient();
		r.setId(name);
		return r;
		}
	
	@GET
    @Path("customer-{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Customer getCustomer(@PathParam("id") String customerId) {
        Customer customer = null;
        customer= new Customer();
        customer.setId(100);
      return customer;
    }
	
	
}

