package edu.harvard.i2b2.converter;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.harvard.i2b2.fhir.I2b2UtilByCategory;
import edu.harvard.i2b2.fhir.oauth2.AccessToken;
import spittr.Spittle;
import spittr.data.SpittleRepository;
import spittr.web.SpittleForm;

@Controller
@RequestMapping("/bs")
public class ConverterController {

	static Logger logger = LoggerFactory.getLogger(ConverterController.class);
	private static final String MAX_LONG_AS_STRING = "9223372036854775807";

	private BundleStatusRepository repository;
	private Converter converter;
	
	@Autowired
	public ConverterController(BundleStatusRepository repository,Converter converter) {
		this.repository = repository;
		this.converter=converter;
	}

	@RequestMapping(value = "/fetch/{pid}", method = RequestMethod.GET)
	public String getBundleBlocking(@PathVariable("pid") String pid) throws InterruptedException, ConverterException {
		
		System.out.println("...fetch:" + pid);
		if (repository.findOne(pid) != null) {
			System.out.println("...found one:" + pid);
			while (repository.isProcessing(pid)) {
				System.out.println("..sleeping as status is processing:" + pid);
				Thread.sleep(2000);
			}
		} else {
			System.out.println("...found NONE:" + pid);
			repository.markProcessing(pid);
			// if (fetchBundle(pid)) {
			AccessToken t=new AccessToken();
			t.setI2b2Url("http://services.i2b2.org:9090/i2b2");
			t.setI2b2Project("demo");
			
			if (fetchPatientBundle(t, pid)) {
				repository.markComplete(pid);
			}
		}
		System.out.println("...redirecting:" + pid);
		return "redirect:/bs/" + pid;
	}

	private boolean fetchBundle(String pid) throws InterruptedException {
		for (int x = 0; x < 50; x++) {
			Thread.sleep(50000);
			System.out.println("FETCH sleep:" + pid);
		}
		return true;
	}

	@RequestMapping(value = "/mark_p/{pid}", method = RequestMethod.GET)
	public String markProcessing(@PathVariable("pid") String pid) {
		repository.markProcessing(pid);
		return "redirect:/bs/" + pid;
	}

	@RequestMapping(value = "/mark_c/{pid}", method = RequestMethod.GET)
	public String markComplete(@PathVariable("pid") String pid) {
		repository.markComplete(pid);
		return "redirect:/bs/" + pid;

	}

	@RequestMapping(value = "/{pid}", method = RequestMethod.GET)
	public String viewBundleStatus(@PathVariable("pid") String pid, Model model) {
		BundleStatus bs = repository.findOne(pid);
		if (bs != null) {
			model.addAttribute(bs);
		}
		return "bundleStatus";
	}

	private boolean fetchPatientBundle(AccessToken tok, String pid) throws ConverterException {
		try {
			if (tok == null)
				logger.error("AccessToken is null");
			logger.trace("fetching PDO for pid:" + pid + " and tok");

			HashMap<String, String> map = new HashMap<String, String>();

			map.put("labs", "\\\\i2b2_LABS\\i2b2\\Labtests\\");
			/*
			 * for (String cat :
			 * Arrays.asList(sConfig.GetString(ConfigParameter.
			 * resourceCategoriesList).split("-"))) { switch (cat) { case
			 * "medications": map.put("medications",
			 * sConfig.GetString(ConfigParameter.medicationsPath)); break; case
			 * "labs": map.put("labs",
			 * sConfig.GetString(ConfigParameter.labsPath)); break; case
			 * "diagnoses": map.put("diagnoses",
			 * sConfig.GetString(ConfigParameter.diagnosesPath)); break; case
			 * "reports": map.put("reports",
			 * sConfig.GetString(ConfigParameter.reportsPath)); case "vitals":
			 * map.put("vitals", sConfig.GetString(ConfigParameter.vitalsPath));
			 * break; } }
			 */
			
			String outputXml=converter.getFhirXmlBundle(pid);
			

			Bundle b = I2b2UtilByCategory.getAllDataForAPatientAsFhirBundle(tok.getResourceUserId(), tok.getI2b2Token(),
					tok.getI2b2Url(), tok.getI2b2Domain(), tok.getI2b2Project(), pid, map, "default");
			
			/*
			 * if
			 * (sConfig.GetString(ConfigParameter.enrichEnabled).equals("true"))
			 * { FhirEnrich.enrich(b); b=ObservationCategoryGenerator.
			 * addObservationCategoryToObservationBundle(b); }
			 * 
			 * String par=sConfig.GetString(ConfigParameter.
			 * createDiagnosticReportsFromObservations); if(par!=null &&
			 * par.equals("true")) {
			 * logger.trace("createDiagnosticReportsFromObservations");
			 * b=DiagnosticReportGenerator.generateAndAddDiagnosticReports(b);
			 * }else{ logger.trace(
			 * "createDiagnosticReportsFromObservations is false"); } //
			 * logger.trace("bundlexML:"+JAXBUtil.toXml(b)); logger.trace(
			 * "fetched bundle of size:" + b.getEntry().size());
			 */
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ConverterException(e);
		}
		return true;
	}

}