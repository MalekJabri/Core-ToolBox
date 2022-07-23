package com.ibm.mj.core.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MappingFieldsTool {
	/** The logger. */
	public static Logger logger = Logger.getLogger(MappingFieldsTool.class.getName());
	private String configFile;

	private Map<String ,String> table; 
	/***
	 * 
	 * Constructor
	 * 
	 * **/

	public MappingFieldsTool(String Paramfile) {
		try {
			System.out.println(Paramfile);
			configFile = Paramfile;
			FileInputStream in = new FileInputStream(configFile);
			Properties prop = new Properties();		
			prop.load(in);
			System.out.println(prop.getProperty("url"));
			in.close();
			extractInfo(prop);
			prop=null;
		}catch(Exception e){
			System.out.println(e.getMessage());
		}	
	}

	public MappingFieldsTool(Properties prop)  {

		try {
			configFile = null;		
			extractInfo(prop);	
			prop=null;
		}catch(Exception e){
			System.out.println(e.getMessage());
		}	
	}

	public MappingFieldsTool(InputStream in) throws IOException {
		Properties prop ;
		try {
			 prop = new Properties();
			prop.load(in);
			extractInfo(prop);

		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		finally {
			in.close();
			prop=null;
		}

	}

	private void extractInfo(Properties prop) {
		table =  new HashMap<String,String>();
		Enumeration<Object> ite = prop.keys();
		while(ite.hasMoreElements()){
			String attri = (String) ite.nextElement();
			if(!attri.startsWith("---"))			{
				table.put(attri, prop.getProperty(attri));
				System.out.println(attri +" -- "+ prop.getProperty(attri));
			}
		}
	}
	public String getMappingValue(String Field){
		return table.get(Field);
	}
	
	public String[] listofmetadat(){	
		String[] listof = new String[table.size()];
		Iterator<String> ite = table.keySet().iterator();
		int i=0;
		while(ite.hasNext())listof[i++]= ite.next();	
		return  listof;
	}


	/*
	 * 
	 * Methods
	 * 
	 * */
	public static void main(String[] args) {
		try {
			MappingFieldsTool p = new MappingFieldsTool("./config/mappMerge.properties");
			Set<String> test = p.table.keySet();
			Iterator<String> it = test.iterator();
			while(it.hasNext()){
				String name=  it.next();
				System.out.println(name + " ---- " + p.table.get(name));
			}
		} catch (Exception e) {
				logger.log(Level.SEVERE, "In The test an exception is occured", e);
		}
	}



	protected String validTheProp(Properties prop,String propertyName) throws Exception{
		String result =null;
		result = prop.getProperty(propertyName);
		if(result!=null&& result.length()>0) result=result.trim();
		return result;

	}

}
