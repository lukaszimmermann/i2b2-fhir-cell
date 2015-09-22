package edu.harvard.i2b2.fhirserver.ejb;

import java.util.HashMap;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class PatientBundleStatus {
	static Logger logger = LoggerFactory.getLogger(PatientBundleStatus.class);
	static HashMap<String, PatientBundleStatusLevel> statusHM = new HashMap<String, PatientBundleStatusLevel>();

	public static boolean isComplete(String pid) {
		if (statusHM.containsKey(pid)
				& statusHM.get(pid).equals(PatientBundleStatusLevel.COMPLETE)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isProcessing(String pid) {
		if (statusHM.containsKey(pid)
				& statusHM.get(pid).equals(PatientBundleStatusLevel.PROCESSING)) {
			return true;
		} else {
			return false;
		}
	}

	public static void markProcessing(String pid) {
		logger.debug("Marking as Processing:" + pid);
		if (statusHM.containsKey(pid)
				& statusHM.get(pid).equals(PatientBundleStatusLevel.PROCESSING)) {
			logger.error("is already Complete . Should not be Processing it again:"
					+ pid);
		}

		if (!statusHM.containsKey(pid)) {
			statusHM.put(pid, PatientBundleStatusLevel.PROCESSING);
			logger.debug("Marked as Processing:" + pid);
		}
	}

	public static void markComplete(String pid) {
		logger.debug("Marking as Complete:" + pid);

		if (!statusHM.containsKey(pid)) {
			statusHM.put(pid, PatientBundleStatusLevel.COMPLETE);
		}
	}

	
}
