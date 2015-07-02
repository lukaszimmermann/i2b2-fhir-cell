package edu.edu.i2b2;

import javaeetutorial.helloservice.endpoint.HelloWebService;
import javaeetutorial.helloservice.endpoint.HelloWebServiceService; 
import javaeetutorial.helloservice.endpoint.SayHello;

import javax.xml.ws.WebServiceRef;

public class HelloAppClient { 
	@WebServiceRef(wsdlLocation ="http://localhost:8080/helloservice-war/HelloService?WSDL") 
	private static HelloWebServiceService service;
/**
* @param args the command line arguments */
	
	
	private static String sayHello(java.lang.String arg0) { 
		HelloWebService port =
			service.getHelloWebServicePort(); 
		return port.sayHello(arg0);
	}
	
	public static void main(String[] args) { 
		System.out.println(sayHello("world"));
	}
}
