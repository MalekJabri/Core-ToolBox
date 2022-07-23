package com.ibm.mj.core.ceObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;


public class ContentTool {

	public static void readDocumentFromRepo(Document document) throws Exception{		
		InputStream stream = document.accessContentStream(0);
		
		BufferedReader reader =	new BufferedReader(new InputStreamReader(stream));
		String text = "", line;
		while ((line = reader.readLine()) != null)
			text += line;
		reader.close();
		System.out.println(text);
	}

	public static InputStream getDocumentStream(Document document) throws Exception{
		InputStream stream = null;
		if(document!=null){
			stream = document.accessContentStream(0);
		}
		return stream;
	}
	public static byte[] readDocContentFromFile(File f) throws Exception{
		FileInputStream is;
		byte[] b = null;
		int fileLength = (int)f.length();
		if(fileLength != 0)
		{
			is = new FileInputStream(f);
			b = new byte[fileLength];
			is.read(b);
			is.close();
		}
		return b;
	}

	public static ContentTransfer createContentTransfer(File f) throws Exception
	{
		ContentTransfer ctNew = null;
		if(readDocContentFromFile(f) != null)
		{
			ctNew = Factory.ContentTransfer.createInstance();
			ByteArrayInputStream is = new ByteArrayInputStream(readDocContentFromFile(f));
			ctNew.setCaptureSource(is);
			ctNew.set_RetrievalName(f.getName());
		}
		return ctNew;
	}

	@SuppressWarnings("unchecked")
	public static ContentElementList createContentElements(File f) throws Exception	{
		ContentElementList cel = null;
		if(createContentTransfer(f) != null)
		{
			cel = Factory.ContentElement.createList();
			ContentTransfer ctNew = createContentTransfer(f);
			cel.add(ctNew);
		}
		return cel;
	}

	public static void writeDocContentToFile(Document doc, String path)throws Exception
	{
		String fileName = doc.get_Name();
		File f = new File(path,fileName);
		InputStream is = doc.accessContentStream(0);
		int c = 0;
		FileOutputStream out = new FileOutputStream(f);
		c = is.read();
		while ( c != -1)
		{
			out.write(c);
			c = is.read();
		}
		is.close();
		out.close();
	}

	public static void writeDocContentToPath(Document doc, String pathAndDoc)throws Exception
	{
		File f = new File(pathAndDoc);
		InputStream is = doc.accessContentStream(0);
		int c = 0;
		FileOutputStream out = new FileOutputStream(f);
		c = is.read();
		while ( c != -1)
		{
			out.write(c);
			c = is.read();
		}
		is.close();
		out.close();
	}
	
	public static void writeDocContentToFileID(Document doc, String path)throws Exception
	{
		String fileName = doc.get_Id().toString();
		File f = new File(path,fileName);
		InputStream is = doc.accessContentStream(0);
		int c = 0;
		FileOutputStream out = new FileOutputStream(f);
		c = is.read();
		while ( c != -1)
		{
			out.write(c);
			c = is.read();
		}
		is.close();
		out.close();
	}

	@SuppressWarnings("unchecked")
	public static ContentElementList getContentlist(InputStream in, String name){
		ContentElementList cel =  Factory.ContentElement.createList();
		ContentTransfer ctNew = Factory.ContentTransfer.createInstance();
		ctNew.setCaptureSource(in);
		ctNew.set_RetrievalName(name); 
		cel.add(ctNew);
		return cel;
	}

	public static void WritetoFile(InputStream input, String Name){
		OutputStream outStream = null;
		try{
			byte[] buffer = new byte[input.available()];
			input.read(buffer);		 
			File targetFile = new File(Name);
			outStream = new FileOutputStream(targetFile);
			outStream.write(buffer);}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			if(outStream!=null)
				try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}


}
