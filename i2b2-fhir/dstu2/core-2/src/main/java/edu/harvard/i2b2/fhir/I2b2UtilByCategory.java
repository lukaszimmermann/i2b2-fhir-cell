package edu.harvard.i2b2.fhir;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import edu.harvard.i2b2.fhir.core.Config;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
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
	public static String getI2b2ResponseXmlForAResourceCategory(
			String i2b2User, String i2b2Token, String i2b2Url,
			String I2b2Domain, String project, String patientId,
			String resourceCategory, String resourceCategoryPath)
			throws IOException, XQueryUtilException, FhirCoreException {
		if (resourceCategory == null)
			throw new FhirCoreException("resourceCategory is null");

		String requestXml = IOUtils
				.toString(I2b2UtilByCategory.class
						.getResourceAsStream("/i2b2query/i2b2RequestTemplateForAPatient.xml"));

		String path = "";
		switch (resourceCategory) {
		case "medications":
			path = Config.getMedicationPath();
			break;
		case "labs":
			path = Config.getLabsPath();
			break;
		case "diagnoses":
			path = Config.getDiagnosesPath();
			break;

		default:
			throw new FhirCoreException("resourceCategory not known:"
					+ resourceCategory);
		}

		requestXml = requestXml.replace("XPATHX", path);
		logger.trace("requestXml with Path:" + requestXml);

		requestXml = I2b2Util.insertI2b2ParametersInXml(requestXml, i2b2User,
				i2b2Token, i2b2Url, I2b2Domain, project);

		if (patientId != null)
			requestXml = requestXml.replaceAll("PATIENTID", patientId);

		String responseXml = WebServiceCall.run(i2b2Url
				+ "/services/QueryToolService/pdorequest", requestXml);
		return responseXml;

	}

	private static String getTransformQueryForAResourceCategory(
			String resourceCategory) throws FhirCoreException, IOException {
		String xml = IOUtils
				.toString(FhirUtil.class
						.getResourceAsStream("/transform/I2b2ToFhir/i2b2ToFHIR.xquery"));
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

		default:
			throw new FhirCoreException("resourceCategory not known:"
					+ resourceCategory);
		}

		xml = xml.replace("(:INSERT_RESOURCE_FUNCTION_HERE:)", functionString);
		logger.trace("XQuery:" + xml);
		return xml;
	}

	public static String getBundleForAResourceCategory(String i2b2User,
			String i2b2Token, String i2b2Url, String I2b2Domain,
			String project, String patientId, String resourceCategory,
			String resourceCategoryPath) throws XQueryUtilException,
			JAXBException, IOException, FhirCoreException {

		String i2b2ResponseXml = getI2b2ResponseXmlForAResourceCategory(
				i2b2User, i2b2Token, i2b2Url, I2b2Domain, project, patientId,
				resourceCategory, resourceCategoryPath);
		String query = getTransformQueryForAResourceCategory(resourceCategory);

		logger.trace("i2b2ResponseXml" + i2b2ResponseXml);
		String bundleXml = XQueryUtil.processXQuery(query, i2b2ResponseXml);
		logger.trace("ran XQuery transform" + query + " to get  bundle Xml:"
				+ bundleXml);

		return bundleXml;
	}

	public static Bundle getAllDataForAPatientAsFhirBundle(String i2b2User,
			String i2b2Token, String i2b2Url, String I2b2Domain,
			String project, String patientId,
			List<String> resourceCategoryPathList) throws FhirCoreException {
		// cycle thru resource categories and get bundles
		String entryXmlCumulative = "";
		try {
			List<String> resourceCategories = Arrays.asList(Config
					.getResourceCategoriesList().split("-"));
			for (String rc : resourceCategories) {
				String bundleXml = getBundleForAResourceCategory(i2b2User,
						i2b2Token, i2b2Url, I2b2Domain, project, patientId, rc,
						null);
				String entryXml = XQueryUtil
						.processXQuery(
								"declare default element namespace \"http://hl7.org/fhir\";//entry",
								bundleXml);
				logger.trace("entriesXml:" + entryXml);
				entryXmlCumulative += entryXml;
				logger.info("added bundle of size:"+XQueryUtil.getStringSequence(
								"declare default element namespace \"http://hl7.org/fhir\";//entry",
								bundleXml).size());
			}

			// merge the bundles

			String finalBundleXml = "<Bundle xmlns=\"http://hl7.org/fhir\">\n"
					+ entryXmlCumulative + "</Bundle>";
			Bundle b=JAXBUtil.fromXml(finalBundleXml, Bundle.class);
			logger.trace("returing bundle of size:"+b.getEntry().size());
			return b;
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new FhirCoreException(
					"Error in getting AllDataBundle for Patient ", e);
		}
	}

}
