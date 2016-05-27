package edu.harvard.i2b2.fhir.converter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.cache.Cache;
import hello.CustomerRepository;
import hello.GreetingController;
import javassist.bytecode.analysis.Util;

@Controller
@RequestMapping("/bs")
@EnableJpaRepositories
public class ConverterController {

	static Logger logger = LoggerFactory.getLogger(ConverterController.class);
	private static final String MAX_LONG_AS_STRING = "9223372036854775807";

	@Autowired
	BundleStatusRepository repository;
	
	@Autowired
	Cache cache;
	
	@Value("${cache.url}")
	String cacheUrl;

	@RequestMapping(value = "/view/{pid}", method = RequestMethod.GET)
	public String viewBundleStatus(@PathVariable("pid") String pid, Model model) {
		// repository.save(new BundleStatus(123,pid,"PROCESSING"));
		List<BundleStatus> list = repository.findByPatientId(pid);
		BundleStatus bs = list.get(0);
		if (bs != null) {
			model.addAttribute(bs);
		}
		return "bundleStatus";
	}

	private BundleStatus findOne(String pid) {
		List<BundleStatus> list = repository.findByPatientId(pid);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	@RequestMapping(value = "/fhir/{path:.*}", method = RequestMethod.GET)
	public ResponseEntity fhirEndpoint(HttpServletRequest request, @PathVariable String path){
		logger.debug("path:"+path+"?"+request.getQueryString());
		//return new ResponseEntity<>(request.getRequestURI(),HttpStatus.OK);
		logger.debug("getContextPath()"+request.getContextPath());
		String basePath=request.getRequestURL().toString().replace(path,"");
		logger.debug("basePath:"+basePath);
		return new ResponseEntity<>(cache.get(cacheUrl+"/"+path+"?"+request.getQueryString()).replace(cacheUrl, basePath),HttpStatus.OK);
		
	}

	@RequestMapping(value = "/fetch/{pid}", method = RequestMethod.GET)
	public ResponseEntity getBundleBlocking(@PathVariable("pid") String pid) throws InterruptedException, ConverterException {

		logger.debug("...fetch:" + pid);
		if (findOne(pid) != null) {
			logger.debug("...found one:" + pid);
			while (isProcessing(pid)) {
				logger.debug("..sleeping as status is processing:" + pid);
				Thread.sleep(2000);
			}
		} else {
			BundleStatus bs = new BundleStatus();
			bs.setPatientId(pid);
			GregorianCalendar now = new GregorianCalendar();
			bs.setCreatedDate(new java.sql.Date(now.getTimeInMillis()));
			repository.save(bs);
			logger.debug("...found NONE:" + pid);
			markProcessing(pid);
			if (fetchPatientBundle(pid)) {
				markComplete(pid);
			}
		}
		logger.debug("...redirecting:" + pid);
		logger.debug("");
		return new ResponseEntity<>(cache.get(cacheUrl+"/Patient/example"),HttpStatus.OK);
		//return "redirect:/bs/view/" + pid;
	}

	private boolean fetchPatientBundle(String pid) {
		try {
			(
					new I2b2Converter()).getFhirXmlBundle(pid);
		} catch (ConverterException e1) {
			logger.error(e1.getMessage(),e1);
			return false;
		}
		
		cache.put(Utils.getFile("/examples/singlePatient.xml"));
		
		for (int i = 0; i < 3; i++) {
			try {
				logger.info("fetch....sleep:"+pid);
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
		return true;
	}

	void markProcessing(String pid) {
		BundleStatus bs = findOne(pid);
		if (pid != null) {
			bs.setBundleStatusLevel("PROCESSING");
			repository.save(bs);
		}
	}

	void markComplete(String pid) {
		BundleStatus bs = findOne(pid);
		//logger.info("marking COMPLETE");
		if (pid != null) {
			bs.setBundleStatusLevel("COMPLETE");
			repository.save(bs);
		}
	}

	void markFailed(String pid) {
		BundleStatus bs = findOne(pid);
		if (pid != null) {
			bs.setBundleStatusLevel("FAILED");
			repository.save(bs);
		}
	}

	boolean isProcessing(String pid) {
		BundleStatus bs = findOne(pid);
		return bs == null ? false : bs.getBundleStatusLevel().equals("PROCESSING");
	}

	boolean isComplete(String pid) {
		BundleStatus bs = findOne(pid);
		return bs == null ? false : bs.getBundleStatusLevel().equals("COMPLETE");
	}

	boolean isFailed(String pid) {
		BundleStatus bs = findOne(pid);
		return bs == null ? false : bs.getBundleStatusLevel().equals("FAILED");
	}

	/*
	 * private BundleStatusRepository repository; private Converter converter;
	 * 
	 * //@Autowired //public ConverterController(BundleStatusRepository
	 * repository,Converter converter) { //this.repository = repository;
	 * //this.converter=converter; //}
	 * 
	 * @RequestMapping(value = "/fetch/{pid}", method = RequestMethod.GET)
	 * public String getBundleBlocking(@PathVariable("pid") String pid) throws
	 * InterruptedException, ConverterException {
	 * 
	 * logger.debug("...fetch:" + pid); if (repository.findOne(pid) != null) {
	 * logger.debug("...found one:" + pid); while (repository.isProcessing(pid))
	 * { logger.debug("..sleeping as status is processing:" + pid);
	 * Thread.sleep(2000); } } else { logger.debug("...found NONE:" + pid);
	 * repository.markProcessing(pid); // if (fetchBundle(pid)) { AccessToken
	 * t=new AccessToken(); t.setI2b2Url("http://services.i2b2.org:9090/i2b2");
	 * t.setI2b2Project("demo");
	 * 
	 * if (fetchPatientBundle(t, pid)) { repository.markComplete(pid); } }
	 * logger.debug("...redirecting:" + pid); return "redirect:/bs/" + pid; }
	 * 
	 * private boolean fetchBundle(String pid) throws InterruptedException { for
	 * (int x = 0; x < 50; x++) { Thread.sleep(50000); logger.debug(
	 * "FETCH sleep:" + pid); } return true; }
	 * 
	 * @RequestMapping(value = "/mark_p/{pid}", method = RequestMethod.GET)
	 * public String markProcessing(@PathVariable("pid") String pid) {
	 * repository.markProcessing(pid); return "redirect:/bs/" + pid; }
	 * 
	 * @RequestMapping(value = "/mark_c/{pid}", method = RequestMethod.GET)
	 * public String markComplete(@PathVariable("pid") String pid) {
	 * repository.markComplete(pid); return "redirect:/bs/" + pid;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * private boolean fetchPatientBundle(AccessToken tok, String pid) throws
	 * ConverterException { try { if (tok == null) logger.error(
	 * "AccessToken is null"); logger.trace("fetching PDO for pid:" + pid +
	 * " and tok");
	 * 
	 * HashMap<String, String> map = new HashMap<String, String>();
	 * 
	 * map.put("labs", "\\\\i2b2_LABS\\i2b2\\Labtests\\");
	 * 
	 * 
	 * String outputXml=converter.getFhirXmlBundle(pid);
	 * 
	 * 
	 * //Bundle b = I2b2UtilByCategory.getAllDataForAPatientAsFhirBundle(tok.
	 * getResourceUserId(), tok.getI2b2Token(), // tok.getI2b2Url(),
	 * tok.getI2b2Domain(), tok.getI2b2Project(), pid, map, "default");
	 * 
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e); throw new
	 * ConverterException(e); } return true; }
	 */

}