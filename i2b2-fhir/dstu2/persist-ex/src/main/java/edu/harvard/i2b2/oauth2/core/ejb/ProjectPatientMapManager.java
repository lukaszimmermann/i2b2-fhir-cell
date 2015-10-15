package edu.harvard.i2b2.oauth2.core.ejb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ranges.RangeException;

import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.oauth2.core.entity.AccessToken;
import edu.harvard.i2b2.oauth2.core.entity.ProjectPatientMap;

@Stateful
public class ProjectPatientMapManager {
	static Logger logger = LoggerFactory
			.getLogger(ProjectPatientMapManager.class);

	@EJB
	ProjectPatientMapService service;

	@EJB
	BundleStatus status;
	
	@PostConstruct
	public void init(){
		logger.debug("initailized");
	}

	public Bundle getProjectPatientBundle(AccessToken token) {
		ProjectPatientMap p = getProjectPatientMap(token.getResourceUserId(),
				token.getI2b2Token(), token.getI2b2Url(),
				token.getI2b2Domain(), token.getI2b2Project());
		String bundleXml = p.getPatientBundle();
		Bundle b = null;
		try {
			b = JAXBUtil.fromXml(bundleXml, Bundle.class);
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		}
		return b;
	}

	public List<String> getProjectPatientList(String i2b2User,
			String i2b2Token, String i2b2Url, String i2b2Domain,
			String i2b2Project) {
		ProjectPatientMap p = getProjectPatientMap(i2b2User, i2b2Token,
				i2b2Url, i2b2Domain, i2b2Project);
		List<String> list = Arrays.asList(p.getPatientIdList().replace("[", "")
				.replace("]", "").split(","));
		return list;
	}

	public ProjectPatientMap getProjectPatientMap(String i2b2User,
			String i2b2Token, String i2b2Url, String i2b2Domain,
			String i2b2Project) {

		if (status.isComplete(i2b2Project)) {
			return getProjectPatientMapLocking(i2b2Project);
		}

		if (!(status.isProcessing(i2b2Project) || status
				.isComplete(i2b2Project))) {
			fetchPatientList(i2b2User, i2b2Token, i2b2Url, i2b2Domain,
					i2b2Project);
		}

		while (status.isProcessing(i2b2Project)) {
			logger.info("waiting on complete status");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return getProjectPatientMap(i2b2User, i2b2Token, i2b2Url, i2b2Domain,
				i2b2Project);
	}

	private void fetchPatientList(String i2b2User, String i2b2Token,
			String i2b2Url, String i2b2Domain, String projectId) {
		status.markProcessing(projectId);
		try {
			logger.trace("fetching all Patients for project:" + projectId);
			String i2b2Xml = I2b2Util.getAllPatients(i2b2User, i2b2Token,
					i2b2Url, i2b2Domain, projectId);
			Bundle b=I2b2Util.getAllPatientsAsFhirBundle(i2b2Xml);
			String bundleXml=JAXBUtil.toXml(b);
			ArrayList<String> list = I2b2Util.getAllPatientsAsList(i2b2Xml);
			service.put(projectId, list, bundleXml);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		status.markComplete(projectId);
	}

	private ProjectPatientMap getProjectPatientMapLocking(String projectId) {
		return service.get(projectId);
	}
	
	public boolean hasAccessToPatient(AccessToken token,String patientId){
		String projectId=token.getI2b2Project();
			ProjectPatientMap p = getProjectPatientMap(token.getResourceUserId(),
					token.getI2b2Token(), token.getI2b2Url(),
					token.getI2b2Domain(), token.getI2b2Project());
			logger.warn("is patientId:"+patientId+" present in list"+ p.getPatientIdList());
		//	if(p.getPatientIdList().length()<2) {
		//		logger.warn("list seems empty for the project");
		//		return false;}
			List<String> list=Arrays.asList(p.getPatientIdList().replaceAll("[\\[\\]\\s]","").split(","));
			logger.warn("list>:"+list);
			return (list.contains(patientId))?true:false;
			
	}

}
