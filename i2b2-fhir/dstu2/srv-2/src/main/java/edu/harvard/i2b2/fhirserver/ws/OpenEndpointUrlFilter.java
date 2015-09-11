package edu.harvard.i2b2.fhirserver.ws;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.fhir.FhirUtil;


/*
 * Allows bypass of authentication
 */
@Provider
@PreMatching
@Priority(2)
public class OpenEndpointUrlFilter implements ContainerRequestFilter {
	static Logger logger = LoggerFactory.getLogger(OpenEndpointUrlFilter .class);
	
 		@Override
	public void filter(ContainerRequestContext context) throws IOException {
 			
 			UriInfo uriInfo=context.getUriInfo();
		String pathStr=uriInfo.getAbsolutePath().toString();
		logger.info("got uri::"+pathStr);
		pathStr=pathStr.replace("open/", "");
		URI uri=null;
		try {
			uri = new URI(pathStr);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.equals(e);
			e.printStackTrace();
			
		}
		logger.info("rewriting open url to:"+uri.toString());
		context.setRequestUri(uri);
		
	}

}