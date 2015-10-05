package edu.harvard.i2b2.oauth2.core.ejb;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.Config;
import edu.harvard.i2b2.fhir.I2b2Util;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.Project;
import edu.harvard.i2b2.fhir.oauth2.ws.OAuth2AuthzEndpoint;

//http://www.javabeat.net/jsf-2-selectoneradio/
@Named
@SessionScoped
public class I2b2AuthenticationManager implements Serializable {
	static Logger logger = LoggerFactory
			.getLogger(I2b2AuthenticationManager.class);

	@EJB
	ProjectPatientMapManager projectPatientMapManager;

	private String i2b2User;
	private String i2b2Password;
	private boolean userValid;
	private String i2b2Token;
	private String pmResponseXml;

	List<Project> i2b2ProjectList = new ArrayList<Project>();
	List<String> i2b2PatientList = new ArrayList<String>();
	List<Tutorial> tutorials = new ArrayList<Tutorial>();

	private String client_id;
	private Project selectedI2b2Project;
	private String selectedI2b2PatientId;

	private String redirect_url;
	private HashSet<String> scope;

	@PostConstruct
	public void init() {
		this.setI2b2User("demo");
		this.setI2b2Password("demouser");

		tutorials.add(new Tutorial(1, "JSF 2"));
		tutorials.add(new Tutorial(2, "EclipseLink"));
		tutorials.add(new Tutorial(3, "HTML 5"));
		tutorials.add(new Tutorial(4, "Spring"));

		// Project p=new Project();
		// p.setId("proj1");p.setIntId(12);p.setName("proj1name");
		// i2b2ProjectList.add(p);

	}

	public Project getSelectedI2b2Project() {
		return selectedI2b2Project;
	}

	public void setSelectedI2b2Project(Project selectedI2b2Project) {
		this.selectedI2b2Project = selectedI2b2Project;
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
		logger.trace("r:" + this.i2b2ProjectList.toString());

		return i2b2ProjectList;
	}

	public void setI2b2ProjectList(List<Project> i2b2ProjectList) {
		this.i2b2ProjectList = i2b2ProjectList;
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

	public HashSet<String> getScope() {
		return scope;
	}

	public void setScope(HashSet<String> scope) {
		this.scope = scope;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public void processAuthUrl() {
		if (scope == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			Map<String, Object> session = context.getExternalContext()
					.getSessionMap();
			logger.trace("session:" + session.toString());
			this.setScope((HashSet<String>) session.get("scope"));
			this.setClient_id((String) session.get("clientId"));
			this.setRedirect_url((String) session.get("redirectUri"));
		}
		if (scope == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "scope parameter is missing!",
					null));
		}

	}

	public String login() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> session = context.getExternalContext()
				.getSessionMap();
		this.getI2b2ProjectList();
		return validate() ? "/i2b2/project_select" : "/i2b2/login";

	}

	public String selectProject() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> session = context.getExternalContext()
				.getSessionMap();
		if (getSelectedI2b2Project() == null) {
			return "/i2b2/project_select";
		}
		logger.trace("selectedI2b2ProjectId:"
				+ getSelectedI2b2Project().getId());
		logger.trace("selectedI2b2ProjectId:"
				+ getSelectedI2b2Project().getId());
		session.put("i2b2Project", getSelectedI2b2Project().getId());
		this.setI2b2PatientList(this.projectPatientMapManager
				.getProjectPatientList((String) session.get("i2b2User"),
						(String) session.get("i2b2Token"), (String) session
								.get("i2b2Url"), (String) session
								.get("i2b2Domain"), this
								.getSelectedI2b2Project().getId()));
		
		if (this.scope.contains("launch/patient")) {
			return "/i2b2/patient_select";
		}
		if (getSelectedI2b2Project().getId() != null) {
			ExternalContext c = FacesContext.getCurrentInstance()
					.getExternalContext();
			c.redirect("../api/authz/processScope");
			return "/i2b2/success";
		} else {
			return "error";
		}
	}

	public String selectPatient() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> session = context.getExternalContext()
				.getSessionMap();
		if (getSelectedI2b2PatientId() == null) {
			return "/i2b2/patient_select";
		}

		if (getSelectedI2b2PatientId() != null) {
			session.put("patientId", getSelectedI2b2PatientId());
			ExternalContext c = FacesContext.getCurrentInstance()
					.getExternalContext();
			c.redirect("../api/authz/processScope");
			return "/i2b2/success";

		} else {
			return "error";
		}
	}

	public String navigate() {
		String target = "/api/authz/processScope";
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> session = context.getExternalContext()
				.getSessionMap();
		if (session.get("pmResponse") == null) {
			target = validate() ? "/i2b2/project_select" : "/i2b2/login";
			// } else if (this.getSelectedItem() == null) {
			// target = "project_select";
		} else {
			// selectProject();
		}// if (session.get("selectedI2b2Patient") == null)
			// return "patient_select";

		/*
		 * if(this.getPmResponseXml()==null)
		 * {setPmResponseXml(I2b2Util.getPmResponseXml
		 * (this.getI2b2User(),this.getI2b2Password(),
		 * Config.i2b2Url,Config.i2b2Domain));}
		 * if(this.getI2b2ProjectList()==null){ return "selectProject"; }
		 */

		logger.info("navigate to:" + target);
		return target;

	}

	public boolean validate() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			Map<String, Object> session = context.getExternalContext()
					.getSessionMap();

			String pmResponseXml = I2b2Util.getPmResponseXml(
					this.getI2b2User(), this.getI2b2Password(), Config.getI2b2Url(),
					Config.getI2b2Domain());
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
				session.put("i2b2Url", Config.getI2b2Url());
				session.put("i2b2Domain", Config.getI2b2Domain());
				session.put("i2b2ProjectList", this.getI2b2ProjectList());
				session.put("scope", this.scope);

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

	private String selectedTutorial = new String();

	public String getSelectedTutorial() {
		return selectedTutorial;
	}

	public void setSelectedTutorial(String selectedTutorial) {
		this.selectedTutorial = selectedTutorial;
	}

	public List<Tutorial> getTutorials() {
		return tutorials;
	}

	public void setTutorials(List<Tutorial> tutorials) {
		this.tutorials = tutorials;
	}

	public List<String> getI2b2PatientList() {
		return i2b2PatientList;
	}

	public void setI2b2PatientList(List<String> i2b2PatientList) {
		this.i2b2PatientList = i2b2PatientList;
	}

}
