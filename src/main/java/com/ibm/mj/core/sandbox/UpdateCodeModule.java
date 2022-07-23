package com.ibm.mj.core.sandbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.filenet.api.admin.CodeModule;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.events.EventAction;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;

public class UpdateCodeModule  {
	public static void updateCodeModule(ObjectStore os , String codeModulePath, String codeModuleName, String id) {
		try {
			// Create a File object for the JAR containing the updated event handler.
			File newHandlerVersion = new File(codeModulePath);
			// Create ContentTransfer object from updated JAR content.
			ContentElementList contentList = Factory.ContentTransfer.createList();
			ContentTransfer ctNew = Factory.ContentTransfer.createInstance();
			FileInputStream fileIS = new FileInputStream(newHandlerVersion.getAbsolutePath());
			
			ctNew.setCaptureSource(fileIS);
			ctNew.set_ContentType("application/java-archive");
			contentList.add(ctNew);

			// Check out current version of CodeModule object.
			CodeModule cm = Factory.CodeModule.getInstance(os, "/CodeModules/"+codeModuleName);
			cm.checkout(com.filenet.api.constants.ReservationType.EXCLUSIVE, null, null, null);
			cm.save(RefreshMode.REFRESH);

			// Get reservation object from the checked-out code module.
			// This will become the new version of the code module.
			CodeModule cmUpdate = (CodeModule) cm.get_Reservation();


			// Set the new content on the reservation object.
			cmUpdate.set_ContentElements(contentList);

			// Check in the new version of the code module.
			cmUpdate.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
			cmUpdate.save(RefreshMode.REFRESH);

			/////////////////////////////////////////////
			// Get event action to update its CodeModule property.
			PropertyFilter pf = new PropertyFilter();
			pf.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_OBJECT, new Integer(1) );
			pf.addIncludeProperty(0, null, Boolean.TRUE, PropertyNames.CODE_MODULE, new Integer(1) );
			EventAction eventAction = Factory.EventAction.fetchInstance(os, new Id(id), pf); 

			// Set CodeModule property.
			eventAction.set_CodeModule(cmUpdate);

			eventAction.save(RefreshMode.REFRESH); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateCodeModule(ObjectStore os , String[] codeModulePath, String codeModuleName, String id) {
		try {
			
			// Create ContentTransfer object from updated JAR content.
			ContentElementList contentList = Factory.ContentTransfer.createList();
			for(String path :codeModulePath) {
				ContentTransfer ctNew = Factory.ContentTransfer.createInstance();
				File newHandlerVersion = new File(path);
				FileInputStream fileIS = new FileInputStream(newHandlerVersion.getAbsolutePath());
				ctNew.set_RetrievalName(newHandlerVersion.getName());
				ctNew.setCaptureSource(fileIS);
				ctNew.set_ContentType("application/java-archive");
				contentList.add(ctNew);
			}

			// Check out current version of CodeModule object.
			CodeModule cm = Factory.CodeModule.getInstance(os, "/CodeModules/"+codeModuleName);
			cm.checkout(com.filenet.api.constants.ReservationType.EXCLUSIVE, null, null, null);
			cm.save(RefreshMode.REFRESH);

			// Get reservation object from the checked-out code module.
			// This will become the new version of the code module.
			CodeModule cmUpdate = (CodeModule) cm.get_Reservation();


			// Set the new content on the reservation object.
			cmUpdate.set_ContentElements(contentList);

			// Check in the new version of the code module.
			cmUpdate.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
			cmUpdate.save(RefreshMode.REFRESH);

			/////////////////////////////////////////////
			// Get event action to update its CodeModule property.
			PropertyFilter pf = new PropertyFilter();
			pf.addIncludeType(1, null, null, FilteredPropertyType.SINGLETON_OBJECT, new Integer(1) );
			pf.addIncludeProperty(0, null, Boolean.TRUE, PropertyNames.CODE_MODULE, new Integer(1) );
			EventAction eventAction = Factory.EventAction.fetchInstance(os, new Id(id), pf); 

			// Set CodeModule property.
			eventAction.set_CodeModule(cmUpdate);

			eventAction.save(RefreshMode.REFRESH); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
