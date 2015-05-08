package standaloneEjb;

import javax.ejb.Stateless;

@Stateless
public class StandaloneBean {
	private static final String message = "Greetings!";

	public String returnMessage() {
		return message;
	}
}
