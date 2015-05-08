package harvard.i2b2.fhir.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyResource {
	String id;
	String name;
	String data;
	
	public MyResource() {
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
