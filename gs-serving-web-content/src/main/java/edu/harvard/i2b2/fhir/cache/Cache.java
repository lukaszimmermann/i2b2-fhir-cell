package edu.harvard.i2b2.fhir.cache;

import java.util.Arrays;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import edu.harvard.i2b2.fhir.converter.Converter;

@Component
public class Cache {

	static Logger logger = LoggerFactory.getLogger(Cache.class);

	public static void put(String xml) {
		final String uri = "http://localhost:8090//hapi-fhir-jpaserver-example/baseDstu2/Patient/example";
		logger.info("Will put:" + xml);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response=null;
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/xml+fhir");
		headers.set("Accept", "application/xml+fhir");
		HttpEntity<String> entity = new HttpEntity<String>(xml, headers);
		try {
			ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
			logger.debug(result.toString());
		} catch (HttpClientErrorException e) {
			logger.error("error is:"+e.getMessage());
			logger.error("error detail:"+e.getResponseBodyAsString());
			
		}

	}

	public static String get(String uri) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/xml+fhir");
		headers.set("Accept", "application/xml+fhir");
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		logger.debug(result.toString());
		return result.getBody().trim();
	}
}
