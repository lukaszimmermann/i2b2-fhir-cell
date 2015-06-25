package edu.harvard.i2b2;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.hl7.fhir.Id;
import org.hl7.fhir.MedicationStatement;
import org.hl7.fhir.Patient;
import org.hl7.fhir.Resource;
import org.hl7.fhir.Reference;
import org.hl7.fhir.String;

import edu.harvard.i2b2.fhir.FhirUtil;
import edu.harvard.i2b2.fhir.core.MetaData;
import edu.harvard.i2b2.fhir.core.MetaResource;
import edu.harvard.i2b2.fhir.core.MetaResourceSet;

public class ResourceSetup {
	
	static public  MetaResourceSet getEGPatient()
			throws DatatypeConfigurationException {
		Patient p = new Patient();
		Id id=new Id();
		id.setValue("Patient/1000000005");;
		p.setId(id);

		MetaData md1 = new MetaData();
		GregorianCalendar gc = new GregorianCalendar();
		md1.setLastUpdated(DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(gc));

		// md1.setId(p.getId());

		MetaResource mr1 = new MetaResource();
		mr1.setResource((Resource) p);
		mr1.setMetaData(md1);

		MetaResourceSet s1 = new MetaResourceSet();
		s1.getMetaResource().add(mr1);

		return s1;
	}

	
	
	
	static public  MetaResourceSet getPatientAndMedicationStatementEg()
			throws DatatypeConfigurationException {
		MetaResourceSet s1 = new MetaResourceSet();

		Patient p = new Patient();
		FhirUtil.setId(p,"Patient/1000000005");

		MedicationStatement ms = new MedicationStatement();
		FhirUtil.setId(ms,"MedicationStatement/1000000005-1");
		ms.setPatient(FhirUtil.getReference(p));
		
		MetaResource mr1 = new MetaResource();
		MetaData md1 = new MetaData();
		// md1.setId(p.getId());
		mr1.setResource(p);
		mr1.setMetaData(md1);
		GregorianCalendar gc = new GregorianCalendar();
		md1.setLastUpdated(DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(gc));
		s1.getMetaResource().add(mr1);

		MetaResource mr2 = new MetaResource();
		MetaData md2 = new MetaData();
		// md2.setId(ms.getId());
		mr2.setResource(ms);
		mr2.setMetaData(md2);
		md2.setLastUpdated(DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(gc));

		s1.getMetaResource().add(mr2);
		return s1;
		}
 	
	 	
}
