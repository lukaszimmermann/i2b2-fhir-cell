package edu.harvard.i2b2.fhir;

import java.util.ArrayList;
import java.util.HashMap;

import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class MetaResourcePrimaryDb extends ArrayList<MetaResource> {
	HashMap<String,MetaResourceSet> hm; 
	public MetaResourcePrimaryDb(){
		hm=new HashMap<String,MetaResourceSet>();
	};
	
	public boolean add(MetaResource mr){
		String patientId=FhirUtil.getPatientId(mr);
		MetaResourceSet ms=null;
		if(hm.keySet().contains(patientId)){
			ms=hm.get(patientId);
		}else{
			ms=new MetaResourceSet();
		}
		ms.getMetaResource().add(mr);
		super.add(mr);
		return true;
	}

	private void addAll(){
	}
	
	public int size(){
		return super.size();
	}
	
}
