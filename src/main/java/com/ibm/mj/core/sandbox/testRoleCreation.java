package com.ibm.mj.core.sandbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.CmRolePermission;
import com.filenet.api.security.CmStaticRole;
import com.filenet.api.util.Id;
import com.ibm.mj.core.ceObject.FolderTool;
import com.ibm.mj.core.ceObject.RoleTool;
import com.ibm.mj.core.p8Connection.ConnectionTool;



public class testRoleCreation {

	public static final String SRC  = "D:/PDF-Modif/ContractExpert.pdf";
	public static final String DEST = "D:/PDF-Modif/ContractExpert2.pdf";
	public static final String DEST2= "D:/PDF-Modif/ContractExpert3.pdf";
	public static final String PATHFOLDER = "/Temp_Import/CTD/CECE";
	public static final String PathLocal = "D:/PDF-Modif/ToMerge";
	static String configFile = "C:\\IBM\\config.properties";			
	static String DocumentID			= "{A94BB38A-4373-424A-9BF6-C181D33AE852}";

	private ObjectStore os =null;
	private  Connection conn =null;
	ArrayList<HashMap<String, Object>> outlines ;

	public testRoleCreation(String configFile) {
		init(configFile);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {	
		testRoleCreation testPack = new testRoleCreation(configFile);
		//CreateTG(testPack);		
		String folderID = "{D69C1C2D-2CB5-435F-8A08-3DA37AD351BD}";

		testPack.listPermission(testPack.getOs(), folderID);
		System.out.println("done");
	}

	public static void CreateTG(testRoleCreation testPack) {
		List<String> attributes = new ArrayList<String>();
		attributes.add("CRMS2_TGType");
		List<String> values = new ArrayList<String>();
		values.add("Open");
		//	CmStaticRole staticRole = RoleTool.createStaticRoleWithProperties(testPack.os, "cool4","CRMS_TargetGroup", attributes, values);	
		//	com.filenet.api.security.User user = SecurityTool.getUser(testPack.getConn(), "p8admin");
		//	RoleTool.addRoleMember( staticRole, user);
		//User user = SecurityTool.getUser(testPack.getConn(), "p8admin");
		//Group group = SecurityTool.getGroup(testPack.getConn(), "Level0");
		//boolean test = SecurityTool.IsMember(testPack.getConn(), group, user);
		//System.out.println(test);
	}

	public  void listPermission(ObjectStore os, String ID  ){
		if(ID!=null){
			Folder casef = FolderTool.getFolder(os, ID);
			System.out.println("Folder :: "+casef.get_Name());
			AccessPermissionList permissions = casef.get_Permissions();			
			for(Object permi : permissions){
				AccessPermission permission2 = (AccessPermission) permi;
				System.out.println("------ Start ---------");
				System.out.println(permission2.get_GranteeName());
				System.out.println(permission2.getClass().getSimpleName());
				if(permission2.getClass().getSimpleName().equals("CmRolePermissionImpl")){
					CmRolePermission permission = (CmRolePermission) permi;
					System.out.println(permission.get_Role().get_DisplayName());
					System.out.println("--------------- End ---------------");
				}


			} }


	}


	public  void removePermission(ObjectStore os, String ID  ){
		if(ID!=null){
			Folder casef = FolderTool.getFolder(os, ID);
			System.out.println("Folder :: "+casef.get_Name());
			Id security = new Id("{218C7AEC-7DF6-CFDB-843D-645ACF200000}");
			CmStaticRole staticrole = RoleTool.getStaticRole(os, security, "CRMS_TargetGroup");
			RoleTool.RemoveRoleFromFolder(staticrole, casef);

		}
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
