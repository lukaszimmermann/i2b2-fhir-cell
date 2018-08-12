package edu.harvard.i2b2.fhir.profile;

import java.io.IOException;

import edu.harvard.i2b2.fhir.Utils;

//** store path for a profile for each resource type
public class PathForReference {
	Class resourceTypeParent;
	Class resourceTypeReference;

	String path;

	PathForReference(Class resourceTypeParent, Class resourceTypeReference) throws IOException {
		String profileTxt=Utils.fileToString("/"+resourceTypeParent.getSimpleName().toLowerCase()+".profile.xml");
		//String path=XQuery
	}

}
