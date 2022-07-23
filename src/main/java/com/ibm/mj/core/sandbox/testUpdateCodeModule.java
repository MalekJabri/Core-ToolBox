package com.ibm.mj.core.sandbox;

import com.ibm.mj.core.sandbox.tester.testerClass;

public class testUpdateCodeModule extends testerClass{

	

	public static void main(String[] args) {
		  
		String uriPath ="/Users/be07463/Dev/Tools/FileNet_Config/config_just.properties";
		String userFile ="config/cpe_just.properties";
		testUpdateCodeModule test = new testUpdateCodeModule(uriPath, userFile);
		test.getAction();
	}
	
	public testUpdateCodeModule(String uriPath, String userFile) {
		super(uriPath);
	}
	
	@Override
	public void getAction() {
		System.out.println(os.get_Name());
	//  Get Count 
   //   String idEvent = "{F407B04C-CFED-4116-8353-81DCAF6357BC}";
		
	//  Move document to Case 
		String idEvent = "{20576431-F066-47A0-8A15-6F06D76A6442}";
		
		String[] path = {"target/CoreP8.jar",
				"target/alternateLocation/PdfAndWord-1.2.jar",
				"target/alternateLocation/openpdf-1.3.13.jar", 
				"target/alternateLocation/poi-4.1.2.jar", 
				"target/alternateLocation/poi-ooxml-4.1.2.jar" , 
				"target/alternateLocation/poi-ooxml-schemas-4.1.2.jar",
				"target/alternateLocation/commons-compress-1.19.jar",
				"target/alternateLocation/xmlbeans-3.1.0.jar",
				};
		
		UpdateCodeModule.updateCodeModule(os, path, "MJ Tools", idEvent);
		System.out.println("end");
		
	}

}
