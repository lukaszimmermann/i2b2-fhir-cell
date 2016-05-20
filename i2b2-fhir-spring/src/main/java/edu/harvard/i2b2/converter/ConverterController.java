package edu.harvard.i2b2.converter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import spittr.Spittle;
import spittr.data.SpittleRepository;
import spittr.web.SpittleForm;

@Controller
@RequestMapping("/bs")
public class ConverterController {

  private static final String MAX_LONG_AS_STRING = "9223372036854775807";
  
  private BundleStatusRepository repository;

  @Autowired
  public ConverterController(BundleStatusRepository repository) {
    this.repository = repository;
  }

  @RequestMapping(value="/mark_p/{pid}",method=RequestMethod.GET)
  public String markProcessing(
		  @PathVariable("pid") String pid) {
    repository.markProcessing(pid);
   return "redirect:/bs/"+pid;
    }
  
  @RequestMapping(value="/mark_c/{pid}",method=RequestMethod.GET)
  public String markComplete(
		  @PathVariable("pid") String pid) {
    repository.markComplete(pid);
   return "redirect:/bs/"+pid;
   
  }
  
  @RequestMapping(value="/{pid}",method=RequestMethod.GET)
  public String viewBundleStatus(
		  @PathVariable("pid") String pid, Model model) {
	  
	  model.addAttribute(repository.findOne(pid));
	  return "bundleStatus";
  }

 

}