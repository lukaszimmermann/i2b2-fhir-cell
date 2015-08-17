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
	
	public boolean authenticate(String accessToken) {
		AccessToken tok=accessTokenBean.find(accessToken);
		return (tok!=null && (!isExpired(tok.getExpiryDate())))?true:false;
	}
	
	private boolean isExpired(Date inputDate){
			
			Date current=new Date();
			boolean result= current.after(inputDate)?true:false;
			logger.info("current Date:"+current.toGMTString()+"\ninputDate:"+inputDate.toGMTString()+"\nisExpired?:"+result);
			return result;
	}

}
