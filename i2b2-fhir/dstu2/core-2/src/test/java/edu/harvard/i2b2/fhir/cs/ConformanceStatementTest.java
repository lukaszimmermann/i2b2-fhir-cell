package edu.harvard.i2b2.fhir.cs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.hl7.fhir.Code;
import org.hl7.fhir.Conformance;
import org.hl7.fhir.Narrative;
import org.hl7.fhir.NarrativeStatusList;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3._1999.xhtml.Div;

import edu.harvard.i2b2.fhir.JAXBUtil;

public class ConformanceStatementTest {
	static Logger logger = LoggerFactory.getLogger(ConformanceStatementTest.class);
	@Test
	public void test() throws JAXBException {
		//fail("Not yet implemented");
		Conformance c= new Conformance();
		c=addConformanceText(c);
		logger.trace(""+JAXBUtil.toXml(c));
		
	}
	private Conformance addConformanceText(Conformance c){
		Narrative t=new Narrative();
		Div d = new Div();
		List<Object> con = d.getContent();
		org.w3._1999.xhtml.H2 h2=new org.w3._1999.xhtml.H2();
		h2.setTitle("FHIR Reference Server Conformance Statement");
		
		Code code= new Code(); 
		code.setValue(NarrativeStatusList.GENERATED.toString());
		c.setStatus(code);
		org.w3._1999.xhtml.Table tb=new org.w3._1999.xhtml.Table();
		List<String> opList=new ArrayList<>();
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
		org.w3._1999.xhtml.Tr row= new org.w3._1999.xhtml.Tr();
		for(String op:opList){
			org.w3._1999.xhtml.Th col=new org.w3._1999.xhtml.Th();
			col.setTitle(op);
			row.getThOrTd().add(col);
		}
		tb.getTr().add(row);
		
		con.add(h2);
		con.add(tb);
		t.setDiv(d);
		c.setText(t);
		
		/*
		<div xmlns="http://www.w3.org/1999/xhtml">
            <h2>FHIR Reference Server Conformance Statement</h2>
            <p>FHIR v1.0.2-7406 released 20131103. Reference Server version 1.0.12 built 2015-12-23</p>
            <table class="grid">
                <tr>
                    <th>Resource Type</th>
                    <th>Profile</th>
                    <th>Read</th>
                    <th>V-Read</th>
                    <th>Search</th>
                    <th>Update</th>
                    <th>Updates</th>
                    <th>Create</th>
                    <th>Delete</th>
                    <th>History</th>
                    <th>Validate</th>
                </tr>
                <tr>
                    <td>Account</td>
                    <td>
                        <a href="http://fhir2.healthintersections.com.au/open/profile/account?format=text/html">account</a>
                    </td>
                    <td align="center">
                        <img src="http://www.healthintersections.com.au/tick.png"/>
                    </td>
                    <td align="center">
                        <img src="http://www.healthintersections.com.au/tick.png"/>
                    </td>
                    <td align="center">
                        <img src="http://www.healthintersections.com.au/tick.png"/>
                    </td>
                    <td align="center">
                        <img src="http://www.healthintersections.com.au/tick.png"/>
                    </td>
                    <td align="center">
                        <img src="http://www.healthintersections.com.au/tick.png"/>
                    </td>
                    <td align="center">
                        <img src="http://www.healthintersections.com.au/tick.png"/>
                    </td>
                    <td align="center">
                        <img src="http://www.healthintersections.com.au/tick.png"/>
                    </td>
                    <td align="center">
                        <img src="http://www.healthintersections.com.au/tick.png"/>
                    </td>
                    <td align="center">
                        <img src="http://www.healthintersections.com.au/tick.png"/>
                    </td>
                </tr>
                
                */
		return c;
	}

}
