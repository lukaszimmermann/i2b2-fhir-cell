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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public List<String> get(String projectId) {
		logger.info("getting: List<String> for pid:" + projectId);

		// return patientList<String>Hm.get(id);
		return find(projectId);
	}

	private List<String> find(String projectId) {
		ProjectPatientMap r = em.find(ProjectPatientMap.class, projectId);
		if (r == null)
			return null;
		String rawList = r.getPatientIdList();
		List<String> list =  Arrays.asList(rawList
				.replace("[", "").replace("]", "").split(","));
		logger.trace("found ProjectPatientMap:" + r.toString());
		return list;
	}

	@Lock(LockType.WRITE)
	public void put(String projectId, ArrayList<String> list) {
		logger.info("putting:" + projectId + "=>" + list.toString());
		createProjectPatientMap(projectId, list);
		// patientList<String>Hm.put(id,b);

	}

	@Remove
	public void remove() {
		// patientList<String>Hm=null;
	}

	private ProjectPatientMap createProjectPatientMap(String projectId,
			ArrayList<String> list) {

		ProjectPatientMap r = new ProjectPatientMap();
		r.setProjectId(projectId);
		r.setPatientIdList(list.toString());
		em.persist(r);
		logger.trace("created ProjectPatientMap:" + r.toString());
		return r;
	}

}
