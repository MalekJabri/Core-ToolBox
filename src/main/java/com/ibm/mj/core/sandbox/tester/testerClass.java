package com.ibm.mj.core.sandbox.tester;

import com.filenet.api.core.Connection;
import com.filenet.api.core.ObjectStore;
import com.ibm.mj.core.p8Connection.ConnectionTool;
import com.ibm.mj.core.properties.MappingFieldsTool;

import java.util.logging.Logger;

public abstract class testerClass implements testerInterface{
	
	private static Logger logger = Logger.getLogger(testerClass.class.getName());
	
	static String configFile = "/Users/jabrimalek/development/tools/FileNet_Config/config_cpe_demo10.properties";
	static String idRole = "{ED6E7265-6E0D-CFC0-B81C-64DC1BB00001}";

 	protected ObjectStore os = null;
	Connection conn = null;
	
	public testerClass() {
		init(configFile);
	}
		
	public testerClass(String uriPath) {
		setConfig(uriPath);
		
	}

	public void init(String userPath) {
		MappingFieldsTool propertiesTool = new MappingFieldsTool(userPath);
		setOs(ConnectionTool.connectionPool(configFile, propertiesTool.getMappingValue("username"), propertiesTool.getMappingValue("password"), propertiesTool.getMappingValue("objectstore")));
		setConn(os.getConnection());
		logger.info("Connect to the image");
	}
	
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public ObjectStore getOs() {
		return os;
	}

	public void setOs(ObjectStore os) {
		this.os = os;
	}
	
	public void setConfig(String file) {	
		configFile=file;
		init(file);
	}


}
