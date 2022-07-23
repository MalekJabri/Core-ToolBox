package com.ibm.mj.core.ceObject;

import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;

public class CustomObjectTool {
	
	public static void LinkToFolder(ObjectStore os, Folder folder,	CustomObject custom) {
		ReferentialContainmentRelationship rcr = 
				Factory.ReferentialContainmentRelationship.createInstance(os, null, 
						AutoUniqueName.AUTO_UNIQUE, 
						DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.set_Tail(folder);
		rcr.set_Head(custom);
		rcr.set_ContainmentName(custom.get_Name());
		rcr.save(RefreshMode.REFRESH);
	}

}
