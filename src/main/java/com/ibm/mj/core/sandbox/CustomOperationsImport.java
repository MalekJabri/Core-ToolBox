package com.ibm.mj.core.sandbox;

import com.ibm.mj.core.configOperations.MethodAttributes;
import com.ibm.mj.core.configOperations.MethodInformation;
import jlibs.xml.sax.XMLDocument;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CustomOperationsImport {

	/* Create custom operation import tool.
	* This meant to be use during the import of custom method*/

	public static void main(String[] args) throws Exception {
		
	//	importDocu(doc);
	
	}



	public static void generateXML(String path, String OperationName, List<MethodInformation> methods) throws Exception {
		FileOutputStream file = new FileOutputStream(path);
		XMLDocument xml = new XMLDocument(new StreamResult(file), false, 4, null);
		
		xml.startDocument();
		{
			startDocumentXML(OperationName, xml);
			xml.startElement("ArrayOfOperationDefinition");
			for(MethodInformation method : methods){			
				xml.startElement("OperationDefinition");
				xml.addAttribute("Name", method.getName());
				xml.addAttribute("Description", method.getDescription());
				xml.startElement("ArrayOfParameterDefinition");
				for(MethodAttributes attr: method.getAttributes()){		
					xml.startElement("Parameter");
					xml.addAttribute("Name",attr.getName());	
					xml.addAttribute("Description","");
					xml.addAttribute("ValueExpr",attr.getValueExpr());
					xml.addAttribute("Type",attr.getType());
					xml.addAttribute("IsArray",attr.getIsArray());
					xml.addAttribute("Mode",attr.getMode());
					xml.endElement();
				}
				xml.endElement("ArrayOfParameterDefinition");
/*
				xml.startElement("ModelAttributes");
				{
					xml.startElement("ModelAttribute");
					xml.addAttribute("Name","F_OperationDescriptor");
					xml.addAttribute("Type","string");
					xml.addAttribute("IsArray","false");			
					{
						xml.startElement("Value");
						xml.addAttribute("Val","Ceci est un test");
						xml.endElement("Value");
					}
					xml.endElement("ModelAttribute");
				}
				xml.endElement("ModelAttributes");*/

				xml.endElement("OperationDefinition");

			}
			xml.endElement("ArrayOfOperationDefinition");
			generateEndOfDoc(xml);
			xml.endDocument();
		}
	}



	private static void startDocumentXML(String OperationName, XMLDocument xml)
			throws Exception {
		xml.startElement("eProcessConfiguration");
		xml.addAttribute("xmlns", "http://filenet.com/vw/api/configuration/1.0");
		xml.addAttribute("ApiVersion", "4.0");
		xml.startElement("SystemConfiguration");
		xml.startElement("ArrayOfLogDefinition");
		xml.endElement("ArrayOfLogDefinition");
		xml.startElement("ArrayOfQueueDefinition");
		xml.startElement("QueueDefinition");
		{
			xml.addAttribute("Name", OperationName);
			xml.addAttribute("Description", "");
			xml.addAttribute("Type","1");
			xml.addAttribute("ServerId","0");
			xml.addAttribute("IsConnectorQueue","true");
		}
		xml.startElement("ArrayOfExposedFieldDefinition");
		{
			importDocu(xml,OperationName);
		}
		xml.endElement("ArrayOfExposedFieldDefinition");
		xml.startElement("ArrayOfInBasketDefinition");
		xml.endElement("ArrayOfInBasketDefinition");
		xml.startElement("ArrayOfIndexDefinition");
		{
			xml.startElement("IndexDefinition");
			xml.addAttribute("Name", "F_WobNum");
			xml.addAttribute("IsSystemIndex", "true");
			xml.addAttribute("IsMandatorySystemIndex","true");
			xml.startElement("FieldNames");
			xml.startElement("Value");
			xml.addText("F_WobNum");
			xml.endElement("Value");
			xml.endElement("FieldNames");				
			xml.endElement("IndexDefinition");
		}
		{
			xml.startElement("IndexDefinition");
			xml.addAttribute("Name", "F_Fifo");
			xml.addAttribute("IsSystemIndex", "true");
			xml.addAttribute("IsMandatorySystemIndex","true");
			xml.startElement("FieldNames");
			xml.endElement("FieldNames");
			xml.endElement("IndexDefinition");
		}
		{
			xml.startElement("IndexDefinition");
			xml.addAttribute("Name", "F_SortRule");
			xml.addAttribute("IsSystemIndex", "true");
			xml.addAttribute("IsMandatorySystemIndex","true");
			xml.startElement("FieldNames");
			xml.startElement("Value");
			xml.addText("F_Locked");
			xml.endElement("Value");
			xml.startElement("Value");
			xml.addText("F_SortOrder");
			xml.endElement("Value");
			xml.endElement("FieldNames");
			xml.endElement("IndexDefinition");
		}
		xml.endElement("ArrayOfIndexDefinition");
		
	}



	private static void generateEndOfDoc(XMLDocument xml) throws SAXException {
		/*	xml.startElement("ReadSecurity");
		xml.endElement("ReadSecurity");
		xml.startElement("WriteSecurity");
		xml.endElement("WriteSecurity");
		
		xml.startElement("ModelAttributes");
		{
			xml.startElement("ModelAttribute");
			xml.addAttribute("Name","F_ComponentDescriptor");
			xml.addAttribute("Type","string");
			xml.addAttribute("IsArray","false");			
			{
				xml.startElement("Value");
				xml.addAttribute("Val","Ceci est un test");
				xml.endElement("Value");
			}
			xml.endElement("ModelAttribute");
		}
		xml.endElement("ModelAttributes"); */
		xml.endElement("QueueDefinition");
		xml.endElement("ArrayOfQueueDefinition");
		xml.startElement("ArrayOfRosterDefinition");
		xml.endElement("ArrayOfRosterDefinition");
		xml.startElement("ArrayOfApplicationSpaceDefinition");
		xml.endElement("ArrayOfApplicationSpaceDefinition");		
		xml.endElement("SystemConfiguration");
		xml.endElement("eProcessConfiguration");
	}




	private static  void importDocu(XMLDocument xml, String OperationsName) throws Exception {
		File XML = new File("config/ExposedFieldDefinition.xml");
		HashMap<String,String> attriXML = new HashMap<String,String>();		
		SAXReader reader = new SAXReader();
		Document document = reader.read(XML);
		Element root = document.getRootElement();
		@SuppressWarnings("unchecked")
		Iterator<Element> element = root.elementIterator();
		String Customerids = "";
		while(element.hasNext()){
			Element attri = (Element) element.next();
			//System.out.println(attri.getName() + "    "+  attri.getStringValue() );
			xml.startElement(attri.getName());
			Attribute test = attri.attribute("");
			for(Attribute field :(List<Attribute>)attri.attributes() ){
			//	System.out.println(field.getName() +"  "+ field.getValue());
				if(field.getName().contains("SourceName")){
				 	xml.addAttribute(field.getName(), OperationsName);
				} else 	xml.addAttribute(field.getName(), field.getValue());
				
			}
			xml.endElement(attri.getName());
			//if(attriV.getName().equals("CustomerIDS"))Customerids = attriV.getStringValue();
			//else		attriXML.put(attriV.getName(), attriV.getStringValue());
			
		}
		System.out.println(Customerids);

	}

}
