package edu.harvard.i2b2.oauth2.core.ejb;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBException;

import org.hl7.fhir.Bundle;
import org.hl7.fhir.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.fhir.core.FhirCoreException;
import edu.harvard.i2b2.fhir.query.QueryEngine;
import edu.harvard.i2b2.fhir.query.QueryException;
import edu.harvard.i2b2.fhir.query.QueryParameterException;
import edu.harvard.i2b2.fhir.query.QueryValueException;

@Singleton
public class QueryService {
	static Logger logger = LoggerFactory.getLogger(QueryService.class);

	
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
				.getResourceBundle(qe.search(list), basePath, "url");
		return res;
		}catch(XQueryUtilException | QueryParameterException | QueryValueException | FhirCoreException | JAXBException | QueryException e){
			logger.error(e.getMessage(),e);
		}
		return null;
	}
}
