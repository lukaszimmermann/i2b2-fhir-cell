package edu.harvard.i2b2.oauth2.register.ejb;

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
import edu.harvard.i2b2.oauth2.register.entity.ConfigDb;

@Stateful
@FacesConverter(forClass=ConfigDb.class, value="configDb")
public class ConfigDbConverter implements Converter {
	static Logger logger = LoggerFactory
			.getLogger(ConfigDbConverter.class);
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		logger.trace("called: got value:"+value);
		
		
		List<ConfigDb> list=(List<ConfigDb>) context.getExternalContext().getSessionMap().get("ConfigDbList");
		if(list==null){logger.trace(" list is null");}
		else{ logger.trace("list Not Null:"+list.toString());}
		for(ConfigDb  c :list){
			if (c.getParameter().equals(value)) return c;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		logger.trace("called");
		return ((ConfigDb)value).getParameter();
	}

}
