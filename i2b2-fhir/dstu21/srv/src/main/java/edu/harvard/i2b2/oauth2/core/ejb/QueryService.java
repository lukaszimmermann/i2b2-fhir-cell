package edu.harvard.i2b2.oauth2.core.ejb;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.server.ServerConfigs;
import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.query.QueryEngine;
import edu.harvard.i2b2.fhir.query.QueryException;
import edu.harvard.i2b2.fhir.query.QueryParameterException;
import edu.harvard.i2b2.fhir.query.QueryValueException;

@Stateless
public class QueryService {
	static Logger logger = LoggerFactory.getLogger(QueryService.class);
	
	@Inject
	QueryCount qc;
	
	@EJB
	ServerConfigs serverConfig;
	
	@PostConstruct
	public void init(){
		while(qc.getCount()>=serverConfig.getMaxQueryThreads()){
			logger.trace("waiting as query count is "+qc.getCount());
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				logger.error(e.getMessage(),e);
			}
		}
		qc.increaseCount();
	}
	
	@PreDestroy
	public void destroy(){
		qc.decreaseCount();
	}
	
	@Lock
    @AccessTimeout(value = 500, unit = TimeUnit.SECONDS)
	public Bundle runQueryEngine(String fhirQuery,Bundle b,String basePath){
		try{
		QueryEngine qe = new QueryEngine(fhirQuery);
		logger.info("created QE:" + qe);
		logger.trace("will search on bundle:" + JAXBUtil.toXml(b));
		List<Resource> list = FhirUtil.getResourceListFromBundle(b);
		logger.trace("list size:" + list.size());
		Bundle res = FhirUtil
				.getResourceBundle(qe.search(list,null), basePath, "url");
		return res;
		}catch(XQueryUtilException | QueryParameterException | QueryValueException | FhirCoreException | JAXBException | QueryException e){
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	

	
	
}
