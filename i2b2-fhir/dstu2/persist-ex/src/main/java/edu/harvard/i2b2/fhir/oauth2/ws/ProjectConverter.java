package edu.harvard.i2b2.fhir.oauth2.ws;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.core.Project;
import edu.harvard.i2b2.oauth2.core.ejb.I2b2AuthenticationManager;

@Stateful
@FacesConverter(forClass=Project.class, value="pc")
public class ProjectConverter implements Converter {
	static Logger logger = LoggerFactory
			.getLogger(ProjectConverter.class);
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		logger.trace("called: got value:"+value);
		
		
		List<Project> list=(List<Project>) context.getExternalContext().getSessionMap().get("i2b2ProjectList");
		if(list==null){logger.trace(" list is null");}
		else{ logger.trace("list Not Null:"+list.toString());}
		for(Project p :list){
			if (p.getId().equals(value)) return p;
		}
		//Project p=new Project();p.setId("proj1");p.setName("proj1name");
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		logger.trace("called");
		return ((Project)value).getId();
	}

}
