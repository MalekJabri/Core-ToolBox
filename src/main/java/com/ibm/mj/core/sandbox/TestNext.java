package com.ibm.mj.core.sandbox;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Factory;
import com.ibm.mj.core.ToolsBox;
import com.ibm.mj.core.sandbox.tester.testerClass;

public class TestNext extends testerClass{
	
	private static Logger logger = Logger.getLogger(TestNext.class);
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);
		TestNext tn = new TestNext();
		tn.getAction();
	}

	@Override
	public void getAction() { 
		
	System.out.println(	ToolsBox.getNext(getOs(), "CRMS2_RIF"));
	CustomObject dispenser = Factory.CustomObject.fetchInstance(os, "/config/"+"CRMS2_RIF", null);
	}

}
