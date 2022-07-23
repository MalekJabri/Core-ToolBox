package com.ibm.mj.core.model;

import lombok.Data;

@Data
public class CounterDetails {
	public static final String COUNTER_CLASS="CounterObject";
	public static final String COUNTER_PREFIX="CounterPrefix";
	public static final String COUNTER_ATTRIBUTE_TARGET="CounterAttribute";
	public static final String COUNTER_NAME="CounterName";
	public static final String COUNTER_NEXT_ID_ATTRIBUTE="Count";
	
	private String attributeName ="";
	private int next = 0;
	private String prefix = "";
	
}
