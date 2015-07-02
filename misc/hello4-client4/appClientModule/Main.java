import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;

import tutorial.ejb.Carto;

public class Main {

	static public void main(String[] args) {
		try {
			// System.setProperty("java.naming.factory.initial",
			// "com.sun.enterprise.naming.SerialInitContextFactory");
			// System.setProperty("java.naming.factory.url.pkgs",
			// "com.sun.enterprise.naming");
			// System.setProperty("java.naming.factory.state",
			// "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
			System.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
			System.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
			InitialContext jndiContext = new InitialContext();

			System.out.println("Attempting to instantiate InitialContext.");
			// Context jndiContext = (Context) new InitialContext (prop);
			System.out.println("InitialContext instatiated: "
					+ jndiContext.getClass().getSimpleName());
			System.out.println("Attempting to lookup ConnectionFactory.");
			// Carto cart = (Carto)
			// jndiContext.lookup("java:comp/env/ejb/tutorial/ejb/Carto");
			String tat = (String) jndiContext.lookup("resource/blogAddress");
			System.out.println("Resource  looked up: " + tat);
			// System.out.println("ConnectionFactory looked up: " +
			// cart.getClass().getSimpleName());
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getClass().getSimpleName() + "\n"
					+ e.getMessage());
		}
	}

	// Define Property key:value pairs
	static private final Properties prop = new Properties();
	static private final String[] keyValueArray = {
	// "java.naming.factory.initial      com.sun.appserv.naming.S1ASCtxFactory"
	// ,"java.naming.factory.url.pkgs     com.sun.enterprise.naming"
	// "java.naming.provider.url     : iiop://localhost:3700"
	// Tried and failed
	// "iiop://localhost:3700"
	// "iiop://localhost:4848"
	// "[http]  ://localhost:8080" I had to bracket http
	// "[http]  ://localhost:4848" because I can’t post “links” yet.

	// ,"java.naming.factory.state    : com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl"
	// ,"org.omg.CORBA.ORBInitialHost   ????", Not sure if needed. Unsure of
	// values
	// ,"org.omg.CORBA.ORBInitialPort   3700", Not sure if needed.
	// ,"com.sun.appserv.iiop.endpoints ????" Not sure if needed. Unsure of
	// values
	};

	// Load Property key:value pairs
	static {
		for (int indx = 0; indx < keyValueArray.length; indx++) {
			String key = keyValueArray[indx].substring(0,
					keyValueArray[indx].indexOf(' '));
			String value = keyValueArray[indx].substring(keyValueArray[indx]
					.lastIndexOf(' ') + 1);
			System.out.println(key + "=" + value);
			prop.put(key, value);
		}
		System.out.println("Property key:value pairs successfully loaded.");
	}

}
