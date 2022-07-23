package com.ibm.mj.core.ceObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import com.ibm.mj.core.properties.MappingFieldsTool;



public class UserTool {
	private final LdapContext ctx;
	private final String user ;
	private final String pass;
	private final String host;
	private final String authType;
	private final String searchString;
	
	public static void main(String[] args) throws Exception {
		UserTool test;
		try {
			test = new UserTool(new FileInputStream("config/ldap.properties"));
			//test.printUserBasicAttributes("p8admin");
			System.out.println(test.getUserMemberShip("anke").toString());
			
		//	test.getUserMemberShip("BE ANW");
		//	System.out.println("anke is member of BE 01 :" + test.isMember("anke", "BE 01"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public UserTool(InputStream Document) throws IOException {
		MappingFieldsTool config = new MappingFieldsTool(Document);
		user = config.getMappingValue("username");
		pass = config.getMappingValue("password");
		host =config.getMappingValue("host");
		authType = config.getMappingValue("authType");
		searchString = config.getMappingValue("SearchString");
		ctx = getLdapContext();
	}
	
	
	public UserTool() {
	
		user = "admin@jabri.eu";
		pass = "Qaja8181";
		host = "ldap://169.55.172.50:389";
		authType = "simple";
		searchString = "CN=Users,DC=ecm,DC=ibm,DC=local";
		ctx = getLdapContext();
	}
	private LdapContext getLdapContext(){
		LdapContext ctx = null;
		try{
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.SECURITY_AUTHENTICATION, authType);
			env.put(Context.SECURITY_PRINCIPAL, user);
			env.put(Context.SECURITY_CREDENTIALS, pass);
			env.put(Context.PROVIDER_URL, host);
			ctx = new InitialLdapContext(env, null);
			System.out.println("Connection Successful.");
		}catch(NamingException nex){
			System.out.println("LDAP Connection: FAILED");
			nex.printStackTrace();
		}
		return ctx;
	}

