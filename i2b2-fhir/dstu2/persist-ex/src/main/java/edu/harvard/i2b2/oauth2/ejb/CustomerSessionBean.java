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
