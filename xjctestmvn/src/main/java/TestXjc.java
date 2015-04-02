
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import javax.xml.xquery.XQSequence;
















import net.sf.saxon.Configuration;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TestXjc {
	static String sep;
	
	public static void main(String[] args) {
		sep= System.getProperty("line.separator");
		// TODO Auto-generated method stub
		System.out.println("will run xquery");
		try{
			processXquery("hi");
		}catch(Exception e){
			e.printStackTrace();
		}
	

	}

	private static void processXquery(String input) throws XQException, FileNotFoundException {
		Configuration conf=new Configuration();
		
	}
	/**
	 * @param input
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws XQException
	 * @throws XMLStreamException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	/*@SuppressWarnings("restriction")
	private static String processXquery1(String input)
	throws InstantiationException, IllegalAccessException, ClassNotFoundException, XQException, XMLStreamException, ParserConfigurationException, SAXException, IOException{
		XQDataSource xqds = new SaxonXQDataSource();
		XQConnection conn = xqds.getConnection();
		String str="";
		XQExpression xqe;
		XQSequence xqs;
		xqe = conn.createExpression();
		xqs=xqe.executeQuery("doc('src/main/resources/example/i2b2/i2b2medspod.txt')//observation");
		
		while (xqs.next()) {
			System.out.println(xqs.getSequenceAsString(null));
			//str+=xqs.getAtomicValue();
			}
		//return str;
		
		
		//
		
		InputStream query;
		query = new FileInputStream("src/main/resources/transform/I2b2ToFhir/i2b2MedsToFHIRMeds.xquery");
		xqe = conn.createExpression();
		xqs = xqe.executeQuery(query);
		
		//
		
		 String query = getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMeds.xquery");
		 XQPreparedExpression expr = conn.prepareExpression(query); 
		 input=getFile("example/i2b2/i2b2medspod.txt");
		 //System.out.println("processsing:"+input);
		
		 
		 
		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 factory.setNamespaceAware(true);

		 DocumentBuilder parser = factory.newDocumentBuilder();
		 InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
		 Document domDocument = parser.parse(stream);
				 
		 expr.bindNode(new QName("doc"), domDocument,null);
		 
		 xqs=expr.executeQuery();
		 while (xqs.next()) {
				System.out.println(xqs.getSequenceAsString(null));
				//str+=xqs.getAtomicValue();
				}
			
		 return str;
		//
		 Reader reader = new StringReader(input);
		 XMLInputFactory factory = XMLInputFactory.newInstance(); // Or newFactory()
		 XMLStreamReader xmlReader = factory.createXMLStreamReader(reader);
		 
		 	expr.bindDocument(new QName("doc"), xmlReader, null);
		
		 String query = "declare variable $x as xs:integer external; "
						+sep+" for $n in $x"+
						sep +" return  <test>$n+1</test>";
		 
		XQPreparedExpression expr = conn.prepareExpression(query); 
		expr.bindInt(new QName("x"), 21, null);
		//
		 
		// Bind a value (21) to an external variable with the QName x
		 
		// Execute the XQuery expression
		//XQExpression expr = conn.createExpression();
		
		
		//XQSequence result = expr.executeQuery();
        //System.out.println(result.getSequenceAsString(null));
        
		//return result.getSequenceAsString(null);
		
	}
	*/
	
	static private String getFile(String fileName){
		 
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
