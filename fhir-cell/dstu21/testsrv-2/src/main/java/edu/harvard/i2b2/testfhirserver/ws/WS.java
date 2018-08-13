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
package edu.harvard.i2b2.testfhirserver.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.harvard.i2b2.fhir.*;

@Path("")
public class WS {
	static Logger logger = LoggerFactory.getLogger(WS.class);

	@GET
	@Path("MedicationPrescription")
	public Response getFile() {
		String str = Utils.getFile("MedicationPrescriptionWithMedication3.json");
		return Response.ok().entity(str).type(MediaType.APPLICATION_JSON)
				.build();

	}

}