package tutorial.hello4;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("myresource")
public class MyResource {

	@GET
	public String get(){
		return "Hello";
	}
}
