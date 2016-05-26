package edu.harvard.i2b2.fhir.converter;

import java.sql.Date;
import java.util.HashMap;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BundleStatus {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	String patientId;
	
	String bundleStatusLevel;
	Date createdTime;
	public BundleStatus(){}
	
	public BundleStatus(long id,  String patientId, String bundleStatusLevel) {
		this.id = id;
		this.patientId = patientId;
		this.bundleStatusLevel=bundleStatusLevel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getBundleStatusLevel() {
		return bundleStatusLevel;
	}

	public void setBundleStatusLevel(String bundleStatusLevel) {
		this.bundleStatusLevel = bundleStatusLevel;
	}

	public Date getCreatedDate() {
		return createdTime;
	}

	public void setCreatedDate(Date createdTime) {
		this.createdTime= createdTime;
	}

	@Override
	public String toString() {
		return "BundleStatus [id=" + id + ", patientId=" + patientId + ", bundleStatusLevel=" + bundleStatusLevel
				+ ", createdTime=" + createdTime + "]";
	}
	
	

}