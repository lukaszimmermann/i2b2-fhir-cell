package edu.harvard.i2b2.fhir;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Medication;
import org.hl7.fhir.Resource;

import edu.harvard.i2b2.rxnorm.RxNormAdapter;

public class FhirEnrich {
	
	static RxNormAdapter rxNormAdapter = null;

	static {
			try {
				rxNormAdapter = new RxNormAdapter();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	static public Resource enrich(Resource r) throws JAXBException{
		//Medication rxnorm
		if (Medication.class.isInstance(r)) {
			Medication m = Medication.class.cast(r);
			rxNormAdapter.addRxCui(m);
			
		}
		
		//Labs loinc
		
		//Diagnosis icd9
		
		return r;
	} 
}
