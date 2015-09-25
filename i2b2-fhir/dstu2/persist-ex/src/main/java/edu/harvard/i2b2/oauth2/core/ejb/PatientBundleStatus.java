package edu.harvard.i2b2.oauth2.core.ejb;

import java.util.HashMap;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class PatientBundleStatus {
	static Logger logger = LoggerFactory.getLogger(PatientBundleStatus.class);
	static HashMap<String, PatientBundleStatusLevel> statusHM = new HashMap<String, PatientBundleStatusLevel>();

	@Lock( LockType.READ)
	public static boolean isComplete(String pid) {
		if (statusHM.containsKey(pid)
				&& statusHM.get(pid).equals(PatientBundleStatusLevel.COMPLETE)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Lock( LockType.READ)
	public static boolean isProcessing(String pid) {
		if (statusHM.containsKey(pid)
				&& statusHM.get(pid).equals(PatientBundleStatusLevel.PROCESSING)) {
			return true;
		} else {
			return false;
		}
	}

	@Lock( LockType.WRITE)
	public static void markProcessing(String pid) {
		logger.debug("Marking as Processing:" + pid);
		if (statusHM.containsKey(pid)
				&& statusHM.get(pid).equals(PatientBundleStatusLevel.PROCESSING)) {
			logger.error("is already Complete . Should not be Processing it again:"
					+ pid);
		}

		if (!statusHM.containsKey(pid)) {
			statusHM.put(pid, PatientBundleStatusLevel.PROCESSING);
			logger.debug("Marked as Processing:" + pid);
		}
	}

	@Lock( LockType.WRITE)
	public static void markComplete(String pid) {
		logger.debug("Marking as Complete:" + pid);
		if (!statusHM.containsKey(pid)) {
			logger.error("Error: The status should tleast be PROCESSING");
		}
		
		if (statusHM.containsKey(pid)) {
			statusHM.remove(pid);
			statusHM.put(pid, PatientBundleStatusLevel.COMPLETE);
		}
	}

	
}
