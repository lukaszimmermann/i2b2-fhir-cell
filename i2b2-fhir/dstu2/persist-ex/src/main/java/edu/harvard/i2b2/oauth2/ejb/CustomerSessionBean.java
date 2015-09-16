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

package edu.harvard.i2b2.oauth2.ejb;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless 
@Named 
public class CustomerSessionBean { 
	public List<String> getCustomerNames() {
		ArrayList<String> list= new ArrayList<String>();
		list.add("1 one");
		list.add("2 two");
		return list;
		
	}
}
