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

package wrapperGson;
import java.io.IOException;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
	 
	public class GsonWrapper {
		static Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		public static String resourceToJson(Object obj) throws IOException {
			String json = gson.toJson(obj);
		return json;
		
	}

}
