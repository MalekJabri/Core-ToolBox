package com.ibm.mj.core.sandbox;

import jlibs.xml.sax.XMLDocument;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class XmlGeneration {


	public static void main(String[] args) throws Exception {
		int nbrDocs=10;
		createMultidocs(nbrDocs,"./XmlFolder/");
	//	createMultiwithfolderdocs(nbrDocs, "./XmlFolder/");
	}

	

	private static void createMultidocs(int number, String path) throws FileNotFoundException,
	TransformerConfigurationException, SAXException {
		checkFolder(path);
		for(int i=0;i<number;i++){
			System.out.println("Create docs : "+i);
			Attribute[] docAttributes = new Attribute[3] ;
			docAttributes[0]=new Attribute("DocumentClass","Document");
			docAttributes[1]=new Attribute("DocumentTitle","Contract "+i+".pdf");
			docAttributes[2]=new Attribute("docName","Hello.pdf");
			

			Document document = new Document(docAttributes,null);

			FileOutputStream file = new FileOutputStream(path+"document"+i+".xml");
			XMLDocument xml = new XMLDocument(new StreamResult(file), false, 4, null);
			xml.startDocument();
			{
				xml.startElement("Document");{

					if(document.attributes!=null){
						for(Attribute attr: document.attributes){	
							xml.startElement(attr.name);
							xml.addText(attr.value);
							//xml.addComment("CEcci est un test ");
							xml.endElement(attr.name);
							
							
						}						
					}
				}
				if(document.folders!=null){
					for(Folder fol: document.folders){
						xml.startElement("Folder");{
							for(Attribute attr : fol.attributes) 	xml.addElement(attr.name,attr.value );	
						}
						xml.endElement("Folder");
					}
				}
			}
			xml.endElement("Document");

			xml.endDocument();
		}
	}


	private static void createMultiwithfolderdocs(int number, String path) throws FileNotFoundException,
	TransformerConfigurationException, SAXException {
		checkFolder(path);
		for(int i=0;i<number;i++){

			Attribute[] docAttributes = new Attribute[2] ;
			Attribute[] folAttributes  = new Attribute[2];
			Folder[] folders = new Folder[1];	
			docAttributes[0]=new Attribute("DocumentClass","Document");
			docAttributes[1]=new Attribute("DocumentTitle","Hello.pdf");	

			folAttributes[0]=new Attribute("FolderClass","Folder");
			folAttributes[1]=new Attribute("IDATTRIbute","234567");
			folders[0] = new Folder(folAttributes);

			Document document = new Document(docAttributes,folders);

			FileOutputStream file = new FileOutputStream(path+"document_fol_"+i+".xml");
			XMLDocument xml = new XMLDocument(new StreamResult(file), false, 4, null);
			xml.startDocument();
			{
				xml.startElement("Document");{

					if(document.attributes!=null){
						for(Attribute attr: document.attributes){													
							xml.addElement(attr.name,attr.value );							
						}						
					}
				}
				if(document.folders!=null){
					for(Folder fol: document.folders){
						xml.startElement("Folder");{
							for(Attribute attr : fol.attributes) 	xml.addElement(attr.name,attr.value );	
						}
						xml.endElement("Folder");
					}
				}
			}
			xml.endElement("Document");

			xml.endDocument();
		}
	}

  private static void checkFolder(String path){
	  File OutPutFolder= new File(path);
	  if(!OutPutFolder.exists()) {
		  OutPutFolder.mkdirs();
	  }
  }
	public  static class Document{
		Attribute attributes[];
		Folder folders[];

		Document( Attribute[] attributes,Folder[] folders){

			this.attributes = attributes;
			this.folders = folders;
		}
	}

	public static class Attribute{		
		String name;
		String value;


		Attribute(String name, String value){		
			this.name = name;
			this.value = value;
		}
	}


	public static class Folder{		
		Attribute attributes[];

		Folder(Attribute[] attributes){			
			this.attributes = attributes;

		}
	}
}
