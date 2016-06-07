package edu.harvard.i2b2.fetcher;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ProjectPatientMap {
	@Id// should actually be combination of domain and project
	String projectId;
	String i2b2Url;
	String i2b2Domain;
	
	@Lob
	String patientIdList;//comma seperated list of ids
	
	@Lob
	String patientBundle;//Xml of Fhir Bundle


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

	
	public String getPatientBundle() {
		return patientBundle;
	}

	public void setPatientBundle(String patientBundle) {
		this.patientBundle = patientBundle;
	}

	@Override
	public String toString() {
		return "ProjectPatientMap [projectId=" + projectId + ", i2b2Url="
				+ i2b2Url + ", i2b2Domain=" + i2b2Domain + ", patientIdList="
				+ patientIdList + ", patientBundle=" + patientBundle + "]";
	}

	
	
}
