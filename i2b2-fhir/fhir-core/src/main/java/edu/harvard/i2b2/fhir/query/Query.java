package edu.harvard.i2b2.fhir.query;

import java.util.ArrayList;

import org.hl7.fhir.Resource;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

//parent to types of query, which implement the different search types
public abstract class Query {
	String parameter;
	String value;
	QueryType type;
	String parameterPath;// this will be resource specific defined at runTime
	Class resourceClass;
	
	private Query(){}
	
	public Query(Class resourceClass,String parameter, String value) throws QueryParameterException,QueryValueException{
		this.resourceClass=resourceClass;
		this.parameter=parameter;
		this.value=value;
		SearchParameterTuple t=SearchParameterTupleMap.getTuple(this.resourceClass, this.parameter);
		if(t!=null)this.parameterPath=t.getPath();
		validateParameter();
		validateValue();
		validateParameterPath();
			
	}
	
	
	private void validateParameterPath() throws QueryParameterException {
		if(this.parameterPath==null) throw new QueryParameterException(
				"parameter Path not found for "+this.parameter+ " for class "+this.resourceClass);
	}

	abstract public boolean match(Resource r);

	final public boolean match(MetaResource mr){
		return match(mr.getResource());
		
	}
	
	final public void removeNonMatching(MetaResourceSet s){
		MetaResourceSet s2=new MetaResourceSet();
		ArrayList<MetaResource> list= (ArrayList<MetaResource>) s2.getMetaResource();
		ArrayList<MetaResource> slist=(ArrayList<MetaResource>) s.getMetaResource();
		for(MetaResource mr:slist){
			if(match(mr)) list.add(mr);
		}
		slist.clear();
		slist.addAll(list);
	}
	
	abstract public void validateParameter() throws QueryParameterException;
	
	abstract public void validateValue() throws QueryValueException;
	
	public String toString(){
		return "resourceClass="+this.resourceClass.getCanonicalName()
				+"\nparameter="+this.parameter
				+"\nvalue="+this.value
				+"\ntype="+this.type
				+"\nparameterPath"+this.parameterPath;
	}
	
	
}
