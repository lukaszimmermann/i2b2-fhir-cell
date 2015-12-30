package edu.harvard.i2b2.fhir.server.ws;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Code;
import org.hl7.fhir.Conformance;
import org.hl7.fhir.ConformanceInteraction;
import org.hl7.fhir.ConformanceResource;
import org.hl7.fhir.ConformanceRest;
import org.hl7.fhir.ConformanceSearchParam;
import org.hl7.fhir.ConformanceSecurity;
import org.hl7.fhir.Extension;
import org.hl7.fhir.Narrative;
import org.hl7.fhir.NarrativeStatusList;
import org.hl7.fhir.TypeRestfulInteraction;
import org.hl7.fhir.TypeRestfulInteractionList;
import org.hl7.fhir.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3._1999.xhtml.Div;

import edu.harvard.i2b2.fhir.FhirUtil;

public class ConformanceStatement {
	static Logger logger = LoggerFactory.getLogger(ConformanceStatement.class);
	

	private static ConformanceResource getReadOnlyConformanceResource(String name, HashMap<String, String> hm) {
		ConformanceResource p = new ConformanceResource();
		Code value2 = new Code();
		value2.setValue(name);
		p.setType(value2);
		List<TypeRestfulInteractionList> list=new ArrayList<>();
		list.add(TypeRestfulInteractionList.READ);
		list.add(TypeRestfulInteractionList.SEARCH_TYPE);
		list.add(TypeRestfulInteractionList.VALIDATE);
		
		for(TypeRestfulInteractionList tril:list){
			ConformanceInteraction ci = new ConformanceInteraction();
			TypeRestfulInteraction value = new TypeRestfulInteraction();
			value.setValue(tril);
			ci.setCode(value);
			p.getInteraction().add(ci);
		}
		for (String k : hm.keySet()) {
			addConformanceSearchParam(p, k, hm.get(k));
		}
		return p;
	}
	
	static private Conformance addConformanceText(Conformance c,URI fhirBase) throws JAXBException {
		Narrative t = new Narrative();
		Div d = new Div();
		List<Object> con = d.getContent();
		org.w3._1999.xhtml.H2 h2 = new org.w3._1999.xhtml.H2();
		h2.getContent().add("FHIR Reference Server Conformance Statement");

		Code code = new Code();
		code.setValue(NarrativeStatusList.GENERATED.toString());
		c.setStatus(code);
		org.w3._1999.xhtml.Table tb = new org.w3._1999.xhtml.Table();
		tb.setBorder(BigInteger.ONE);
		tb.setCellpadding("0");
		tb.setCellspacing("0");
		
		List<String> opList = new ArrayList<>();
		opList.add("Resource Type");
		opList.add("Profile");
		opList.add("Read");
		opList.add("V-Read");
		opList.add("Search");
		opList.add("Update");
		opList.add("Updates");
		opList.add("Create");
		opList.add("Delete");
		opList.add("History");
		opList.add("Validate");
		org.w3._1999.xhtml.Tr row = new org.w3._1999.xhtml.Tr();
		for (String op : opList) {
			org.w3._1999.xhtml.Th col = new org.w3._1999.xhtml.Th();
			col.getContent().add(op);
			row.getThOrTd().add(col);
		}
		tb.getTr().add(row);

		for (ConformanceResource cr : c.getRest().get(0).getResource()) {
			// logger.trace("cr:" + JAXBUtil.toXml(cr));
			row = new org.w3._1999.xhtml.Tr();
			org.w3._1999.xhtml.Td col1 = new org.w3._1999.xhtml.Td();
			col1.getContent().add(cr.getType().getValue());
			col1.setAlign("left");
			row.getThOrTd().add(col1);
			row.getThOrTd().add(new org.w3._1999.xhtml.Td());//Profile
			
			
			boolean interactF = false;
			for (String op : opList) {
				if(op.equals("Resource Type")||op.equals("Profile")){continue;}
				org.w3._1999.xhtml.Td col = new org.w3._1999.xhtml.Td();
				String value = "";
				for (ConformanceInteraction i : cr.getInteraction()) {
					// logger.trace(""+i.getCode().getValue().toString().toLowerCase());
					op=(op.toLowerCase().equals("search"))?"search_type":op;
					if (i.getCode().getValue().toString().toLowerCase().equals(op.toLowerCase())) {
						interactF = true;
						logger.trace(cr.getType().getValue() + "-->" + op);
						org.w3._1999.xhtml.Img img=new org.w3._1999.xhtml.Img();
						img.setSrc(fhirBase+"../tick.png");
						img.setTitle("Y");
						col.getContent().add(img);
						//col.getContent().add(op);
						col.setAlign("center");
					}
				}
				if(!interactF){
					col.getContent().add(value);
				}
				row.getThOrTd().add(col);
			}
			tb.getTr().add(row);

		}

		con.add(h2);
		con.add(new org.w3._1999.xhtml.Br());
		con.add(tb);
		t.setDiv(d);
		c.setText(t);

		
		return c;
	}

	static private void addConformanceSearchParam(ConformanceResource p, String name, String type) {
		ConformanceSearchParam sp = new ConformanceSearchParam();
		org.hl7.fhir.String s = new org.hl7.fhir.String();
		s.setValue(name);
		sp.setName(s);
		Code value3 = new Code();
		value3.setValue(type);
		sp.setType(value3);
		p.getSearchParam().add(sp);

	}

	public static Conformance getStatement(URI fhirBase) throws JAXBException {
		Conformance c = new Conformance();
		FhirUtil.setId(c, "I2b2FHIRCell");
		ConformanceRest rest = new ConformanceRest();
		ConformanceSecurity security = new ConformanceSecurity();
		Extension OAuthext = new Extension();
		security.getExtension().add(OAuthext);
		OAuthext.setUrl("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");

		Extension authExt = new Extension();
		authExt.setUrl("authorize");
		Uri uri = new Uri();
		uri.setValue(fhirBase + "authz/authorize");
		authExt.setValueUri(uri);
		OAuthext.getExtension().add(authExt);

		Extension tokenExt = new Extension();
		tokenExt.setUrl("token");
		uri = new Uri();
		uri.setValue(fhirBase + "token");
		tokenExt.setValueUri(uri);
		OAuthext.getExtension().add(tokenExt);

		rest.setSecurity(security);

		// Patient

		rest.getResource().add(getReadOnlyConformanceResource("Patient", new HashMap<String, String>() {
			{
				put("_id", "token");
				put("gender", "token");
			}
		}));
		rest.getResource().add(getReadOnlyConformanceResource("MedicationOrder", new HashMap<String, String>() {
			{
				put("_id", "token");
				put("patient", "token");
				put("medication", "reference");
			}
		}));
		rest.getResource().add(getReadOnlyConformanceResource("Medication", new HashMap<String, String>() {
			{
				put("_id", "token");
				put("code", "token");
			}
		}));
		rest.getResource().add(getReadOnlyConformanceResource("Observation", new HashMap<String, String>() {
			{
				put("_id", "token");
				put("code", "token");
				put("value", "string");
			}
		}));
		rest.getResource().add(getReadOnlyConformanceResource("Condition", new HashMap<String, String>() {
			{
				put("_id", "token");
				put("code", "token");
			}
		}));

		c.getRest().add(rest);
		c=addConformanceText(c,fhirBase);
		return c;
	}

}
