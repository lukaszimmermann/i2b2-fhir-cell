package srv;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import edu.harvard.i2b2.fhirserver.entity.Person;

public class Testing {
    @Test
    public void test2(){

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("thePersistenceUnit");
        EntityManager theManager = factory.createEntityManager();
        theManager .getTransaction().begin();
        Person person = new Person();
        person.setFirstName("ana");
        theManager.persist(person);
        theManager.getTransaction().commit();
        }
}