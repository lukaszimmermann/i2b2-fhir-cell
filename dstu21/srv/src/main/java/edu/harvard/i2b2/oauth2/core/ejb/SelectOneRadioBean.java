package edu.harvard.i2b2.oauth2.core.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SelectOneRadioBean {

	private List<Tutorial> tutorials = new ArrayList<Tutorial>();

	private String selectedTutorial = new String();

	public SelectOneRadioBean() {
		this.tutorials.add(new Tutorial(1,"JSF 2"));
		this.tutorials.add(new Tutorial(2,"EclipseLink"));
		this.tutorials.add(new Tutorial(3,"HTML 5"));
		this.tutorials.add(new Tutorial(4,"Spring"));
	}

	public String register() {
		return "registrationInfo";
	}

	public List<Tutorial> getTutorials() {
		return tutorials;
	}

	public void setTutorials(List<Tutorial> tutorials) {
		this.tutorials = tutorials;
	}

	public String getSelectedTutorial() {
		return selectedTutorial;
	}

	public void setSelectedTutorial(String selectedTutorial) {
		this.selectedTutorial = selectedTutorial;
	}
}