package edu.harvard.i2b2.fhirServerApi.jba.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.harvard.i2b2.fhirServerApi.jba.entity.BlogRepository;
import edu.harvard.i2b2.fhirServerApi.jba.entity.ItemRepository;
import edu.harvard.i2b2.fhirServerApi.jba.entity.Role;
import edu.harvard.i2b2.fhirServerApi.jba.entity.RoleRepository;
import edu.harvard.i2b2.fhirServerApi.jba.entity.User;
import edu.harvard.i2b2.fhirServerApi.jba.entity.UserRepository;

@Service
public class InitDbService {

	@Autowired
	private RoleRepository roleRespository;

	@Autowired
	private UserRepository userRespository;

	@Autowired
	private BlogRepository blogRespository;

	@Autowired
	private ItemRepository itemRespository;
	
	@PostConstruct
	public void init(){
		Role roleAdmin=new Role();
		roleAdmin.setName("ROLE_ADMIN");
		roleRespository.save(roleAdmin);
	
		Role roleUser=new Role();
		roleUser.setName("ROLE_USER");
		roleRespository.save(roleUser);
		
		User userAdmin=new User();
		userAdmin.setName("admin");
		List <Role> roles=new ArrayList<Role>();
		roles.add(roleAdmin);
		roles.add(roleUser);
		userAdmin.setRoles(roles);
	}
	

}
