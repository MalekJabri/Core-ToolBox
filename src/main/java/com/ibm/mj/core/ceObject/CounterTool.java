package com.ibm.mj.core.ceObject;
import com.filenet.api.admin.*;
import com.filenet.api.core.ObjectStore;
import com.ibm.mj.core.model.CounterDetails;

public class CounterTool {
	
	public static void createCounterClass(ObjectStore os) {
		
		 ClassDefinition counterClass =   ObjectDefinitionTool.CreateClassDefinition(os,"CustomObject",CounterDetails.COUNTER_CLASS, CounterDetails.COUNTER_CLASS);
		 ObjectDefinitionTool.addStringPropertyDefinition(os,counterClass,CounterDetails.COUNTER_PREFIX,CounterDetails.COUNTER_PREFIX,false);
		 ObjectDefinitionTool.addStringPropertyDefinition(os,counterClass,CounterDetails.COUNTER_ATTRIBUTE_TARGET,CounterDetails.COUNTER_ATTRIBUTE_TARGET,false);
		 ObjectDefinitionTool.addStringPropertyDefinition(os,counterClass,CounterDetails.COUNTER_NAME,CounterDetails.COUNTER_NAME,true);
		 ObjectDefinitionTool.addIntPropertyDefinition(os,counterClass,CounterDetails.COUNTER_NEXT_ID_ATTRIBUTE,CounterDetails.COUNTER_NEXT_ID_ATTRIBUTE);
		 FolderTool.createFolder(os, "/", "config", "Folder");
	}
}
