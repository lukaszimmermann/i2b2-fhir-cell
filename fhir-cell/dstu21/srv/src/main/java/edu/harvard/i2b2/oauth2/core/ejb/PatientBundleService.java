package edu.harvard.i2b2.oauth2.core.ejb;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

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
import edu.harvard.i2b2.oauth2.register.entity.User;

@Singleton
@Startup
public class PatientBundleService {
	static Logger logger = LoggerFactory.getLogger(PatientBundleService.class);
	// static HashMap<String,Bundle> patientBundleHm=new
	// HashMap<String,Bundle>();

	@PersistenceContext
	EntityManager em;

	@PostConstruct
	public void init() {
		// patientBundleHm=new HashMap<String,Bundle>();
	}

	@Lock
	public Bundle get(String patientId) {
		logger.info("getting: bundle for pid:" + patientId);
		Bundle b = find(patientId);
		if (b == null) {
			logger.info("returning NULL bundle ");
		} else {
			logger.info("returning bundle of size:" + b.getEntry().size());
		}
		return b;
	}

	private Bundle find(String patientId) {
		PatientBundleRecord r = em.find(PatientBundleRecord.class, patientId);
		if (r == null)
			return null;
		logger.debug("r.getBundleXml()" + r.getBundleXml());
		String bundleXml = r.getBundleXml();
		
		Bundle b = null;
		try {
			b = JAXBUtil.fromXml(bundleXml, Bundle.class);
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		}
		logger.trace("found PatientBundleRecord:" + r.toString() + "\n" + b.getEntry().size());
		return b;
	}

	@Lock
	public void put(String patientId, Bundle b) {
		logger.info("putting:" + patientId + "=>" + b);
		createPatientRecord(patientId, b);

	}

	@Remove
	public void remove(PatientBundleRecord r) {
		em.remove(em.contains(r) ? r : em.merge(r));
		logger.info("removed client");
	}

	@Lock
	private PatientBundleRecord createPatientRecord(String patientId, Bundle b) {

		PatientBundleRecord r = new PatientBundleRecord();
		r.setPatientId(patientId);
		try {
			r.setBundleXml(JAXBUtil.toXml(b));
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
		}
		em.persist(r);
		logger.trace("created PatientBundleRecord:" + r.toString());
		return r;
	}

	@Lock
	public List<String> getIdList() {
		List<String> idList = new ArrayList<String>();
		List<PatientBundleRecord> pbList = em.createQuery(" SELECT s FROM PatientBundleRecord s").getResultList();
		String msg = "";
		for (Object x : pbList) {
			idList.add(((PatientBundleRecord) x).getPatientId());
		}

		return idList;
	}

	@Lock
	public void deleteAll() {
		for (String patientId : getIdList()) {
			PatientBundleRecord r = em.find(PatientBundleRecord.class, patientId);
			remove(r);
		}
	}

	
}
