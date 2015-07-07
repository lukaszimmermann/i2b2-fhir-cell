package edu.harvard.i2b2.fhir.map;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.JAXBUtil;
import edu.harvard.i2b2.fhir.Utils;
import edu.harvard.i2b2.fhir.XQueryUtil;
import edu.harvard.i2b2.fhir.XQueryUtilException;
import edu.harvard.i2b2.map.*;

public class MapSystemEngine {
	static Logger logger = LoggerFactory.getLogger(MapSystemEngine.class);
	MapSystemSet mss;
	
	public MapSystemEngine(String xml) throws JAXBException, IOException{
		//read config file
		mss=JAXBUtil.fromXml(xml,MapSystemSet.class);
		//iterate through MapSystem and create output Xml
		logger.info(JAXBUtil.toXml(mss));
	}
	
	public String runMapSystem(String input) throws XQueryUtilException{
		String outputXml=null;
		
		//input=XQueryUtil.processXQuery(Utils.getFile("transform/I2b2ToFhir/i2b2ObservationToUniqueWithDistinctId.xquery"), input);
		logger.trace("input:"+input);
		
		for(MapSystem ms:mss.getMapSystem()){
			String outputObjXml="<a></a>";
			String ns=getXQueryNameSpaceDeclaration(ms);
			String query=ns+"/"+ms.getFromPath().getPath();
			logger.trace("query:"+query);
			String aValue=XQueryUtil.processXQuery(query, input);
			logger.trace("aValue:"+aValue);
			
			outputObjXml=getNested(outputObjXml,ms.getToPath().getPath(),ns);
			
			
			outputObjXml=getNested(outputObjXml,"f:Patient/gender/Coding/Code",ns);
			
			outputXml+=outputObjXml;
		}
		
		
		return outputXml;
	}
	
	static public String getXQueryNameSpaceDeclaration(MapSystem ms){
		String msg="";
			for(NameSpaceDeclaration n:ms.getNameSpaceDeclaration()){
				msg+="declare namespace "+n.getName()+"=\""+n.getUri()+"\";";
			}
		return msg;
	}
	static public String appendPath(String inputXml,String path,String nameSpaceDeclaration) throws XQueryUtilException{
		List<String> pathList= Arrays.asList(path.split("/"));
		logger.trace(pathList.toString());
		String last=pathList.get(pathList.size()-1);
		List<String> woAttrList=(last.matches("@.*"))? pathList.subList(0, pathList.size()-1):pathList;
		String modPath= woAttrList.toString().replace(",", "/").replace("[", "").replace("]", "").replace(" ", "");
		String resXml=getNested(inputXml,modPath,nameSpaceDeclaration);
		logger.trace("resXml:"+resXml);
		logger.trace("addAttr:"+addAttribute(resXml,modPath));
		return resXml;
	}
	
	static public String getNested(String inputXml,String path,String nameSpaceDeclaration) throws XQueryUtilException{
		String query=getNestedQuery(inputXml,path,nameSpaceDeclaration);
logger.trace("query:"+query);
		return XQueryUtil.processXQuery(query, inputXml);
	}
	
	static public String getNestedQuery(String inputXml,String path,String nameSpaceDeclaration){
		return nameSpaceDeclaration+
				Utils.getFile("transform/modules/dynamicPath.xquery").replace("module", "declare")
				+ "let $P:=<a></a>\n"
				+"let $path:=\"Patient/maritalStatus/Coding/Code\"\n"
				+"return dp:overwrite-path("+inputXml+", tokenize(\""+path+"\",\"/\"))";
	
	}
	
	static  public String addAttribute(String inputXml,String path) throws XQueryUtilException{
		String query =" copy $c :="+inputXml
				+"modify (insert node (attribute { 'a' } { 5 }, 'text', <e/>) into $c/a/"+path+") return $c";
		logger.trace("insert query:"+query);
		return XQueryUtil.processXQuery(query, inputXml);
	}
	
	
	
	
}
