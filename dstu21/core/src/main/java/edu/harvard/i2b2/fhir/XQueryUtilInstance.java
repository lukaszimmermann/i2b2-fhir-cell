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
 * 		July 4, 2015
 */
package edu.harvard.i2b2.fhir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.basex.core.*;
import org.basex.core.cmd.*;
import org.basex.data.Result;
import org.basex.query.QueryException;
import org.basex.query.QueryIOException;
import org.basex.query.QueryProcessor;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class  XQueryUtilInstance{
	static Logger logger = LoggerFactory.getLogger(XQueryUtilInstance.class);
	
	String dbName;
	Context context; 
	CreateDB db;
	public XQueryUtilInstance() throws BaseXException{
		dbName=UUID.randomUUID().toString();
		
			context = new Context();
		
			try {
				new Set("intparse", true).execute(context);
			} catch (BaseXException e) {
				logger.error(e.getMessage(),e);
				throw e;
			}
			db = new org.basex.core.cmd.CreateDB(dbName);
			
		
	}
	
	public void close() throws BaseXException{
		new DropDB(dbName).execute(context);
		context.close();
	}
	
	public String processXQuery(String query, String input)
			throws XQueryUtilException, BaseXException {
		String result = null;
		InputStream is=  new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
		db.setInput(is);
		db.execute(context);
		
		// Create a database from a remote XML document
		// System.out.println("\n* Create a database from a file via http.");

		try (QueryProcessor proc = new QueryProcessor(query, context)) {
			// Store the pointer to the result in an iterator:
			result = proc.execute().serialize();
		} catch (IOException |QueryException e) {
			e.printStackTrace();
			logger.error("",e);
			throw new XQueryUtilException(e);

		} 	
		
		return result;
	}

	

	public  ArrayList<String> getStringSequence(String query, String input)
			throws XQueryUtilException {
		ArrayList<String> resList = new ArrayList<String>();
		// Database context.
		

		// Create a database from a remote XML document
		// System.out.println("\n* Create a database from a file via http.");

		// Use internal parser to skip DTD parsing
		try {
			new Set("intparse", true).execute(context);
			new org.basex.core.cmd.CreateDB(dbName, input).execute(context);

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

			// System.out.println("\n* Drop the database.");
			new DropDB(dbName).execute(context);
		} catch (BaseXException e1) {
			e1.printStackTrace();
			logger.error("",e1);
			throw new XQueryUtilException(e1);
		}
		context.close();
		return resList;
	}

	

	
}
