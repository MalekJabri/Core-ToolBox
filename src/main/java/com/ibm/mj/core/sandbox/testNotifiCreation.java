package com.ibm.mj.core.sandbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.filenet.api.core.CmAbstractPersistable;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ObjectStore;
import com.ibm.mj.core.ceObject.AbstractPersistableTool;
import com.ibm.mj.core.p8Connection.ConnectionTool;



public class testNotifiCreation {

	
	static String configFile = "C:\\IBM\\config.properties";			
	static String DocumentID			= "{A94BB38A-4373-424A-9BF6-C181D33AE852}";

	private ObjectStore os =null;
	private  Connection conn =null;
	ArrayList<HashMap<String, Object>> outlines ;

	public testNotifiCreation(String configFile) {
		init(configFile);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {	
		testNotifiCreation testPack = new testNotifiCreation(configFile);
		testPack.CreateTG();
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



	public void init(String configFile){
		ConnectionTool cTool = new ConnectionTool(configFile,"p8admin","filenet","CMTOS");
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
