package edu.harvard.i2b2.fhirserver.ws;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHelper {
	static Logger logger = LoggerFactory.getLogger(HttpHelper.class);

	static public URI getBasePath(HttpServletRequest request)
			throws URISyntaxException {
		String uri = request.getScheme()
				+ "://"
				+ request.getServerName()
				+ ("http".equals(request.getScheme())
						&& request.getServerPort() == 80
						|| "https".equals(request.getScheme())
						&& request.getServerPort() == 443 ? "" : ":"
						+ request.getServerPort())
				+ request.getRequestURI()
				+ (request.getQueryString() != null ? "?"
						+ request.getQueryString() : "");
		if(uri.contains("?")) uri=uri.split("\\?")[0];
		logger.trace("full uri:" + uri);
		uri = uri.substring(0, uri.lastIndexOf('/')) + "/";
		logger.trace("base uri:" + uri);
		return new URI(uri);
	}
}
