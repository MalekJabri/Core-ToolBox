package com.ibm.mj.core.ceObject;

import com.filenet.api.collection.DateTimeList;
import com.filenet.api.collection.IdList;
import com.filenet.api.collection.Integer32List;
import com.filenet.api.collection.StringList;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.filenet.apiimpl.collection.DateTimeListImpl;
import com.filenet.apiimpl.collection.Integer32ListImpl;
import com.filenet.apiimpl.collection.StringListImpl;

import java.text.SimpleDateFormat;
import java.util.*;

public class PropertiesTool {

	private HashMap<String,String> tableOFValue;	

	@SuppressWarnings("unchecked")
	public PropertiesTool(Properties properties,String filtre) {

		tableOFValue=new HashMap<String,String>();	
		Iterator<Property> it = properties.iterator();	
		while (it.hasNext()){
			Property field = it.next();	
			String propertyName =field.getPropertyName();
			if(filtre==null || propertyName.contains(filtre))	{
				tableOFValue.put(propertyName, getPropertyValue(field,null));				
			}
		}
	}
	public static void PrintProperties(Properties properties,String filtre){		
		@SuppressWarnings("unchecked")
		Iterator<Property> it = properties.iterator();
		while (it.hasNext()){
			Property field = it.next();	
			String propertyName =field.getPropertyName();
			if(filtre==null || propertyName.contains(filtre))
				System.out.println(propertyName+"  : "+getPropertyValue(field,null));
		}
	}

	public static List<Date> getDateListProperties(Property dateProperty){
		List<Date> listOfDate = new ArrayList<Date>();
		String className = dateProperty.getClass().getSimpleName();		
		if(className.contains("List") && className.contains("DateTime")){
			DateTimeList dateList = dateProperty.getDateTimeListValue();
			for(int i=0;i<dateList.size();i++)	{ 
				listOfDate.add((Date) dateList.get(i));				
			}
		}
		return listOfDate;		
	}

	public static List<String> getStringListProperties(Property dateProperty){
		List<String> listOfString = new ArrayList<String>();
		String className = dateProperty.getClass().getSimpleName();		
		if(className.contains("List") && className.contains("String")){
			StringList stringList = dateProperty.getStringListValue();
			for(int i=0;i<stringList.size();i++)	{ 
				listOfString.add((String) stringList.get(i));				
			}
		}
		return listOfString;		
	}

	public static List<Integer> getIntListProperties(Property dateProperty){
		List<Integer> listOfInt = new ArrayList<Integer>();	
		Integer32List intList = dateProperty.getInteger32ListValue();
		for(int i=0;i<intList.size();i++)	{ 				
			listOfInt.add((Integer) intList.get(i));				
		}
		return listOfInt;		
	}

	public static DateTimeList getDateListImp( List<Date> listofDates){
		DateTimeList dateList = new DateTimeListImpl(listofDates);		
		return dateList;
	}

	public static StringList getStringListImp(List<String> listofString){
		StringList stringList = new StringListImpl(listofString);		
		return stringList;
	}

	public static Integer32List getIntListImpl(List<Integer> listofInteger){
		Integer32List intList  = new Integer32ListImpl(listofInteger);	
		return intList;	
	}
	

	public static String getPropertyValue(Property field,String DateFormat){
		if(DateFormat ==null || DateFormat.isEmpty()) DateFormat = "dd-MM-yyyy";
		SimpleDateFormat format = 
				new SimpleDateFormat(DateFormat);
		String result =null;
		String className = field.getClass().getSimpleName();
		if(className.contains("List")){
			if(className.contains("Id")){				
				IdList idlist = field.getIdListValue();
				if(idlist!=null && idlist.size()>0){
					result="";
					for(int i=0;i<idlist.size();i++) result = result + ";" + idlist.get(i).toString();				
				}


			}else if(className.contains("String")){

			}else if(className.contains("Boolean")){

			}else if(className.contains("DateTime")){
				DateTimeList dateList = field.getDateTimeListValue();

				for(int i=0;i<dateList.size();i++)	{ 
					Date date = (Date) dateList.get(i);
					result  = result + ";" + format.format(date);	
				}

			}			 
		}else{
			if(className.contains("IdImpl")){
				Id id = field.getIdValue();
				if(id!=null) result=id.toString();					
			}else if(className.contains("StringImpl")){
				result=field.getStringValue();						
			}else if(className.contains("BooleanImpl")){
				Boolean bo = field.getBooleanValue();
				if(bo!=null)result=bo.toString();
			}else if(className.contains("DateTimeImpl")){
				Date date = field.getDateTimeValue();
				if(date!=null) result= format.format(date);
			}
		}     
		return result;
	}
	public String getPropertyValue(String field){
		String result =null;
		if(field!=null && field.length()>0){
			result= tableOFValue.get(field);
		}
		return result;
	}

	public static PropertyFilter getPropertyFilter(List<String>  properties, boolean include) {
		PropertyFilter propFilter = new PropertyFilter();
		if(properties!=null)
		for(String property :properties){
			if(include) {
				propFilter.addIncludeProperty(new FilterElement(0,null,null,property,null));
			}else {
				propFilter.addExcludeProperty(property);
			}
		}
		return propFilter;
	}
}
