package edu.harvard.i2b2.fhirServerApi.jba.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/trim")
	public String index(){
		//return "Hi from index controller";
		System.out.println("got TRIM");
		return "/WEB-INF/jsp/index.jsp";
	}
}
