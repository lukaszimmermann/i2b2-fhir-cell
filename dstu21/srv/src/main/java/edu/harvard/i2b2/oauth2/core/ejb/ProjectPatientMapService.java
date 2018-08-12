package edu.harvard.i2b2.oauth2.core.ejb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remove;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.oauth2.core.entity.PatientBundleRecord;
import edu.harvard.i2b2.oauth2.core.entity.ProjectPatientMap;

@Singleton
@Startup
public class ProjectPatientMapService {
	static Logger logger = LoggerFactory
			.getLogger(ProjectPatientMapService.class);

	@PersistenceContext
	EntityManager em;

	@PostConstruct
	public void init() {
		// patientList<String>Hm=new HashMap<String,List<String>>();
	}

	@Lock(LockType.READ)
	public ProjectPatientMap get(String projectId) {
		logger.info("getting: List<String> for pid:" + projectId);

		// return patientList<String>Hm.get(id);
		return find(projectId);
	}

	private ProjectPatientMap find(String projectId) {
		ProjectPatientMap r = em.find(ProjectPatientMap.class, projectId);
		if (r == null)
			return null;
		logger.trace("found ProjectPatientMap:" + r.toString());
		return r;
	}

	@Lock(LockType.WRITE)
	public void put(String projectId, ArrayList<String> list, String bundleXml) {
		logger.info("putting:" + projectId + "=>" + list.toString());
		createProjectPatientMap(projectId, list, bundleXml);
		// patientList<String>Hm.put(id,b);

	}

	@Remove
	public void remove() {
		// patientList<String>Hm=null;
	}

	private ProjectPatientMap createProjectPatientMap(String projectId,
			ArrayList<String> list, String bundleXml) {

		ProjectPatientMap r = new ProjectPatientMap();
		r.setProjectId(projectId);
		r.setPatientIdList(list.toString());
		r.setPatientBundle(bundleXml);
		em.persist(r);
		logger.trace("created ProjectPatientMap:" + r.toString());
		return r;
	}

	
	
	@Remove
	public void remove(ProjectPatientMap  r) {
		em.remove(em.contains(r) ? r : em.merge(r));
		logger.info("removed client");
	}

	

	@Lock
	public List<String> getIdList() {
		List<String> idList = new ArrayList<String>();
		List<PatientBundleRecord> pbList = em.createQuery(" SELECT s FROM ProjectPatientMap s").getResultList();
		String msg = "";
		for (Object x : pbList) {
			idList.add(((ProjectPatientMap ) x).getProjectId());
		}

		return idList;
	}


	@Lock
	public void deleteAll() {
		for (String projectId : getIdList()) {
			ProjectPatientMap  r = em.find(ProjectPatientMap.class, projectId);
			remove(r);
		}
	}
}
