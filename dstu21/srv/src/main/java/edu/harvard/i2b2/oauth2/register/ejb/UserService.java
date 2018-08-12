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

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.oauth2.register.entity.Client;
import edu.harvard.i2b2.oauth2.register.entity.User;
import edu.harvard.i2b2.oauth2.register.entity.UserType;

@Stateless
public class UserService {
	static Logger logger = LoggerFactory.getLogger(UserService.class);

	@PersistenceContext
	EntityManager em;

	public void setup() {
		createBlank();
		list();
		find(0);
		// remove("client1");
		//find("client1");
	}


	public void createBlank() {
		User u=new User();
		u.setEmail("email1");
		u.setPassword("p1");
		u.setUserType(UserType.ADMIN);
		create(u);
		
		u=new User();
		u.setEmail("email2");
		u.setPassword("p2");
		u.setUserType(UserType.DEVELOPER);
		create(u);
		
		u=new User();
		u.setEmail("email3");
		u.setPassword("p3");
		u.setUserType(UserType.DEVELOPER);
		create(u);
	}

	public void create(User u) {
		logger.info("persisting:" + u.toString());
		u.setRegisteredOn(new Date());
		em.persist(u);
		logger.info("persisted:" + u.toString());
	}

	public List<User> list() {
		List<User> list = em.createQuery(" SELECT s FROM User s")
				.getResultList();
		String msg = "";
		for (Object x : list) {
			msg += x.toString() + "\n";
		}
		logger.info("list:" + msg);
		return list;
	}

	public User find(int i) {
		User u = em.find(User.class, i);
		if (u != null) {
			logger.info("found:" + u.toString());
		} else {
			logger.info("no user with id:" + i);
		}
		return u;
	}

	public void delete(User u) {
		em.remove(em.contains(u) ? u : em.merge(u));
		logger.info("removed client");
	}

	public void update(User u) {
		em.merge(u);
		logger.trace("updated:" + u);
	}

	public void save(User u) {
		if (em.contains(u)) {
			update(u);
		} else {
			create(u);
		}
		logger.trace("updated:" + u);

	}


	public boolean authenticate(String email, String password) {
		Query q= em.createQuery(" SELECT s FROM User s where email=:email and password=:password");
		q.setParameter("email", email);
		q.setParameter("password", password);
		
		List<User> list =q.getResultList();
		String msg = "";
		for (Object x : list) {
		//	msg += x.toString() + "\n";
		}
		logger.info("list:" + msg);
		return (list.size()>0)?true:false;
	}


	public User findByEmail(String email) {
		Query q= em.createQuery(" SELECT s FROM User s where email=:email");
		q.setParameter("email", email);
		List<User> list =q.getResultList();
		if(list.size()>0){
			if(list.size()>1) logger.error("Email id is not unique:"+email);
			return list.get(0);
		}
		return null;
	}

}
