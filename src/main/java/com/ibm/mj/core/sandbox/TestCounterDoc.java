package com.ibm.mj.core.sandbox;

import com.ibm.mj.core.ToolsBox;
import com.ibm.mj.core.sandbox.tester.testerClass;

public class TestCounterDoc  extends testerClass  implements Runnable{
	String name = "classname";
	public static void main(String[] args) {
		Runnable counter = new TestCounterDoc();
		for(int i =0;i<10;i++) {
			Thread tst = new Thread(counter);
			tst.start();
		}
	}
	@Override
	public void getAction() {
		ToolsBox.getNext(os, "CRMS2_RIF");	
		}

	@Override
	public void run() {		
		getAction();

	}

}
