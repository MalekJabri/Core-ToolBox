package com.ibm.mj.core.ceObject;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DependentObjectList;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.VersionableSet;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.ComponentRelationshipType;
import com.filenet.api.constants.CompoundDocumentState;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.core.ComponentRelationship;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.DynamicReferentialContainmentRelationship;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.UpdatingBatch;
import com.filenet.api.core.VersionSeries;
import com.filenet.api.core.Versionable;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.ibm.mj.core.p8Connection.CEUtil;


public class DocumentTool {
	
	private static Logger logger = Logger.getLogger(DocumentTool.class.toString());
	public static String MIMETYPE_WORD_DOCX		= "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public static String MIMETYPE_WORD_DOC		= "application/msword";
	public static String MIMETYPE_MSG			= "application/vnd.ms-outlook";
	public static String MIMETYPE_PDF			= "application/pdf";
	public static String MIMETYPE_XML			= "application/xml";
	public static String MIMETYPE_EXCEL_XLS 	= "application/vnd.ms-excel";
	public static String MIMETYPE_EXCEL_XLSX	= "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static String MIMETYPE_PPOINT_PPT	= "application/vnd.ms-powerpoint";
	public static String MIMETYPE_PPOINT_PPTX	= "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	public static String MIMETYPE_TXT 			= "text/plain";
	public static String MIMETYPE_HTML 			= "text/html";
	public static String MIMETYPE_ZIP 			= "application/zip";
	public static String MIMETYPE_JAR			= "application/java-archive";

