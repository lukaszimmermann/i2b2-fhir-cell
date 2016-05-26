package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import hello.CustomerRepository;

@Controller
@RequestMapping("/greeting")
public class GreetingController {

	
	private static final Logger log = LoggerFactory.getLogger(GreetingController.class);
	
	@Autowired
	CustomerRepository repository;
	
	 @RequestMapping("/a")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
    	System.out.println(">>>CALLED");
        model.addAttribute("name", name);
        repository.save(new Customer("Jack", "Bauer"));
        for (Customer customer : repository.findAll()) {
			log.info(customer.toString());
			System.out.println(customer.toString());
		}
        return "greeting";
    }
    
    @RequestMapping("/b")
    public String some(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
    	System.out.println(">>>CALLED SOME");
        
        return "some";
    }

}
