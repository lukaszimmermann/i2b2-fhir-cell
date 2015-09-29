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
public class BundleStatus {
	static Logger logger = LoggerFactory.getLogger(BundleStatus.class);
	static HashMap<String, BundleStatusLevel> statusHM = new HashMap<String, BundleStatusLevel>();

	@Lock( LockType.READ)
	public static boolean isComplete(String pid) {
		if (statusHM.containsKey(pid)
				&& statusHM.get(pid).equals(BundleStatusLevel.COMPLETE)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Lock( LockType.READ)
	public static boolean isProcessing(String pid) {
		if (statusHM.containsKey(pid)
				&& statusHM.get(pid).equals(BundleStatusLevel.PROCESSING)) {
			return true;
		} else {
			return false;
		}
	}

	@Lock( LockType.WRITE)
	public static void markProcessing(String pid) {
		logger.debug("Marking as Processing:" + pid);
		if (statusHM.containsKey(pid)
				&& statusHM.get(pid).equals(BundleStatusLevel.PROCESSING)) {
			logger.error("is already Complete . Should not be Processing it again:"
					+ pid);
		}

		if (!statusHM.containsKey(pid)) {
			statusHM.put(pid, BundleStatusLevel.PROCESSING);
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
			statusHM.put(pid, BundleStatusLevel.COMPLETE);
		}
	}

	
}
