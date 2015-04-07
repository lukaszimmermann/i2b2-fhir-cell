package basex;


import java.io.IOException;

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
public final class WikiExample {
  /**
   * Runs the example code.
   * @param args (ignored) command-line arguments
   * @throws BaseXException if a database command fails
   */
  public static void main(final String[] args) throws BaseXException {
	  String query = getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMeds.xquery");
		String input=getFile("example/i2b2/i2b2medspod.txt");
    // Database context.
    Context context = new Context();

    System.out.println("=== WikiExample ===");

    // Create a database from a remote XML document
    System.out.println("\n* Create a database from a file via http.");

    // Use internal parser to skip DTD parsing
    new Set("intparse", true).execute(context);

    final String doc = "http://en.wikipedia.org/wiki/Wikipedia";
    //new CreateDB("WikiExample", doc).execute(context);

    
    new org.basex.core.cmd.CreateDB("LocalXml", input).execute(context);
    

    
    try(QueryProcessor proc = new QueryProcessor(query, context)) {
        // Store the pointer to the result in an iterator:
        Iter iter = proc.iter();

        // Iterate through all items and serialize
        for(Item item; (item = iter.next()) != null;) {
          System.out.println(item.serialize().toString());
          System.out.println("----------");
        }
      } catch (QueryException | QueryIOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    

    // ----------------------------------------------------------------------
    // Drop the database
    System.out.println("\n* Drop the database.");

    new DropDB("WikiExample").execute(context);

    // ------------------------------------------------------------------------
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