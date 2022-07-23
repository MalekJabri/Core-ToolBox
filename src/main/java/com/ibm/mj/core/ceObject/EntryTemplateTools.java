package com.ibm.mj.core.ceObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
//import org.apache.commons.json.JSONArray;
//import org.apache.commons.json.JSONObject;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.ibm.mj.core.ToolsBox;
public class EntryTemplateTools {

	private static String osName ;
	private static  String versionId;
	private Document document;
	private boolean templateICN;

	public EntryTemplateTools(String os, String id, boolean icn) {
		osName = os;
		versionId=id;
		templateICN=icn;
	}

	public static void main(String[] args) {
		EntryTemplateTools entry = new EntryTemplateTools("cmtos", "zefjidsufoidsquofiudsoifudsqoiuf",false);
		entry.printXmlDoc();	
	}

	public void CreateAnnotEntry(){

		//Init Attribute		
		Attribute AkfolderPref = new Attribute("key","folderPreference");
		Attribute AvfolderPref = new Attribute("version","1.0");
		Attribute key =  new Attribute("key","folderTemplates");
		Attribute keyt =  new Attribute("key","folderTemplate");
		Attribute keyOS =  new Attribute("key","templateObjectStoreName");
		Attribute keyVersionID =  new Attribute("key","templateVersionSeriesId");
		Attribute arrKey =  new Attribute("key","fileTypes");		

		//Init Element
		Element folderPref = new Element("object");
		Element objFolderTemp = new Element("object");
		Element eleSetOS = new Element("setting");
		Element eleSetEntryVersion = new Element("setting");    
		Element eleArray = new Element("array");	
		Element list = new Element("list");

		// Set different Attribute
		folderPref.setAttribute(AkfolderPref);
		folderPref.setAttribute(AvfolderPref);
		list.setAttribute(key);
		objFolderTemp.setAttribute(keyt);
		eleSetEntryVersion.setAttribute(keyVersionID);
		eleSetOS.setAttribute(keyOS);
		eleArray.setAttribute(arrKey);

		//Creation the document structured
		document = new Document(folderPref);			
		folderPref.addContent(list);		
		list.addContent(objFolderTemp);
		eleSetOS.setText(osName);		
		eleSetEntryVersion.setText(versionId);	
		objFolderTemp.addContent(eleSetOS);
		objFolderTemp.addContent(eleSetEntryVersion);
		objFolderTemp.addContent(eleArray);			
	}

	public void printXmlDoc()
	{
		try
		{	   
			CreateAnnotEntry();
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, System.out);
		}
		catch (java.io.IOException e){}
	}

	public void WritetoLocalFile(String fichier )
	{
		try
		{	    
			CreateAnnotEntry();
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, new FileOutputStream(fichier));
		}
		catch (java.io.IOException e){}
	}

	public InputStream getInputStream() 
	{		

		try {
			if(templateICN)
				return null;// getInputJsonStream();
			else 
				return getInputXMLStream(); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public InputStream getInputXMLStream() throws Exception
	{

		InputStream fileIn = null;
		try
		{			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, out);
			fileIn = ToolsBox.OutToInStream(out);
		}
		catch (java.io.IOException e){}

		return fileIn;
	}
	public InputStream getInputJsonStream() throws Exception
	{
		InputStream fileIn = null;
		JSONObject jo = new JSONObject();		
		jo.put("isDefault", true);
		jo.put("currentFolderAsParent", true);		
		jo.put("type", "document");		
		jo.put("fileTypes", new ArrayList<String>());
		jo.put("entryTemplateVsId", versionId);
		JSONArray ja = new JSONArray();
		ja.put(jo);
		fileIn = new ByteArrayInputStream(ja.toString().getBytes(StandardCharsets.UTF_8));		
		return fileIn;
	}

}
