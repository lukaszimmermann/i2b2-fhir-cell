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

package edu.harvard.i2b2.fhirserver.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="Users")
@NamedQuery(name="Users.findAll", query ="SELECT * FROM Users")
public class User {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id;
     
 public String Name = null;
 public String Login = null;
 public String Password = null;

public int get_Id(){
     return id;
}
public void set_Id(int id){
this.id = id;
}
public String getName(){
return Name;
}
public void setName(String Name){
this.Name = Name;
}
public String getLogin(){     
return Login;
}
public void setLogin(String Login){
     this.Login = Login;
}
public String getPassword(){
     return Password;
}
public void setPassword(String Password){
     this.Password = Password;
}
     
}
