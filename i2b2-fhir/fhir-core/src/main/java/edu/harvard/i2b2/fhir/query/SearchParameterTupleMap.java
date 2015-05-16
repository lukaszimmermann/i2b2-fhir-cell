package edu.harvard.i2b2.fhir.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hl7.fhir.Patient;

public class SearchParameterTupleMap {
	static HashMap<Class,List<SearchParameterTuple>> hm;
	
	static{
		//TODO
		//to auto-generate from specification html
		hm =new HashMap<Class,List<SearchParameterTuple>>();
		List<SearchParameterTuple> patientTuple= new ArrayList<SearchParameterTuple>();
		patientTuple.add(new SearchParameterTuple("birthdate","date","The patient's date of birth","Patient.birthDate"));
		patientTuple.add(new SearchParameterTuple("gender","token","Gender of the patient",	"Patient.gender"));
		patientTuple.add(new SearchParameterTuple("identifier","token","A patient identifier","Patient.identifier"));
		//patientTuple.add(new SearchParameterTuple("_id","token","The logical resource id associated with the resource (must be supported by all servers)","Patient.id"));
		patientTuple.add(new SearchParameterTuple("active","token","Whether the patient record is active","Patient.active"));
		hm.put(Patient.class, patientTuple);
	}
	
	static SearchParameterTuple getTuple(Class c,String parameter){
		for(SearchParameterTuple t: hm.get(c)){
			if(t.getParameter().equals(parameter)) return t;
		}
		return null;
	}
}
