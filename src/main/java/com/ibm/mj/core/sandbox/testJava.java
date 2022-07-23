package com.ibm.mj.core.sandbox;

import java.util.HashMap;

public class testJava {
	
	public static void main(String[] args) {
		HashMap<String, Integer> propertiesIntMap = new HashMap<String, Integer>();
		for (int i = 0; i < 4; i++) {
			propertiesIntMap.put("tedt"+i, i);
			
		}
		
		for(String key : propertiesIntMap.keySet()) {
			System.out.println(key + "  -->" + propertiesIntMap.get(key));
		}
	}

}
