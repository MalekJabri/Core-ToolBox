package com.ibm.mj.core.sandbox;

import com.ibm.mj.core.sandbox.tester.testerClass;

public class TestConnection extends testerClass{


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestConnection ts = new TestConnection();
		System.out.println(ts.getOs().get_SymbolicName());
		ts.getAction();
	}


	@Override
	public void getAction() {
		System.out.println("test");
	}


}
