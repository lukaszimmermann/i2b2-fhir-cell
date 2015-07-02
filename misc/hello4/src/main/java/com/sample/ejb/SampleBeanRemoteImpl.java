
package com.sample.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.sample.ejb.SampleBeanRemote;
 
@Stateless
@Remote(SampleBeanRemote.class) 
public class  SampleBeanRemoteImpl implements SampleBeanRemote  {
 
    @Override
    public String echo(String s) {
 
        return "Hello "+s;
    }
 
 
}