package edu.harvard.i2b2;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.basex.core.*;
import org.basex.core.cmd.*;
import org.basex.data.Result;
import org.basex.query.QueryException;
import org.basex.query.QueryIOException;
import org.basex.query.QueryProcessor;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;

/**
 * This example demonstrates how databases can be created from remote XML
 * documents, and how XQuery can be used to locally update the document and
 * perform full-text requests.
 *
 * @author BaseX Team 2005-15, BSD License
 */
public final class XQueryUtil {
	/**
	 * Runs the example code.
	 * 
	 * @param args
	 *            (ignored) command-line arguments
	 * @throws BaseXException
	 *             if a database command fails
	 */
	
	public static ArrayList<String> getStringSequence(String input, String query) {
		ArrayList<String> resList = new ArrayList<String>();
		// Database context.
		Context context = new Context();

		// Create a database from a remote XML document
		System.out.println("\n* Create a database from a file via http.");

		// Use internal parser to skip DTD parsing
		try {
			new Set("intparse", true).execute(context);
			new org.basex.core.cmd.CreateDB("LocalXml", input).execute(context);

			try (QueryProcessor proc = new QueryProcessor(query, context)) {
				// Store the pointer to the result in an iterator:
				Iter iter = proc.iter();

				// Iterate through all items and serialize
				int count = 0;
				for (Item item; (item = iter.next()) != null;) {
					String r = item.serialize().toString();
					resList.add(r);
				}
			} catch (QueryException | QueryIOException e) {
				e.printStackTrace();
			}

			System.out.println("\n* Drop the database.");
			new DropDB("WikiExample").execute(context);
		} catch (BaseXException e1) {
			e1.printStackTrace();
		}
		context.close();
		return resList;
	}

	public static String processXQuery(String query,String input)  {
		String resultString =null;
	    // Database context.
		  
		  //String query = getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMeds.xquery");
			//String input=getFile("example/i2b2/i2b2medspod.txt");
			
	    Context context = new Context();

	    System.out.println("=== BindContext ===");

	    // Specify query to be executed
	    //query = "declare context item external; .";

	    // Show query
	    System.out.println("\n* Query:");
	    //System.out.println(query);

	    // Create a query processor
	    try(QueryProcessor proc = new QueryProcessor(query, context)) {
	    	Result res= proc.execute();
	    	resultString =res.serialize();

	  }catch(Exception e){
		  e.printStackTrace();
	  }
	    
   	    context.close();
   	    return resultString;
	  }
	  

}
