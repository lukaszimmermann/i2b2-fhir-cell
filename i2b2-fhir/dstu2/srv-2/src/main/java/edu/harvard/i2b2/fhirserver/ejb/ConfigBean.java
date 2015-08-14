package edu.harvard.i2b2.fhirserver.ejb;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.h2.jdbcx.JdbcConnectionPool;
//import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhirserver.entity.AccessToken;
import edu.harvard.i2b2.fhirserver.entity.Person;

@Singleton
@Startup
public class ConfigBean {
	static Logger logger = LoggerFactory.getLogger(ConfigBean.class);

	//@PostConstruct
	public void init() {
		try {
			JdbcConnectionPool cp = JdbcConnectionPool.create("jdbc:h2:~/test",
					"sa", "sa");
			Connection conn = cp.getConnection();
			// conn.createStatement().execute("CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR);"
			// +"INSERT INTO TEST VALUES(1, 'Hello World');"
			// +"CALL FT_CREATE_INDEX('PUBLIC', 'TEST', NULL);");
			// conn.createStatement().execute(
			// "INSERT INTO TEST VALUES(2, 'Hello2 World');");
			Statement stmt = conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stmt.executeQuery("SELECT * FROM AuthToken");
			// logger.info("r:" + rs.toString());
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1)
						System.out.print(",  ");
					String columnValue = rs.getString(i);
					logger.info(rs.getString(i));
					logger.info(columnValue + " " + rsmd.getColumnName(i));
				}
				// logger.info("");
			}

			conn.close();

			cp.dispose();
			 test2();
			 createData();
		} catch (Exception e) {
			logger.info("erro", e);
			e.printStackTrace();
		}

	}

	private void test2() {

		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("testPer");
		EntityManager theManager = factory.createEntityManager();
		//theManager.getTransaction().begin();
		Person person = new Person();
		person.setFirstName("ana3");
		theManager.persist(person);
		//theManager.getTransaction().commit();
		logger.info("COMPLETED");
	}

	private void createData() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("testPer");
		EntityManager theManager = factory.createEntityManager();
		//theManager.getTransaction().begin();
		AccessToken a = new AccessToken("resourceUserId1", "i2b2Token1",
				"authorizationCode1", "clientRedirectUri1", "clientId");
		theManager.persist(a);
		a = new AccessToken("resourceUserId2", "i2b2Token2",
				"authorizationCode2", "clientRedirectUri2", "clientId2");
		theManager.persist(a);
		//theManager.getTransaction().commit();
		logger.info("COMPLETEDa");
	}

	@PreDestroy
	public void deleteData() {

	}
}