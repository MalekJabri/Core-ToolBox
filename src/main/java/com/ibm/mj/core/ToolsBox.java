package com.ibm.mj.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;

import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.ibm.casemgmt.api.Case;
import com.ibm.casemgmt.api.Comment;
import com.ibm.casemgmt.api.constants.CommentContext;
import com.ibm.casemgmt.api.constants.ModificationIntent;
import com.ibm.mj.core.ceObject.CustomObjectTool;
import com.ibm.mj.core.ceObject.FolderTool;
import com.ibm.mj.core.model.CounterDetails;

public class ToolsBox {
	

	private static Logger logger = Logger.getLogger(ToolsBox.class.toString());
	public static String statcifiled ="test";
	/* Convert outputStream to an InputStream version*/
	public static InputStream OutToInStream (ByteArrayOutputStream out) throws Exception{
		//descirption
		InputStream isFromFirstData = new ByteArrayInputStream(out.toByteArray()); 
		out.close();
		return isFromFirstData;
	}
	public static DocumentSet searchDocuments(ObjectStore os, String DocumentClass, String properties, String value)
	{

		SearchScope search = new SearchScope(os);

		SearchSQL sql = new SearchSQL();

		sql.setSelectList("*");

		sql.setFromClauseInitialValue(DocumentClass, "d", true);

		sql.setWhereClause(properties+ "="+"'"+value+"'");

		DocumentSet documents = (DocumentSet)search.fetchObjects(sql,
				Integer.valueOf(50),null, Boolean.valueOf(true));

		return documents;
	}
	
	public static IndependentObjectSet  searchObjects(ObjectStore os, String objectClass, String properties, String value)
	{
		SearchScope search = new SearchScope(os);
		SearchSQL sql = new SearchSQL();
		sql.setSelectList("ID");
		sql.setFromClauseInitialValue(objectClass, "o", true);
		if(properties!=null && !properties.isEmpty())	sql.setWhereClause(properties+ "="+"'"+value+"'");
		IndependentObjectSet objects = search.fetchObjects(sql,
				Integer.valueOf(50),null, Boolean.valueOf(true));
		return objects;
	}
	
	
	
	public static FolderSet searchFolders(ObjectStore os, String FolderClass, String properties, String value)
	{

		SearchScope search = new SearchScope(os);

		SearchSQL sql = new SearchSQL();

		sql.setSelectList("*");

		sql.setFromClauseInitialValue(FolderClass, "d", true);
		if(properties!=null && !properties.isEmpty())	sql.setWhereClause(properties+ "="+"'"+value+"'");

		FolderSet folders = (FolderSet)search.fetchObjects(sql, 	Integer.valueOf(50),null, Boolean.valueOf(true));

		return folders;
	}
	public static Id getID(String idString) {
		Id id = new Id(idString);
		return id;
	}
	/* Get the outputFolder form a string and a case location*/
	public static Folder getOutputFolder(String folderOut, Folder caseFolder, ObjectStore os) throws Exception {
		Folder output  =null;
		if(folderOut==null || folderOut.length()==0){
			output =caseFolder;
		}else if(folderOut.startsWith("{")){			
			output = FolderTool.getFolder(os, folderOut);
		}else if(!folderOut.startsWith("/")){
			folderOut= caseFolder.get_PathName() + "/" + folderOut ;
			output = FolderTool.getFolder(os, folderOut);
		}else{
			output= FolderTool.getFolder(os, folderOut);			
		}
		return output;
	}
	/*  Add Comment to a case*/
	public static String addCommentToCase(String message, Case cs) throws Exception {
		Comment cmt = cs.addCaseComment(CommentContext.CASE, message );
		cs.save(RefreshMode.REFRESH, null, ModificationIntent.MODIFY);
		return cmt.getId().toString();
	}
	
	//test
	public  Document[] getStatus(String Name, boolean[] moldu, Case cs, int integer , float number){
		
		return null;
	}	
	
	public static int getNext(ObjectStore os, String className) {
		CounterDetails resutl = getNextDetails(os, className);
		return resutl.getNext();
				
	}
	
