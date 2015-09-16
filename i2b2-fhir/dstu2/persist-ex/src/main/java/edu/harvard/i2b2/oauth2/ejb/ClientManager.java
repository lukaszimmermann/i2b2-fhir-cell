package edu.harvard.i2b2.oauth2.ejb;

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.commons.lang.RandomStringUtils;

import edu.harvard.i2b2.oauth2.entity.Client;

@Named
@RequestScoped 
public class ClientManager {
	
	private Client client;
	
	

	@EJB
	ClientService service;
	
	@PostConstruct
	public void init(){
		client=new Client();
		client.setClientId(RandomStringUtils.randomAlphanumeric(20));
		client.setClientSecret(RandomStringUtils.randomAlphanumeric(20));
	}
	
	public void save(){
		if(service.find(client.getClientId())!=null){
			service.update(client);
		}else{
			service.create(client);
		}
		
	}
	
	public void update(){
		service.update(this.client);
	}
	
	public void delete(){
		service.delete(client);
	}

	public List<Client> list(){
		return service.list();
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	
}