	public void printUserBasicAttributes(String username) {       
		try { 
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String[] attrIDs = { "distinguishedName","sn","givenname","mail","telephonenumber"};
			constraints.setReturningAttributes(attrIDs);
			NamingEnumeration<SearchResult> answer = ctx.search(searchString, "sAMAccountName="
					+ username, constraints);
			if (answer.hasMore()) {
				Attributes attrs = ((SearchResult) answer.next()).getAttributes();
				System.out.println("distinguishedName "+ attrs.get("distinguishedName"));
				System.out.println("givenname "+ attrs.get("givenname"));
				System.out.println("sn "+ attrs.get("sn"));
				System.out.println("mail "+ attrs.get("mail"));
				System.out.println("telephonenumber "+ attrs.get("telephonenumber"));
			}else{
				throw new Exception("Invalid User");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public String getPhoneUser(String username) {       
		try { 
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String[] attrIDs = {"telephonenumber"};
			constraints.setReturningAttributes(attrIDs);
			NamingEnumeration<SearchResult> answer = ctx.search(searchString, "sAMAccountName="
					+ username, constraints);
			if (answer.hasMore()) {
				Attributes attrs = ((SearchResult) answer.next()).getAttributes();
				return attrs.get("telephonenumber").toString();
			}else{
				throw new Exception("Invalid User");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public String getAttributeUser(String username, String attribute) {       
		try { 
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String[] attrIDs = { attribute};
			constraints.setReturningAttributes(attrIDs);
			NamingEnumeration<SearchResult> answer = ctx.search(searchString, "sAMAccountName="
					+ username, constraints);
			if (answer.hasMore()) {
				Attributes attrs = ((SearchResult) answer.next()).getAttributes();
				return attrs.get(attribute).toString();
			}else{
				throw new Exception("Invalid User");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	public String getGroupMemberShip(String username) throws Exception{
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String[] attrIDs = { "distinguishedName","sn","givenname","memberof"};
		constraints.setReturningAttributes(attrIDs);
	
		NamingEnumeration<SearchResult> answer = ctx.search(searchString, "sAMAccountName="
				+ username, constraints);
		if (answer.hasMore()) {
			Attributes attrs = ((SearchResult) answer.next()).getAttributes();
			if (attrs.get("memberof")!=null && attrs.get("memberof").size()>0){
				System.out.println("a number of Group have been found"+attrs.get("memberof").size() );
			System.out.println(attrs.get("memberof").toString());
					
			if (attrs.get("memberof") != null) {
                for ( Enumeration<?> e1 = attrs.get("memberof").getAll() ; e1.hasMoreElements() ; ) {
                    String unprocessedChildGroupDN = e1.nextElement().toString();
                    String unprocessedChildGroupCN = getCN(unprocessedChildGroupDN);
               // System.out.println("unprocessedChildGroupDN"+unprocessedChildGroupDN);   
                System.out.println("unprocessedChildGroupCN : "+unprocessedChildGroupCN);   
                }
            }
			}
		}else{
			System.out.println("No user found");
			//throw new Exception("Invalid User");
		}
		return "";
		
	}
	
	public GroupDTO getUserMemberShip(String username) throws Exception{
		GroupDTO groupDto = new GroupDTO();
		groupDto.setName(username);
		groupDto.setGroups(getMemberShip(username));		
		return groupDto;
	}
	
	public List<GroupDTO> getMemberShip(String username) throws Exception{
		List<GroupDTO> listGroup = new ArrayList<GroupDTO>();		
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String[] attrIDs = { "distinguishedName","sn","givenname","memberof"};
		constraints.setReturningAttributes(attrIDs);
	
		NamingEnumeration<SearchResult> answer = ctx.search(searchString, "sAMAccountName="
				+ username, constraints);
		if (answer.hasMore()) {
			Attributes attrs = ((SearchResult) answer.next()).getAttributes();
			if (attrs.get("memberof")!=null && attrs.get("memberof").size()>0){
			//	System.out.println("a number of Group have been found"+attrs.get("memberof").size() );
		//	System.out.println(attrs.get("memberof").toString());
					
			if (attrs.get("memberof") != null) {
                for ( Enumeration<?> e1 = attrs.get("memberof").getAll() ; e1.hasMoreElements() ; ) {
                    String unprocessedChildGroupDN = e1.nextElement().toString();
                    String unprocessedChildGroupCN = getCN(unprocessedChildGroupDN);
                    // System.out.println("unprocessedChildGroupDN"+unprocessedChildGroupDN);   
           //         System.out.println("unprocessedChildGroupCN : "+unprocessedChildGroupCN); 
                    GroupDTO group = new GroupDTO();
                    group.setName(unprocessedChildGroupCN); 
                    group.setGroups(getMemberShip(unprocessedChildGroupCN));
                    listGroup.add(group);
                }
            }
			else {
				
			}
			}
		}else{
			System.out.println("No user found");
			//throw new Exception("Invalid User");
		}
		return listGroup;
		
	}
	
	public boolean isMember(String username, String group) throws Exception{
		boolean result = false;
		SearchControls constraints = new SearchControls();
		constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String[] attrIDs = { "distinguishedName","sn","givenname","memberof"};
		constraints.setReturningAttributes(attrIDs);
	
		NamingEnumeration<SearchResult> answer = ctx.search(searchString, "sAMAccountName="
				+ username, constraints);
		if (answer.hasMore()) {
			Attributes attrs = ((SearchResult) answer.next()).getAttributes();
			if (attrs.get("memberof")!=null && attrs.get("memberof").size()>0){
			//	System.out.println("a number of Group have been found"+attrs.get("memberof").size() );
		//	System.out.println(attrs.get("memberof").toString());
					
			if (attrs.get("memberof") != null) {
                for ( Enumeration<?> e1 = attrs.get("memberof").getAll() ; e1.hasMoreElements() ; ) {
                    String unprocessedChildGroupDN = e1.nextElement().toString();
                    String unprocessedChildGroupCN = getCN(unprocessedChildGroupDN);
               // System.out.println("unprocessedChildGroupDN"+unprocessedChildGroupDN);   
              //  System.out.println("unprocessedChildGroupCN : "+unprocessedChildGroupCN);  
                if(group.equalsIgnoreCase(unprocessedChildGroupCN)) result= true;
                }
            }
			}
		}else{
			System.out.println("No user found");
			//throw new Exception("Invalid User");
		}
		return result;
		
	}
	
	
	
	  public static final String DISTINGUISHED_NAME = "distinguishedName";
	    public static final String CN = "cn";
	    public static final String MEMBER = "member";
	    public static final String memberof = "memberOf";
	    public static final String SEARCH_BY_SAM_ACCOUNT_NAME = "(SAMAccountName={0})";
	    public static final String SEARCH_GROUP_BY_GROUP_CN = "(&(objectCategory=group)(cn={0}))";

	    /*
	     * Prepares and returns CN that can be used for AD query
	     * e.g. Converts "CN=**Dev - Test Group" to "**Dev - Test Group"
	     * Converts CN=**Dev - Test Group,OU=Distribution Lists,DC=DOMAIN,DC=com to "**Dev - Test Group"
	     */
	    public static String getCN(String cnName) {
	        if (cnName != null && cnName.toUpperCase().startsWith("CN=")) {
	            cnName = cnName.substring(3);
	        }
	        int position = cnName.indexOf(',');
	        if (position == -1) {
	            return cnName;
	        } else {
	            return cnName.substring(0, position);
	        }
	    }
	    public static boolean isSame(String target, String candidate) {
	        if (target != null && target.equalsIgnoreCase(candidate)) {
	            return true;
	        }
	        return false;
	    }
	
	 
}