	public static String getReferenceAttribute(ObjectStore os, String className) {
		String result = null;
		CustomObject dispenser = null;
		FilterElement counterProperty1 =	new FilterElement(null,null,null,CounterDetails.COUNTER_PREFIX,null);
		FilterElement counterProperty2 =	new FilterElement(null,null,null,CounterDetails.COUNTER_ATTRIBUTE_TARGET,null);
		FilterElement counterProperty3 =	new FilterElement(null,null,null,CounterDetails.COUNTER_NEXT_ID_ATTRIBUTE,null);
		PropertyFilter propertyFilter =  new PropertyFilter();
		propertyFilter.addIncludeProperty(counterProperty1);
		propertyFilter.addIncludeProperty(counterProperty2);
		propertyFilter.addIncludeProperty(counterProperty3);
		try { 
			dispenser = Factory.CustomObject.fetchInstance(os, "/config/"+className, propertyFilter);	
			result = dispenser.getProperties().getStringValue(CounterDetails.COUNTER_ATTRIBUTE_TARGET);
			dispenser = null;
		}catch (EngineRuntimeException e) {
			logger.warning("Counter is not found for the class "+className);
		}
		
		return result;
	}
	
	public static CounterDetails getNextDetails(ObjectStore os, String className ) {
		CounterDetails details = new CounterDetails();
		CustomObject dispenser = null;
		FilterElement counterProperty1 =	new FilterElement(null,null,null,CounterDetails.COUNTER_PREFIX,null);
		FilterElement counterProperty2 =	new FilterElement(null,null,null,CounterDetails.COUNTER_ATTRIBUTE_TARGET,null);
		FilterElement counterProperty3 =	new FilterElement(null,null,null,CounterDetails.COUNTER_NEXT_ID_ATTRIBUTE,null);
		PropertyFilter propertyFilter =  new PropertyFilter();
		propertyFilter.addIncludeProperty(counterProperty1);
		propertyFilter.addIncludeProperty(counterProperty2);
		propertyFilter.addIncludeProperty(counterProperty3);
		
		int compt = -1;
		try { 
			dispenser = Factory.CustomObject.fetchInstance(os, "/config/"+className, propertyFilter);			
		}catch (EngineRuntimeException e) {
			logger.warning("Counter is not found for the class "+className);
		}
		if(dispenser==null) {
			logger.warning("Create new counter for the class "+className);
			Folder folder = FolderTool.getFolderbyPath(os, "/config");	
			dispenser = Factory.CustomObject.createInstance(os,CounterDetails.COUNTER_CLASS);
			Properties dispenserProperties = dispenser.getProperties();
			dispenserProperties.putValue(CounterDetails.COUNTER_NAME, className);		
			dispenserProperties.putValue(CounterDetails.COUNTER_NEXT_ID_ATTRIBUTE, 0);
			dispenser.save(RefreshMode.REFRESH);			
			CustomObjectTool.LinkToFolder(os, folder, dispenser);
			compt=-1;
			logger.info("New counter saved and linked to Config directory for the class "+className);
		}else {
			for(int i =0;i<50;i++) {
				logger.info("Counter Exist for the class: "+className);
				try {				
					if(dispenser.isLocked()) {
						logger.info("Counter is locked by another process for the class: "+className);
						dispenser= Factory.CustomObject.fetchInstance(os, "/config/"+className, propertyFilter);				
					}
					else{
						logger.severe("Counter is locked for the class: "+className);
						dispenser.lock(10, null);
						dispenser.save(RefreshMode.REFRESH);
						dispenser.fetchProperties(propertyFilter);
						Properties dispenserProperties = dispenser.getProperties();
						try {
						compt  = dispenserProperties.getInteger32Value(CounterDetails.COUNTER_NEXT_ID_ATTRIBUTE);	
						}catch(Exception e ) {
							System.out.println("the id is null set 0");
							
						}
						compt++;
						dispenserProperties.putValue(CounterDetails.COUNTER_NEXT_ID_ATTRIBUTE,compt);					
						logger.info("Counter is update to the value "+compt+" for the class: "+className);
						dispenser.unlock();
						details.setAttributeName(dispenserProperties.getStringValue(CounterDetails.COUNTER_ATTRIBUTE_TARGET));
						details.setPrefix(dispenserProperties.getStringValue(CounterDetails.COUNTER_PREFIX));
						dispenser.save(RefreshMode.NO_REFRESH);
						break;					
					}

				}catch (EngineRuntimeException e ) {
					try {
						dispenser = Factory.CustomObject.fetchInstance(os, "/config/"+className, null);
						Thread.sleep(5);
						logger.warning("Counter failed due to another process: "+className+ " -- Loop-- "+i);
						logger.info(e.getMessage());
					} catch (InterruptedException e1) {						
						e1.printStackTrace();
					}
				}
			}
		}
		if(compt>=0) details.setNext(compt);
		return details;
	}
}
