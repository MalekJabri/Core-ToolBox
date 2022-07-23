package com.ibm.mj.core.ceObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.Link;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.util.Id;

public class FolderTool {
	private static Logger logger = Logger.getLogger( FolderTool.class );

public static void main(String[] args) {
	FolderTool.unzipallDossier("C:/zip");
}
	public static void createFolder(ObjectStore os, String folderPath, String folderName, String className)
	{

		Folder parentFolder = Factory.Folder.fetchInstance(os, folderPath, null);
		Folder newFolder = null;
		String path = folderPath;
		if(!path.endsWith("/")) path=path+"/";
		try{

			if (className.equals(""))
				newFolder = Factory.Folder.createInstance(os, null);
			else
				newFolder = Factory.Folder.createInstance(os, className);
			newFolder.set_Parent(parentFolder);
			
			newFolder.set_FolderName(folderName);
			newFolder.save(RefreshMode.REFRESH);
		}
		catch(EngineRuntimeException e){
			ExceptionCode er = e.getExceptionCode();
			if(er.equals(ExceptionCode.E_NOT_UNIQUE))
				System.out.println(""+e.getMessage());
		}

	}

	public static Folder createFolderAndRetriveIt(ObjectStore os, String folderPath, String folderName, String className)
	{
		Folder parentFolder = Factory.Folder.fetchInstance(os, folderPath, null);
		Folder newFolder = null;
		String path = folderPath;
		if(!path.endsWith("/")) path=path+"/";
		try{
			if (className==null || className.equals(""))
				newFolder = Factory.Folder.createInstance(os, null);
			else
				newFolder = Factory.Folder.createInstance(os, className);
			newFolder.set_Parent(parentFolder);
			newFolder.set_FolderName(folderName);
			newFolder.save(RefreshMode.REFRESH);
		}
		catch(EngineRuntimeException e)
		{

			newFolder= Factory.Folder.fetchInstance(os, path+folderName, null);
			System.out.println("The folder existz"+ e.getMessage());
		}		
		return newFolder;
	}
	
	

	public static Folder createCaseFolderAndRetriveIt(ObjectStore os, Folder casef, String pathFolder)
	{

		String pathName = casef.get_PathName();	
		List<String> subfolders = parseSubfolderPath(pathFolder);
		Iterator<String> iter = subfolders.iterator();
		Folder newSubFolder= null;
		while (iter.hasNext())
		{
			String folderName = (String)iter.next();
			if ((folderName.contentEquals("/") != true) && (folderName.isEmpty() != true))    {
				try
				{
					Folder folder = Factory.Folder.fetchInstance(os, pathName, null);
					newSubFolder = Factory.Folder.createInstance(os, "CmAcmCaseSubfolder");
					newSubFolder.set_Parent(folder);
					newSubFolder.set_FolderName(folderName);
					newSubFolder.getProperties().putValue("CmAcmParentCase", casef);
					newSubFolder.save(RefreshMode.REFRESH);
				}
				catch (EngineRuntimeException e) {
					logger.error(e.getMessage());
				}
				catch (Exception e) {
					logger.error(e.getMessage());
				}
				pathName = pathName + "/" + folderName;
			}
		}
		return newSubFolder;
	}
	
	public static Folder createCaseFolderAndRetriveIt(ObjectStore os, Folder casef, String pathFolder, String ClassName)
	{

		String pathName = casef.get_PathName();	
		List<String> subfolders = parseSubfolderPath(pathFolder);
		Iterator<String> iter = subfolders.iterator();
		Folder newSubFolder= null;
		while (iter.hasNext())
		{
			String folderName = (String)iter.next();
			if ((folderName.contentEquals("/") != true) && (folderName.isEmpty() != true))    {
				try
				{
					Folder folder = Factory.Folder.fetchInstance(os, pathName, null);
					if(ClassName.isEmpty()) 	newSubFolder = Factory.Folder.createInstance(os, "CmAcmCaseSubfolder");
					else	newSubFolder = Factory.Folder.createInstance(os, ClassName);
					newSubFolder.set_Parent(folder);
					newSubFolder.set_FolderName(folderName);
					newSubFolder.getProperties().putValue("CmAcmParentCase", casef);
					newSubFolder.save(RefreshMode.REFRESH);
				}
				catch (EngineRuntimeException e) {
					logger.error(e.getMessage());
				}
				catch (Exception e) {
					logger.error(e.getMessage());
				}
				pathName = pathName + "/" + folderName;
			}
		}
		return newSubFolder;
	}

