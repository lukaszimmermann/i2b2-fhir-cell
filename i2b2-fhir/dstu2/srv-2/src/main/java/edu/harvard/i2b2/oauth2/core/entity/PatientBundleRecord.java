package edu.harvard.i2b2.oauth2.core.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.sun.mail.iap.ByteArray;

@Entity
public class PatientBundleRecord  {
	@Id
	String patientId;
	
	@Lob
	String bundleXml;
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getBundleXml() {
		return bundleXml;
	}
	public void setBundleXml(String bundleXml) {
		this.bundleXml = bundleXml;
	}
	
	
}
