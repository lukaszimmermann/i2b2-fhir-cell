package edu.harvard.i2b2.fhir;


import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.XML;

public class Utils {
	public static String getFile(String fileName) {

		String result = "";

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			result = IOUtils
					.toString(classLoader.getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}
	
	public static String getFilePath(String fileName) {

		String result = "";

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		result = classLoader.getResource(fileName).getPath();

		return result;

	}
	
	public static Class getClassFromClassPath(String resourceName) {
		ClassLoader loader = Utils.class.getClassLoader();
		try {
			return Class.forName(resourceName, false, loader);
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Found for:"
					+ resourceName);
			e.printStackTrace();
			return null;
		}
	}
	
	public static String xmlToJson(String xmlStr) throws JSONException {
		return XML.toJSONObject(xmlStr).toString(2);
	}

/*
 * public String convert(String input) throws ParserConfigurationException, SAXException, IOException{
		// Create the encoder and decoder for ISO-8859-1
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		CharsetEncoder encoder = charset.newEncoder();

		    // Convert a string to ISO-LATIN-1 bytes in a ByteBuffer
		    // The new ByteBuffer is ready to be read.
		    ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(input));

		    // Convert ISO-LATIN-1 bytes in a ByteBuffer to a character ByteBuffer and then to a string.
		    // The new ByteBuffer is ready to be read.
		    CharBuffer cbuf = decoder.decode(bbuf);
		    //if(1==1)return Utils.getFile("example/i2b2/medicationsForAPatient2.xml");
		    if(1==1)return FromI2b2WebService.getStringFromDocument( FromI2b2WebService.xmltoDOM(cbuf.toString()));
		    return cbuf.toString();
		
	}
	
	public static Document xmltoDOM(String xmlStr) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlStr));

		return db.parse(is);
	}
	
	public static String getStringFromDocument(Document doc)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	} 
 */

}
