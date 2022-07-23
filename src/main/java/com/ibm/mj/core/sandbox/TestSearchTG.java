package com.ibm.mj.core.sandbox;

import com.filenet.api.security.CmRole;
import com.ibm.mj.core.ceObject.RoleTool;
import com.ibm.mj.core.sandbox.tester.testerClass;



public class TestSearchTG extends testerClass{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestSearchTG ts = new TestSearchTG();
		ts.getAction();
	}


	@Override
	public void getAction() {
		String objectClass = "CmStaticRole";
		String value = "NoTargetGroupRelated";	
		CmRole role = RoleTool.getRoleByName(getOs(), value, objectClass);
		System.out.println(role.get_DisplayName() + "  - "+ role.get_Id().toString());

	}


}
