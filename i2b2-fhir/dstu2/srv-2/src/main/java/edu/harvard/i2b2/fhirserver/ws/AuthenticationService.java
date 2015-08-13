package edu.harvard.i2b2.fhirserver.ws;

import javax.ejb.EJB;

import edu.harvard.i2b2.fhirserver.ejb.AuthTokenBean;
import edu.harvard.i2b2.fhirserver.entity.AuthToken;

public class AuthenticationService {
	
	@EJB
	AuthTokenBean authTokenBean;
	
	public boolean authenticate(String authCredentials) {
		return true;
		//AuthToken tok=authTokenBean.authTokenByTokenString(authCredentials);
		//return (tok!=null)?true:false;
	}

}
