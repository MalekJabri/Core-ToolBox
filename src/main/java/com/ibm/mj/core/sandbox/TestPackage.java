package com.ibm.mj.core.sandbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory.User;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.ibm.mj.core.ceObject.FolderTool;
import com.ibm.mj.core.p8Connection.ConnectionTool;
import filenet.pe.ce.wfeventhandler.WorkflowEventActionHandler;



public class TestPackage {
	

	static String configFile = "C:\\IBM\\config.properties";			
	static String DocumentID			= "{A94BB38A-4373-424A-9BF6-C181D33AE852}";

	public ObjectStore os =null;
	ArrayList<HashMap<String, Object>> outlines ;

	public TestPackage(String configFile) {
		init(configFile);
	}

	public static void main(String[] args) throws Exception {	
		TestPackage testPack = new TestPackage(configFile);			
		String[] Attributes = {"DocumentTitle"};
		String[] Values = {"test"};
testPack.SearchFolder(testPack.os);
	/*	DocumentSet docs = testPack.searchDocuments(testPack.os,"Document",Attributes,Values);
		Iterator ite = docs.iterator();
		while(ite.hasNext()){
			Document doc = (Document) ite.next();
			System.out.println(doc);
		}*/
		
		
		
		//testPack.test();
		//testPack.testMergingFolder(PATHFOLDER);
		//String[] listUser = {"p8admin","psmall","jdunn"};
		//testPack.listuser(listUser);
//		testPack.linkFolder2("{9E84B7E1-23A0-C76D-85EF-57C258600000}", "{DA4EB3CB-F86E-CB41-850D-58788B800000}");

		System.out.println("done");
	}



	private void listuser(String[] listUser) {		
		for(String worker : listUser){			
			com.filenet.api.security.User test = User.fetchInstance(os.getConnection(), worker, null);
			System.out.println(test.get_Email());
		}
	}

	public void init(String configFile){
		os = ConnectionTool.connectionPool(configFile,"p8admin","filenet","CMTOS");
	}

	
	public DocumentSet searchDocuments(ObjectStore os)
	{
		// Instantiate a search scope to search our object store
		SearchScope search = new SearchScope(os);
		// Instantiate an SQL object to hold our search criteria
		SearchSQL sql = new SearchSQL();
		// When searching, retrieve certain document
		sql.setSelectList("DocumentTitle, Description");
		// Search for all documents
		sql.setFromClauseInitialValue("Document", "d", true);
		// Search where customer number is equal to 12345
		sql.setWhereClause("DocumentTitle ='test'");
		// Perform search and create results set
		DocumentSet documents = (DocumentSet)search.fetchObjects(sql, new
				Integer(50),null, Boolean.valueOf(true));
		// Return results set object
		return documents;
	}
	
	public void SearchFolder(ObjectStore os)
	{
		// Instantiate a search scope to search our object store
		SearchScope search = new SearchScope(os);
		// Instantiate an SQL object to hold our search criteria
		SearchSQL sql = new SearchSQL();
		// When searching, retrieve certain document
		sql.setSelectList("FolderName");
		// Search for all documents
		sql.setFromClauseInitialValue("CRMS2_RIF", "f", true);
		// Search where customer number is equal to 12345
		//sql.setWhereClause("DocumentTitle ='Counterfeit'");
		// Perform search and create results set
		FolderSet folders = (FolderSet)search.fetchObjects(sql, new
				Integer(50),null, Boolean.valueOf(true));
		
	Iterator ite = folders.iterator();
	while(ite.hasNext()){
		Folder folder = (Folder) ite.next();
		System.out.println(folder.get_FolderName());
	}
		
	}

	public DocumentSet searchDocuments(ObjectStore os,String DocType, String[] Attributes , String[] Values)
	{
		// Instantiate a search scope to search our object store
		SearchScope search = new SearchScope(os);
		// Instantiate an SQL object to hold our search criteria
		SearchSQL sql = new SearchSQL();
		// When searching, retrieve certain document
		sql.setSelectList("DocumentTitle, Description");
		// Search for all documents
		sql.setFromClauseInitialValue(DocType, "d", true);
		// Search where customer number is equal to 12345
		StringBuffer whereClause= new StringBuffer();
		for(int i =0; i< Attributes.length;i++ ){
			whereClause.append(Attributes[i]).append(" LIKE '").append(Values[i]).append("'");
		}
		System.out.println(whereClause);
		sql.setWhereClause(whereClause.toString());
		// Perform search and create results set
		DocumentSet documents = (DocumentSet)search.fetchObjects(sql, new
				Integer(50),null, Boolean.valueOf(true));
		// Return results set object
		return documents;
	}

	public void test(){
		// Search for documents
		DocumentSet documents = searchDocuments(os);
		// Iterate through results set (documents) to retrieve each document

		@SuppressWarnings("rawtypes")
		Iterator it = documents.iterator();
		while (it.hasNext())
		{
			// Get the document item
			Document document = (Document)it.next();
			// Get document metadata attributes
			System.out.println(document.toString());			
			System.out.println(document.getObjectReference().getObjectIdentity());
		} 
	}


	public void testMergingFolder(String folderPath){
		Folder origin = FolderTool.getFolder(os, folderPath);
		DocumentSet documents = origin.get_ContainedDocuments();
		FolderSet folders = origin.get_SubFolders();
		@SuppressWarnings("rawtypes")
		Iterator iteDocs = documents.iterator();
		while(iteDocs.hasNext()){
			Document doc = (Document) iteDocs.next();
			System.out.println(doc.get_Name());
		}
		@SuppressWarnings("rawtypes")
		Iterator iteFolds = folders.iterator();		
		while(iteFolds.hasNext()){
			Folder fol = (Folder) iteFolds.next();
			System.out.println(fol.get_Name());
		}
	}


	public void linkFolder2(String toSynch, String Target){
		Folder folderToSynch = FolderTool.getFolder(os, toSynch);
		Folder FolderCase = FolderTool.getFolder(os,  "{90FBEDB9-F32A-CCC8-90B0-531163500000}");
		
	//	System.out.println(folderToSynch.get_Name());
		Folder folderTarget = FolderTool.getFolder(os, Target);
		System.out.println(folderTarget.get_Name());
		/**	FolderTool.LinkFolderToFolder2(os, folderToSynch, folderTarget);
		ReferentialContainmentRelationshipSet relationSet = folderTarget.get_Containers();
		folderTarget.get_CmHoldRelationships();
		ReferentialContainmentRelationshipSet relationSet2 = folderTarget.get_Containees();
		Iterator ite = relationSet.iterator();
		System.out.println("------------");
		while(ite.hasNext()){
			ReferentialContainmentRelationship relation=(ReferentialContainmentRelationship) ite.next();
			System.out.println(	relation.get_ContainmentName());
			//relation.delete();
			//relation.save(RefreshMode.REFRESH);
			
		}
		
		
		Folder newSubFolder = Factory.Folder.createInstance(os, "CmAcmCaseSubfolder");
		newSubFolder.set_Parent(FolderCase);
		newSubFolder.set_FolderName("Case Folder 2");
		newSubFolder.getProperties().putValue("CmAcmParentCase",FolderCase);
		newSubFolder.save(RefreshMode.REFRESH); **///
		FolderTool.LinkFolderToFolder2(os, folderToSynch, folderTarget);

	}


}
