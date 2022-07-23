package com.ibm.mj.core.ceObject;


import java.util.Date;
import java.util.List;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CmAbstractPersistable;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;

public class AbstractPersistableTool {

	public static CmAbstractPersistable createAbstractWithProperties( ObjectStore os,  String className, List<String> Attributes , List<String> values)
	{		
		CmAbstractPersistable abstractP = Factory.CmAbstractPersistable.createInstance(os, className);
		if(Attributes!=null && values!=null && Attributes.size()==values.size() )
		{
			for(int i =0;i<Attributes.size();i++)
			{
				abstractP.getProperties().putValue(Attributes.get(i), values.get(i));
			}
		}		
		abstractP.save(RefreshMode.REFRESH);
		return abstractP;


	}
}
