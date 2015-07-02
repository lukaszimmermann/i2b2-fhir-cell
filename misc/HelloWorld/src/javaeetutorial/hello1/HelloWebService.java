package javaeetutorial.hello1;

import javax.jws.WebService; import javax.jws.WebMethod;

@WebService
public class HelloWebService {
	private final String message = "Hello, ";
	public HelloWebService() { }
	@WebMethod
	public String sayHello(String name) {
		return message + name + "."; }
}