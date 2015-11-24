package edu.harvard.i2b2.fhir;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.JAXBException;

import edu.harvard.i2b2.fhir.core.CoreConfig;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.hl7.fhir.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.core.FhirCoreException;

public class I2b2UtilByCategory {
	static Logger logger = LoggerFactory.getLogger(I2b2UtilByCategory.class);

	/*
	 * (:{local:processMedObs(<A>{$medObs}</A>)/entry}
	 * {local:processLabObs(<A>{$labObs}</A>)/entry}
	 * {local:processDiagObs(<A>{$diagObs}</A>)/entry} :)
	 * (:INSERT_RESOURCE_FUNCTION_HERE:)
	 */
	public static String getI2b2ResponseXmlForAResourceCategory(String i2b2User, String i2b2Token, String i2b2Url,
			String I2b2Domain, String project, String patientId, String resourceCategory, String resourceCategoryPath)
					throws IOException, XQueryUtilException, FhirCoreException {
		if (resourceCategory == null)
			throw new FhirCoreException("resourceCategory is null");

		String requestXml = IOUtils.toString(
				I2b2UtilByCategory.class.getResourceAsStream("/i2b2query/i2b2RequestTemplateForAPatient.xml"));

		String path = "";
		if (resourceCategoryPath != null) {
			path = resourceCategoryPath;
		} else {
			switch (resourceCategory) {
			case "medications":
				path = CoreConfig.getMedicationPath();
				break;
			case "labs":
				path = CoreConfig.getLabsPath();
				break;
			case "diagnoses":
				path = CoreConfig.getDiagnosesPath();
				break;
			case "reports":
				path = CoreConfig.getReportsPath();
				break;
			default:
				throw new FhirCoreException("resourceCategory not known:" + resourceCategory);
			}
		}

		requestXml = requestXml.replace("XPATHX", path);
		logger.trace("requestXml with Path:" + requestXml);

		requestXml = I2b2Util.insertI2b2ParametersInXml(requestXml, i2b2User, i2b2Token, i2b2Url, I2b2Domain, project);

		if (patientId != null)
			requestXml = requestXml.replaceAll("PATIENTID", patientId);

		String responseXml = WebServiceCall.run(i2b2Url + "/services/QueryToolService/pdorequest", requestXml);
		return responseXml;

	}

	private static String getTransformQueryForAResourceCategory(String resourceCategory, String ontologyType)
			throws FhirCoreException, IOException {
		String xml = IOUtils.toString(
				FhirUtil.class.getResourceAsStream("/transform/I2b2ToFhir/i2b2ToFHIR_" + ontologyType + ".xquery"));
		String functionString = "";
		switch (resourceCategory) {
		case "medications":
			functionString = "{local:processMedObs(<A>{$medObs}</A>)/entry}";
			break;
		case "labs":
			functionString = "{local:processLabObs(<A>{$labObs}</A>)/entry}";
			break;
		case "diagnoses":
			functionString = "{local:processDiagObs(<A>{$diagObs}</A>)/entry}";
			break;
		case "reports":
			functionString = "{local:processReportObs(<A>{$reportObs}</A>)/entry}";
			break;

		default:
			throw new FhirCoreException("resourceCategory not known:" + resourceCategory);
		}

		xml = xml.replace("(:INSERT_RESOURCE_FUNCTION_HERE:)", functionString);
		logger.trace("XQuery:" + xml);
		return xml;
	}

	public static Bundle getAllDataForAPatientAsFhirBundle(String i2b2User, String i2b2Token, String i2b2Url,
			String I2b2Domain, String project, String patientId, HashMap<String, String> categoryPathMap,
			String ontologyType) throws FhirCoreException {
		// cycle thru resource categories and get bundles
		String entryXmlCumulative = "";
		Patient p = null;
		try {
			List<String> resourceCategories = Arrays.asList(CoreConfig.getResourceCategoriesList().split("-"));
			for (String rc : resourceCategories) {

					String path = null;
				if (categoryPathMap != null) {
					String pathFromMap = categoryPathMap.get(rc);
					path = pathFromMap;
					if (pathFromMap == null) {
						throw new FhirCoreException(
								"There is no path provided for category:" + rc + " in the configuration");
					}
					if (pathFromMap.contains("SKIP"))
						continue;

				}

				String i2b2ResponseXml = getI2b2ResponseXmlForAResourceCategory(i2b2User, i2b2Token, i2b2Url,
						I2b2Domain, project, patientId, rc, path);
				logger.trace("i2b2ResponseXml" + i2b2ResponseXml);

				if (p == null) {
					p = FhirUtil.getPatientResource(i2b2ResponseXml);
				}
				String query = getTransformQueryForAResourceCategory(rc, ontologyType);

				String bundleXml = XQueryUtil.processXQuery(query, i2b2ResponseXml);
				logger.trace("ran XQuery transform" + query + " to get  bundle Xml:" + bundleXml);

				String entryXml = XQueryUtil
						.processXQuery("declare default element namespace \"http://hl7.org/fhir\";//entry", bundleXml);
				logger.info("entriesXml:" + entryXml);
				entryXmlCumulative += entryXml;
				logger.info("added bundle of size:" + XQueryUtil.getStringSequence(
						"declare default element namespace \"http://hl7.org/fhir\";//entry", bundleXml).size());
			}

			// merge the bundles

			String finalBundleXml = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + entryXmlCumulative + "</Bundle>";
			Bundle b = JAXBUtil.fromXml(finalBundleXml, Bundle.class);

			b.getEntry().add(FhirUtil.newBundleEntryForResource(p));

			logger.trace("returing bundle of size:" + b.getEntry().size());
			return b;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FhirCoreException("Error in getting AllDataBundle for Patient ", e);
		}
	}

}
