import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.assertEquals;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.cache.Cache;
import edu.harvard.i2b2.fhir.converter.Application;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = Application.class)
//@TransactionConfiguration(defaultRollback = true)
//@Transactional
public class Spring4JUnit4Test {

	static Logger logger = LoggerFactory.getLogger(Cache.class);

	
	
	@Test
	public void testPut() {
		
		Cache cache= new Cache();
		logger.trace("hi");
		cache.put(
				Utils.getFile("examples/singlePatient.xml")
				);
	}
	
	@Test
	public void testGet() {
		
		Cache cache= new Cache();
		logger.trace("hi");
		logger.info(cache.get("http://localhost:8090//hapi-fhir-jpaserver-example/baseDstu2/Patient/example"));
	}
}
