package com.ibm.mj.core.sandbox;

import com.filenet.api.collection.DocumentSet;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.ibm.mj.core.ceObject.FolderTool;
import com.ibm.mj.core.sandbox.tester.testerClass;

import java.util.Iterator;

public class ServitiaTest extends testerClass {
    private final static String  configFile = "/Users/jabrimalek/development/tools/FileNet_Config/baw/config.properties";


    public static void main(String[] args) {
        ServitiaTest servitiaTest = new ServitiaTest();
        servitiaTest.getAction();
    }
    ServitiaTest(){
        super(configFile);
    }
    @Override
    public void getAction() {
        System.out.println("objectstore connected to "+ getOs().get_Name() );
        // retrieve folder based on a path
         Folder folder =  FolderTool.getFolder(getOs(), "/Servitia");
        System.out.println(folder.get_Name());
        // Retrieve all the docs from the folder
        DocumentSet docs = folder.get_ContainedDocuments();
        Iterator iterator = docs.iterator();
        while(iterator.hasNext()){
            Document document = (Document)iterator.next();
            System.out.println(document.get_Name());
        }
    }
}
