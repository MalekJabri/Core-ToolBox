package com.ibm.mj.core.sandbox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.PageIterator;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.core.Folder;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.ibm.mj.core.sandbox.tester.testerClass;

public class TestSQLQuery extends testerClass{

	static String configFile = "/Users/be07463/Dev/Tools/FileNet_Config/config_cpe_icp.properties";
	
	
	public static void main(String[] args) {
		TestSQLQuery tq = new TestSQLQuery(configFile);
		tq.getAction();		
	}
	
	public TestSQLQuery(String uriPath) {
		// TODO Auto-generated constructor stub
		super(uriPath);
	}
	
	@Override
	public void getAction() {
		method1();
		//method3();		
	}
	
	public void method1() {
		SearchScope search = new SearchScope(os);

		SearchSQL sql = new SearchSQL();

		sql.setSelectList("Id");

		sql.setFromClauseInitialValue("Document", "d", true);
		

		//sql.setWhereClause(properties+ "="+"'"+value+"'");
		

		 IndependentObjectSet documents = search.fetchObjects(sql, new
				Integer(50),null, Boolean.valueOf(true));
		 
		 Iterator ite = documents.iterator();
		 int i = 0;
		 while(ite.hasNext()) {
			 ite.next();
			 i++;
		 }

		System.out.println(i);
		
	}
	
	public void method2() {
		SearchScope search = new SearchScope(os);

		SearchSQL sql = new SearchSQL();

		sql.setSelectList("id");

		sql.setFromClauseInitialValue("Document", "d", true);
		

		//sql.setWhereClause(properties+ "="+"'"+value+"'");
		
		RepositoryRowSet test = search.fetchRows(sql, new
				Integer(50),null, Boolean.valueOf(true));

		 
		 Iterator ite = test.iterator();
		 int i = 0;
		 while(ite.hasNext()) {
			 ite.next();
			 i++;
		 }

		System.out.println(i);
	}
	
	public void method3() {
		
		//String query = "select Id from CustomerDossier where CustomerName like '%Malek%'";
		String query = "select Id from CustomerDossier where Company like '%IBM%'";
		SearchSQL searchSQL = new SearchSQL(query);
		SearchScope searchScope = new SearchScope(os);

		Integer pageSize =4;
		// Retrieve the first pageSize results.
		List<Object> searchResults = new ArrayList<Object>(pageSize);
		PropertyFilter filter = new PropertyFilter();
		IndependentObjectSet resultsObjectSet = searchScope.fetchObjects(searchSQL, pageSize, filter, true);
		PageIterator pageIterator = resultsObjectSet.pageIterator();
		int itemCount = 0;
		int pageIteratorCount = 0;
		while (pageIterator.nextPage()) {
			for (Object obj : pageIterator.getCurrentPage()) {
				searchResults.add(obj);
				itemCount++;
				
			}
			pageIteratorCount++;
		}

		System.out.println("count "+itemCount+ " --  Page iterator "+pageIteratorCount );
		for (Object searchResult : searchResults) {
			Folder folder = (Folder) searchResult;
			folder.fetchProperties(filter);
			System.out.println(folder.get_Name());
			System.out.println(folder.getProperties().getStringValue("pathname"));
		}
	}

}
