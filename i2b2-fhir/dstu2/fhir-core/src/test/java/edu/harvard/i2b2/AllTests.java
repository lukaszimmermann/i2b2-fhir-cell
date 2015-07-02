package edu.harvard.i2b2;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ AllTests.class, MetaResourceDbTest.class,
		PdoEGtoFhirBundle.class, SearchParameterMapTest.class, WSi2b2.class,
		xmlIOMetaResourceSet.class })

public class AllTests {

	
}
