package edu.harvard.i2b2.oauth2.register.ejb;


import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.oauth2.register.entity.ConfigDb;



@Named
@RequestScoped
public class ConfigDbManager {
	static Logger logger = LoggerFactory.getLogger(ConfigDbManager.class);

	@EJB
	ConfigDbService service;

	List<ConfigDb> list=null;
	String lastSave;
	
	@PostConstruct
	public void init(){
		list=service.list();
		lastSave="h1";
	}
	
	public void save(AjaxBehaviorEvent evt){
		String msg="";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		
		for(UIComponent c:evt.getComponent().getParent().getChildren()){
					UIComponent root = facesContext.getViewRoot();
				      UIComponent component = root.findComponent(c.getId());
				      if(component!=null)
				      {  msg+="\n"+c.getId()+"="+component.toString();}
						     
		}
		logger.trace("msg:"+msg);
		logger.trace("running save:"+((UIOutput)evt.getSource()).getValue());
		logger.trace("running save:"+((UIOutput)evt.getSource()).getValueExpression("label"));
		
		lastSave+="1";
	}
	
	public void update(ConfigDb c){
		logger.trace("updating"+c.toString());
		service.update(c);
	}
	
	public List<ConfigDb> getList() {
		return list;
	}

	
	
	public void setList(List<ConfigDb> list) {
		this.list = list;
	}

	public String getLastSave() {
		return lastSave;
	}

	public void setLastSave(String lastSave) {
		this.lastSave = lastSave;
	}

	
	
	
		
	
	
}
