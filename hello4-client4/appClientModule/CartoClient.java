

/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import tutorial.bean.CartoBean;
import tutorial.ejb.Carto;
import tutorial.util.BookException;

import javax.naming.Binding;

import com.sample.ejb.SampleBeanRemote;
import com.sample.ejb.SampleBeanRemoteImpl;

/**
 *
 * The client class for the CartBean example. Client adds books to the cart,
 * prints the contents of the cart, and then removes a book which hasn't been
 * added yet, causing a BookException.
 * @author ian
 */
public class CartoClient {
   @EJB
    private static Carto cart;

    public CartoClient(String[] args) {
    }

    /**
     * @param args the command line arguments
     * @throws NamingException 
     */
    public static void main(String[] args) throws NamingException {
        CartoClient client = new CartoClient(args);
        client.doTest();
    }

    public void doTest() throws NamingException {
    	
    	final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
 
        final Context context = new InitialContext(jndiProperties);
 
 
 //System.out.println("C:"+context.listBindings(""));
/*
     NamingEnumeration namingenumeration = context.listBindings("");
     while (namingenumeration.hasMore()) {
       Binding binding = (Binding)namingenumeration.next();
       System.out.println(
         binding.getName() + " " +
         binding.getObject()
       );
     }
     System.out.println("l");
  */ 
   //context.close();
 //cart=	     (Carto)context.lookup("ejb:/hello4//!tutorial.ejb.Carto");
    
        //ejb:/hello4//SampleBeanRemoteImpl!com.sample.ejb.SampleBeanRemote
        cart=(Carto) context.lookup("ejb:/hello4//CartoBean!tutorial.ejb.Carto?stateful" );
        
        try {

        	cart.initialize("Duke dUrl", "123");
            cart.addBook("Infinite Jest");
            cart.addBook("Bel Canto");
            cart.addBook("Kafka on the Shore");
            
            

            List<String> bookList = cart.getContents();

            Iterator<String> iterator = bookList.iterator();

            while (iterator.hasNext()) {
                String title = iterator.next();
                System.out.println("Retrieving book title from cart: " + title);
            }

            System.out.println("Removing \"Gravity's Rainbow\" from cart.");
            cart.removeBook("Gravity's Rainbow");
            cart.remove();

            System.exit(0);
     
        } catch (BookException ex) {
            System.err.println("Caught a BookException: " + ex.getMessage());
            System.exit(0);
        }
  }
}
