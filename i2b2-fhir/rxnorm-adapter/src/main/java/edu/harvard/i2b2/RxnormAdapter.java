package edu.harvard.i2b2;

import java.util.HashMap;

import com.sun.deploy.uitoolkit.impl.fx.Utils;


public class RxnormAdapter {
	HashMap<Integer,Integer> NdcToRxCuiMap;
	public RxnormAdapter(){
		init();
	}
	
	public void init(){
		NdcToRxCuiMap= new HashMap<Integer,Integer>();
	}
	
}
