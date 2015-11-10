/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

package edu.harvard.i2b2.oauth2.register.ejb;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;

import edu.harvard.i2b2.oauth2.register.entity.Client;
import edu.harvard.i2b2.oauth2.register.entity.User;

/*
 * currently supports only admin user, who can create multiple clients
 * next step would be to allow developer users, each of whom can create multiple clients
 */
@Named
@RequestScoped
public class UserManager {

	private User user;

	@EJB
	UserService service;
	
	
	@PostConstruct
	public void init() {
		user = new User();
		user.setEmail("email1");
		user.setPassword("pass1");
	}

	public void save() {
		if (service.find(user.getId()) != null) {
			service.update(user);
		} else {
			service.create(user);
		}

	}

	public void update() {
		service.update(this.user);
	}

	public void delete() {
		service.delete(user);
	}

	public List<User> list() {
		return service.list();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String login() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (service.authenticate(user.getEmail(), user.getPassword())) {
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO, "Login successful!", null));
			User authorizedUser=service.findByEmail(user.getEmail());
			context.getExternalContext().getSessionMap()
					.put("authenticatedUser", authorizedUser);
			return "/user/successlogin";
		}
		{

			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Login failed.", null));
			return "failedlogin";
		}
	}

	public String printSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> s = context.getExternalContext().getSessionMap();
		return s.toString();
	}

	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		

		context.addMessage(null, new FacesMessage("Logout successful!"));
		
        HttpSession httpSession = (HttpSession)context.getExternalContext().getSession(false);
        httpSession.invalidate();
		return "/login/signin";

	}

}
