package edu.harvard.i2b2.fhir;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Medication;
import org.hl7.fhir.Observation;
import org.hl7.fhir.Condition;
import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.icd9.Icd9FhirAdapter;
import edu.harvard.i2b2.loinc.LoincFhirAdapter;
import edu.harvard.i2b2.rxnorm.RxNormAdapter;

public class FhirEnrich {
	static Logger logger = LoggerFactory.getLogger(FhirEnrich.class);

	static RxNormAdapter rxNormAdapter = null;
	static LoincFhirAdapter loincAdapter = null;
	static Icd9FhirAdapter icd9Adapter = null;

	static {
		try {
			rxNormAdapter = new RxNormAdapter();
			loincAdapter = new LoincFhirAdapter();
	//		icd9Adapter = new Icd9FhirAdapter();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}

	}

	static public Resource enrich(Resource r) throws JAXBException {
		// Medication rxnorm
		if (Medication.class.isInstance(r)) {
			Medication m = Medication.class.cast(r);
			rxNormAdapter.addRxCui(m);

		}

		if (Observation.class.isInstance(r)) {
			Observation ob = Observation.class.cast(r);
			loincAdapter.addLoincName(ob);
		}
		/*
		if (Condition.class.isInstance(r)) {
			Condition cond = Condition.class.cast(r);
			 icd9Adapter.addIcd9Name(cond);
		}*/

		// Labs loinc

		// Diagnosis icd9

		return r;
	}
}
