package edu.harvard.i2b2.fhir;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.BundleEntry;
import org.hl7.fhir.DiagnosticReport;
import org.hl7.fhir.Observation;
import org.hl7.fhir.Reference;
import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class Composition {
	static Logger logger = LoggerFactory.getLogger(Composition.class);

	static public Bundle DiagnositicReportBundleFromObservationBundle(Bundle obsBundle) throws FhirCoreException {
		Bundle diagRepB = new Bundle();
		Reader is = null;
		try {
			logger.trace(JAXBUtil.toXml(obsBundle));

			String fileContent = Utils.fileToString("/DiagnosticReportCategories.txt");
			is = new StringReader(fileContent);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FhirCoreException(e);
		}
		// if(1==1){System.exit(0);}

		// read configuration of Diagnostic Report
		JsonReader rdr = Json.createReader(is);
		JsonObject obj = rdr.readObject();
		JsonArray catArr = obj.getJsonArray("categories");
		// logger.trace(":"+catArr.getJsonArray("categories"));
		// logger.trace(":"+catArr.getJsonArray("tests"));
		// logger.trace(":"+catArr.getJsonObject("code"));
		// Parse through the observation Bundle to build reports
		List<Object> diagRepArr = new ArrayList<Object>();
		for (JsonObject cat : catArr.getValuesAs(JsonObject.class)) {
			logger.trace("cat:" + cat);
			JsonObject category = cat.getJsonObject("category");
			logger.trace("category" + category);
			JsonObject code = cat.getJsonObject("code");
			logger.trace("code" + code);
			JsonArray tests = cat.getJsonArray("tests");
			logger.trace("tests" + tests);

			DiagnosticReport diagRep = new DiagnosticReport();

			for (Resource r : FhirUtil.getResourceListFromBundle(obsBundle)) {
				if (Observation.class.isInstance(r)) {
					Observation ob = (Observation) r;
					String codeFound = ob.getCode().getCoding().get(0).getCode().getValue();
					logger.trace("code Found:" + codeFound);
					for (int i = 0; i < tests.size(); i++) {
						String codeExpected = tests.getString(i);
						if (codeExpected.equals(codeFound)) {
							logger.trace("code Found+++:" + codeFound);
							addObservationToDiagnosticReport(diagRep, ob);
							break;
						}
					}

				}
			}

			diagRepArr.add(diagRep);
		}

		diagRepB = FhirUtil.createBundle(diagRepArr);

		return diagRepB;
	}

	static public void addObservationToDiagnosticReport(DiagnosticReport diagReport, Observation obs) {
		// logger.trace("adding obs ref to DiagBundle");
		Reference r = new Reference();
		r.setId(obs.getId().getValue());
		diagReport.getResult().add(r);
	}
}
