package com.ibm.mj.core.ceObject;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.CmRoleMemberList;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.CmRole;
import com.filenet.api.security.CmRolePermission;
import com.filenet.api.security.CmStaticRole;
import com.filenet.api.security.Group;
import com.filenet.api.security.User;
import com.filenet.api.util.Id;

public class RoleTool {

	public static CmStaticRole createStaticRoleWithProperties( ObjectStore os, String name, String roleClass, List<String> Attributes , List<String> values)
	{		
		CmStaticRole staticRole = Factory.CmStaticRole.createInstance(os, roleClass);
		staticRole.set_DisplayName(name);
		if(Attributes!=null && values!=null && Attributes.size()==values.size() )
		{
			for(int i =0;i<Attributes.size();i++)
			{
				staticRole.getProperties().putValue(Attributes.get(i), values.get(i));
			}
		}		
		staticRole.save(RefreshMode.REFRESH);
		return staticRole;


	}
	
	public static CmStaticRole getStaticRole( ObjectStore os, Id id, String roleClass)
	{		
		CmStaticRole staticRole = Factory.CmStaticRole.fetchInstance(os, id, null);
		return staticRole;
	}
	
	public static CmRole getRoleByName( ObjectStore os, String name, String roleClass)
	{	
		
		CmRole role =null;
		try {
			String property= "DisplayName";
			IndependentObjectSet objects = SearchTool.searchObjects(os, roleClass, property, name);
			@SuppressWarnings("rawtypes")
			Iterator ite = objects.iterator();
			while(ite.hasNext()) {
				role = (CmRole) ite.next();				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return role;
	}

	public static CmStaticRole createStaticRole( ObjectStore os, String name, String roleClass, Map<String,String> Attributes)
	{		
		CmStaticRole staticRole = Factory.CmStaticRole.createInstance(os, roleClass);
		staticRole.set_DisplayName(name);
		staticRole.save(RefreshMode.REFRESH);	
		return staticRole;
	}

	@SuppressWarnings("unchecked")
	public static boolean addRoleMember( CmStaticRole role , List<String> users, List<String> groups){
		CmRoleMemberList memberlist = role.get_RoleMembers();		
		if(role!=null){			
			if(users!=null&& users.size()>0){
				for(String useri : users)
				{
					User user = SecurityTool.getUser(role.getConnection(), useri);
					if(user!=null){
					com.filenet.api.security.CmRolePrincipalMember principalMember = Factory.CmRolePrincipalMember.createInstance(role.getObjectStore());
					principalMember.set_MemberPrincipal(user);	
					if(!memberlist.contains(principalMember)) memberlist.add(principalMember);
					}
					
				}
			}
			if(groups!=null && groups.size()>0){
				for(String groupi : groups){
					Group group = SecurityTool.getGroup(role.getConnection(), groupi);
					if(group!=null){
					com.filenet.api.security.CmRolePrincipalMember principalMember = Factory.CmRolePrincipalMember.createInstance(role.getObjectStore());
					principalMember.set_MemberPrincipal(group);	
					if(!memberlist.contains(principalMember)) memberlist.add(principalMember);
					}
				}
			}
			role.set_RoleMembers(memberlist);
			role.save(RefreshMode.REFRESH);
		}		
		return true;
	}

	@SuppressWarnings("unchecked")
	public static boolean AddRoleToFolder(CmStaticRole role, Folder folder){
		boolean result = false;
		AccessPermissionList permissions = folder.get_Permissions();
		CmRolePermission permission = Factory.CmRolePermission.createInstance();
		permission.set_Role(role);
		permission.set_InheritableDepth(0);
		permissions.add(permission);
		folder.set_Permissions(permissions);
		folder.save(RefreshMode.REFRESH);  
		result = true;
		return result;		
	}
	
	public static boolean RemoveRoleFromFolder(CmStaticRole role, Folder folder){
		boolean result = false;
		AccessPermissionList permissions = folder.get_Permissions();
		CmRolePermission permTodelete = null;
		for (Object permi : permissions){
			AccessPermission permission2 = (AccessPermission) permi;
			if(permission2.getClass().getSimpleName().equals("CmRolePermissionImpl")){
				CmRolePermission permission = (CmRolePermission) permi;
				if(permission.get_Role().equals(role)) { 
					System.out.println("Role Found in the list and will be deleted :"+permission.get_Role().get_DisplayName());
					permTodelete = permission;					
					}
			}			
		} 
		if(permTodelete!=null)permissions.remove(permTodelete);
		folder.set_Permissions(permissions);
		folder.save(RefreshMode.REFRESH);  
		result = true;
		return result;		
	}

}
