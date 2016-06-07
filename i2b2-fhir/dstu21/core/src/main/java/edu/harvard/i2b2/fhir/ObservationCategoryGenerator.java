package edu.harvard.i2b2.fhir;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.BundleEntry;
import org.hl7.fhir.CodeableConcept;
import org.hl7.fhir.Coding;
import org.hl7.fhir.DateTime;
import org.hl7.fhir.DiagnosticReport;
import org.hl7.fhir.DiagnosticReportStatus;
import org.hl7.fhir.DiagnosticReportStatusList;
import org.hl7.fhir.Observation;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Reference;
import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class ObservationCategoryGenerator {
	static Logger logger = LoggerFactory.getLogger(ObservationCategoryGenerator.class);

	static public Bundle addObservationCategoryToObservationBundle(Bundle totalBundle) throws FhirCoreException {
		Bundle diagRepB = new Bundle();
		Reader is = null;
		try {
			// logger.trace(JAXBUtil.toXml(totalBundle));

			String fileContent = Utils.fileToString("/ObservationCategories.txt");
			is = new StringReader(fileContent);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FhirCoreException(e);
		}

		// read configuration of Diagnostic Report
		JsonReader rdr = Json.createReader(is);
		JsonObject obj = rdr.readObject();
		JsonArray catArr = obj.getJsonArray("categories");
		List<Object> diagRepArr = new ArrayList<Object>();
		for (JsonObject cat : catArr.getValuesAs(JsonObject.class)) {

			// logger.trace("cat:" + cat);
			JsonObject category = cat.getJsonObject("category");
			// logger.trace("category" + category);
			JsonObject code = cat.getJsonObject("code");
			// logger.trace("code" + code);
			JsonArray tests = cat.getJsonArray("tests");
			// logger.trace("tests" + tests);

			/*
			 * "category": { "coding": [{ "system":
			 * "http://hl7.org/fhir/v2/0074", "code": "CH", "display":
			 * "Chemistry"}] },
			 */
			JsonObject codeConceptParent = category.getJsonArray("coding").getJsonObject(0);

			CodeableConcept categoryR = FhirUtil.generateCodeableConcept(
					codeConceptParent.getString("code"),
					codeConceptParent.getString("system"),
					codeConceptParent.getString("display"));
			categoryR.setText(FhirUtil.generateFhirString(codeConceptParent.getString("display")));

			for (Resource r : FhirUtil.getResourceListFromBundle(totalBundle)) {
				if (Observation.class.isInstance(r)) {
					Observation ob = (Observation) r;
					String codeFound = ob.getCode().getCoding().get(0).getCode().getValue();
					// logger.trace("code Found:" + codeFound);
					for (int i = 0; i < tests.size(); i++) {
						String codeExpected = tests.getString(i);
						if (codeExpected.equals(codeFound)) {
							// logger.trace("code Found+++:" + codeFound);
							// addObservationToDiagnosticReport(diagRep, ob);

							ob.setCategory(categoryR);
							
							break;
						}
					}

				}
			}

		}

		return totalBundle;
	}

}
