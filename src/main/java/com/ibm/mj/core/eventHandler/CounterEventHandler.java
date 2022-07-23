package com.ibm.mj.core.eventHandler;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.ibm.mj.core.ToolsBox;
import com.ibm.mj.core.ceObject.ContentTool;
import com.ibm.mj.core.ceObject.DocumentTool;
import com.ibm.mj.core.model.CounterDetails;

import mj.document.openpdf.model.PdfProfiles;
import mj.document.toolBox.ProfilesService;

public class CounterEventHandler   implements com.filenet.api.engine.EventActionHandler {

	private Logger logger =   Logger.getLogger(this.getClass().toString());
	private final static String PATH = "/tmp/copyCrossOS/";
	private final static String NB_PAGES  ="cu_nbPage";
	private final static String NB_CARAC  ="cu_caractereCount";

	@Override
	public void onEvent(ObjectChangeEvent event, Id id) throws EngineRuntimeException {
		logger.log(Level.SEVERE, "Counter Test");
		ObjectStore os = event.getObjectStore();
		Id idDoc = event.get_SourceObjectId();
		logger.info("Listed id doc   -- > " + idDoc);
		Document doc = DocumentTool.getDocument(os, idDoc.toString());
		// Retrieve Reference Object : 
		String referenceAttribute = ToolsBox.getReferenceAttribute(os, doc.getClassName());
		FilterElement counterProperty1 =	new FilterElement(null,null,null,referenceAttribute,null);
		PropertyFilter propertyFilter =  new PropertyFilter();
		propertyFilter.addIncludeProperty(counterProperty1);
		doc.fetchProperties(propertyFilter);
		logger.info("the reference attribute is xx "+referenceAttribute);
		// get current reference
		String docReference = doc.getProperties().getStringValue(referenceAttribute);
		logger.info("the reference value is "+docReference);
		Properties properties = doc.getProperties();
		if(docReference ==null || docReference.length()==0) {
			CounterDetails result = ToolsBox.getNextDetails(os,doc.getClassName());
			logger.severe(result.toString());
			properties.putValue(result.getAttributeName(), result.getPrefix()+result.getNext());
			
		logger.severe("Update Reference ended Using counter : "+result);
		}
		
		//Count Page and Character :
		
		File file = new File(PATH+doc.get_Id().toString());
		logger.log(Level.INFO,"Import Document locally + "+PATH+doc.get_Id().toString());
		try {
			ContentTool.writeDocContentToFileID(doc, PATH);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Get PDF Details 
		PdfProfiles pdfProfile = null;
		if(doc.get_MimeType().equalsIgnoreCase(DocumentTool.MIMETYPE_PDF)) pdfProfile = ProfilesService.getPdfProfiles(file.getAbsolutePath());
		if(doc.get_MimeType().equalsIgnoreCase(DocumentTool.MIMETYPE_WORD_DOCX)) pdfProfile = ProfilesService.getWordProfiles(file.getAbsolutePath());
		
	  if(pdfProfile!=null) {
		logger.log(Level.SEVERE,"Profile of the documents: " + pdfProfile);
		logger.log(Level.INFO,"Content have been imported in the local folder");
		properties.putValue(NB_CARAC, pdfProfile.getNbCaracteres());
		properties.putValue(NB_PAGES, pdfProfile.getNbpages());
		}else {
			logger.info("The profile is null");
		}
	  	
		doc.save(RefreshMode.NO_REFRESH);
		file.delete();
	}
}
