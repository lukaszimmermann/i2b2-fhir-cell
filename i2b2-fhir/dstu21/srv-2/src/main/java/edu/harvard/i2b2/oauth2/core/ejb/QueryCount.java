package edu.harvard.i2b2.oauth2.core.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class QueryCount {
	static Logger logger = LoggerFactory.getLogger(QueryCount.class);
	private int count;
	
	@PostConstruct
	public void init(){
		count=0;
	}

	@Lock
	public int getCount() {
		return count;
	}

	@Lock
	public void increaseCount() {
		this.count +=1;
		logger.debug("current Query count:"+count);
	}
	
	@Lock
	public void decreaseCount() {
		this.count -= 1;
	}
	

}
