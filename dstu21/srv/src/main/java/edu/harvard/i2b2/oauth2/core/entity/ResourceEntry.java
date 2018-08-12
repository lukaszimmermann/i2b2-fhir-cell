package edu.harvard.i2b2.oauth2.core.entity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//profiles, tags, and security labels  are CSV strings for now.
//may be implemented as references to their own tables
//(one to many mappings) from resources to tags profiles and labels
@Table(indexes={@Index(columnList="PATIENTID")})
public class ResourceEntry {
	static Logger logger = LoggerFactory.getLogger(ResourceEntry.class);

	@Id
	String Id;

	String patientId;

	String resourceType;//Simple class Name of the resource
	
	String profiles;
	
	String tags;
	
	String securityLabels;
	
	@Lob
	String xml;
	
	Byte[] bytes;

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public Byte[] getBytes() {
		return bytes;
	}

	public void setBytes(Byte[] bytes) {
		this.bytes = bytes;
	}

	
}
