package edu.harvard.i2b2.oauth2.core.entity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.iap.ByteArray;

import edu.harvard.i2b2.oauth2.core.ejb.PatientBundleService;

@Entity
public class PatientBundleRecord {
	static Logger logger = LoggerFactory.getLogger(PatientBundleRecord.class);

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

	public String getBundleXml()  {
		return //unzip(
				bundleXml;
				//);
	}

	public void setBundleXml(String bundleXml) {
		this.bundleXml = //zip(
					bundleXml;
				    //);
	}

	private String zip(String str) {
		if (str == null || str.length() == 0) {
            return str;
        }
		try {
			 
		        //System.out.println("String length : " + str.length());
		        ByteArrayOutputStream obj=new ByteArrayOutputStream();
		        GZIPOutputStream gzip = new GZIPOutputStream(obj);
		        gzip.write(str.getBytes("UTF-8"));
		        gzip.close();
		        String outStr = obj.toString("UTF-8");
		        logger.trace("zipped:"+outStr);
		        //System.out.println("Output String length : " + outStr.length());
		        return outStr;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return e.getMessage();
		}

	}

	private String unzip(String str) {
		if (str == null || str.length() == 0) {
            return str;
        }
		try {
			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("UTF-8")));
			BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
			String outStr = "";
			String line;
			while ((line = bf.readLine()) != null) {
				outStr += line;
			}
			logger.trace("unzipped:"+outStr);
			return outStr;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return e.getMessage();
		}

	}

}
