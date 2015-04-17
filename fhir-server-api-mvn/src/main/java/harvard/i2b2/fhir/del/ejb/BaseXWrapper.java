package harvard.i2b2.fhir.del.ejb;



import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.Stateless;

import org.apache.commons.io.IOUtils;
import org.basex.core.*;
import org.basex.core.cmd.*;
import org.hl7.fhir.Resource;

@Stateless
public class BaseXWrapper {

	public static String processXquery(String query, String input)  {
	  System.out.println("query:"+query.substring(0,(query.length()>200)?200:0));
		System.out.println("input:"+input.substring(0,(input.length()>200)?200:0));
	
	  String resultStr=null;
	 
	  try{
	  //String query = getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMeds.xquery");
	//	String input=getFile("example/i2b2/i2b2medspod.txt");
    // Database context.
    Context context = new Context();

    //System.out.println("=== WikiExample ===");

    // Create a database from a remote XML document
    //System.out.println("\n* Create a database from a file via http.");

    // Use internal parser to skip DTD parsing
    new Set("intparse", true).execute(context);

    //final String doc = "http://en.wikipedia.org/wiki/Wikipedia";
    //new CreateDB("WikiExample", doc).execute(context);

    
    new org.basex.core.cmd.CreateDB("LocalXml", input).execute(context);
    
    // Insert a node before the closing body tag
    // N.B. do not forget to specify the namespace
    //System.out.println("\n* Update the document.");

    resultStr=new XQuery(
      query
    	
    ).execute(context);


    // ----------------------------------------------------------------------
    // Drop the database
    //System.out.println("\n* Drop the database.");

    new DropDB("WikiExample").execute(context);

    // ------------------------------------------------------------------------
    // Close the database context
    context.close();
	  }catch( BaseXException e){
		  e.printStackTrace();
	  }
	  
    return resultStr;
  }
  /*
	public static ArrayList<Resource> processXqueryForResourceList(String query, String input)  {
		  System.out.println("query:"+query.substring(0,(query.length()>200)?200:0));
			System.out.println("input:"+input.substring(0,(input.length()>200)?200:0));
		
		  String resultStr=null;
		 
		  try{
		  //String query = getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMeds.xquery");
		//	String input=getFile("example/i2b2/i2b2medspod.txt");
	    // Database context.
	    Context context = new Context();

	    //System.out.println("=== WikiExample ===");

	    // Create a database from a remote XML document
	    //System.out.println("\n* Create a database from a file via http.");

	    // Use internal parser to skip DTD parsing
	    new Set("intparse", true).execute(context);

	    //final String doc = "http://en.wikipedia.org/wiki/Wikipedia";
	    //new CreateDB("WikiExample", doc).execute(context);

	    
	    new org.basex.core.cmd.CreateDB("LocalXml", input).execute(context);
	    
	    // Insert a node before the closing body tag
	    // N.B. do not forget to specify the namespace
	    //System.out.println("\n* Update the document.");

	    resultStr=new XQuery(
	      query
	    	
	    ).


	    // ----------------------------------------------------------------------
	    // Drop the database
	    //System.out.println("\n* Drop the database.");

	    new DropDB("WikiExample").execute(context);

	    // ------------------------------------------------------------------------
	    // Close the database context
	    context.close();
		  }catch( BaseXException e){
			  e.printStackTrace();
		  }
		  
	    resultStr.
	  }*/
}
