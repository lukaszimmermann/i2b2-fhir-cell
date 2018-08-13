/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
package basex;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.basex.core.*;
import org.basex.query.*;

/**
 * This example demonstrates how items can be bound as context item of
 * the XQuery processor.
 *
 * @author BaseX Team 2005-15, BSD License
 */
public final class BindContext {
  /**
   * Runs the example code.
   * @param args (ignored) command-line arguments
   * @throws QueryException if an error occurs while evaluating the query
   */
  public static void main(final String[] args) throws QueryException {
    // Database context.
	  
	  String query = getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMeds.xquery");
		String input=getFile("example/i2b2/i2b2medspod.txt");
		
    Context context = new Context();

    System.out.println("=== BindContext ===");

    // Specify query to be executed
    //query = "declare context item external; .";

    // Show query
    System.out.println("\n* Query:");
    //System.out.println(query);

    // Create a query processor
    try(QueryProcessor proc = new QueryProcessor(query, context)) {
    	System.out.println(proc.execute());
        // Store the pointer to the result in an iterator:
       /* Iter iter = proc.iter();

        // Iterate through all items and serialize
        int count=0;
        for(Item item; (item = iter.next()) != null;) {
        	count++;
          System.out.println(item.toJava()+"\n"+count+"\n--------------\n");
        }
        */
      }
     // System.out.println("\n* Result:");
      //System.out.println(result);
    

    // Close the database context
    context.close();
  }
  
   

	
  
  public static String getFile(String fileName){
		 
	  String result = "";
 
	  ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	  try {
		result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
	  } catch (IOException e) {
		e.printStackTrace();
	  }
 
	  return result;
 
  }

}