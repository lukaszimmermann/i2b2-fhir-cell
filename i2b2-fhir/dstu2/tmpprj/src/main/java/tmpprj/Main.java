package tmpprj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.i2b2.loinc.Utils;



public class Main {
	static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args){
		logger.trace("p:"+Main.class.getResource("/validation-min.xml.zip").getPath());
	} 
}
