package com.ibm.mj.core.p8Connection;

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

public class Connections {
	
 
	  private static String m_url;
	  private static String m_password;
	  private static String m_username;
	  private static String m_domain;
	  private static String m_osName;
	  private static String m_jaasconf;
	  private static String m_jaasstanza;
	  static Logger log = Logger.getLogger(Connections.class.getName());

	public static void main(String[] args) {
		
		String configFile = "C:\\IBM\\config.properties";
		Connections ce = new Connections();
		ce.loadConfig(configFile);
		ce.connection(m_username, m_password, m_osName);
		

	}
	protected static Connection getConnection()
	  {
	    return (com.filenet.api.core.Connection) Factory.Connection.getConnection(m_url);
	  }
	public void connection(String user, String pass, String objectstore){
		
		

		      System.setProperty("java.security.auth.login.config", m_jaasconf);

		      Connection c = getConnection();
		      Subject sub = UserContext.createSubject((com.filenet.api.core.Connection) c, m_username, m_password, 
		        m_jaasstanza);
		      UserContext uc = new UserContext();
		      uc.pushSubject(sub);
		      UserContext.set(uc);
		      Domain domain = Factory.Domain.fetchInstance(c, m_domain, null);
		      ObjectStore os =
		        Factory.ObjectStore.fetchInstance(domain, m_osName, null);
		      log.info("Connec :--5 \n");
		      if (os != null) {
		        log.info("Object store found:" + 
		          os.get_DisplayName() + "\n");
		        System.out.println("Object store found:" + 
				          os.get_DisplayName() + "\n");
		      } else {
		        log.info("Object store not found \n");
		      }
		      log.info("Connec :--6 \n");
		      
		      
		
	}
	public void loadConfig(String configFile){
		try
	    {
	      FileInputStream is = new FileInputStream(configFile);
	      java.util.Properties prop = new java.util.Properties();
	      prop.load(is);
	      m_url = prop.getProperty("url");
	      m_password = prop.getProperty("password");
	      m_username = prop.getProperty("username");
	      m_domain = prop.getProperty("domainName");
	      m_osName = prop.getProperty("objectstore");
	      m_jaasconf = prop.getProperty("jaasconf");
	      m_jaasstanza = prop.getProperty("jaasstanza");
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
}
