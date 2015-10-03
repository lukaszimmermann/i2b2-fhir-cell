package edu.harvard.i2b2.util;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.Observation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.query.QueryIdTest;

public class AutoJAXBAnnotationTest {
	static Logger logger = LoggerFactory
			.getLogger(AutoJAXBAnnotationTest.class);

	@Test
	public void test() {

		try {
			FhirUtil.addJAXBAnnotationsToClasses();
			String xml = IOUtils
					.toString(AutoJAXBAnnotationTest.class
							.getResourceAsStream("/example/fhir/DSTU2/singleObservation.xml"));
			Observation r=JAXBUtil.fromXml(xml, Observation.class);
			//logger.trace(arg0);
		} catch (IOException | JAXBException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Test
	public void reloadClass(){
		try{
			String className="org.hl7.fhir.Observation";
		  ClassPool cpool = ClassPool.getDefault();
	        CtClass cclass = cpool.get(className);
	        
	       ClassFile cf =cclass.getClassFile();
			ConstPool cp = cf.getConstPool();
			
	        AnnotationsAttribute attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
	       // @javax.xml.bind.annotation.XmlRootElement(namespace=##default, name=Observation)
			Annotation a = new Annotation("javax.xml.bind.annotation.XmlRootElement", cp);
			a.addMemberValue("name", new StringMemberValue("Observation", cp));
			attr.setAnnotation(a);
			cf.addAttribute(attr);
	        Class c = cclass.toClass();
	       // Observation h = (Observation)c.newInstance();
	        java.lang.annotation.Annotation[] annotations = c.getAnnotations();

			for(java.lang.annotation.Annotation annotation : annotations){
			    //if(annotation instanceof MyAnnotation){
			       logger.trace("annot: " + annotation);
			    //}
			}
			String xml = IOUtils
					.toString(AutoJAXBAnnotationTest.class
							.getResourceAsStream("/example/fhir/DSTU2/singleObservation.xml"));
			Observation r=JAXBUtil.fromXml(xml, Observation.class);
			logger.trace(JAXBUtil.toXml(r));
	        
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
	}

}
