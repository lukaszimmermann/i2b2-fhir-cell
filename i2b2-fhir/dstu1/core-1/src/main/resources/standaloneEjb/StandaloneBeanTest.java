/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */
package standaloneEjb;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import org.junit.After;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ian
 */
public class StandaloneBeanTest {

    private EJBContainer ec;
    private Context ctx;
    private static final Logger logger = Logger.getLogger("standalone.ejb");

    public StandaloneBeanTest() {
    }

    @Before
    public void setUp() {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put("org.glassfish.ejb.embedded.glassfish.installation.root", 
    	"/Users/***REMOVED***/local/opt/glassfish4/glassfish");
    	properties.put(EJBContainer.APP_NAME, "app");

        ec = EJBContainer.createEJBContainer(properties);
        ctx = ec.getContext();
    }

    @After
    public void tearDown() {
        if (ec != null) {
            ec.close();
        }
    }

    /**
     * Test of returnMessage method, of class StandaloneBean.
     * @throws java.lang.Exception
     */
    //@Test
    public void testReturnMessage() throws Exception {
        logger.info("Testing standalone.ejb.StandaloneBean.returnMessage()");
        StandaloneBean instance = 
                (StandaloneBean) ctx.lookup("java:global/classes/StandaloneBean");
        String expResult = "Greetings!";
        String result = instance.returnMessage();
        assertEquals(expResult, result);
    }
}
