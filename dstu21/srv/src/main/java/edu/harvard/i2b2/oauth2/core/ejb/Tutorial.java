package edu.harvard.i2b2.oauth2.core.ejb;

public class Tutorial {
	private int tutorialId;
	private String tutorialDescription;

	public Tutorial(int id, String desc){
		this.tutorialId = id;
		this.tutorialDescription = desc;
	}

	public int getTutorialId() {
		return tutorialId;
	}
	public void setTutorialId(int tutorialId) {
		this.tutorialId = tutorialId;
	}
	public String getTutorialDescription() {
		return tutorialDescription;
	}
	public void setTutorialDescription(String tutorialDescription) {
		this.tutorialDescription = tutorialDescription;
	}
}