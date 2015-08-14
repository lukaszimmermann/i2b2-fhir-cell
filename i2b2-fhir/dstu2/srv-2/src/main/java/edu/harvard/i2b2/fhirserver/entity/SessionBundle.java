package edu.harvard.i2b2.fhirserver.entity;



import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;

@Entity
@NamedQuery(name = "sessionBundleById", query = "SELECT b FROM SessionBundle b WHERE b.id = :id")
public class SessionBundle implements java.io.Serializable {
	static Logger logger = LoggerFactory.getLogger(SessionBundle.class);

	@Id
	private String id;// session id
	
	@Lob
	private String bundleXml;

	public SessionBundle() {

	}

	public SessionBundle(String sessionId, Bundle b) throws JAXBException {
		this.id = sessionId;
		if (b == null)
			b = new Bundle();
		this.bundleXml = JAXBUtil.toXml(b);
		
	}

	

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		SessionBundle.logger = logger;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBundleXml() {
		return bundleXml;
	}

	public void setBundleXml(String bundleXml) {
		this.bundleXml = bundleXml;
	}

	public Bundle getBundle() throws JAXBException {

		return JAXBUtil.fromXml(this.bundleXml, Bundle.class);
	}

	@Override
	public String toString() {
		return "SessionBundle [id=" + id + ", bundleXml=" + bundleXml + "]";
	}

}
