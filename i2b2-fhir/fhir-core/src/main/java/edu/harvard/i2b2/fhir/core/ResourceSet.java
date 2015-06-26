package edu.harvard.i2b2.fhir.core;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Resource;

import edu.harvard.i2b2.fhir.JAXBUtil;


public class ResourceSet{
	Set<Resource> set;
	public ResourceSet(){
		set= new HashSet<Resource>();
	}
	
	public String toString(){
		String msg="";
		for(Resource r:set){
			try {
				msg+=JAXBUtil.toXml(r);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return msg;
	}

}
