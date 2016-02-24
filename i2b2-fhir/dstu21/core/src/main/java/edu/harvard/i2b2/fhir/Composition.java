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

		// read configuration of Diagnostic Report
		JsonReader rdr = Json.createReader(is);
		JsonObject obj = rdr.readObject();
		JsonArray catArr = obj.getJsonArray("categories");
		List<Object> diagRepArr = new ArrayList<Object>();

		HashMap<String, List<Resource>> hm = groupByEffectiveDateTime(obsBundle);

		for (String dateTime : hm.keySet()) {
			List<Resource> listByDate = hm.get(dateTime);

			for (JsonObject cat : catArr.getValuesAs(JsonObject.class)) {

				// logger.trace("cat:" + cat);
				JsonObject category = cat.getJsonObject("category");
				// logger.trace("category" + category);
				JsonObject code = cat.getJsonObject("code");
				// logger.trace("code" + code);
				JsonArray tests = cat.getJsonArray("tests");
				// logger.trace("tests" + tests);

				DiagnosticReport diagRep = new DiagnosticReport();

				for (Resource r : listByDate) {
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

				if (diagRep.getResult().size() > 0) {
					FhirUtil.setId(diagRep, "diagRep.getSubject().getId()" + "-" + (diagRepArr.size() + 1));
					// set status
					DiagnosticReportStatus status = new DiagnosticReportStatus();
					status.setValue(DiagnosticReportStatusList.FINAL);
					diagRep.setStatus(status);

					// set Category
					JsonArray catOArr = category.getJsonArray("coding");
					JsonObject categoryO = catOArr.getJsonObject(0);
					diagRep.setCategory(FhirUtil.generateCodeableConcept(categoryO.getString("code"),
							categoryO.getString("system"), categoryO.getString("display")));

					// set Code
					JsonArray codingArr = code.getJsonArray("coding");
					JsonObject coding = codingArr.getJsonObject(0);
					diagRep.setCode(FhirUtil.generateCodeableConcept(coding.getString("code"),
							coding.getString("system"), coding.getString("display")));

					DateTime dt = new DateTime();
					dt.setValue(dateTime);
					diagRep.setEffectiveDateTime(dt);
					// set EffectiveDateTime

					// add report to array
					diagRepArr.add(diagRep);

				}
			}
		}
		diagRepB = FhirUtil.createBundle(diagRepArr);

		return diagRepB;
	}

	static public void addObservationToDiagnosticReport(DiagnosticReport diagReport, Observation obs) {
		// logger.trace("adding obs ref to DiagBundle");
		Reference r = new Reference();
		r.setReference(FhirUtil.getFhirString(obs.getId().getValue()));
		diagReport.getResult().add(r);
	}

	static HashMap<String, List<Resource>> groupByEffectiveDateTime(Bundle b) throws FhirCoreException {
		HashMap<String, List<Resource>> hm = new HashMap();

		for (Resource r : FhirUtil.getResourceListFromBundle(b)) {
			if (!Observation.class.isInstance(r))
				throw new FhirCoreException("Resource is not an Observation:");
			Observation ob = (Observation) r;
			String dateTime = ob.getEffectiveDateTime().getValue();
			if (!hm.containsKey(dateTime)) {
				List<Resource> list = new ArrayList();
				hm.put(dateTime, list);
			}
			List<Resource> list = hm.get(dateTime);
			list.add(ob);
		}
		logger.trace("number of distinct dates:" + hm.keySet().size());
		return hm;
	}

}
