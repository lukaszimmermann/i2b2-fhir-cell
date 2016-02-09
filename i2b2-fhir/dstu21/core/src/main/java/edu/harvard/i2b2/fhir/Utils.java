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
package edu.harvard.i2b2.fhir;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Utils {
	static Logger logger = LoggerFactory.getLogger(Utils.class);

	public static String getFile(String fileName) {

		String result = "";

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try {
			result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static String getFilePath(String fileName) {

		String result = "";

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		result = classLoader.getResource(fileName).getPath();

		return result;

	}

	public static Class getClassFromClassPath(String resourceName) {
		ClassLoader loader = Utils.class.getClassLoader();
		try {
			return Class.forName(resourceName, false, loader);
		} catch (ClassNotFoundException e) {
			logger.error("Class Not Found for:" + resourceName, e);
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * public String convert(String input) throws ParserConfigurationException,
	 * SAXException, IOException{ // Create the encoder and decoder for
	 * ISO-8859-1 Charset charset = Charset.forName("UTF-8"); CharsetDecoder
	 * decoder = charset.newDecoder(); CharsetEncoder encoder =
	 * charset.newEncoder();
	 * 
	 * // Convert a string to ISO-LATIN-1 bytes in a ByteBuffer // The new
	 * ByteBuffer is ready to be read. ByteBuffer bbuf =
	 * encoder.encode(CharBuffer.wrap(input));
	 * 
	 * // Convert ISO-LATIN-1 bytes in a ByteBuffer to a character ByteBuffer
	 * and then to a string. // The new ByteBuffer is ready to be read.
	 * CharBuffer cbuf = decoder.decode(bbuf); //if(1==1)return
	 * Utils.getFile("example/i2b2/medicationsForAPatient2.xml"); if(1==1)return
	 * FromI2b2WebService.getStringFromDocument(
	 * FromI2b2WebService.xmltoDOM(cbuf.toString())); return cbuf.toString();
	 * 
	 * }
	 */
	public static Document xmltoDOM(String xmlStr) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
		dbFac.setIgnoringElementContentWhitespace(true);
		DocumentBuilder db = dbFac.newDocumentBuilder();

		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlStr));

		return db.parse(is);
	}

	public static String getStringFromDocument(Document doc) {
		try {
			doc.normalize();
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			//
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			int indent = 2;
			if (indent > 0) {
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(indent));
			}
			//

			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static InputStream getInputStream(String fileName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		return classLoader.getResourceAsStream(fileName);

	}

	public static int countLines(String fileName) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		InputStream is = new BufferedInputStream(new FileInputStream(fileName));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	public static String fileToString(String filePath) throws IOException {
		return IOUtils.toString(Utils.class.getResourceAsStream(filePath));
	}

	
}
