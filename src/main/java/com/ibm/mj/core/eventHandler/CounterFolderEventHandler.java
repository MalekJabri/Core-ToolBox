package com.ibm.mj.core.eventHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.util.Id;
import com.ibm.mj.core.ToolsBox;
import com.ibm.mj.core.ceObject.FolderTool;
import com.ibm.mj.core.model.CounterDetails;

public class CounterFolderEventHandler   implements com.filenet.api.engine.EventActionHandler {

	private Logger logger =   Logger.getLogger(this.getClass().toString());

	@Override
	public void onEvent(ObjectChangeEvent event, Id id) throws EngineRuntimeException {
		logger.log(Level.SEVERE, "Counter Test");
		ObjectStore os = event.getObjectStore();
		Id idDoc = event.get_SourceObjectId();
		logger.info("Listed id doc   -- > " + idDoc);
		Folder folder = FolderTool.getFolder(os, idDoc.toString());	
		String[] test = null;
		folder.fetchProperties(test);
		String folderReference = folder.getProperties().getStringValue(ToolsBox.getReferenceAttribute(os, folder.getClassName()));
		if(folderReference == null || folderReference.length() == 0 ) {
			CounterDetails result = ToolsBox.getNextDetails(os,folder.getClassName());
			folder.getProperties().putValue(result.getAttributeName(),result.getPrefix()+result.getNext());
			folder.save(RefreshMode.NO_REFRESH);
			logger.severe("Update Reference ended Using counter : "+result);
		}
	}
}
