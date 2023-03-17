package com.ibm.mj.core.sandbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.json.JSONException;
import com.filenet.api.collection.GroupSet;
import com.filenet.api.collection.UserSet;
import com.filenet.api.constants.PrincipalSearchAttribute;
import com.filenet.api.constants.PrincipalSearchSortType;
import com.filenet.api.constants.PrincipalSearchType;
import com.filenet.api.core.Connection;
import com.filenet.api.core.EntireNetwork;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.Realm;
import com.filenet.api.security.User;
import com.ibm.mj.core.p8Connection.ConnectionTool;

public class TestSecurity {

	public static final String SRC = "D:/PDF-Modif/ContractExpert.pdf";
	public static final String DEST = "D:/PDF-Modif/ContractExpert2.pdf";
	public static final String DEST2 = "D:/PDF-Modif/ContractExpert3.pdf";
	public static final String PATHFOLDER = "/Temp_Import/CTD/CECE";
	public static final String PathLocal = "D:/PDF-Modif/ToMerge";
	static String configFile = "C:\\IBM\\config.properties";
	static String DocumentID = "{A94BB38A-4373-424A-9BF6-C181D33AE852}";

	private ObjectStore os = null;
	private Connection conn = null;
	ArrayList<HashMap<String, Object>> outlines;

	public TestSecurity(String configFile) {
		init(configFile);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		TestSecurity testPack = new TestSecurity(configFile);
		// CreateTG(testPack);
		String folderID = "{D69C1C2D-2CB5-435F-8A08-3DA37AD351BD}";
		testPack.listusers();
		System.out.println("done");

	}

	private void listusers() {
		EntireNetwork entireNetwork = Factory.EntireNetwork.fetchInstance(
				getConn(), null);
		Realm realm = entireNetwork.get_MyRealm();
		String realmName = realm.get_Name();
		System.out.println(realmName + " is retrieved");
		GroupSet groups = realm.findGroups("DG Taxud Member",
				PrincipalSearchType.PREFIX_MATCH,
				PrincipalSearchAttribute.SHORT_NAME,
				PrincipalSearchSortType.NONE, Integer.valueOf("50"), null);
		com.filenet.api.security.Group group;
		Iterator<?> groupIt = groups.iterator();
		if (groupIt.hasNext()) {
			group = (com.filenet.api.security.Group) groupIt.next();
			UserSet users = group.get_Users();
			Iterator it = users.iterator();
			while (it.hasNext()) {
				User user = (User) it.next();
		
				System.out.println(user.get_DisplayName()+ " ; "+ user.get_ShortName());
				
			}
		}
	}

	private void listgroup(ObjectStore os2, String groupName) {
		EntireNetwork entireNetwork = Factory.EntireNetwork.fetchInstance(
				getConn(), null);
		Realm realm = entireNetwork.get_MyRealm();
		String realmName = realm.get_Name();
		System.out.println(realmName + " is retrieved");
		GroupSet groups = realm.findGroups("",
				PrincipalSearchType.PREFIX_MATCH,
				PrincipalSearchAttribute.SHORT_NAME,
				PrincipalSearchSortType.NONE, Integer.valueOf("50"), null);
		com.filenet.api.security.Group group;
		Iterator<?> groupIt = groups.iterator();
		while (groupIt.hasNext()) {
			group = (com.filenet.api.security.Group) groupIt.next();
			System.out.println("name=" + group.get_Name());
			System.out.println("Distinguishedname = "
					+ group.get_DistinguishedName());
			System.out.println("shortname =" + group.get_ShortName());
			System.out.println("Users =" + group.get_Users());
		}

	}

	public void init(String configFile) {
		ConnectionTool cTool = new ConnectionTool(configFile, "p8admin",
				"filenet", "CMTOS");
		setOs(cTool.getOs());
		setConn(cTool.getConnection());

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
}
