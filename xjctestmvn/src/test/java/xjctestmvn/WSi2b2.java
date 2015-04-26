package xjctestmvn;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.junit.Test;

import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtil;

public class WSi2b2 {

	//@Test
	public void test() {
String request = Utils.getFile("i2b2query/i2b2RequestMedsForAPatient.xml");
String query = Utils
.getFile("transform/I2b2ToFhir/i2b2MedsToFHIRMedStatement.xquery");

		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client
				.target("http://services.i2b2.org:9090/i2b2/services/QueryToolService/pdorequest");
		String oStr = webTarget.request().accept("Context-Type","application/xml").post(
				Entity.entity(request, MediaType.APPLICATION_XML), String.class);
		String xQueryResultString = XQueryUtil.processXQuery(query,  oStr);
		System.out.println(xQueryResultString);
	}
	
	@Test
	public void test2() {
		String request = Utils.getFile("i2b2query/i2b2RequestMedsForAPatient.xml");
		String query="declare namespace ns3=\"http://www.i2b2.org/xsd/cell/crc/pdo/1.1/\";"
				+"copy $c :=//fn:root() "
				+"modify("
				+"replace value of node $c//message_body/ns3:request/input_list/patient_list/patient_id with 'BaseX'"
				+ ")"
				+" return $c";
				//"replace value of node //message_body/ns3:request/input_list/patient_list/patient_id/text() with '222' "+
				//" //message_body/ns3:request/input_list/patient_list/patient_id ";
		//String query="replace value of node /n with 'newValue'";
		String xQueryResultString = XQueryUtil.processXQuery(query,  request);
		System.out.println(xQueryResultString);
	}
	
	static void writeFileBytes(String filename, String content) {
        try {
        	Files.write( FileSystems.getDefault().getPath(".", filename), 
                         content.getBytes(), 
                         StandardOpenOption.CREATE);
        }
        catch ( IOException ioe ) {
            ioe.printStackTrace();
        }
    }

}
