package com.ibm.mj.core.ceObject;

import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;

public class SearchTool {


	public static IndependentObjectSet  searchObjects(ObjectStore os, String objectClass, String property, String value)
	{
		SearchScope search = new SearchScope(os);
		SearchSQL sql = new SearchSQL();
		sql.setSelectList("*");
		sql.setFromClauseInitialValue(objectClass, "o", true);
		if(property!=null && !property.isEmpty())	sql.setWhereClause(property+ "="+"'"+value+"'");
		IndependentObjectSet objects = search.fetchObjects(sql, 
				Integer.valueOf(50),null, Boolean.valueOf(true));
		return objects;
	}

	public static DocumentSet searchDocuments(ObjectStore os, String DocumentClass, String property, String value)
	{

		SearchScope search = new SearchScope(os);

		SearchSQL sql = new SearchSQL();

		sql.setSelectList("*");

		sql.setFromClauseInitialValue(DocumentClass, "d", true);

		sql.setWhereClause(property+ "="+"'"+value+"'");

		DocumentSet documents = (DocumentSet)search.fetchObjects(sql, 
				Integer.valueOf(50),null, Boolean.valueOf(true));

		return documents;
	}

	public static FolderSet searchFolders(ObjectStore os, String FolderClass, String property, String value)
	{

		SearchScope search = new SearchScope(os);

		SearchSQL sql = new SearchSQL();

		sql.setSelectList("*");

		sql.setFromClauseInitialValue(FolderClass, "d", true);
		if(property!=null && !property.isEmpty())	sql.setWhereClause(property+ "="+"'"+value+"'");

		FolderSet folders = (FolderSet)search.fetchObjects(sql, Integer.valueOf(50),null, Boolean.valueOf(true));

		return folders;
	}
}
