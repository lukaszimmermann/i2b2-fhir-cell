package edu.harvard.i2b2.fhir.map;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.map.MapSystemSet;


public class MapSystemEngine {
	static Logger logger = LoggerFactory.getLogger(MapSystemEngine.class);
	MapSystemSet m;
	
	public MapSystemEngine(String xml) throws JAXBException, IOException{
		//read config file
		m=JAXBUtil.fromXml(xml,MapSystemSet.class);
		//iterate through MapSystem and create output Xml
		logger.info(JAXBUtil.toXml(m));
	}
	
	public String runMapSystem(String input){
		String outputXml=null;
		
		return outputXml;
	}
	
	
	
}
