package edu.harvard.i2b2.oauth2.core.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProjectPatientMap {
	@Id// should actually be combination of domain and project
	String projectId;
	String i2b2Url;
	String i2b2Domain;
	
	String patientIdList;//comma seperated list of ids

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getI2b2Url() {
		return i2b2Url;
	}

	public void setI2b2Url(String i2b2Url) {
		this.i2b2Url = i2b2Url;
	}

	public String getI2b2Domain() {
		return i2b2Domain;
	}

	public void setI2b2Domain(String i2b2Domain) {
		this.i2b2Domain = i2b2Domain;
	}

	public String getPatientIdList() {
		return patientIdList;
	}

	public void setPatientIdList(String patientIdList) {
		this.patientIdList = patientIdList;
	}

	@Override
	public String toString() {
		return "ProjectPatientMap [projectId=" + projectId + ", i2b2Url="
				+ i2b2Url + ", i2b2Domain=" + i2b2Domain + ", patientIdList="
				+ patientIdList + "]";
	}
	
	
}
