package harvard.i2b2.fhir;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.hl7.fhir.Patient;

/**
 * Provide a customized JAXBContext that makes the concrete implementations 
 * known and available for marshalling
 * 
 * @author Michael Irwin
 */
@Provider
@Produces({"application/xml", "application/json"})
public class JaxbContextResolver implements ContextResolver<JAXBContext> {

  private JAXBContext jaxbContext;

  public JaxbContextResolver() {
    try {
      jaxbContext =
          JAXBContext.newInstance(Patient.class);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public JAXBContext getContext(Class<?> clazz) {
    return jaxbContext;
  }
  
}