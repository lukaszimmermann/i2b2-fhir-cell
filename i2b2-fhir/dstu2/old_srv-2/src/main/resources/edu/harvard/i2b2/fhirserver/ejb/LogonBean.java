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

package edu.harvard.i2b2.fhirserver.ejb;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import edu.harvard.i2b2.fhirserver.entity.Person;
import edu.harvard.i2b2.fhirserver.entity.User;
public class LogonBean {
     
      private static final String PERSISTENCE_UNIT_NAME = "User";
      private static EntityManagerFactory factory;
     
      
     private String userName;
     private String password;
     public LogonBean() { }
     public String getUserName() { return userName; }
     public void setUserName(String userName) { this.userName=userName; }
     public String getPassword() { return password; }
     public void setPassword(String password) { this.password=password; }
     public String validate() {
          String flag="failure";
              factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            EntityManager em = factory.createEntityManager();
            Query q = em.createQuery("SELECT u FROM User u WHERE u.Login = :login AND u.Password = :pass");
           q.setParameter("login", userName);
           q.setParameter("pass", password);
           try{
               User user = (User) q.getSingleResult();
             if (userName.equalsIgnoreCase(user.Login)&&password.equals(user.Password)) {
                flag="success";
             }
           }catch(Exception e){      
               return null;
           }

          return flag;
     }
}