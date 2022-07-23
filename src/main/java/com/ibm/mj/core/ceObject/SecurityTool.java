package com.ibm.mj.core.ceObject;

import java.util.Iterator;
import java.util.Set;

import com.filenet.api.collection.GroupSet;
import com.filenet.api.collection.UserSet;
import com.filenet.api.constants.PrincipalSearchAttribute;
import com.filenet.api.constants.PrincipalSearchSortType;
import com.filenet.api.constants.PrincipalSearchType;
import com.filenet.api.core.Connection;
import com.filenet.api.core.EntireNetwork;
import com.filenet.api.core.Factory;
import com.filenet.api.security.Group;
import com.filenet.api.security.Realm;
import com.filenet.api.security.User;

public class SecurityTool {

	public static boolean IsMember(Connection connection, Group group, User user){

		if(group !=null && user !=null)
		{

			UserSet users = group.get_Users();
			Iterator<?> it = users.iterator();
			while(it.hasNext()){
				User userI = (User) it.next();
				//System.out.println(userI.get_DisplayName());
				if(userI.equals(user)) return true;
			}
			GroupSet groups = group.get_Groups();
			Iterator<?> itG = groups.iterator();
			while(itG.hasNext()){
				Group groupI = (Group) itG.next();
				//System.out.println(groupI.get_DisplayName());
				if(IsMember(connection,groupI,user)) return true;
			}

		}
		return false;
	}

	
	
	public static void getUserMembership(Connection connection, Group group, Set<String> username){

		if(group !=null && username !=null)
		{
			UserSet users = group.get_Users();
			Iterator<?> it = users.iterator();
			while(it.hasNext()){
				User userI = (User) it.next();
				//System.out.println(userI.get_DisplayName());
				username.add(userI.get_ShortName());
			}
			GroupSet groups = group.get_Groups();
			Iterator<?> itG = groups.iterator();
			while(itG.hasNext()){
				Group groupI = (Group) itG.next();
				getUserMembership(connection, groupI, username);
			}
		}
		
	}
	public static User getUser(Connection connection, String UserName){

		EntireNetwork entireNetwork= Factory.EntireNetwork.fetchInstance(connection, null);
		Realm realm= entireNetwork.get_MyRealm();
		//String realmName= realm.get_Name();
		//System.out.println(realmName+ " is retrieved");
		UserSet users = realm.findUsers(UserName, PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);
		com.filenet.api.security.User user;
		Iterator<?> it = users.iterator();
		while (it.hasNext())
		{
			user = (com.filenet.api.security.User)it.next();
			/*	System.out.println("distinguishedname =" +user.get_DistinguishedName());
			System.out.println("shortname =" + user.get_ShortName());
			System.out.println("name=" + user.get_Name());
			System.out.println("displayname =" + user.get_DisplayName());
			 */
			return user;
		}
		return null;
	}
	
	

	public static Group getGroup(Connection connection, String groupName){

		EntireNetwork entireNetwork= Factory.EntireNetwork.fetchInstance(connection, null);
		Realm realm= entireNetwork.get_MyRealm();//String realmName= realm.get_Name();
		//System.out.println(realmName+ " is retrieved");		
		GroupSet groups = realm.findGroups(groupName,PrincipalSearchType.PREFIX_MATCH,PrincipalSearchAttribute.SHORT_NAME,PrincipalSearchSortType.NONE,Integer.valueOf("50"), null);
		com.filenet.api.security.Group group;
		Iterator<?> groupIt= groups.iterator(); 
		while (groupIt.hasNext())
		{
			group=(com.filenet.api.security.Group)groupIt.next();
			/*	System.out.println("name=" + group.get_Name());
			System.out.println("Distinguishedname = " + group.get_DistinguishedName());
			System.out.println("shortname =" + group.get_ShortName());
			System.out.println("Users =" + group.get_Users());*/
			return group;
		}

		return null;
	}
	
	
}
