package edu.harvard.i2b2.oauth2.core.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.hl7.fhir.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.Config;

@Stateless
public class QueryManager {
	static Logger logger = LoggerFactory.getLogger(QueryManager.class);

	@EJB
	QueryService service;
	
	@Inject
	QueryCount qc;
	
	public Bundle runQueryEngine(String fhirQuery,Bundle b,String basePath){
		while(qc.getCount()>=Config.getMaxQueryThreads()){
			logger.trace("waiting as query count is "+qc.getCount());
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				logger.error(e.getMessage(),e);
			}
		}
		
		return service.runQueryEngine(fhirQuery, b, basePath);
		
	}

}
