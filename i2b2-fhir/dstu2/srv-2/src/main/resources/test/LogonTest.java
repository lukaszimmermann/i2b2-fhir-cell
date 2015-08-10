package test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import edu.harvard.i2b2.fhirserver.entity.User;

public class LogonTest {
     private static final String PERSISTENCE_UNIT_NAME = "User";
     private static EntityManagerFactory factory;

     public static void main(String[] args) {
          factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
          EntityManager em = factory.createEntityManager();
          // Read the existing entries and write to console
          Query q = em.createQuery("SELECT u FROM User u");
          List<User> userList = q.getResultList();
          for (User user : userList) {
               System.out.println(user.Name);
          }
          System.out.println("Size: " + userList.size());

          // Create new user
          em.getTransaction().begin();
          User user = new User();
          user.setName("Tom Johnson");
          user.setLogin("tomj");
          user.setPassword("pass");
          em.persist(user);
          em.getTransaction().commit();

          em.close();
     }
}
