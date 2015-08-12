package edu.harvard.i2b2.fhir;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class I2b2Util {
	static Logger logger = LoggerFactory.getLogger(I2b2Util.class);

	public static String insertI2b2ParametersInXml(String xml, String username,
			String password, String i2b2domain, String i2b2domainUrl)
			throws XQueryUtilException {

		xml = replaceXMLString(xml, "//security/username", username);
		xml = replaceXMLString(xml, "//security/password", password);
		xml = replaceXMLString(xml, "//security/domain", i2b2domain);
		xml = replaceXMLString(xml, "//proxy/redirect_url", i2b2domainUrl
				+ "/services/QueryToolService/pdorequest");
		// logger.info("returning xml:"+xml);
		return xml;
	}
	
	public static String insertI2b2ParametersAuthTokenInXml(String xml, String username,
			String authToken, String i2b2domain, String i2b2domainUrl)
			throws XQueryUtilException {
		xml = replaceXMLString(xml, "//security/username", username);
		xml = replaceXMLString(xml, "//security/password", authToken);
		xml = replaceXMLString(xml, "//security/domain", i2b2domain);
		xml = replaceXMLString(xml, "//proxy/redirect_url", i2b2domainUrl
				+ "/services/QueryToolService/pdorequest");
		// logger.info("returning xml:"+xml);
		xml=xml.replace("<password>", "<password is_token=\"true\">");
		return xml;
	}

	private static String replaceXMLString(String xmlInput, String path,
			String value) throws XQueryUtilException {
		String query = "copy $c := root()\n"
				+ "modify ( replace value of node $c" + path + " with \""
				+ value + "\")\n" + " return $c";
		logger.trace("query:" + query);
		return XQueryUtil.processXQuery(query, xmlInput);
	}

	
	
	    
	public static String getAuthorizationToken(String pmResponseXml)
			throws XQueryUtilException {
		return XQueryUtil
				.processXQuery("//user/password/text()", pmResponseXml);

	}

	public static String getPmResponseXml(String username, String password,
			String i2b2domain, String i2b2domainUrl) throws XQueryUtilException {
		String requestStr = Utils.getFile("i2b2query/getServices.xml");
		requestStr = insertI2b2ParametersInXml(requestStr, username, password,
				i2b2domain, i2b2domainUrl);
		logger.trace("requestStr:"+requestStr);
		String oStr = WebServiceCall.run(i2b2domainUrl
				+ "/services/PMService/getServices", requestStr);
		return oStr;
	}

	public static String getPmResponseXmlWithAuthToken(String username,String authToken,
			String i2b2domain, String i2b2domainUrl) throws XQueryUtilException {
		String requestStr = Utils.getFile("i2b2query/getServices.xml");
		requestStr = insertI2b2ParametersAuthTokenInXml(requestStr,username, authToken,
				i2b2domain, i2b2domainUrl);
		logger.trace("requestStr:"+requestStr);
		String oStr = WebServiceCall.run(i2b2domainUrl
				+ "/services/PMService/getServices", requestStr);
		return oStr;
	}

	public static boolean authenticateUser(String pmResponseXml) throws XQueryUtilException {
		String loginStatusquery = "//response_header/result_status/status/@type/string()";
		String loginError = XQueryUtil.processXQuery(loginStatusquery,
				pmResponseXml);
		logger.trace("ERROR?:<" + loginError + ">");

		return (loginError.equals("ERROR")) ? false : true;

	}

	public static List<String> getUserProjects(String pmResponseXml)
			throws XQueryUtilException {
		return XQueryUtil.getStringSequence("//user/project/@id/string()",
				pmResponseXml);

	}
}
