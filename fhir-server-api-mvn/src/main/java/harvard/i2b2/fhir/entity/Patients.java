package harvard.i2b2.fhir.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.hl7.fhir.Patient;

@XmlRootElement
public class Patients {
	public List<Patient> patients=new ArrayList<Patient>();

	}
