package harvard.i2b2.fhir.ejb;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.ws.rs.core.MultivaluedMap;

import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.Resource;
import org.hl7.fhir.instance.model.ResourceReference;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.MetaResourceDb;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

@Startup
@Singleton
//@Stateful
//@StatefulTimeout(unit=TimeUnit.MINUTES,value = 200)
public class MetaResourceDbWrapper {
	private MetaResourceDb mrdb=null;

	@PostConstruct
	void init() {
		mrdb = new MetaResourceDb();
	}

	
	@Lock(LockType.WRITE)
	public void addMetaResourceSet(MetaResourceSet s) {
		mrdb.addMetaResourceSet(s);
	}

	@Lock(LockType.READ)
	public MetaResourceSet getIncludedMetaResources(
			MetaResourceSet inputSet, Class c, List<String> includeResources) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return mrdb.getIncludedMetaResources(c,inputSet, includeResources);
	}

	@Lock(LockType.READ)
	public Resource getParticularResource(Class c, String id) {
		
		return mrdb.getParticularResource(c, id);
	}

	
}
