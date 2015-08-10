package edu.harvard.i2b2.fhirserver.ejb;


import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhirserver.entity.AuthToken;
import edu.harvard.i2b2.fhirserver.ws.I2b2FhirWS;

@ManagedBean
@SessionScoped
public class AuthTokenManager implements Serializable{
	static Logger logger = LoggerFactory.getLogger(AuthTokenManager.class);
	
    private static final long serialVersionUID = 2142383151318489373L;
    
    @EJB
    private AuthTokenBean request;

	private List<AuthToken> authTokens;
    
  
    /**
     * @return the orders
     */
    public List<AuthToken> getAuthTokens() {
        try {
            this.authTokens = request.getAuthTokens();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.authTokens;
    }

}