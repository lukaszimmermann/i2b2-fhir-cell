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
package edu.harvard.i2b2.loinc;

import edu.harvard.i2b2.lib.mapper.LoincMapper;
import org.hl7.fhir.Coding;
import org.hl7.fhir.Observation;



public class LoincFhirAdapter {

	//static Logger logger = LoggerFactory.getLogger(LoincFhirAdapter.class);


	public void addLoincName(Observation ob) {

		for (Coding coding : ob.getCode().getCoding()) {

			String systemVal = coding.getSystem().getValue().toString();
			if (systemVal.equals("http://loinc.org")) {
				
				String loincNumber = coding.getCode().getValue().replace("LOINC:", "");

				if(loincNumber==null || loincNumber.length()==0)  {

				    continue;
                }
				
				String display = LoincMapper.getLoincName(loincNumber);

				if (display==null || display.length()==0) continue;
				//logger.trace("loincNum:"+loincNumber);
				org.hl7.fhir.String displayValue = new org.hl7.fhir.String();
				displayValue.setValue(display);
				coding.setDisplay(displayValue);
			}
		}
	}	
		

}