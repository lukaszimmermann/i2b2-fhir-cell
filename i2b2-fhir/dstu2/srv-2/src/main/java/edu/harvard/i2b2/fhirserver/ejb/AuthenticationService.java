package edu.harvard.i2b2.fhirserver.ejb;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhirserver.entity.AccessToken;
import edu.harvard.i2b2.fhirserver.ws.AuthenticationFilter;

@Singleton
@Startup
public class AuthenticationService {
	static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
	
	@EJB
	AccessTokenBean accessTokenBean;

	public boolean authenticate(String authHeaderContent) {
		if (authHeaderContent == null){
			logger.info("authfailure: authHeaderContent is null");
			return false;
		}

		
		AccessToken tok=getAccessTokenString(authHeaderContent);
		boolean res= (tok != null && (!isExpired(tok.getExpiryDate()))) ? true
				: false;
		logger.info("auth is"+res);
		return res;
	}
	
	public AccessToken getAccessTokenString(String authHeaderContent) {
		if (authHeaderContent == null) return null;
		String accessTokenId= authHeaderContent.replaceAll("Bearer\\s*", "");
		
		//if (accessTokenId.equals(AccessTokenBean.HARD_CODED_DAFEFAULT_TOKEN)) {
		//	return accessTokenBean.createIfNotExistsDemoAccessToken();
		//}
		return accessTokenBean.find(accessTokenId);
	}

	private boolean isExpired(Date inputDate) {

		Date current = new Date();
		boolean result = current.after(inputDate) ? true : false;
		logger.info("current Date:" + current.toGMTString() + "\ninputDate:"
				+ inputDate.toGMTString() + "\nisExpired?:" + result);
		return result;
	}

}
