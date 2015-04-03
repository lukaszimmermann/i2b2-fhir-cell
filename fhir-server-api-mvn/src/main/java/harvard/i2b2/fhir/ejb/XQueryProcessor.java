package harvard.i2b2.fhir.ejb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import javax.ejb.Stateless;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQConstants;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import net.sf.saxon.xqj.SaxonXQDataSource;

import org.xml.sax.SAXException;

@Stateless
public class XQueryProcessor {

	public static String processXquery(String query, String input)  {
		System.out.println("query:"+query.substring(0,(query.length()>200)?200:0));
		System.out.println("input:"+input.substring(0,(input.length()>200)?200:0));
		try{
		XQDataSource ds = new SaxonXQDataSource();
		XQConnection xqjc = ds.getConnection();
		XQPreparedExpression xqje = //xqjc.prepareExpression(new FileInputStream("/Users/kbw19/git/res/xjctestmvn/src/main/resources/transform/I2b2ToFhir/i2b2MedsToFHIRMeds.xquery"));
				xqjc.prepareExpression(new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8)));
				
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader streamReader = //factory.createXMLStreamReader(new FileReader("/Users/kbw19/git/res/xjctestmvn/src/main/resources/example/i2b2/i2b2medspod.txt"));
				factory.createXMLStreamReader(new StringReader(input));
		xqje.bindDocument(XQConstants.CONTEXT_ITEM,streamReader, xqjc.createDocumentType());

		XQResultSequence xqjs  = xqje.executeQuery();

		//xqjs.writeSequence(System.out, null);
		return xqjs.getSequenceAsString(null);
		}catch( XQException| XMLStreamException e){
			e.printStackTrace();
		}
		return null;
	}
}
