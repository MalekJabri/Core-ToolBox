package com.ibm.mj.core.eventHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.ibm.mj.core.ToolsBox;
import com.ibm.mj.core.ceObject.DocumentTool;
import com.ibm.mj.core.model.CounterDetails;

public class CounterDocEventHandler   implements com.filenet.api.engine.EventActionHandler {

	private Logger logger =   Logger.getLogger(this.getClass().toString());

	@Override
	public void onEvent(ObjectChangeEvent event, Id id) throws EngineRuntimeException {
		logger.log(Level.SEVERE, "Counter Test");
		ObjectStore os = event.getObjectStore();
		Id idDoc = event.get_SourceObjectId();
		logger.info("Listed id doc   -- > " + idDoc);
		Document doc = DocumentTool.getDocument(os, idDoc.toString()); 
		String referenceAttribute = ToolsBox.getReferenceAttribute(os, doc.getClassName());
		FilterElement counterProperty1 =	new FilterElement(null,null,null,referenceAttribute,null);
		PropertyFilter propertyFilter =  new PropertyFilter();
		propertyFilter.addIncludeProperty(counterProperty1);
		doc.fetchProperties(propertyFilter);
		logger.info("the reference attribute is yy "+referenceAttribute);
		// get current reference
		String docReference = doc.getProperties().getStringValue(referenceAttribute);
		logger.info("the reference value is "+docReference);
		if(docReference ==null || docReference.length()==0) {
		CounterDetails result = ToolsBox.getNextDetails(os,doc.getClassName());
		doc.getProperties().putValue(result.getAttributeName(), result.getPrefix()+result.getNext());
		doc.save(RefreshMode.NO_REFRESH);
		logger.severe("Update Reference ended Using counter : "+result);
		}
	}
}
