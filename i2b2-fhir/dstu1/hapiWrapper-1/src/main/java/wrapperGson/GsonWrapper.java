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
