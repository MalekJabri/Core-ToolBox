package com.ibm.mj.core.configOperations;

import java.util.ArrayList;
import java.util.List;

public class MethodInformation {
	
	String Name ;
	String Description;
	List<MethodAttributes> attributes ;
	
	public MethodInformation(String name, String description) {
		Name = name;
		Description = description;
		attributes = new ArrayList<MethodAttributes>();
	}
	
	
	public void setAtttributes(String name, String valueExpr, String type, boolean isArray, boolean isInMode){		
		MethodAttributes attribute = new MethodAttributes(name, valueExpr, type, isArray, isInMode);
	//	System.out.println(name);
	//	System.out.println(valueExpr);
	//	System.out.println(type);
	//	System.out.println(isArray);
		attributes.add(attribute);		
	}


	public String getName() {
		return Name;
	}


	public String getDescription() {
		return Description;
	}


	public List<MethodAttributes> getAttributes() {
		return attributes;
	}

	 
	
}
