package com.ibm.mj.core.configOperations;

public class MethodAttributes {

	public String ATTRIBUTES_STRING = "string";
	public String ATTRIBUTES_BOOLEAN = "boolean";
	public String ATTRIBUTES_INTEGER = "int";
	public String ATTRIBUTES_FLOAT = "float";	
	public String ATTRIBUTES_ATTACHMENTS = "attachment";
	public String ATTRIBUTES_TIME = "time";
	
	String Name;
	String ValueExpr;
	String Type;
	String IsArray;
	String Mode;
	
	public MethodAttributes(String name, String valueExpr, String type, boolean isArray, boolean isInMode ) {
		Name = name.trim();
		ValueExpr = valueExpr;
		if(type.equals("VWAttachment")) Type = ATTRIBUTES_ATTACHMENTS;
		else if(type.equals("Date"))	Type = ATTRIBUTES_TIME;
		else Type = type.toLowerCase();
		if(isArray) IsArray ="true"; else IsArray = "false";
		if(isInMode) Mode="in"; else Mode ="out";
	}

	public String getName() {
		return Name;
	}

	public String getValueExpr() {
		return ValueExpr;
	}

	public String getType() {
		return Type;
	}

	public String getIsArray() {
		return IsArray;
	}

	public String getMode() {
		return Mode;
	}
	
	
}
