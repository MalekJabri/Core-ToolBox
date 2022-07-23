package com.ibm.mj.core.configOperations;


public class Operation {
	String OperationsName;
	String Path;
	String PathJavaClass;
	
	public Operation(String operationsName,	String path,String pathJavaClass) {		
		OperationsName = operationsName;
		Path  = path + "/" + operationsName + ".xml" ;
		PathJavaClass = pathJavaClass;
		
	}	

	public String getOperationsName() {
		return OperationsName;
	}

	public String getPath() {
		return Path;
	}

	public String getPathJavaClass() {
		return PathJavaClass;
	}
	
	
}
