package com.ibm.mj.core.eventHandler;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.ibm.mj.core.ceObject.ContentTool;
import com.ibm.mj.core.ceObject.DocumentTool;
import com.ibm.mj.core.ceObject.FolderTool;

import mj.document.openpdf.model.PdfProfiles;
import mj.document.toolBox.ProfilesService;



public class MoveToCaseEventHandler implements com.filenet.api.engine.EventActionHandler {
	Logger log =  Logger.getLogger( FolderTool.class.toString());
	
	private final static String CASEID= "cu_caseID";
	private final static String CU_TITLE= "cu_title";
	private final static String NB_PAGES  ="cu_nbPage";
	private final static String NB_CARAC  ="cu_caractereCount";
	private final static String CASE_MOVES_STATUS = "cu_moveStatus";
	private final static String CASEOS= "tos";
	private final static String DOC_CLASS_NAME= "cu_document";
	private final static String PATH = "/tmp/copyCrossOS/";
	private final static String[] attributes = {CASEID, CASE_MOVES_STATUS, CU_TITLE};
	private final static String[] attributesString = {CU_TITLE};
	
	@Override
	public void onEvent(ObjectChangeEvent event, Id id) throws EngineRuntimeException {
		log.log(Level.INFO, "HANDLER STARTED 2");
		log.log(Level.INFO,"Move Begin");
		PropertyFilter propertyFilter =  new PropertyFilter();
		for(String prop : attributes) {
			FilterElement caseIDProperties =	new FilterElement(null,null,null,prop,null);
			propertyFilter.addIncludeProperty(caseIDProperties);
		}
		log.log(Level.INFO,"Get all the objectstore ");
		// Get Different object Store 
		ObjectStore os = event.getObjectStore();
		PropertyFilter propertyFilterOS =  new PropertyFilter();
		os.fetchProperties(propertyFilterOS);
		ObjectStore	osCase	=Factory.ObjectStore.fetchInstance(os.get_Domain(), CASEOS, null);	
		log.log(Level.INFO,"END all the  objectstore");
		// Get Doc and Folder ID
		Id idDoc = event.get_SourceObjectId();
		Document doc = DocumentTool.fetchDocById(os, idDoc.toString(),propertyFilter );

		// Retrieve Properties
		log.log(Level.INFO,"Init and all the Object Store are ready   -- > " + idDoc);	
		log.log(Level.INFO,"END all the  objectstore");
		Properties properties = doc.getProperties();
		String CaseID = properties.getStringValue(CASEID);
		boolean move = properties.getBooleanValue(CASE_MOVES_STATUS);
		if(CaseID != null && CaseID.length()>0 && !move) {
			File file = null;
			HashMap<String, String> propertiesMap = new HashMap<String, String>();
			for(String prop : attributesString) {
				if(properties.isPropertyPresent(prop)) propertiesMap.put(prop, properties.getStringValue(prop));
			}
			
			try {
				log.log(Level.INFO,"Start Moving Document");
				init(idDoc.toString());
				log.log(Level.INFO,"create temp folder");
				file = new File(PATH+idDoc.toString());
				Folder folder = FolderTool.getFolderbyID(osCase, CaseID);
				log.log(Level.INFO,"Import Document locally");
				ContentTool.writeDocContentToFileID(doc, PATH);
				// Get doc Details 
				PdfProfiles profile = null;
				if(doc.get_MimeType().equalsIgnoreCase(DocumentTool.MIMETYPE_PDF)) profile = ProfilesService.getPdfProfiles(file.getAbsolutePath());
				if(doc.get_MimeType().equalsIgnoreCase(DocumentTool.MIMETYPE_WORD_DOCX)) profile = ProfilesService.getWordProfiles(file.getAbsolutePath());
				HashMap<String, Integer> propertiesIntMap = new HashMap<String, Integer>();
			    if(profile!=null) {
			    	propertiesIntMap.put(NB_CARAC,profile.getNbCaracteres() );
				    propertiesIntMap.put(NB_PAGES,profile.getNbpages() );
				    properties.putValue(NB_CARAC, profile.getNbCaracteres());
					properties.putValue(NB_PAGES, profile.getNbpages());
			    }
				DocumentTool.createDocWithContent(file, osCase, doc.get_Name(),DOC_CLASS_NAME, folder, propertiesMap, propertiesIntMap);
				properties.putValue(CASE_MOVES_STATUS, true);
				doc.save(RefreshMode.NO_REFRESH);
			} catch (Exception e) {
				e.printStackTrace();
				log.log(Level.SEVERE,e.getMessage());
				throw new EngineRuntimeException();		
			}finally {
				log.log(Level.INFO,"Content deleted from the local folder");
				file.delete();

			}
		}else {
			if(!move) log.log(Level.INFO,"No case ID defined");
			else log.log(Level.INFO,"the document has been already moved to the case folder");
		}

		log.log(Level.SEVERE,"End Move : Doc ");
	}

	private void init( String docName ) {
		if(PATH!=null) {
			// check folder or create it
			File folder = new File(PATH);
			if(!folder.exists()) folder.mkdirs();
			File file = new File(PATH+docName);	
			if(file.exists()) {
				file.delete();
			}
		}
	}
}
