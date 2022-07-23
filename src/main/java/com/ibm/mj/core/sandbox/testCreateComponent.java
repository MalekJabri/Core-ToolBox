package com.ibm.mj.core.sandbox;

import java.util.ArrayList;
import java.util.HashMap;

import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.util.Id;
import com.ibm.mj.core.ceObject.DocumentTool;
import com.ibm.mj.core.ceObject.FolderTool;
import com.ibm.mj.core.sandbox.tester.testerClass;

public class testCreateComponent extends testerClass{

	private static final String DocumentID = null;
	ArrayList<HashMap<String, Object>> outlines ;	

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {	
		testCreateComponent testPack = new testCreateComponent();
	//	testPack.CreateTG();
	//	testPack.createComponent();
		//testPack.linkSub();
		System.out.println("done");
	}

	@Override
	public void getAction() {
		// TODO Auto-generated method stub
		
	}

	public void createComponent(){
		String objectID = "{3697C2FC-5BE1-4B14-A35F-6F3462302C85}";
		Document child = DocumentTool.getDocument(getOs(), DocumentID);
		Folder folder = FolderTool.getFolderbyID(getOs(), new Id(objectID));
		Document parent = DocumentTool.createCoumpountDoc(getOs(), "Ceci est un test ", "Document", folder)	;
		DocumentTool.linkSubCompoundDoc(parent, child, 0, os);
	}
	public void linkSub(){
		Document child = DocumentTool.getDocument(getOs(), "{6C805616-7819-C148-840F-647F67100000}");
		System.out.println(child.get_Name());
		Document parent = DocumentTool.getDocument(getOs(), "{0D4DE01D-E529-C4C5-87B2-647F5E600000}");
		System.out.println(parent.get_Name());
		DocumentTool.linkSubCompoundDoc(parent, child, 1, os);
	}

	public void  createDocumentID(){
		Document doc =  DocumentTool.getDocument(getOs(), DocumentID);
		DocumentTool.checkinDocwithSameContent(doc);
		
	}

}
