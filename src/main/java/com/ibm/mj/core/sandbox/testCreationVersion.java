package com.ibm.mj.core.sandbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.filenet.api.core.CmAbstractPersistable;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Document;
import com.filenet.api.core.ObjectStore;
import com.ibm.mj.core.ceObject.AbstractPersistableTool;
import com.ibm.mj.core.ceObject.DocumentTool;
import com.ibm.mj.core.p8Connection.ConnectionTool;



public class testCreationVersion {

	
	static String configFile = "/Users/be07463/Dev/Tools/FileNet_Config/config.properties";			
	static String DocumentID	= "{83BBD1AF-F0CF-C16F-847B-64719C200000}";

	private ObjectStore os =null;
	private  Connection conn =null;
	ArrayList<HashMap<String, Object>> outlines ;

	public testCreationVersion(String configFile) {
		init(configFile);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {	
		testCreationVersion testPack = new testCreationVersion(configFile);
	//	testPack.CreateTG();
		testPack.createDocumentID();
		
		System.out.println("done");
	}

	public  void CreateTG() {
		List<String> attributes = new ArrayList<String>();
		attributes.add("CmAsaSubject");		
		List<String> values = new ArrayList<String>();
		values.add("Ceci est un test");
		Map<String,String> list =  new HashMap<String,String>();
		CmAbstractPersistable notification = AbstractPersistableTool.createAbstractWithProperties(getOs(), "CmAsaNotification", attributes, values);	
	}

	public void  createDocumentID(){
		Document doc =  DocumentTool.getDocument(getOs(), DocumentID);
		DocumentTool.checkinDocwithSameContent(doc);
		
	}

	public void init(String configFile){
		ConnectionTool cTool = new ConnectionTool(configFile,"p8admin","filenet","ECM");
		setOs(cTool.getOs());
		setConn(cTool.getConnection());

	}

	public Connection getConn() {
		return conn;
	}

	public  void setConn(Connection conn) {
		this.conn = conn;
	}

	public ObjectStore getOs() {
		return os;
	}

	public void setOs(ObjectStore os) {
		this.os = os;
	}
}
