package edu.harvard.i2b2.fetcher;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.iap.ByteArray;

import edu.harvard.i2b2.fhir.Utils;




@Entity
public class PatientBundleRecord {
	static Logger logger = LoggerFactory.getLogger(PatientBundleRecord.class);

	@Id
	String patientId;

	@Lob
	byte[] bundleXml;

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getBundleXml() {
		try {
			return Utils.unCompressString(bundleXml,"UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public void setBundleXml(String bundleXml) {
		try {
			this.bundleXml = Utils.compress(bundleXml,"UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}


	@Override
	public String toString() {
		return "PatientBundleRecord [patientId=" + patientId + ", bundleXml=" + this.getBundleXml() + "]";
	}

	
}
