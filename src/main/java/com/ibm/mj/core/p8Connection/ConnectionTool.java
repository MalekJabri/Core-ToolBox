

package com.ibm.mj.core.p8Connection;

import com.filenet.api.collection.ObjectStoreSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import org.apache.log4j.Logger;

import javax.security.auth.Subject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;


public class ConnectionTool 
{
	private Connection con;
	private ObjectStore os;
	private Domain dom;
	private String domainName;
	private String username;
	private String password;
	private ObjectStoreSet ost;
	@SuppressWarnings({ "rawtypes" })
	private Vector osnames;
	private boolean isConnected;
	private UserContext uc;	
	private static String m_url;

	private static String m_osName;
	@SuppressWarnings("unused")
	private static String m_jaasconf;
	private static String m_jaasstanza;




	/*
	 * constructor
	 */
	
	private static Logger logger = Logger.getLogger(ConnectionTool.class);

	public ConnectionTool()
	{
		init();
	}
	
	
	public ConnectionTool(String configFile, String userName, String password, String objectStore)
	{
		init();
		establishConnection(userName, password, configFile);
		if(isConnected()){
			logger.info("Connection is estbalished");
		}
		
		os = fetchOS(objectStore);
		if (os!=null){
			logger.info("The default object store is "+os.get_DisplayName());
		}else{
			logger.error("Object store not found");
		}
	}

	public ConnectionTool(String configFile)
	{
		init();
		loadConfig(configFile);
		establishConnection(username, password, configFile);
		if(isConnected()){
			logger.info("Connection is estbalished");
		}
		os = fetchOS(m_osName);
		if (os!=null){
			logger.info("The default object store is "+os.get_DisplayName());
		}else{
			logger.error("Object store not found");
		}
	}
	
	public void init(){
		con = null;
		uc = UserContext.get();
		dom = null;
		domainName = null;
		ost = null;
		osnames = new Vector<String>();
		isConnected = false;

	}
	public ObjectStore getOs() {
		return os;
	}
	public Connection getConnection() {
		return con;
	}
	/* 
	 * Establishes connection with Content Engine using
	 * supplied username, password, JAAS stanza and CE Uri.
	 */
	public void establishConnection(String userName, String passWord, String stanza, String uri)
    {
			username = userName;
			password= passWord;
        con = Factory.Connection.getConnection(uri);
        Subject sub = UserContext.createSubject(con,username,password,stanza);
        uc.pushSubject(sub);
        dom = fetchDomain();
        domainName = dom.get_Name();
        ost = getOSSet();
        isConnected = true;
    }
	/*
	 * Establishes connection with Content Engine using
	 * supplied username, password, config file path.
	 */
	public void establishConnection(String userName, String passWord, String path)
    {
		username = userName;
		password= passWord;
		loadConfig(path);
        con = Factory.Connection.getConnection(m_url);
        Subject sub = UserContext.createSubject(con,username,password,m_jaasstanza);
        uc.pushSubject(sub);
        dom = fetchDomain();
        domainName = dom.get_Name();
        ost = getOSSet();
        isConnected = true;
    }

	/*
	 * Returns Domain object.
	 */
	public Domain fetchDomain()
    {
        dom = Factory.Domain.fetchInstance(con, null, null);
        return dom;
    }
    
    /*
     * Returns ObjectStoreSet from Domain
     */
	public ObjectStoreSet getOSSet()
    {
        ost = dom.get_ObjectStores();
        return ost;
    }
    
    /*
     * Returns vector containing ObjectStore
     * names from object stores available in
     * ObjectStoreSet.
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getOSNames()
    {
    	if(osnames.isEmpty())
    	{
    		Iterator it = ost.iterator();
    		while(it.hasNext())
    		{
    			ObjectStore os = (ObjectStore) it.next();
    			osnames.add(os.get_DisplayName());
    		}
    	}
        return osnames;
    }

	/*
	 * Checks whether connection has established
	 * with the Content Engine or not.
	 */
	public boolean isConnected() 
	{
		return isConnected;
	}
	
	/*
	 * Returns ObjectStore object for supplied
	 * object store name.
	 */
	public ObjectStore fetchOS(String name)
    {
        ObjectStore os = Factory.ObjectStore.fetchInstance(dom, name, null);
        return os;
    }
	
	/*
	 * Returns the domain name.
	 */
	public String getDomainName()
    {
        return domainName;
    }
	
	public void loadConfig(String configFile){
		try
	    {
	      FileInputStream is = new FileInputStream(configFile);
	      java.util.Properties prop = new java.util.Properties();
	      prop.load(is);
	      m_url = prop.getProperty("url");
		  domainName = prop.getProperty("domainName");
	      m_osName = prop.getProperty("objectstore");
	      m_jaasconf = prop.getProperty("jaasconf");
	      m_jaasstanza = prop.getProperty("jaasstanza");
		  if(username==null){
			  username = prop.getProperty("username");
			  password = prop.getProperty("password");
		  }
	      // Clean memory
	      if(is!=null) {
	    	  	is.close();
	    	  	prop.clear();
	      }
	      
	    }
	    catch (FileNotFoundException e)
	    {
	      e.printStackTrace();
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	}
	
	public static ObjectStore connectionPool(String configFile, String userName, String password, String objectStore) {
		ConnectionTool ceC = new ConnectionTool();

		ceC.establishConnection(userName, password, configFile);
		if(ceC.isConnected()){
			logger.info("Connection is estbalished n");
		}
		
		ObjectStore os = ceC.fetchOS(objectStore);
		if (os!=null){
			logger.info(os.get_DisplayName());
		}else{
			logger.warn("Object store not found");
		}
		return os;
	}
}
