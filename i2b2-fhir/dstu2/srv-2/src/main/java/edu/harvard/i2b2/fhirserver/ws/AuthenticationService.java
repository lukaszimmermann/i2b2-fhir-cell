package edu.harvard.i2b2.fhirserver.ws;

import javax.ejb.EJB;

import edu.harvard.i2b2.fhirserver.ejb.AccessTokenBean;
import edu.harvard.i2b2.fhirserver.entity.AccessToken;

public class AuthenticationService {
	
	@EJB
	AccessTokenBean accessTokenBean;
	
	public boolean authenticate(String authCredentials) {
		return true;
		//AuthToken tok=authTokenBean.authTokenByTokenString(authCredentials);
		//return (tok!=null)?true:false;
	}

}