	public static Document copyDocument(ObjectStore os, Document  doc, String docName, boolean linkToFolder) {
		Document copy = null;	
		try {
			InputStream in = ContentTool.getDocumentStream(doc);
			copy = DocumentTool.createDocWithContent(in, os, docName, doc.getClassName());
			if(linkToFolder) {
				FolderSet folderSet = doc.get_FoldersFiledIn();
				@SuppressWarnings("unchecked")
				Iterator<Folder> ite = folderSet.iterator();
				while(ite.hasNext())
				{
						Folder folder = (Folder) ite.next();
						ReferentialContainmentRelationship rcr = folder.file(copy,
								AutoUniqueName.AUTO_UNIQUE, "New Document via Java API",
								DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
						rcr.save(RefreshMode.NO_REFRESH);
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
		return copy;
	}
	
	public static Document copyDocument(ObjectStore os, Document  doc, String docName,String docClassName, boolean linkToFolder) {
		Document copy = null;	
		try {
			InputStream in = ContentTool.getDocumentStream(doc);
			copy = DocumentTool.createDocWithContent(in, os, docName, docClassName);
			if(linkToFolder) {
				FolderSet folderSet = doc.get_FoldersFiledIn();
				@SuppressWarnings("unchecked")
				Iterator<Folder> ite = folderSet.iterator();
				while(ite.hasNext())
				{
						Folder folder = (Folder) ite.next();
						ReferentialContainmentRelationship rcr = folder.file(copy,
								AutoUniqueName.AUTO_UNIQUE, "New Document via Java API",
								DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
						rcr.save(RefreshMode.NO_REFRESH);
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE,e.getMessage());
		}
		return copy;
	}

	public static String getMimeType(String docname){
		String result =MIMETYPE_TXT;
		if(docname!=null && docname.length()>0 && docname.contains(".") ){
			String extension = docname.substring(docname.lastIndexOf("."), docname.length());
			if(extension.length()>0){
				if(extension.compareToIgnoreCase(".doc")==0) 	result= MIMETYPE_WORD_DOC;
				if(extension.compareToIgnoreCase(".docx")==0) 	result= MIMETYPE_WORD_DOCX;
				if(extension.compareToIgnoreCase(".pdf")==0) 	result= MIMETYPE_PDF;
				if(extension.compareToIgnoreCase(".xml")==0) 	result= MIMETYPE_XML;
				if(extension.compareToIgnoreCase(".xls")==0) 	result= MIMETYPE_EXCEL_XLS;
				if(extension.compareToIgnoreCase(".xlsx")==0) 	result= MIMETYPE_EXCEL_XLSX;
				if(extension.compareToIgnoreCase(".ppt")==0) 	result= MIMETYPE_PPOINT_PPT;
				if(extension.compareToIgnoreCase(".txt")==0) 	result= MIMETYPE_TXT;
				if(extension.compareToIgnoreCase(".pptx")==0) 	result= MIMETYPE_PPOINT_PPTX;	
				if(extension.compareToIgnoreCase(".html")==0) 	result= MIMETYPE_HTML;	
				if(extension.compareToIgnoreCase(".zip")==0) 	result= MIMETYPE_ZIP;
				if(extension.compareToIgnoreCase(".jar")==0) 	result= MIMETYPE_JAR;
			}			
		}		
		return result;
	}

	public static Document createCoumpountDocWithContent(InputStream in,  ObjectStore os, String docName, String docClass, Folder folder)
	{
		Document doc = null;
		if (docClass.equals(""))
			doc = Factory.Document.createInstance(os, null);
		else
			doc = Factory.Document.createInstance(os, docClass);
		doc.getProperties().putValue("DocumentTitle", docName);
		doc.set_MimeType(getMimeType(docName));
		ContentElementList cel = ContentTool.getContentlist(in, docName);
		if (cel != null)
			doc.set_ContentElements(cel);
		doc.set_CompoundDocumentState(CompoundDocumentState.COMPOUND_DOCUMENT);
		doc.checkin(null, CheckinType.MAJOR_VERSION); 
		doc.save(RefreshMode.REFRESH);
		LinkDocToFolder(os, folder, doc);
		return doc;
	}

	public static Document createCoumpountDoc(  ObjectStore os, String docName, String docClass, Folder folder)
	{
		Document doc = null;
		if (docClass.equals(""))
			doc = Factory.Document.createInstance(os, null);
		else
			doc = Factory.Document.createInstance(os, docClass);
		doc.getProperties().putValue("DocumentTitle", docName);
		doc.set_CompoundDocumentState(CompoundDocumentState.COMPOUND_DOCUMENT);
		doc.checkin(null, CheckinType.MAJOR_VERSION); 
		doc.save(RefreshMode.REFRESH);
		LinkDocToFolder(os, folder, doc);
		return doc;
	}

	public static void linkSubCompoundDoc(Document parent, Document child, int order, ObjectStore os)
	{
		ComponentRelationship cr = Factory.ComponentRelationship.createInstance(os, null);
		cr.set_ParentComponent(parent);
		cr.set_ChildComponent(child);
		cr.set_ComponentSortOrder(new Integer(order));
		cr.set_ComponentRelationshipType(
				ComponentRelationshipType.STATIC_CR);            
		cr.save(RefreshMode.REFRESH);

	}

	public static void LinkDocToFolder(ObjectStore os, Folder folder,	Document doc) {
		DynamicReferentialContainmentRelationship rcr = 
				Factory.DynamicReferentialContainmentRelationship.createInstance(os, null, 
						AutoUniqueName.AUTO_UNIQUE, 
						DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.set_Tail(folder);
		rcr.set_Head(doc);
		rcr.set_ContainmentName("Linked Folder");
		rcr.save(RefreshMode.REFRESH);
	}





	@SuppressWarnings({"unchecked" })
	public static String createDocByBatch( ObjectStore os, String filename, InputStream in, Folder folder)
	{
		ContentTransfer ct = Factory.ContentTransfer.createInstance();
		ct.setCaptureSource(in);
		ct.set_RetrievalName(filename);
		ct.set_ContentType(getMimeType(filename));
		ContentElementList cel = Factory.ContentElement.createList();
		cel.add(ct);
		Document doc = Factory.Document.createInstance(os, null);
		doc.getProperties().putValue("DocumentTitle", filename);  
		doc.set_ContentElements(cel);
		doc.checkin(null, CheckinType.MAJOR_VERSION);  		
		DynamicReferentialContainmentRelationship rcr = 
				Factory.DynamicReferentialContainmentRelationship.createInstance(os, null, 
						AutoUniqueName.AUTO_UNIQUE, 
						DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.set_Tail(folder);
		rcr.set_Head(doc);
		rcr.set_ContainmentName("this is a link");
		UpdatingBatch ub = UpdatingBatch.createUpdatingBatchInstance(os.get_Domain(), 
				RefreshMode.REFRESH);
		ub.add(doc, null);
		ub.add(rcr, null);
		ub.updateBatch();
		return doc.get_Id().toString();
	}

	public static Document createDocWithContent(InputStream in, ObjectStore os, String docName, String docClass)
	{
		Document doc = null;
		if (docClass.equals(""))	doc = Factory.Document.createInstance(os, null);
		else	doc = Factory.Document.createInstance(os, docClass);
		doc.getProperties().putValue("DocumentTitle", docName);
		doc.set_MimeType(getMimeType(docName));
		ContentElementList cel = ContentTool.getContentlist(in, docName);
		if (cel != null)
			doc.set_ContentElements(cel);
		doc.checkin(null, CheckinType.MAJOR_VERSION); 
		doc.save(RefreshMode.REFRESH);
		return doc;
	}

	public static Document createDocWithContent(InputStream in,  ObjectStore os, String docName, String docClass, Folder folder)
	{
		Document doc = null;
		if (docClass.equals(""))
			doc = Factory.Document.createInstance(os, null);
		else
			doc = Factory.Document.createInstance(os, docClass);

		doc.getProperties().putValue("DocumentTitle", docName);
		doc.set_MimeType(getMimeType(docName));
		ContentElementList cel = ContentTool.getContentlist(in, docName);
		if (cel != null)
			doc.set_ContentElements(cel);		
		doc.checkin(null, CheckinType.MAJOR_VERSION); 
		doc.save(RefreshMode.REFRESH);
		LinkDocToFolder(os, folder, doc);
		return doc;
	}

	/*
	 * Creates the Document with content from supplied file.
	 */
	public static Document createDocWithContent(File file,  ObjectStore os, String docName, String docClass)
	{
		Document doc = null;
		if (docClass==null || docClass.equals(""))
			doc = Factory.Document.createInstance(os, null);
		else
			doc = Factory.Document.createInstance(os, docClass);
		doc.getProperties().putValue("DocumentTitle", docName);
		doc.set_MimeType(getMimeType(docName));
		ContentElementList cel = CEUtil.createContentElements(file);
		if (cel != null)
			doc.set_ContentElements(cel);
		doc.checkin(null, CheckinType.MAJOR_VERSION);
		doc.save(RefreshMode.REFRESH);
		doc.refresh();
		return doc;
	}

	public static Document createDocWithContent(File file,  ObjectStore os, String docName, String docClass, Folder folder)
	{
		Document doc = null;
		if (docClass==null || docClass.equals(""))
			doc = Factory.Document.createInstance(os, null);
		else
			doc = Factory.Document.createInstance(os, docClass);
		doc.getProperties().putValue("DocumentTitle", docName);
		doc.set_MimeType(getMimeType(docName));
		ContentElementList cel = CEUtil.createContentElements(file);
		if (cel != null)
			doc.set_ContentElements(cel);
		doc.checkin(null, CheckinType.MAJOR_VERSION);
		doc.save(RefreshMode.REFRESH);
		doc.refresh();
		LinkDocToFolder(os, folder, doc);
		return doc;
	}
	
	public static Document createDocWithContent(File file,  ObjectStore os, String docName, String docClass, Folder folder, HashMap<String, String> propertiesMap,HashMap<String, Integer> propertiesIntMap)
	{
		Document doc = importDoc(file, os, docName, docClass, propertiesMap,propertiesIntMap);
		LinkDocToFolder(os, folder, doc);
		return doc;
	}
	
	public static Document createDocWithContent(File file,  ObjectStore os, String docName, String docClass, Folder folder, HashMap<String, String> propertiesMap)
	{
		return createDocWithContent( file,   os,  docName,  docClass,  folder, propertiesMap,  null);
	}
	
	
	public static Document importDoc(File file, ObjectStore os,String docName, String docClass,HashMap<String, String> propertiesMap,HashMap<String, Integer> propertiesIntMap) {
		Document doc = null;
		if (docClass==null || docClass.equals(""))
			doc = Factory.Document.createInstance(os, null);
		else
			doc = Factory.Document.createInstance(os, docClass);
		Properties properties = doc.getProperties();
		properties.putValue("DocumentTitle", docName);
		if(propertiesMap!=null) {
		for(String propertyName : propertiesMap.keySet()){
			logger.log(Level.SEVERE,"add properties "+ propertyName + "  "+ properties.isPropertyPresent(propertyName) );
			properties.putValue(propertyName, propertiesMap.get(propertyName));
		} }
		if(propertiesIntMap!=null) {
		for(String propertyName : propertiesIntMap.keySet()) {
			logger.log(Level.SEVERE,"add properties "+ propertyName + "  "+ properties.isPropertyPresent(propertyName) );
			 properties.putValue(propertyName, propertiesIntMap.get(propertyName));
		}	}
		doc.set_MimeType(getMimeType(docName));
		ContentElementList cel = CEUtil.createContentElements(file);
		if (cel != null)
			doc.set_ContentElements(cel);
		doc.checkin(null, CheckinType.MAJOR_VERSION);
		doc.save(RefreshMode.REFRESH);
		doc.refresh();
		return doc;
	}
	
	
	/*
	 * Creates the Document without content.
	 */
	public static Document createDocNoContent( ObjectStore os, String docName, String docClass)
	{
		Document doc = null;
		if (docClass==null || docClass.equals(""))
			doc = Factory.Document.createInstance(os, null);
		else
			doc = Factory.Document.createInstance(os, docClass);
		doc.getProperties().putValue("DocumentTitle", docName);
		doc.set_MimeType(getMimeType(docName));
		return doc;
	}

	/*
	 * Retrives the Document object specified by path.
	 */
	public static Document fetchDocByPath(ObjectStore os, String path)
	{
		Document doc = Factory.Document.fetchInstance(os, path, null);
		return doc;
	}

	/*
	 * Retrives the Document object specified by id.
	 */
	public static Document fetchDocById(ObjectStore os, String id)
	{
		return fetchDocById(os, id,null);
	}

	
	public static Document fetchDocById(ObjectStore os, String id, PropertyFilter properties )
	{
		Document doc =null;

		Id id1 = new Id(id);
		
		try{
			doc = Factory.Document.fetchInstance(os, id1, null);
			if(!doc.isCurrent()){			
				Document currentDoc = (Document) doc.get_VersionSeries().get_CurrentVersion();				
				doc = Factory.Document.fetchInstance(os, currentDoc.get_Id(), properties);
			}
		}catch(EngineRuntimeException e){			
			if(e.toString().contains("E_OBJECT_NOT_FOUND")){
				VersionSeries versionSeries = (VersionSeries) os.fetchObject("VersionSeries", id,null);		
				Document currentDoc = (Document) versionSeries.get_CurrentVersion();				
				doc = Factory.Document.fetchInstance(os, currentDoc.get_Id(), properties);				
			}			
		}
		return doc;
	}

	/*
	 * Checks in the Document object.
	 */
	public static void checkinDoc(Document doc)
	{
		doc.checkout(ReservationType.EXCLUSIVE, null, null, null);
		doc.save(RefreshMode.REFRESH);
		doc = (Document) doc.get_Reservation();
		doc.checkin(null, CheckinType.MAJOR_VERSION);
		doc.save(RefreshMode.REFRESH);
		doc.refresh();
	}


	public static void markCurrentVerison(Document doc, Boolean major, String version)
	{

		@SuppressWarnings("unused")
		VersionableSet versionableSet = doc.get_Versions();


	}

	@SuppressWarnings("unchecked")
	public static void checkinDocwithSameContent(Document doc)
	{
		DependentObjectList contentlist = doc.get_ContentElements();
		@SuppressWarnings("unused")
		Versionable vers = doc.get_CurrentVersion();


		if(!doc.get_CurrentVersion().get_IsReserved())	{
			doc.checkout(ReservationType.EXCLUSIVE, null, null, null);
			doc.save(RefreshMode.REFRESH);
			doc = (Document) doc.get_Reservation();
		}

		ContentElementList cel =  Factory.ContentElement.createList();
		cel.addAll(contentlist);
		doc.set_ContentElements(cel);
		doc.checkin(null, CheckinType.MAJOR_VERSION);
		//doc.set_MimeType(getMimeType(doc.get_Name()));
		//doc.getProperties().putValue("DocumentTitle", doc.get_Name());
		doc.save(RefreshMode.REFRESH);

	}

	public static String checkinDoc(Document doc, InputStream content, String docName, Boolean Major){
		String ID =  doc.get_Id().toString();
		if(!doc.isLocked()){
			doc.checkout(ReservationType.EXCLUSIVE, null, null, null);
			doc.save(RefreshMode.REFRESH);}
		doc = (Document) doc.get_Reservation();
		ContentElementList cel = ContentTool.getContentlist(content, docName);
		if (cel != null)
			doc.set_ContentElements(cel);
		CheckinType checkinVersion = CheckinType.MINOR_VERSION;
		if(Major)  checkinVersion = CheckinType.MAJOR_VERSION;
		doc.checkin(null, checkinVersion);
		doc.set_MimeType(getMimeType(docName));
		doc.getProperties().putValue("DocumentTitle", docName);
		doc.save(RefreshMode.REFRESH);
		doc.refresh();	
		ID = doc.get_VersionSeries().get_Id().toString();
		return ID;
	}

	public static String checkinModule(Document doc, InputStream content, String docName, Boolean Major){
		String ID =  doc.get_Id().toString();
		if(!doc.isLocked()){
			doc.checkout(ReservationType.EXCLUSIVE, null, null, null);
			doc.save(RefreshMode.REFRESH);}
		doc = (Document) doc.get_Reservation();
		ContentElementList cel = ContentTool.getContentlist(content, docName);
		if (cel != null)
			doc.set_ContentElements(cel);
		CheckinType checkinVersion = CheckinType.MINOR_VERSION;
		if(Major)  checkinVersion = CheckinType.MAJOR_VERSION;
		doc.checkin(null, checkinVersion);
		doc.set_MimeType(MIMETYPE_JAR);
		doc.getProperties().putValue("DocumentTitle", docName);
		doc.save(RefreshMode.REFRESH);
		doc.refresh();	
		ID = doc.get_VersionSeries().get_Id().toString();
		return ID;
	}
	public static Document getDocument(ObjectStore os, String value){	
		Document doc = null;
		if(Id.isId(value)){
			doc = fetchDocById(os, value);			 
		}else{
			doc = fetchDocByPath(os,value);
		}
		return doc;
	}
}