	public static void importFolder(ObjectStore os,File dossierSever, Folder folder){
		if(dossierSever.isDirectory()){
			Folder folderOutput = FolderTool.createFolderAndRetriveIt(os, folder.get_PathName(), dossierSever.getName(), null);
			File[] documents = dossierSever.listFiles();
			for(File fl : documents){
				System.out.println("Dossier " + fl.getName());
				if(fl.isDirectory()) importFolder(os,fl, folderOutput);
				else{
					DocumentTool.createDocWithContent(fl, os, fl.getName(), null,folderOutput);
				}
			}			
		}else {
			DocumentTool.createDocWithContent(dossierSever, os, dossierSever.getName(), null,folder);
		}
	} 
	
	public static void LinkFolderToFolder(ObjectStore os, Folder folder,Folder folder2) {
		ReferentialContainmentRelationship  rcr = 
				Factory.ReferentialContainmentRelationship.createInstance(os, null, 
						AutoUniqueName.NOT_AUTO_UNIQUE, 
						DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.set_Tail(folder);
		rcr.set_Head(folder2);
		rcr.set_ContainmentName("Linked Folder");
		rcr.save(RefreshMode.REFRESH);
	}
	
	public static void LinkFolderToFolder2(ObjectStore os, Folder folder,Folder folder2) {
			
		Link link=Factory.Link.createInstance(os,"RelatedItems");
		link.set_Head(folder);
		link.set_Tail(folder2);
		link.getProperties().putValue("Title", "linked folder");
		link.save(RefreshMode.REFRESH);
		
	}
	
	public static void importCaseSubFolder(ObjectStore os,Folder Casef, File dossierSever, String path, String Classname){
		String output = Casef.get_PathName() ;
		FolderTool.createCaseFolderAndRetriveIt(os,Casef, path);
		if(path!=null && !path.isEmpty()){
			if(path.startsWith("/")) output = output +path;
			else output = output + "/"+path;
		}
		Folder folderOutput = FolderTool.getFolder(os,output);	
		if(dossierSever.isDirectory()){			
			File[] documents = dossierSever.listFiles();
			for(File fl : documents){
				System.out.println("Dossier " + fl.getName());
				if(fl.isDirectory()) importCaseSubFolder(os,Casef,fl, path+"/"+fl.getName(),Classname);
				else{
					if(!fl.getName().endsWith(".zip")) DocumentTool.createDocWithContent(fl, os, fl.getName(), Classname,folderOutput);
				}
			}			
		}else {			
			if(!dossierSever.getName().endsWith(".zip")) DocumentTool.createDocWithContent(dossierSever, os, dossierSever.getName(), Classname,folderOutput);
		}
	}
	
	public static int importCaseSubCompoundFolder(ObjectStore os, Folder Casef, File dossierSever, String path, Document parent, int compteur, String Classname){
		String output = Casef.get_PathName() ;
		FolderTool.createCaseFolderAndRetriveIt(os,Casef, path);
		if(path!=null && !path.isEmpty()){
			if(path.startsWith("/")) output = output +path;
			else output = output + "/"+path;
		}
		Folder folderOutput = FolderTool.getFolder(os,output);	
		if(dossierSever.isDirectory()){			
			File[] documents = dossierSever.listFiles();
			for(File fl : documents){
				
				if(fl.isDirectory()) importCaseSubCompoundFolder(os,Casef,fl, path+"/"+fl.getName(),parent,compteur,Classname);
				else{
					if(!fl.getName().endsWith(".zip")) {
						Document doc = DocumentTool.createDocWithContent(fl, os, fl.getName(), Classname,folderOutput);
						DocumentTool.linkSubCompoundDoc(parent, doc, compteur, os);
						compteur++;
					}
				}
			}			
		}else {			
			if(!dossierSever.getName().endsWith(".zip")) { 
				Document doc = DocumentTool.createDocWithContent(dossierSever, os, dossierSever.getName(), Classname,folderOutput);
				DocumentTool.linkSubCompoundDoc(parent, doc, compteur, os);
				compteur++;
				}
		}
		return compteur;
	}

	public static void unzipallDossier(String dossierSever) {
		File dossierFSever = new File(dossierSever);
		unzipallDossier(dossierFSever);
	}
	public static void unzipallDossier(File dossierSever) {
		if(dossierSever.isDirectory()){	
			File[] documents = dossierSever.listFiles();
			for(File fl : documents){
				if(fl.isDirectory())unzipallDossier(fl);
				else if(fl.getName().endsWith(".zip")){
					unZipFolder(fl);
					fl.delete();
				}
			}
		}else if(dossierSever.getName().endsWith(".zip")){
			unZipFolder(dossierSever);
			dossierSever.delete();
		}	
	}

	public static void unZipFolder(File zipFile){
		String path = zipFile.getAbsolutePath().replace(zipFile.getName(), "");	
		try {
			final int BUFFER = 2048;
			BufferedOutputStream dest = null;
			FileInputStream fis = new FileInputStream(zipFile);
			CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
			ZipInputStream zis = new ZipInputStream(new	BufferedInputStream(checksum));
			ZipEntry entry;
			while((entry = zis.getNextEntry()) != null) {
				System.out.println("Extracting: " +entry);
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				if(entry.isDirectory()) {
					File directory = new File(path+entry.getName().substring(0, entry.getName().length()-1));
					System.out.println(path+entry.getName());
					directory.mkdir();
				}
				else{
					FileOutputStream fos = new 	FileOutputStream(path+entry.getName());
					dest = new BufferedOutputStream(fos, BUFFER);
					while ((count = zis.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
				}
			}
			zis.close();			
			System.out.println("Checksum: "+checksum.getChecksum().getValue());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Folder getFolderbyID(ObjectStore os, Id objectID)
	{

		Folder folder = null;

		try{
			folder = Factory.Folder.fetchInstance(os, objectID, null);
		}
		catch(EngineRuntimeException e)
		{		

			if(e.toString().contains("E_OBJECT_NOT_FOUND")){
				e.printStackTrace();			
			}		
		}		
		return folder;
	}

	

	public static Folder getFolderbyID(ObjectStore os, String objectID)
	{

		Folder folder = null;
		if(Id.isId(objectID))
		{
			Id id = new Id(objectID);
			try{
				folder = Factory.Folder.fetchInstance(os, id, null);
			}
			catch(EngineRuntimeException e)
			{		

				if(e.toString().contains("E_OBJECT_NOT_FOUND")){
					e.printStackTrace();			
				}		
			}	
		}			
		return folder;
	}

	public static Folder getFolderbyPath(ObjectStore os, String path) {
		Folder folder = null;
		try{
			folder = Factory.Folder.fetchInstance(os, path, null);
		}
		catch(EngineRuntimeException e)
		{		
			//System.out.println("An Error Occured "+ e.getMessage());
		}		
		return folder;
	}

	public static Folder getFolder(ObjectStore os, String value){	
		Folder folder = null;
		if(Id.isId(value)){
			folder = getFolderbyID(os, new Id(value));
		}else{
			folder = getFolderbyPath(os,value);
		}
		return folder;
	}

	private static List<String> parseSubfolderPath(String path)
	{
		List<String> list = null;
		if ((path != null) && (!path.equalsIgnoreCase("")))
		{
			String[] tokens = path.split("/");
			list = new ArrayList<String>();
			for (int i = 0; i < tokens.length; i++) {
				list.add(tokens[i]);
			}
		}
		return list;
	}

}
