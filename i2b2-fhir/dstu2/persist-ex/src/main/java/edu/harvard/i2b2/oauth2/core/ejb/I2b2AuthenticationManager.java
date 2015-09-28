package edu.harvard.i2b2.oauth2.core.ejb;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.Config;
import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.Project;
import edu.harvard.i2b2.fhir.oauth2.ws.OAuth2AuthzEndpoint;

@Named
@RequestScoped
public class I2b2AuthenticationManager {
	static Logger logger = LoggerFactory
			.getLogger(I2b2AuthenticationManager.class);

	private String i2b2User;
	private String i2b2Password;
	private boolean userValid;
	private String i2b2Token;
	private String pmResponseXml;
	
	private List<Project> i2b2ProjectList;
	private Project selectedI2b2Project;
	private String selectedI2b2PatientId;
	
	private String redirect_url;
	private String scope;
	private String clientId;
	
	@PostConstruct
	public void init(){
		this.setI2b2User("demo");
		this.setI2b2Password("demouser");
	}

	public String getI2b2User() {
		return i2b2User;
	}

	public void setI2b2User(String i2b2User) {
		this.i2b2User = i2b2User;
	}

	public String getI2b2Password() {
		return i2b2Password;
	}

	public void setI2b2Password(String i2b2Password) {
		this.i2b2Password = i2b2Password;
	}

	public List<Project> getI2b2ProjectList() {
		return i2b2ProjectList;
	}

	public void setI2b2ProjectList(List<Project> i2b2ProjectList) {
		this.i2b2ProjectList = i2b2ProjectList;
	}

	public Project getSelectedI2b2Project() {
		return selectedI2b2Project;
	}

	public void setSelectedI2b2Project(Project selectedI2b2Project) {
		this.selectedI2b2Project = selectedI2b2Project;
	}

	public String getSelectedI2b2PatientId() {
		return selectedI2b2PatientId;
	}

	public void setSelectedI2b2PatientId(String selectedI2b2PatientId) {
		this.selectedI2b2PatientId = selectedI2b2PatientId;
	}

	public String getPmResponseXml() {
		return pmResponseXml;
	}

	public void setPmResponseXml(String pmResponseXml) {
		this.pmResponseXml = pmResponseXml;
	}

	public boolean isUserValid() {
		return userValid;
	}

	public void setUserValid(boolean userValid) {
		this.userValid = userValid;
	}

	public String getI2b2Token() {
		return i2b2Token;
	}

	public void setI2b2Token(String i2b2Token) {
		this.i2b2Token = i2b2Token;
	}
	
	public String getRedirect_url() {
		return redirect_url;
	}

	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String navigate() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> session = context.getExternalContext()
				.getSessionMap();
		if (session.get("pmResponse") == null) {
			return validate() ? "/i2b2/project_select" : "/i2b2/login";
		}
		if (session.get("selectedI2b2Project") == null)
			return "project_select";

		if (session.get("selectedI2b2Patient") == null)
			return "patient_select";

		if (this.userValid) {
			return "resourceOwnerLogin";
		}
		/*
		 * if(this.getPmResponseXml()==null)
		 * {setPmResponseXml(I2b2Util.getPmResponseXml
		 * (this.getI2b2User(),this.getI2b2Password(),
		 * Config.i2b2Url,Config.i2b2Domain));}
		 * if(this.getI2b2ProjectList()==null){ return "selectProject"; }
		 */

		return "ERROR";

	}

	public boolean validate() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			Map<String, Object> session = context.getExternalContext()
					.getSessionMap();

			String pmResponseXml = I2b2Util.getPmResponseXml(
					this.getI2b2User(), this.getI2b2Password(), Config.i2b2Url,
					Config.i2b2Domain);
			if (I2b2Util.authenticateUser(pmResponseXml)) {
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Login Sucessful!", null));
				this.setPmResponseXml(pmResponseXml);
				this.setI2b2Token(I2b2Util.getToken(pmResponseXml));
				this.setI2b2ProjectList(I2b2Util
						.getUserProjectMap(pmResponseXml));
				session.put("i2b2User", this.getI2b2User());
				session.put("pmResponseXml", this.getPmResponseXml());
				session.put("i2b2Token", this.getI2b2Token());
				session.put("selectedI2b2Project",
						this.getSelectedI2b2Project());
				return true;

			} else {
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Login failed!", null));
				return false;
			}
		} catch (XQueryUtilException | IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}

	}

}
