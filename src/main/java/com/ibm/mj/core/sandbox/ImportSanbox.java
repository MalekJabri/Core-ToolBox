package com.ibm.mj.core.sandbox;

import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.ibm.mj.core.ceObject.DocumentTool;
import com.ibm.mj.core.ceObject.FolderTool;
import com.ibm.mj.core.sandbox.tester.testerClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ImportSanbox  extends testerClass implements Runnable {

    private static int maxNumber = 5;
    private static int maxThread = 1;
    private int code =0;
    private static Logger logger = Logger.getLogger(ImportSanbox.class.toString());
    ImportSanbox(String config, int threadID){
        setConfig(config);
        code = threadID;
    }

    public static void main(String[] args) {

        long last_time = System.nanoTime();
        for(int i =0;i<maxThread;i++) {
            Runnable importSanbox = new ImportSanbox("/Users/jabrimalek/development/tools/FileNet_Config/config_cpe_generali.properties", i);
            Thread tst = new Thread(importSanbox);
            tst.start();
        }
        long time = System.nanoTime();
        int delta_time = (int) ((time - last_time) / 1000000);


    }
    @Override
    public void getAction() {
        List<Document> listdoc = new ArrayList<>();
        long last_time = System.nanoTime();
        ObjectStore os = getOs();
        File file=  new File("/Users/jabrimalek/development/workspace/ibm/content/Core-ToolBox/XmlFolder/ejemplo1.pdf");
        Folder folder = FolderTool.getFolder(os, "/ImportFolder");
        long time = System.nanoTime();
        int delta_time = (int) ((time - last_time) / 1000000);
        logger.info("Document upload started");
        for (int i = 0; i < maxNumber; i++) {
            time = System.nanoTime();
            listdoc.add(DocumentTool.createDocWithContent(file, os,"ejemplo1 doc "+i+".pdf", "Document", folder));
            delta_time = (int) ((System.nanoTime()-time) / 1000000);
            logger.info("Document "+i+" upload in "+delta_time +"ms");
        }

        logger.info("Document download started");
        time = System.nanoTime();
        for (Document doc : listdoc)        DocumentTool.downloadDocument(doc, new File("/Users/jabrimalek/Demo/Content/Generali/Export/"+code+"-"+doc.get_Name()));

        delta_time = (int) ((System.nanoTime() - time) / 1000000);
        logger.info(maxNumber+"  documents has been downloaded in  " + delta_time  + " ms");
        logger.info("A document has been downloaded in " + delta_time/maxNumber  + " ms");
        }


    @Override
    public void run() {
        init("/Users/jabrimalek/development/tools/FileNet_Config/config_cpe_generali.properties");
        getAction();
    }
}
