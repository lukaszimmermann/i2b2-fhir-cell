package edu.harvard.i2b2.converter;

import java.sql.Date;
import java.util.HashMap;
import java.util.Set;

public class BundleStatus {

	long id;
	String patientId;
	BundleStatusLevel bundleStatusLevel;
	Date createdDate;

	public BundleStatus(long id,  String patientId, String bundleStatusLevel) {
		this.id = id;
		this.patientId = patientId;
		switch (bundleStatusLevel) {
		case "PROCESSING":
			this.bundleStatusLevel = BundleStatusLevel.PROCESSING;
			break;
		case "COMPLETE":
			this.bundleStatusLevel = BundleStatusLevel.COMPLETE;
			break;
		case "FAILED":
			this.bundleStatusLevel = BundleStatusLevel.FAILED;
			break;
		}
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

	public BundleStatusLevel getBundleStatusLevel() {
		return bundleStatusLevel;
	}

	public void setBundleStatusLevel(BundleStatusLevel bundleStatusLevel) {
		this.bundleStatusLevel = bundleStatusLevel;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}